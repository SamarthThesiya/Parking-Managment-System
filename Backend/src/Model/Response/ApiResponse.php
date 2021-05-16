<?php

namespace App\Model\Response;

use Cake\Http\Response;
use Cake\Utility\Xml;

class ApiResponse extends Response
{

    protected function encode($body, $mimeType = 'json'): string
    {
        if (!$body) {
            return '';
        }

        $encoders = [
            'json' => function ($body) {
                return json_encode($body);
            },
            'xml'  => function (array $body) {
                if (1 !== count($body)) {
                    $body = ['response' => $body];
                }
                return Xml::fromArray($body)->asXML();
            },
        ];

        $mimeType = array_key_exists($mimeType, $encoders) ? $mimeType : 'json';
        $encoder = $encoders[$mimeType];

        if (is_callable($encoder)) {
            return call_user_func($encoder, $body);
        }

        return $body;
    }

    public function __construct(int $httpStatusCode = 200, $body = null, string $mimeType = null)
    {
        $mimeType = $mimeType ?? 'json';

        if (false !== strpos($mimeType, '/')) {
            $mimeType = $this->mapType($mimeType);
        }

        $options = [
            'status' => $httpStatusCode,
            'type'   => $mimeType,
            'body'   => $this->encode($body, $mimeType),
        ];
        parent::__construct($options);
    }
}
