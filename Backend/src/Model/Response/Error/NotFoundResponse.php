<?php

namespace App\Model\Response\Error;

/**
 * @SWG\Definition(type="object")
 */
class NotFoundResponse extends ApiProblemResponse
{
    /**
     * @SWG\Property(description="HTTP status code of the error response", example=404)
     * @var int
     */
    protected $status_code = 404;

    /**
     * @SWG\Property(example="not_found")
     * @var string
     */
    protected $code = 'not_found';

    /**
     * @SWG\Property(description="A human readable error description, if any")
     * @var string
     */
    protected $message = 'Not Found.';

    public function __construct($message = null, $data = null)
    {
        parent::__construct($this->status_code, $this->code, $message ?? $this->message, null, $data);
    }
}
