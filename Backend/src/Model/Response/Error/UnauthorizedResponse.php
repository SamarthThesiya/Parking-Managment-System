<?php

namespace App\Model\Response\Error;

/**
 * @SWG\Definition(type="object")
 */
class UnauthorizedResponse extends ApiProblemResponse
{
    /**
     * @SWG\Property(description="HTTP status code of the error response", example=401)
     * @var int
     */
    protected $status_code = 401;

    /**
     * @SWG\Property(example="unauthorized")
     * @var string
     */
    protected $code = 'unauthorized';

    /**
     * @SWG\Property(description="A human readable error description, if any")
     * @var string
     */
    protected $message = 'Unauthorized.';

    public function __construct($message = null, $data = null)
    {
        parent::__construct($this->status_code, $this->code, $message ?? $this->message, null, $data);
    }
}
