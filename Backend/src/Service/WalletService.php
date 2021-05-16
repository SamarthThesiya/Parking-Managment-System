<?php


namespace App\Service;

use App\Exceptions\NotAllowedException;

/**
 * Class WalletService
 *
 * @package App\Service
 *
 * @property \App\Model\Table\UsersTable               $Users
 * @property \App\Model\Table\VehicleOwnerWalletsTable $VehicleOwnerWallets
 */
class WalletService extends Service
{
    public function initialize(): void
    {
        parent::initialize();

        $this->loadModel('Users');
        $this->loadModel('VehicleOwnerWallets');
    }

    public function addAmount($userId, float $amount) {
        $wallet = $this->VehicleOwnerWallets->find()
            ->where([
                $this->VehicleOwnerWallets->aliasField("vehicle_owner_id") => $userId,
            ])->first();

        if (empty($wallet)) {
            $wallet                   = $this->VehicleOwnerWallets->newEntity();
            $wallet->vehicle_owner_id = $userId;
            $wallet->wallet_balance   = 0;
        }

        $wallet->wallet_balance += floatval($amount);

        $this->VehicleOwnerWallets->saveOrFail($wallet);

        return $wallet;
    }

    public function deductAmount($userId, float $amount) {

        /** @var \App\Model\Entity\VehicleOwnerWallet $wallet */
        $wallet = $this->VehicleOwnerWallets->find()
            ->where([
                $this->VehicleOwnerWallets->aliasField("vehicle_owner_id") => $userId,
            ])->first();

        if (empty($wallet)) {
            throw new NotAllowedException("Wallet is not created.");
        }

        if ($wallet->wallet_balance < $amount) {
            throw new NotAllowedException("Wallet balance is not sufficient.");
        }

        $wallet->wallet_balance -= floatval($amount);

        $this->VehicleOwnerWallets->saveOrFail($wallet);

        return $wallet->id;
    }
}
