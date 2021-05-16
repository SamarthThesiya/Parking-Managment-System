<?php

namespace App\Service;

use App\Event\EventAwareTrait;
use App\Mailer\EmailAwareTrait;
use Cake\Core\InstanceConfigTrait;
use Cake\Datasource\ModelAwareTrait;

/**
 * Base class for services.
 *
 * @package App\Service
 */
abstract class Service
{
    use InstanceConfigTrait;
    use ModelAwareTrait;
    use ServiceAwareTrait;

    /**
     * Default config.
     *
     * @var array
     */
    protected $_defaultConfig = [];

    /**
     * Initialize hook. Called right after constructor.
     */
    public function initialize(): void
    {
        $this->_loadServices();
    }
}
