<?php


namespace App\Controller\V1\VehicleOwner;


use App\Controller\ApiController;
use App\Exceptions\FailedDependencyException;
use App\Model\Response\Error\FailedDependencyResponse;
use App\Model\Validation\AddVehicleValidator;
use Cake\Event\Event;
use Crud\Error\Exception\ValidationException;

/**
 * Class VehiclesController
 *
 * @package App\Controller\V1\VehicleOwner
 *
 * @property \App\Model\Table\VehiclesTable     $Vehicles
 * @property \App\Model\Table\VehicleTypesTable $VehicleTypes
 */
class VehiclesController extends ApiController
{
    public function initialize()
    {
        parent::initialize();
        $this->Crud->enable(['index', 'view', 'add', 'edit']);

        $this->loadModel("Vehicles");
        $this->loadModel("VehicleTypes");

        $this->RequestValidator->addRequestDataValidators([
            'add' => AddVehicleValidator::class,
        ]);
    }

    public function index()
    {
        $userId = $this->Auth->user('id');
        $this->Crud->on('beforePaginate', function (Event $event) use ($userId) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->contain([
                'VehicleTypes',
            ]);

            $query->find('all')
                ->where([
                    $this->Vehicles->aliasField('owner_id') => $userId,
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
                'VehicleTypes',
            ]);

            $query->find('all')
                ->where([
                    $this->Vehicles->aliasField('owner_id') => $userId,
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

            /** @var \App\Model\Entity\Vehicle $vehicle */
            $vehicle = $event->getSubject()->entity;

            $vehicle->owner_id = $userId;

            if (!empty($vehicle->getErrors())) {
                throw new FailedDependencyException("Unable to add the vehicle", "validation_error",
                    $vehicle->getErrors());
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
