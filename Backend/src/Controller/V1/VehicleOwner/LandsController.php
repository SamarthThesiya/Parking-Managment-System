<?php


namespace App\Controller\V1\VehicleOwner;


use App\Controller\ApiController;
use App\Model\Entity\Api\GetVehicleOwnerLand;
use App\Model\Enums\LandStatuses;
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
    public function initialize()
    {
        parent::initialize();
        $this->Crud->enable(['index', 'view', 'add', 'edit']);

        $this->loadModel('Lands');
        $this->loadModel('LandStatuses');
    }

    public function index()
    {
        $this->Lands->setEntityClass(GetVehicleOwnerLand::class);
        $this->Crud->on('beforePaginate', function (Event $event) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->contain([
                'LandStatuses',
            ]);

            $query->find('all')
                ->where([
                    $this->LandStatuses->aliasField('name') => LandStatuses::APPROVED_AND_ACTIVATED,
                ]);

            $nonUsableKeys = ["startTime", "endTime"];

            $queryParams = $this->request->getQueryParams();

            foreach ($nonUsableKeys as $nonUsableKey) {
                if (isset($queryParams[$nonUsableKey])) {
                    unset($queryParams[$nonUsableKey]);
                }
            }

            $lands     = $query->all();
            $excludeId = [];
            foreach ($lands as $land) {
                if (!$land["is_land_available"]) {
                    $excludeId[] = $land->id;
                }
            }

            if (!empty($excludeId)) {
                $query->andWhere([
                    $this->Lands->aliasField("id not in ") => $excludeId,
                ]);
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
                'LandStatuses',
            ]);

            $query->find('all')
                ->where([
                    $this->LandStatuses->aliasField('name') => LandStatuses::APPROVED_AND_ACTIVATED,
                ]);

            if (!empty($this->request->getQueryParams())) {
                $query->andWhere($this->request->getQueryParams());
            }
        });

        return $this->Crud->execute();
    }

}
