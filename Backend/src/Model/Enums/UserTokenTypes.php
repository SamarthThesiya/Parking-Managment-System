<?php


namespace App\Model\Enums;


use vijinho\Enums\Enum;

class UserTokenTypes extends Enum
{
    const ACCESS_TOKEN = 'access_token';

    protected static $values = [
        'ACCESS_TOKEN' => self::ACCESS_TOKEN,
    ];
}
