<?php

namespace App\Exceptions;

use App\Core\ResponseConvertibleInterface;
use App\Model\Response\ApiErrorResponse;
use Cake\Http\Response;
use Cake\Network\Exception\HttpException;

/**
 * Class ExtendedException
 *
 * @package App\Exception
 */
class ExtendedException extends HttpException implements ResponseConvertibleInterface
{
    /**
     * @var string|null
     */
    protected $errorCode = null;

    /**
     * @var mixed|null
     */
    public $fields = null;

    /**
     * @var mixed|null
     */
    public $data = null;

    /**
     * Returns error code.
     *
     * @return null|string
     */
    public function getErrorCode()
    {
        return $this->errorCode;
    }

    /**
     * Returns fields that contains errors.
     *
     * @return mixed|null
     */
    public function getErrorFields()
    {
        return $this->fields;
    }

    /**
     * Returns additional data for the error.
     *
     * @return mixed|null
     */
    public function getErrorData()
    {
        return $this->data;
    }

    /**
     * ExtendedException constructor.
     *
     * @param string          $message     Error message.
     * @param mixed|null      $errorCode   Error code.
     * @param mixed|null      $errorData   Error data.
     * @param mixed|null      $errorFields Error fields.
     * @param int             $code        HTTP Status code.
     * @param \Exception|null $previous    Previous exception.
     */
    public function __construct(
        $message = '',
        $errorCode = null,
        $errorData = null,
        $errorFields = null,
        $code = 0,
        \Exception $previous = null
    ) {
        $this->errorCode = $errorCode;
        $this->data      = $errorData;
        $this->fields    = $errorFields;

        parent::__construct($message, $code, $previous);
    }

    /**
     * @inheritdoc
     */
    public function toResponse(): Response
    {
        return new ApiErrorResponse(
            $this->getCode(),
            $this->getErrorCode(),
            $this->getMessage(),
            $this->getErrorFields(),
            $this->getErrorData()
        );
    }
}
