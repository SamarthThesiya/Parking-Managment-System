<?php

namespace App\Exceptions;

use App\Model\Response\Error\NotAllowedResponse;
use Cake\Http\Response;

/**
 * Class NotAllowedException
 *
 * @package App\Exception
 */
class NotAllowedException extends ExtendedException
{
    /**
     * @inheritdoc
     */
    public function toResponse(): Response
    {
        return new NotAllowedResponse($this->getMessage(), $this->getErrorCode(), $this->getErrorData());
    }
}
