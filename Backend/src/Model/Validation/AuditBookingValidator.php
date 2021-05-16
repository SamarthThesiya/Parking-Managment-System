<?php


namespace App\Model\Validation;


use App\Model\Enums\BookingStatuses;

class AuditBookingValidator extends BaseValidator
{
    public function __construct()
    {
        parent::__construct();

        $this->requirePresence("result");
        $this->requirePresence("denial_reason", function ($context) {
            if (isset($context['data']['result']) && $context['data']['result'] == 'deny') {
                return true;
            }

            return false;
        });
        $this->allowEmpty("denial_reason", function ($context) {
            if (isset($context['data']['result']) && $context['data']['result'] == 'allow') {
                return true;
            }

            return false;
        });
        $this->inList("result", ["deny", "allow"], "Allowed values are 'deny' or 'allow'.");
    }
}
