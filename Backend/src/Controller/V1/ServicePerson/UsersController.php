<?php


namespace App\Controller\V1\ServicePerson;


use App\Controller\ApiController;
use App\Model\Response\ApiOKResponse;

/**
 * Class UsersController
 *
 * @package App\Controller\V1\ServicePerson
 *
 * @property \App\Model\Table\LandServicePersonsTable $LandServicePersons
 * @property \App\Model\Table\UsersTable              $Users
 * @property \App\Model\Table\LandsTable              $Lands
 */
class UsersController extends ApiController
{
    public function initialize()
    {
        parent::initialize();

        $this->loadModel("LandServicePersons");
        $this->loadModel("Users");
        $this->loadModel("Lands");
    }

    public function me() {

        $userId = $this->Auth->user('id');
        $user = $this->Users->find()
            ->contain("LandServicePersons")
            ->where([
                $this->Users->aliasField("id") => $userId
            ])->firstOrFail();

        return new ApiOKResponse($user);
    }
}
