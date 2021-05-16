<?php


namespace App\Model\Validation;


class CreateOrUpdateWalletValidator extends BaseValidator
{
    public function __construct()
    {
        parent::__construct();

        $this->requirePresence("add_amount");
        $this->decimal("add_amount");
    }
}
