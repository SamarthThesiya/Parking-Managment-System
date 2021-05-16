<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * UserToken Entity
 *
 * @property int                    $id
 * @property string                 $token_type
 * @property int                    $user_id
 * @property string                 $token_value
 * @property \Cake\I18n\FrozenTime  $created_at
 * @property \Cake\I18n\FrozenTime  $modified_at
 * @property \Cake\I18n\FrozenTime  $expired_at
 *
 * @property \App\Model\Entity\User $user
 */
class UserToken extends Entity
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
        'token_type'  => true,
        'user_id'     => true,
        'token_value' => true,
        'created_at'  => true,
        'modified_at' => true,
        'expired_at'  => true,
        'user'        => true,
    ];
}
