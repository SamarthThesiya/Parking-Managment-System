<?php


namespace App\Model\Enums;


use vijinho\Enums\Enum;

class BookingStatuses extends Enum
{
    const BOOKED                      = 'booked';
    const PARKED                      = 'parked';
    const COMPLETED                   = 'completed';
    const CANCELLED                   = 'cancelled';
    const CANCELLED_BY_SERVICE_PERSON = "cancelled_by_service_person";

    protected static $values = [
        'BOOKED'                      => self::BOOKED,
        'PARKED'                      => self::PARKED,
        'COMPLETED'                   => self::COMPLETED,
        'CANCELLED'                   => self::CANCELLED,
        'CANCELLED_BY_SERVICE_PERSON' => self::CANCELLED_BY_SERVICE_PERSON,
    ];
}
