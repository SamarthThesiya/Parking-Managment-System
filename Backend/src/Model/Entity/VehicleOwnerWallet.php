<?php

namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * VehicleOwnerWallet Entity
 *
 * @property int                    $id
 * @property int                    $vehicle_owner_id
 * @property float                  $wallet_balance
 *
 * @property \App\Model\Entity\User $user
 */
class VehicleOwnerWallet extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * Note that when '*' is set to true, this allows all unspecified fields to
     * be mass assigned. For security purposes, it is advised to set '*' to false
     * (or remove it), and explicitly make individual fields accessible as needed.
     *
     * @var array
     */
    protected $_accessible = [
        'vehicle_owner_id' => true,
        'wallet_balance'   => true,
        'user'             => true,
    ];
}
