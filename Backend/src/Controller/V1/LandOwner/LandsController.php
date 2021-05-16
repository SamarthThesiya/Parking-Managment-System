<?php

namespace App\Controller\V1\LandOwner;


use App\Controller\ApiController;
use App\Exceptions\FailedDependencyException;
use App\Model\Entity\Api\LandOwnerGetLands;
use App\Model\Entity\Api\PatchLand;
use App\Model\Enums\LandStatuses;
use App\Model\Response\ApiOKResponse;
use App\Model\Response\Error\NotFoundResponse;
use App\Model\Response\Error\ValidationErrorResponse;
use App\Model\Validation\BaseValidator;
use Cake\Datasource\Exception\RecordNotFoundException;
use Cake\Event\Event;

/**
 * Lands Controller
 *
 * @property \App\Model\Table\LandBookingsTable $LandBookings
 * @property \App\Model\Table\LandStatusesTable $LandStatuses
 *
 * @property \App\Service\LandService           $Land
 */
class LandsController extends ApiController
{
    public $services = [
        'Land',
    ];

    public function initialize()
    {
        parent::initialize();
        $this->Crud->enable(['index', 'view', 'add', 'edit']);

        $this->loadModel('LandBookings');
        $this->loadModel('LandStatuses');
    }

    public function index()
    {
        $userId = $this->Auth->user('id');

        $this->Lands->setEntityClass(LandOwnerGetLands::class);

        $this->Crud->on('beforePaginate', function (Event $event) use ($userId) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->contain([
                'LandStatuses',
            ]);

            $query->find('all')
                ->where([
                    'owner_id' => $userId,
                ]);

            if (!empty($this->request->getQueryParams())) {
                $query->andWhere($this->request->getQueryParams());
            }
        });

        return $this->Crud->execute();
    }

    public function view(int $id)
    {
        $this->Crud->on('beforeFind', function (Event $event) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->contain([
                'LandStatuses'
            ]);

        });
        return $this->Crud->execute();
    }

    public function add()
    {
        $userId = $this->Auth->user('id');
        $this->Crud->on('beforeSave', function (Event $event) use ($userId) {

            /** @var \App\Model\Entity\Land $land */
            $land = $event->getSubject()->entity;

            $land->owner_id = $userId;
        });
        return $this->Crud->execute();
    }

    public function edit(int $id)
    {
        $userId = $this->Auth->user('id');

        $this->Lands->setEntityClass(PatchLand::class);
        $this->Crud->on('beforeSave', function (Event $event) use ($userId) {

            /** @var \App\Model\Entity\Land $land */
            $land = $event->getSubject()->entity;

            $land->owner_id = $userId;

            if (!empty($land->getErrors())) {
                throw new FailedDependencyException("Unable to edit the land","validation_error", $land->getErrors());
            }
        });

        try {
            $this->Crud->execute();

            return new ApiOKResponse($this->Lands->get($id));
        } catch (FailedDependencyException $exception) {
            return $exception->toResponse();
        }

    }

    public function register(int $id)
    {
        try {
            $land = $this->Lands->get($id);

            $land->auditor_id = null;

            $this->Lands->saveOrFail($land);

            $landEntity = $land->toArray();
            $optionKeys = [
                'auditor_id',
                'auditor_comment',
            ];
            $validator  = new BaseValidator();
            $validator->notEmpty(array_keys($landEntity));
            $validator->allowEmpty($optionKeys);
            $validator->latitude('latitude');
            $validator->longitude('longitude');

            $errors = $validator->errors($landEntity);
            if (!empty($errors)) {
                return new ValidationErrorResponse($errors, null, null, $this->response->getType());
            }

            $this->Land->changeLandStatus($id, LandStatuses::PENDING_APPROVAL);

            return new ApiOKResponse();
        } catch (RecordNotFoundException $exception) {
            return new NotFoundResponse($exception->getMessage());
        }

    }

    public function requestCancel(int $id)
    {
        try {
            $this->Land->changeLandStatus($id, LandStatuses::REQUEST_CANCELLED);

            return new ApiOKResponse();
        } catch (RecordNotFoundException $exception) {
            return new NotFoundResponse($exception->getMessage());
        }
    }

    public function deactivate(int $id)
    {
        try {
            $this->Land->changeLandStatus($id, LandStatuses::APPROVED_AND_DEACTIVATED);

            return new ApiOKResponse();
        } catch (RecordNotFoundException $exception) {
            return new NotFoundResponse($exception->getMessage());
        }
    }

    public function reactivate(int $id)
    {
        try {
            $this->Land->changeLandStatus($id, LandStatuses::APPROVED_AND_ACTIVATED);

            return new ApiOKResponse();
        } catch (RecordNotFoundException $exception) {
            return new NotFoundResponse($exception->getMessage());
        }
    }
}
