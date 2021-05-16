<?php


namespace App\Controller\V1\ServicePerson;


use App\Controller\ApiController;
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

        $this->loadModel("Lands");
        $this->loadModel("LandStatuses");
    }

    public function index()
    {
        $this->Crud->on('beforePaginate', function (Event $event) {
            /** @var \Cake\ORM\Query $query */

            $nonUsableKeys = ["limit", "page"];

            $query = $event->getSubject()->query;

            $query->contain([
                'Owner',
                'LandStatuses',
            ]);

            $query->find('all')
                ->where([
                    'LandStatuses.name' => LandStatuses::APPROVED_AND_ACTIVATED,
                ]);

            $queryParams = $this->request->getQueryParams();

            foreach ($nonUsableKeys as $nonUsableKey) {
                if (isset($queryParams[$nonUsableKey])) {
                    unset($queryParams[$nonUsableKey]);
                }
            }

            if (isset($queryParams['title']) && !empty($queryParams['title'])) {
                $query->andWhere([
                    $this->Lands->aliasField("title ilike ") => '%' . $queryParams['title'] . '%',
                ]);
                unset($queryParams['title']);
            }

            if (!empty($queryParams)) {
                $query->andWhere($queryParams);
            }
        });

        return $this->Crud->execute();
    }
}
