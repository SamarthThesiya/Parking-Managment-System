<?php


namespace App\Controller\V1\ServicePerson;


use App\Controller\ApiController;
use App\Model\Response\ApiOKResponse;
use App\Model\Response\Error\NotFoundResponse;

/**
 * Class LandServicePersonsController
 *
 * @package App\Controller\V1\ServicePerson
 *
 * @property \App\Model\Table\LandServicePersonsTable $LandServicePersons
 * @property \App\Model\Table\UsersTable              $Users
 * @property \App\Model\Table\LandsTable              $Lands
 */
class LandServicePersonsController extends ApiController
{
    public function initialize()
    {
        parent::initialize();

        $this->loadModel("LandServicePersons");
        $this->loadModel("Users");
        $this->loadModel("Lands");
    }

    public function assignLand(int $landId)
    {
        $userId = $this->Auth->user('id');

        $land = $this->Lands->find()
            ->where([
                $this->Lands->aliasField('id') => $landId,
            ])->first();

        if (empty($land)) {
            return new NotFoundResponse("Land with given id is not found");
        }

        $landServicePerson                    = $this->LandServicePersons->newEntity();
        $landServicePerson->service_person_id = $userId;
        $landServicePerson->land_id           = $landId;

        $this->LandServicePersons->saveOrFail($landServicePerson);

        return new ApiOKResponse();
    }
}
