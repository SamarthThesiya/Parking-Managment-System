<?php


namespace App\Model\Validation;


class SignOnValidation extends BaseValidator
{
    public function __construct()
    {
        parent::__construct();

        $this->requirePresence('phone');
        $this->requirePresence('username');
        $this->requirePresence('role_id');

        $this->existsInTable('role_id', 'roles', 'id');
    }
}
