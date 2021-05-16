<?php

namespace App\Model\Response\Error;

/**
 * @SWG\Definition(type="object")
 */
class ValidationErrorResponse extends ApiProblemResponse
{
    /**
     * @SWG\Property(description="HTTP status code of the error response", example=422)
     * @var int
     */
    protected $status_code = 422;

    /**
     * @SWG\Property(example="invalid_request")
     * @var string
     */
    protected $code = 'invalid_request';

    /**
     * @SWG\Property(description="A human readable error description, if any")
     * @var string
     */
    protected $message = 'Invalid request.';

    public function __construct($fields = null, $message = null, $data = null, $mimeType = null)
    {
        parent::__construct($this->status_code, $this->code, $message ?? $this->message, $fields, $data, $mimeType);
    }
}
