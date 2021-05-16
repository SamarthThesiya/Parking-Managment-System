<?php


namespace App\Exceptions;


use App\Model\Response\Error\NotFoundResponse;
use Cake\Http\Response;

class NotFoundException extends ExtendedException
{
    /**
     * @inheritdoc
     */
    public function toResponse(): Response
    {
        return new NotFoundResponse($this->getMessage(), $this->getErrorData());
    }
}
