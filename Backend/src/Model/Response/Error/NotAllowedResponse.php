<?php

namespace App\Model\Response\Error;

/**
 * @SWG\Definition(type="object")
 */
class NotAllowedResponse extends ApiProblemResponse
{
    /**
     * @SWG\Property(description="HTTP status code of the error response", example=405)
     * @var int
     */
    protected $status_code = 405;

    /**
     * @SWG\Property(example="not_allowed")
     * @var string
     */
    protected $code = 'not_allowed';

    /**
     * @SWG\Property(description="A human readable error description, if any")
     * @var string
     */
    protected $message = 'Not allowed.';

    public function __construct($message = null, $code = null, $data = null)
    {
        parent::__construct($this->status_code, $code ?? $this->code, $message ?? $this->message, null, $data);
    }
}
