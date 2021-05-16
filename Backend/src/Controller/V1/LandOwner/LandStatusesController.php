<?php


namespace App\Controller\V1\LandOwner;


use App\Controller\ApiController;
use App\Model\Response\ApiOKResponse;
use Cake\Event\Event;

class LandStatusesController extends ApiController
{
    public function initialize()
    {
        parent::initialize();
        $this->Crud->enable(['index']);
    }

    public function index()
    {
        $this->Crud->on('beforePaginate', function (Event $event) {
            /** @var \Cake\ORM\Query $query */
            $query = $event->getSubject()->query;

            $query->where($this->request->getQueryParams());
        });

        return $this->Crud->execute();
    }

}
