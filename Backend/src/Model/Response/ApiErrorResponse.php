<?php

namespace App\Model\Response;

use Cake\Utility\Inflector;

class ApiErrorResponse extends ApiResponse
{
    protected $status_code = 500;
    protected $code;
    protected $message = 'Internal error occurred';
    protected $fields = null;
    protected $data = null;
    protected $referenceId = null;

    public function __construct(
        int $httpStatusCode = 500,
        string $code = null,
        string $message = '',
        $fields = null,
        $data = null,
        string $referenceId = null,
        string $mimeType = null
    ) {
        if (!$code) {
            $code = $this->_statusCodes[$httpStatusCode] ?? null;
        }
        $code = $code ? Inflector::delimit(Inflector::camelize($code, ' ')) : null;

        $this->status_code = $httpStatusCode;
        $this->code = $code;
        $this->message = $message;

        $this->fields = $fields;
        $this->data = $data;
        $this->referenceId = $referenceId;

        $body = [
            'status_code' => $this->status_code,
            'code' => $this->code,
            'message' => $this->message
        ];

        if ($this->fields) {
            $body['fields'] = $this->fields;
        }
        if ($this->data) {
            $body['data'] = $this->data;
        }
        if ($this->referenceId) {
            $body['reference_id'] = $this->referenceId;
        }

        parent::__construct($httpStatusCode, $body, $mimeType);
    }
}
