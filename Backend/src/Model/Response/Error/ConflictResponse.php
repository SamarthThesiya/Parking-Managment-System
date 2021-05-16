<?php

namespace App\Model\Response\Error;

/**
 * @SWG\Definition(type="object")
 */
class ConflictResponse extends ApiProblemResponse
{
    /**
     * @SWG\Property(description="HTTP status code of the error response", example=409)
     * @var int
     */
    protected $status_code = 409;

    /**
     * @SWG\Property(example="conflict")
     * @var string
     */
    protected $code = 'conflict';

    /**
     * @SWG\Property(description="A human readable error description, if any")
     * @var string
     */
    protected $message = 'Conflict.';

    public function __construct($message = null, $data = null)
    {
        parent::__construct($this->status_code, $this->code, $message ?? $this->message, null, $data);
    }
}
