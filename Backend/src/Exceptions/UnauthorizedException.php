<?php


namespace App\Exceptions;


use App\Exceptions\ExtendedException;
use App\Model\Response\Error\UnauthorizedResponse;
use Cake\Http\Response;

class UnauthorizedException extends ExtendedException
{
    public function toResponse(): Response
    {
        return new UnauthorizedResponse($this->getMessage(), $this->getErrorData());
    }
}
