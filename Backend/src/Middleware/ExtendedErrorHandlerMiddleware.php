<?php
/**
 * Created by PhpStorm.
 * User: jaydee
 * Date: 10.03.17
 * Time: 14:12
 */

namespace App\Middleware;

use App\Core\ResponseConvertibleInterface;
use App\Error\ReferencedExceptionRendererInterface;
use App\Model\Response\Error\ApiFatalErrorResponse;
use Cake\Core\Configure;
use Cake\Error\Middleware\ErrorHandlerMiddleware;
use Cake\Log\Log;
use Cake\Utility\Text;
use Psr\Http\Message\MessageInterface;
use Psr\Http\Message\ServerRequestInterface;

/**
 * Extended error handling middleware.
 *
 * Traps exceptions and converts them into HTML or content-type appropriate
 * error pages using the CakePHP ExceptionRenderer.
 *
 * Extends CakePHP's standard error handler middleware, wraps the exception and
 * adds reference id to messages generated for logs
 */
class ExtendedErrorHandlerMiddleware extends ErrorHandlerMiddleware
{
    protected $exceptionReferenceId = null;

    /**
     * @inheritdoc
     */
    public function __invoke($request, $response, $next)
    {
        try {
            return $next($request, $response);
        } catch (ResponseConvertibleInterface $e) {
            return $e->toResponse();
        } catch (\Exception $e) {
            if ($this->exceptionMustBeLogged($e)) {
                $this->exceptionReferenceId = Text::uuid();
            }
            return new ApiFatalErrorResponse(500, 'internal_server_error', $e->getMessage());
        }
    }

    /**
     * @inheritdoc
     */
    protected function getRenderer($exception)
    {
        // The following is to to handle cases when we need to switch to custom exception renderer at runtime
        if (Configure::read('Error.exceptionRenderer') !== $this->getConfig('exceptionRenderer')) {
            $this->setConfig('exceptionRenderer', Configure::read('Error.exceptionRenderer'));
        }

        $renderer = parent::getRenderer($exception);
        if ($this->exceptionReferenceId && $renderer instanceof ReferencedExceptionRendererInterface) {
            $renderer->setExceptionReferenceId($this->exceptionReferenceId);
        }
        return $renderer;
    }

    /**
     * @inheritdoc
     */
    protected function logException($request, $exception)
    {
        if (!$this->exceptionMustBeLogged($exception)) {
            return;
        }

        // Exception with the stacktrace logged to error log
        Log::error($this->getMessage($request, $exception));

        // For debug purposes also logging request to separate api debug log
        Log::debug($this->getRequestAsString($request, $this->exceptionReferenceId), ['scope' => ['api_debug']]);
    }

    /**
     * @inheritdoc
     */
    protected function getMessage($request, $exception)
    {
        $message = "Reference Id: " . $this->exceptionReferenceId;

        $message .= sprintf(
            "\nException [%s] %s",
            get_class($exception),
            $exception->getMessage()
        );

        $debug = Configure::read('debug');
        if ($debug && method_exists($exception, 'getAttributes')) {
            $attributes = $exception->getAttributes();
            if ($attributes) {
                $message .= "\nException Attributes: " . var_export($exception->getAttributes(), true);
            }
        }
        $message .= "\nRequest Method: " .  $request->getMethod() . ", Request URL: " . $request->getRequestTarget();
        $referer = $request->getHeaderLine('Referer');
        if ($referer) {
            $message .= "\nReferer URL: " . $referer;
        }
        if ($this->getConfig('trace')) {
            $message .= "\nFile: " . $exception->getFile() . ", Line: " . $exception->getLine();
            $message .= "\nStack Trace:\n" . $exception->getTraceAsString() . "\n\n";
        }

        return $message;
    }

    /**
     * @inheritdoc
     */
    protected function getRequestAsString(ServerRequestInterface $request, string $referenceId)
    {
        $message = "Reference Id: " . $referenceId . "\n";
        $message .= "Method: " .  $request->getMethod() . ", URL: " . $request->getRequestTarget();

        $referer = $request->getHeaderLine('Referer');
        if ($referer) {
            $message .= "\nReferer URL: " . $referer;
        }

        $message .= self::getHTTPMessageAsString($request);
        $message .= "\n\n";

        return $message;
    }

    /**
     * Convenience method to convert http message to string.
     *
     * @param \Psr\Http\Message\MessageInterface $httpMessage HTTP message
     * @return string String representation of http message
     */
    private function getHTTPMessageAsString(MessageInterface $httpMessage) : string
    {
        $message = '';
        $headers = $httpMessage->getHeaders();
        if (!empty($headers)) {
            foreach ($headers as $name => $values) {
                $message .= "\n" . $name . ': ' . implode(', ', $values);
            }
        }
        $body = (string) $httpMessage->getBody();
        if (strlen($body) > 0) {
            $message .= "\n" . $body;
        }
        return $message;
    }

    /**
     * Return whether exception must be logged or not.
     * @param \Exception $exception The exception to handle.
     * @return bool Whether exception must be logged or not.
     */
    protected function exceptionMustBeLogged(\Exception $exception) : bool
    {
        if (!$this->getConfig('log')) {
            return true;
        }

        $skipLog = $this->getConfig('skipLog');
        if ($skipLog) {
            foreach ((array)$skipLog as $class) {
                if ($exception instanceof $class) {
                    return false;
                }
            }
        }
        return true;
    }
}
