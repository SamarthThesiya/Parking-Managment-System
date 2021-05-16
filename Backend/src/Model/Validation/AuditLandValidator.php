<?php


namespace App\Model\Validation;


class AuditLandValidator extends BaseValidator
{
    public function __construct()
    {
        parent::__construct();

        $this->inList('response', ['approve', 'reject']);

        $this->requirePresence('response');
        $this->requirePresence('comment', function ($context) {
           if (isset($context['data']['response']) && $context['data']['response'] == 'reject') {
               return true;
           }
           return false;
        });

        $this->allowEmpty("comment");
    }
}
