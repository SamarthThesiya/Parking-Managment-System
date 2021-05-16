<?php

namespace App\Model\Response\Error;

/**
 * @SWG\Definition(type="object")
 */
class TooManyRequestsResponse extends ApiProblemResponse
{
    /**
     * @SWG\Property(description="HTTP status code of the error response", example=429)
     * @var int
     */
    protected $status_code = 429;

    /**
     * @SWG\Property(example="not_found")
     * @var string
     */
    protected $code = 'too_may_requests';

    /**
     * @SWG\Property(description="A human readable error description, if any")
     * @var string
     */
    protected $message = 'You have reached requests limit. Please try again later.';

    public function __construct($message = null, $data = null)
    {
        parent::__construct($this->status_code, $this->code, $message ?? $this->message, null, $data);
    }
}
