<?php

namespace App\Model\Response\Error;

/**
 * @SWG\Definition(type="object")
 */
class ForbiddenResponse extends ApiProblemResponse
{
    /**
     * @SWG\Property(description="HTTP status code of the error response", example=403)
     * @var int
     */
    protected $status_code = 403;

    /**
     * @SWG\Property(example="forbidden")
     * @var string
     */
    protected $code = 'forbidden';

    /**
     * @SWG\Property(description="A human readable error description, if any")
     * @var string
     */
    protected $message = 'Forbidden.';

    public function __construct($message = null, $code = null, $data = null)
    {
        parent::__construct($this->status_code, $code ?? $this->code, $message ?? $this->message, null, $data);
    }
}
