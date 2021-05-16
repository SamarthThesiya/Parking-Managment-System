<?php

namespace App\Model\Response\Error;

use App\Model\Response\ApiErrorResponse;

/**
 * @SWG\Definition(type="object")
 */
class ApiProblemResponse extends ApiErrorResponse
{
    /**
     * @SWG\Property(description="HTTP status code of the error response", example=500)
     * @var int
     */
    protected $status_code;

    /**
     * @SWG\Property(example="internal_error")
     * @var string
     */
    protected $code;

    /**
     * @SWG\Property(description="A human readable error description, if any", default="Internal error occurred")
     * @var string
     */
    protected $message = 'Internal error occurred';

    /**
     * @SWG\Property(description="A hash of field names that have validation errors.")
     * @var object
     */
    protected $fields = null;

    /**
     * @SWG\Property(description="An optional data (either hash or array) to be passed with the error.")
     * @var object
     */
    protected $data = null;

    public function getErrorCode()
    {
        return $this->code;
    }

    public function getErrorMessage()
    {
        return $this->message;
    }

    public function getErrorFields()
    {
        return $this->fields;
    }

    public function getErrorData()
    {
        return $this->data;
    }

    public function __construct(
        int $httpStatusCode = 500,
        string $code = null,
        string $message = '',
        $fields = null,
        $data = null,
        $mimeType = null
    ) {
        parent::__construct($httpStatusCode, $code, $message, $fields, $data, null, $mimeType);
    }
}
