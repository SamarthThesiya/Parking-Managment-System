<?php
namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * LandServicePerson Entity
 *
 * @property int $id
 * @property int $land_id
 * @property int $service_person_id
 *
 * @property \App\Model\Entity\Land $land
 * @property \App\Model\Entity\User $user
 */
class LandServicePerson extends Entity
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
        'land_id' => true,
        'service_person_id' => true,
        'land' => true,
        'user' => true
    ];
}
