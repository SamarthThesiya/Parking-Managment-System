<?php


namespace App\Controller\V1\VehicleOwner;


use App\Controller\ApiController;
use App\Model\Response\ApiOKResponse;
use App\Model\Validation\CreateOrUpdateWalletValidator;

/**
 * Class UsersController
 *
 * @package App\Controller\V1\VehicleOwner
 *
 * @property \App\Model\Table\UsersTable               $Users
 * @property \App\Model\Table\VehicleOwnerWalletsTable $VehicleOwnerWallets
 *
 * @property \App\Service\WalletService                $Wallet
 */
class UsersController extends ApiController
{
    public $services = [
        "Wallet"
    ];

    public function initialize()
    {
        parent::initialize();
        $this->Crud->enable(['index', 'view', 'add', 'edit']);

        $this->loadModel('Users');
        $this->loadModel('VehicleOwnerWallets');

        $this->RequestValidator->addRequestDataValidators([
            'createOrUpdateWallet' => CreateOrUpdateWalletValidator::class,
        ]);
    }

    public function createOrUpdateWallet()
    {
        $userId = $this->Auth->user('id');
        $amount = $this->request->getData("add_amount");

        $wallet = $this->Wallet->addAmount($userId, $amount);

        return new ApiOKResponse($wallet);
    }

    public function getOrCreateWallet() {
        $userId = $this->Auth->user('id');

        $wallet = $this->Wallet->addAmount($userId, 0);

        return new ApiOKResponse($wallet);
    }
}
