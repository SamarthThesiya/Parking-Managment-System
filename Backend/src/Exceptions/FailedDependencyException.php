<?php

namespace App\Exceptions;

use App\Model\Response\Error\FailedDependencyResponse;
use Cake\Http\Response;

/**
 * Class FailedDependencyException
 *
 * @package App\Exception
 */
class FailedDependencyException extends ExtendedException
{
    /**
     * @inheritdoc
     */
    public function toResponse(): Response
    {
        return new FailedDependencyResponse($this->getMessage(), $this->getErrorCode(), $this->getErrorData());
    }
}
