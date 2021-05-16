<?php


namespace App\Model\Validation;


class LandOwnerWithdrawValidator extends BaseValidator
{
    public function __construct()
    {
        parent::__construct();

        $this->requirePresence('amount');
        $this->decimal("amount");
    }
}
