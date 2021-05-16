<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * User Entity
 *
 * @property int                           $id
 * @property string                        $username
 * @property string                        $phone
 * @property int                           $role_id
 *
 * @property \App\Model\Entity\UserToken[] $user_tokens
 * @property \App\Model\Entity\Role        $role
 */
class User extends Entity
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
        'username'    => true,
        'phone'       => true,
        'role'        => true,
        'user_tokens' => true,
        'role_id'     => true,
    ];
}
