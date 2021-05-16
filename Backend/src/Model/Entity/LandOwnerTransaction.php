<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * LandOwnerTransaction Entity
 *
 * @property int $id
 * @property int $land_owner_id
 * @property string $transaction_type
 * @property float $amount
 * @property \Cake\I18n\FrozenTime $created_at
 *
 * @property \App\Model\Entity\User $user
 */
class LandOwnerTransaction extends Entity
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
        'land_owner_id' => true,
        'transaction_type' => true,
        'amount' => true,
        'created_at' => true,
        'user' => true
    ];
}
