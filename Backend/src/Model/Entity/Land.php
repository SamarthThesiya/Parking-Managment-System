<?php

namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * Land Entity
 *
 * @property int                          $id
 * @property string                       $title
 * @property string                       $length
 * @property string                       $width
 * @property string                       $description
 * @property int                          $s_to_n_img
 * @property int                          $n_to_s_img
 * @property int                          $e_to_w_img
 * @property int                          $w_to_e_img
 * @property float                        $latitude
 * @property float                        $longitude
 * @property string                       $address_line_1
 * @property string                       $address_line_2
 * @property string                       $area
 * @property string                       $city
 * @property string                       $state
 * @property string                       $zip_code
 * @property string                       $price_4w_100_to_50
 * @property string                       $price_4w_50_to_10
 * @property string                       $price_4w_less_than_10
 * @property string                       $price_2w_100_to_50
 * @property string                       $price_2w_50_to_10
 * @property string                       $price_2w_less_than_10
 * @property int                          $status_id
 * @property int                          $owner_id
 * @property int                          $auditor_id
 * @property string                       $auditor_comment
 * @property \Cake\I18n\FrozenTime        $created_at
 * @property \Cake\I18n\FrozenTime        $modified_at
 *
 * @property \App\Model\Entity\LandStatus $land_status
 * @property \App\Model\Entity\User       $Owner
 * @property \App\Model\Entity\User       $Auditor
 */
class Land extends Entity
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
        '*'  => true,
        'id' => false,
    ];
}
