<?php

namespace App\Model\Entity;

use App\Service\JwtHelperService;
use Cake\ORM\Entity;
use Cake\Utility\Security;

/**
 * LandBooking Entity
 *
 * @property int                             $id
 * @property int                             $user_id
 * @property int                             $land_id
 * @property \Cake\I18n\Time                 $start_time
 * @property \Cake\I18n\Time                 $end_time
 * @property string                          $booking_fee
 * @property int                             $status_id
 * @property int                             $vehicle_id
 * @property string                          $booking_token
 * @property string                          $denial_reason
 *
 * @property \App\Model\Entity\User          $user
 * @property \App\Model\Entity\Land          $land
 * @property \App\Model\Entity\BookingStatus $booking_status
 * @property \App\Model\Entity\Vehicle       $vehicle
 */
class LandBooking extends Entity
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
        'user_id'        => true,
        'land_id'        => true,
        'start_time'     => true,
        'end_time'       => true,
        'booking_fee'    => true,
        'status_id'      => true,
        'vehicle_id'     => true,
        'user'           => true,
        'land'           => true,
        'booking_status' => true,
        'vehicle'        => true,
        'booking_token'  => true,
        'denial_reason'  => true,
    ];

    public function setBookingToken() {
        $jwt = new JwtHelperService();
        $this->booking_token = $jwt->encodeToken([
            'bid' => Security::hash($this->start_time . $this->vehicle_id, 'sha256')
        ]);
    }

}
