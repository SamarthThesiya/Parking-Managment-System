<?php


namespace App\Controller\V1\VehicleOwner;


use App\Controller\ApiController;
use App\Exception\NotAllowedException;
use App\Exceptions\FailedDependencyException;
use App\Model\Enums\BookingStatuses;
use App\Model\Response\Error\FailedDependencyResponse;
use Cake\Event\Event;
use Crud\Error\Exception\ValidationException;

/**
 * Class LandBookingsController
 *
 * @package App\Controller\V1\VehicleOwner
 *
 * @property \App\Model\Table\BookingStatusesTable $BookingStatuses
 * @property \App\Model\Table\LandBookingsTable    $LandBookings
 *
 * @property \App\Service\WalletService            $Wallet
 */
class LandBookingsController extends ApiController
{

    public $services = [
        "Wallet"
    ];

    public function initialize()
    {
        parent::initialize();
        $this->Crud->enable(['index', 'view', 'add', 'edit']);

        $this->loadModel("BookingStatuses");
        $this->loadModel("LandBookings");

        $this->RequestValidator->addRequestDataValidators([

        ]);
    }

    public function index()
    {
        $userId = $this->Auth->user('id');
        $this->Crud->on('beforePaginate', function (Event $event) use ($userId) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->contain([
                'Vehicles',
                'Vehicles.VehicleTypes',
                "Lands",
                "BookingStatuses"
            ]);

            $query->find('all')
                ->where([
                    $this->LandBookings->aliasField('user_id') => $userId,
                ]);

            if (!empty($this->request->getQueryParams())) {
                $query->andWhere($this->request->getQueryParams());
            }
        });

        return $this->Crud->execute();
    }

    public function view(int $id)
    {
        $userId = $this->Auth->user('id');
        $this->Crud->on('beforeFind', function (Event $event) use ($userId) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->contain([
                'Vehicles',
                'Vehicles.VehicleTypes',
                "Lands",
                "BookingStatuses"
            ]);

            $query->find('all')
                ->where([
                    $this->LandBookings->aliasField('user_id') => $userId,
                ]);

            if (!empty($this->request->getQueryParams())) {
                $query->andWhere($this->request->getQueryParams());
            }
        });

        return $this->Crud->execute();
    }

    public function add()
    {
        $userId = $this->Auth->user('id');
        $this->Crud->on('beforeSave', function (Event $event) use ($userId) {

            /** @var \App\Model\Entity\LandBooking $landBookings */
            $landBookings = $event->getSubject()->entity;

            $landBookings->user_id = $userId;

            /** @var \App\Model\Entity\BookingStatus $bookingStatus */
            $bookingStatus = $this->BookingStatuses->find()
                ->where([
                    $this->BookingStatuses->aliasField("name") => BookingStatuses::BOOKED,
                ])->firstOrFail();

            $landBookings->status_id = $bookingStatus->id;
            $landBookings->setBookingToken();

            if (!empty($landBookings->getErrors())) {
                throw new FailedDependencyException("Unable to add the booking", "validation_error",
                    $landBookings->getErrors());
            }

            try {
                $this->Wallet->deductAmount($userId, $landBookings->booking_fee);
            } catch (NotAllowedException $exception) {
                throw new NotAllowedException("Insufficient balance.");
            }
        });

        try {
            return $this->Crud->execute();
        } catch (ValidationException $exception) {
            return new FailedDependencyResponse($exception->getMessage(), $exception->getCode(),
                $exception->getValidationErrors());
        }
    }
}
