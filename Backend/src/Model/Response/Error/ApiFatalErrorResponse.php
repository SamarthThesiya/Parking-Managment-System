<?php

namespace App\Model\Response\Error;

use App\Model\Response\ApiErrorResponse;

/**
 * @SWG\Definition(type="object")
 */
class ApiFatalErrorResponse extends ApiErrorResponse
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
     * @SWG\Property(description="Reference Id that identifies this concrete error instance.", example="RefID")
     * @var string
     */
    protected $referenceId = null;

    public function __construct(
        int $httpStatusCode = 500,
        string $code = null,
        string $message = '',
        $fields = null,
        $data = null,
        string $referenceId = null
    ) {
        parent::__construct($httpStatusCode, $code, $message, $fields, $data, $referenceId);
    }
}
