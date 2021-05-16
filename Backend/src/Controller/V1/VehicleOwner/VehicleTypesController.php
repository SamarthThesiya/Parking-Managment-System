<?php


namespace App\Controller\V1\VehicleOwner;


use App\Controller\ApiController;

class VehicleTypesController extends ApiController
{
    public function initialize()
    {
        parent::initialize();
        $this->Crud->enable(['index']);
    }
}
