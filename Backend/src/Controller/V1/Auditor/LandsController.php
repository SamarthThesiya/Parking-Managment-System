<?php

namespace App\Controller\V1\Auditor;


use App\Controller\ApiController;
use App\Exceptions\FailedDependencyException;
use App\Model\Entity\Api\PatchLand;
use App\Model\Enums\LandStatuses;
use App\Model\Response\ApiOKResponse;
use App\Model\Response\Error\NotFoundResponse;
use App\Model\Response\Error\ValidationErrorResponse;
use App\Model\Validation\AuditLandValidator;
use App\Model\Validation\BaseValidator;
use Cake\Datasource\Exception\RecordNotFoundException;
use Cake\Event\Event;

/**
 * Lands Controller
 *
 * @property \App\Model\Table\LandsTable        $Lands
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

        $this->loadModel('Lands');
        $this->loadModel('LandStatuses');

        $this->RequestValidator->addRequestDataValidators([
            'auditLand' => AuditLandValidator::class,
        ]);
    }

    public function index()
    {
        $this->Crud->on('beforePaginate', function (Event $event) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->contain([
                'LandStatuses',
                'Owner',
            ]);

            $queryParams = $this->request->getQueryParams();
            $query->find('AuditorAll', $queryParams);

        });

        return $this->Crud->execute();
    }

    public function view(int $id)
    {
        $this->Crud->on('beforeFind', function (Event $event) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->contain([
                'LandStatuses',
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
                throw new FailedDependencyException("Unable to edit the land", "validation_error", $land->getErrors());
            }
        });

        try {
            $this->Crud->execute();

            return new ApiOKResponse($this->Lands->get($id));
        } catch (FailedDependencyException $exception) {
            return $exception->toResponse();
        }

    }

    public function assignToMe(int $landId)
    {
        $userId = $this->Auth->user('id');
        $land   = $this->Lands->get($landId);

        $this->Lands->patchEntity($land, [
            'auditor_id' => $userId,
        ]);
        $this->Lands->saveOrFail($land);

        return new ApiOKResponse();
    }

    public function auditLand(int $landId)
    {
        $response = $this->request->getData('response');
        $comment  = $this->request->getData('comment');

        $landStatusId = null;
        if ($response == 'approve') {
            $landStatusId = $this->Land->getLandStatusFromName(LandStatuses::APPROVED_AND_ACTIVATED)->id;
        } elseif ($response == 'reject') {
            $landStatusId = $this->Land->getLandStatusFromName(LandStatuses::REJECTED)->id;
        }

        $land = $this->Lands->get($landId);

        $this->Lands->patchEntity($land, [
            'status_id'       => $landStatusId,
            'auditor_comment' => $comment,
        ]);

        $this->Lands->saveOrFail($land);

        return new ApiOKResponse();
    }

    public function myAuditedLand()
    {
        $userId = $this->Auth->user('id');

        $lands = $this->Lands->find()
            ->contain([
                "LandStatuses",
            ])
            ->where([
                $this->Lands->aliasField('auditor_id')           => $userId,
                $this->LandStatuses->aliasField('name') . ' IN ' => [
                    LandStatuses::APPROVED_AND_ACTIVATED,
                    LandStatuses::APPROVED_AND_DEACTIVATED,
                    LandStatuses::REJECTED,
                ],
            ])->all();

        return new ApiOKResponse($lands);
    }
}
