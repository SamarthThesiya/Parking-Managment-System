<?php


namespace App\Model\Validation;


class LoginValidator extends BaseValidator
{
    public function __construct()
    {
        parent::__construct();

        $this->requirePresence('user_name');
        $this->requirePresence('password');
    }
}
