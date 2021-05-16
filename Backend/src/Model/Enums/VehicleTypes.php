<?php


namespace App\Model\Enums;


use vijinho\Enums\Enum;

class VehicleTypes extends Enum
{
    const TWO_WHEELER  = '2_wheeler';
    const FOUR_WHEELER = '4_wheeler';

    protected static $values = [
        'TWO_WHEELER'  => self::TWO_WHEELER,
        'FOUR_WHEELER' => self::FOUR_WHEELER,
    ];
}
