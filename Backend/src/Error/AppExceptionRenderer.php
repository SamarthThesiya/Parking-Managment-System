<?php

namespace App\Error;

use App\Model\Response\Error\ApiFatalErrorResponse;
use Cake\Core\Configure;
use Cake\Error\Debugger;
use Cake\Error\ExceptionRenderer;
use Cake\Network\Exception\HttpException;
use Cake\Core\Exception\Exception as CakeException;

class AppExceptionRenderer extends ExceptionRenderer implements ReferencedExceptionRendererInterface
{
    /**
     * @var string
     */
    protected $exceptionReferenceId = null;

    /**
     * @inheritdoc
     */
    public function setExceptionReferenceId(string $referenceId)
    {
        $this->exceptionReferenceId = $referenceId;
    }

    /**
     * @inheritdoc
     */
    public function render()
    {
        $exception = $this->error;
        $code = $this->_code($exception);
        $method = $this->_method($exception);
        $template = $this->_template($exception, $method, $code);
        $unwrapped = $this->_unwrap($exception);

        $isDebug = Configure::read('debug');

        if ($this->controller->request->accepts('application/json')) {
            $httpCode = $this->_code($exception);
            $message = $exception->getMessage();

            $response = new ApiFatalErrorResponse($httpCode, null, $message, null, null, $this->exceptionReferenceId);
            return $response;
        }

        if (($isDebug || $exception instanceof HttpException) &&
            method_exists($this, $method)
        ) {
            return $this->_customMethod($method, $unwrapped);
        }

        $message = $this->_message($exception, $code);
        $url = $this->controller->request->getRequestTarget();

        if (method_exists($exception, 'responseHeader')) {
            $this->controller->response->header($exception->responseHeader());
        }
        $this->controller->response->statusCode($code);

        $viewVars = [
            'message' => $message,
            'url' => h($url),
            'error' => $unwrapped,
            'code' => $code,
            '_serialize' => ['message', 'url', 'code']
        ];

        if ($isDebug) {
            $viewVars['trace'] = Debugger::formatTrace($unwrapped->getTrace(), [
                'format' => 'array',
                'args' => false
            ]);
        }
        $this->controller->set($viewVars);

        if ($unwrapped instanceof CakeException && $isDebug) {
            $this->controller->set($unwrapped->getAttributes());
        }

        return $this->_outputMessage($template);
    }
}
