<?php

namespace App\Model\Response\Error;

/**
 * @SWG\Definition(type="object")
 */
class FailedDependencyResponse extends ApiProblemResponse
{
    /**
     * @SWG\Property(description="HTTP status code of the error response", example=424)
     * @var int
     */
    protected $status_code = 424;

    /**
     * @SWG\Property(example="failed_dependency")
     * @var string
     */
    protected $code = 'failed_dependency';

    /**
     * @SWG\Property(description="A human readable error description, if any")
     * @var string
     */
    protected $message = 'Failed Dependency.';

    public function __construct($message = null, $code = null, $data = null)
    {
        parent::__construct($this->status_code, $code ?? $this->code, $message ?? $this->message, null, $data);
    }
}
