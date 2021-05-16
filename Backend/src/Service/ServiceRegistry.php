<?php

namespace App\Service;

use App\Exception\MissingServiceException;
use Cake\Core\App;
use Cake\Core\ObjectRegistry;

/**
 * Registry for Service. Provides features for lazily loading services.
 *
 * @package App\Service
 */
class ServiceRegistry extends ObjectRegistry
{
    /**
     * @var self
     */
    private static $instance;

    private function __construct()
    {
    }

    /**
     * Resolve a service classname.
     *
     * Part of the template method for Cake\Core\ObjectRegistry::load()
     *
     * @param string $class Partial classname to resolve.
     *
     * @return string|false Either the correct classname or false.
     */
    protected function _resolveClassName($class)
    {
        return App::className($class, 'Service', 'Service');
    }

    /**
     * Throws an exception when a service is missing.
     *
     * Part of the template method for Cake\Core\ObjectRegistry::load()
     * and Cake\Core\ObjectRegistry::unload()
     *
     * @param string $class  The classname that is missing.
     * @param string $plugin The plugin the task is missing in.
     *
     * @return void
     * @throws \Cake\Console\Exception\MissingTaskException
     */
    protected function _throwMissingClassError($class, $plugin)
    {
        throw new MissingServiceException([
            'class'  => $class,
            'plugin' => $plugin,
        ]);
    }

    /**
     * Create the service instance.
     *
     * Part of the template method for Cake\Core\ObjectRegistry::load()
     *
     * @param string $class    The classname to create.
     * @param string $alias    The alias of the service.
     * @param array  $settings An array of settings to use for the service.
     *
     * @return \App\Service\Service The constructed service class.
     */
    protected function _create($class, $alias, $settings)
    {
        /** @var \App\Service\Service $instance */
        $instance              = new $class($settings);
        $this->_loaded[$alias] = $instance;
        $instance->initialize();

        return $instance;
    }

    /**
     * Loads/constructs an object instance.
     *
     * Will return the instance in the registry if it already exists.
     * If a subclass provides event support, you can use `$config['enabled'] = false`
     * to exclude constructed objects from being registered for events.
     *
     * Using Cake\Controller\Controller::$components as an example. You can alias
     * an object by setting the 'className' key, i.e.,
     *
     * ```
     * public $components = [
     *   'Email' => [
     *     'className' => '\App\Controller\Component\AliasedEmailComponent'
     *   ];
     * ];
     * ```
     *
     * All calls to the `Email` component would use `AliasedEmail` instead.
     *
     * @param string $objectName The name/class of the object to load.
     * @param array  $config     Additional settings to use when loading the object.
     *
     * @return mixed
     */
    public function load($objectName, $config = [])
    {
        if (is_array($config) && isset($config['className'])) {
            $name       = $objectName;
            $objectName = $config['className'];
        } else {
            list(, $name) = pluginSplit($objectName);
        }

        $isLoaded = isset($this->_loaded[$name]);
        if (true === $isLoaded) {
            return $this->_loaded[$name];
        }

        $className = $this->_resolveClassName($objectName);
        if (!$className || (is_string($className) && !class_exists($className))) {
            list($plugin, $objectName) = pluginSplit($objectName);
            $this->_throwMissingClassError($objectName, $plugin);
        }

        return $this->_create($className, $name, $config);
    }

    public static function getInstance(): self
    {
        if (null === self::$instance) {
            self::$instance = new static();
        }

        return self::$instance;
    }

    public static function clear()
    {
        self::$instance = null;
    }
}
