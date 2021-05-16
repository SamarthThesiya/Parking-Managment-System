<?php

namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * Vehicle Entity
 *
 * @property int                           $id
 * @property int                           $type_id
 * @property string                        $brand
 * @property string                        $model
 * @property string                        $registration_number
 * @property int                           $image_id
 * @property int                           $owner_id
 *
 * @property \App\Model\Entity\VehicleType $vehicle_type
 * @property \App\Model\Entity\Document    $document
 * @property \App\Model\Entity\User        $user
 */
class Vehicle extends Entity
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
        'type_id'             => true,
        'brand'               => true,
        'model'               => true,
        'registration_number' => true,
        'image_id'            => true,
        'owner_id'            => true,
        'vehicle_type'        => true,
        'document'            => true,
        'user'                => true,
    ];
}
