<?php


namespace App\Model\Enums;


use vijinho\Enums\Enum;

class LandStatuses extends Enum
{
    const PENDING_APPROVAL         = 'pending_approval';
    const APPROVED_AND_ACTIVATED   = 'approved_and_activated';
    const APPROVED_AND_DEACTIVATED = 'approved_and_deactivated';
    const REJECTED                 = 'rejected';
    const DRAFTED                  = 'drafted';
    const REQUEST_CANCELLED        = 'request_cancelled';

    protected static $values = [
        'PENDING_APPROVAL'         => self::PENDING_APPROVAL,
        'APPROVED_AND_ACTIVATED'   => self::APPROVED_AND_ACTIVATED,
        'APPROVED_AND_DEACTIVATED' => self::APPROVED_AND_DEACTIVATED,
        'REJECTED'                 => self::REJECTED,
        'DRAFTED'                  => self::DRAFTED,
        'REQUEST_CANCELLED'        => self::REQUEST_CANCELLED,
    ];
}
