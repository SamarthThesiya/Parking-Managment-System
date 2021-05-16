<?php

namespace App\Model\Response;

/**
 * @SWG\Definition(type="object")
 */
class ApiOKResponse extends ApiResponse
{
    /**
     * @SWG\Property(description="An optional data to be passed with the response. Either hash or array.")
     * @var object
     */
    protected $data = null;

    /**
     * @SWG\Property(description="An optional pagination information.")
     * @var object
     */
    protected $pagination = null;

    protected $httpStatusCode = 200;

    /**
     * Returns response data if any
     * @return null|object
     */
    public function getData()
    {
        return $this->data;
    }

    /**
     * Returns response pagination if any
     * @return null|object
     */
    public function getPagination()
    {
        return $this->pagination;
    }

    public function __construct(
        $data = null,
        $pagination = null,
        int $httpStatusCode = 200,
        string $mimeType = null,
        array $options = []
    )
    {
        $this->httpStatusCode = $httpStatusCode;
        $this->data = $data;
        $this->pagination = $pagination;

        $responseBody = null;

        $responseBody['success'] = true;
        if (null !== $this->data) {
            $root = $options['root'] ?? 'data';
            $responseBody = [$root => $this->data];
            if ($this->pagination) {
                $responseBody['pagination'] = $this->pagination;
            }
            $responseBody['success'] = true;
        }

        parent::__construct($this->httpStatusCode, $responseBody, $mimeType);
    }
}
