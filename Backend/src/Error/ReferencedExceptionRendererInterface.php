<?php

namespace App\Error;

use Cake\Error\ExceptionRendererInterface;

interface ReferencedExceptionRendererInterface extends ExceptionRendererInterface
{
    /**
     * Sets exception reference id to be added to the response for the exception.
     *
     * @param string $referenceId The exception reference id
     * @return void
     */
    public function setExceptionReferenceId(string $referenceId);
}
