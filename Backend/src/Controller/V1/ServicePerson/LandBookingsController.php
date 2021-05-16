<?php


namespace App\Controller\V1\ServicePerson;


use App\Controller\ApiController;
use App\Model\Enums\BookingStatuses;
use App\Model\Response\ApiOKResponse;
use App\Model\Response\Error\NotFoundResponse;
use App\Model\Validation\AuditBookingValidator;
use Cake\Event\Event;
use Cake\I18n\Time;
use Cake\Network\Exception\NotFoundException;

/**
 * Class LandBookingsController
 *
 * @package App\Controller\V1\ServicePerson
 *
 * @property \App\Model\Table\BookingStatusesTable    $BookingStatuses
 * @property \App\Model\Table\LandBookingsTable       $LandBookings
 * @property \App\Model\Table\VehiclesTable           $Vehicles
 * @property \App\Model\Table\LandServicePersonsTable $LandServicePersons
 * @property \App\Model\Table\LandsTable              $Lands
 *
 * @property \App\Service\LandOwnerTransactionService $LandOwnerTransaction
 */
class LandBookingsController extends ApiController
{
    public $services = [
        'LandOwnerTransaction'
    ];
    public function initialize()
    {
        parent::initialize();
        $this->Crud->enable(['index', 'view', 'add', 'edit']);

        $this->loadModel("BookingStatuses");
        $this->loadModel("LandBookings");
        $this->loadModel("Vehicles");
        $this->loadModel("LandServicePersons");
        $this->loadModel("Lands");

        $this->RequestValidator->addRequestDataValidators([
            'auditLandBooking' => AuditBookingValidator::class,
        ]);
    }

    public function index()
    {
        $userId = $this->Auth->user('id');

        /** @var \App\Model\Entity\LandServicePerson $landServicePerson */
        $landServicePerson = $this->LandServicePersons->find()
            ->where([
                $this->LandServicePersons->aliasField('service_person_id') => $userId,
            ])->first();

        $this->Crud->on('beforePaginate', function (Event $event) use ($userId, $landServicePerson) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->contain([
                "Users",
                'Vehicles',
                'Vehicles.VehicleTypes',
                "Lands",
                "BookingStatuses",
            ]);

            $query->find('all')
                ->where([
                    $this->LandBookings->aliasField('land_id') => $landServicePerson->land_id,
                ]);

            $queryParams = $this->request->getQueryParams();

            if (isset($queryParams['page'])) {
                unset($queryParams['page']);
            }
            if (isset($queryParams['limit'])) {
                unset($queryParams['limit']);
            }
            if (isset($queryParams['filter'])) {

                $start_time = null;
                switch ($queryParams['filter']) {
                    case "current_bookings":
                        $query->andWhere([
                            $this->LandBookings->aliasField('start_time >=') => Time::now()->format("Y-m-d"),
                            $this->LandBookings->aliasField('start_time <')  => Time::now()->addDay()->format("Y-m-d"),
                        ]);
                        break;
                    case "past_bookings":
                        $query->andWhere([
                            $this->LandBookings->aliasField('start_time') . ' < ' => Time::now()->format("Y-m-d"),
                        ]);
                        break;
                    case "All Bookings":
                        break;
                }
                unset($queryParams['filter']);
            }
            if (isset($queryParams['vehicle-number']) && !empty($queryParams['vehicle-number'])) {
                $query->where([
                    $this->Vehicles->aliasField("registration_number ilike ") => '%' . $queryParams['vehicle-number'] . '%'
                ]);
                unset($queryParams['vehicle-number']);
            }
            if (!empty($queryParams)) {
                $query->andWhere($this->request->getQueryParams());
            }

        });

        return $this->Crud->execute();
    }

    public function view($bookingToken)
    {
        try {
            $userId = $this->Auth->user('id');

            /** @var \App\Model\Entity\LandServicePerson $landServicePerson */
            $landServicePerson = $this->LandServicePersons->find()
                ->where([
                    $this->LandServicePersons->aliasField('service_person_id') => $userId,
                ])->first();

            /** @var \App\Model\Entity\LandBooking $booking */
            $booking = $this->LandBookings->find()
                ->contain([
                    'Vehicles',
                    'Vehicles.VehicleTypes',
                    "Lands",
                    "BookingStatuses",
                ])
                ->where([
                    $this->LandBookings->aliasField('booking_token') => $bookingToken,
                ])
                ->firstOrFail();

            $error = '';
            if ($this->BookingStatuses->getBookingStatusNameFromId($booking->status_id)->name != BookingStatuses::BOOKED) {
                $error .= "Booking is not in booked state.";
            }

            if ($booking->land_id != $landServicePerson->land_id) {
                $error .= " Wrong parking plot.";
            }

            $booking['error'] = $error;

            return new ApiOKResponse($booking);
        } catch (NotFoundException $exception) {
            return new NotFoundResponse("Booking not found for given token.");
        }
    }

    public function auditLandBooking(int $landBookingId)
    {
        /** @var \App\Model\Entity\LandBooking $booking */
        $booking = $this->LandBookings->find()
            ->where([
                $this->LandBookings->aliasField('id') => $landBookingId,
            ])->first();

        if (empty($booking)) {
            return new NotFoundResponse("Booking with this id not found");
        }

        $result            = $this->request->getData("result");
        $landBookingStatus = null;

        $this->LandBookings->getConnection()->transactional(function () use ($result, $booking) {
            if ($result == "deny") {
                $landBookingStatus      = BookingStatuses::CANCELLED_BY_SERVICE_PERSON;
                $booking->denial_reason = $this->request->getData("denial_reason");
            } else {
                $landBookingStatus      = BookingStatuses::PARKED;
                $booking->denial_reason = null;

                $land = $this->Lands->get($booking->land_id);
                $this->LandOwnerTransaction->credit($land->owner_id, $booking->booking_fee);
            }

            $booking->status_id = $this->BookingStatuses->getBookingStatusIdFromName($landBookingStatus)->id;

            $this->LandBookings->saveOrFail($booking);
        });

        return new ApiOKResponse();
    }
}
