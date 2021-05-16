<?php

namespace App\Exception;

use Cake\Core\Exception\Exception;

/**
 * Used when a Task cannot be found.
 *
 * @package App\Exception
 */
class MissingServiceException extends Exception
{
    protected $_messageTemplate = 'Service class %s could not be found.';
}
