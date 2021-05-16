<?php

namespace App\Service;

/**
 * Trait ServiceAwareTrait
 *
 * @package App\Service
 */
trait ServiceAwareTrait
{
    /**
     * Array containing the names of services this controller uses. Service names
     * should not contain the "Service" portion of the class name.
     *
     * Example:
     * ```
     * public $services = ['UserDocuments','Auth'];
     * ```
     *
     * @var array
     */
    public $services = [];

    /**
     * Loads the defined services using the Service factory.
     */
    protected function _loadServices(): void
    {
        if (empty($this->services)) {
            return;
        }
        $registry = ServiceRegistry::getInstance();
        $services = $registry->normalizeArray($this->services);
        foreach ($services as $properties) {
            $this->loadService($properties['class'], $properties['config']);
        }
    }

    /**
     * Add a service to the objects's registry.
     *
     * This method will also set the service to a property.
     * For example:
     *
     * ```
     * $this->loadService('Documents.UserDocuments');
     * ```
     *
     * Will result in a `UserDocuments` property being set.
     *
     * @param string $name   The name of the service to load.
     * @param array  $config The config for the service.
     *
     * @return static
     */
    public function loadService($name, array $config = [])
    {
        list(, $prop) = pluginSplit($name);

        return $this->{$prop} = ServiceRegistry::getInstance()->load($name, $config);
    }
}
