<?php

namespace App\Model\Base;

class JsonModel extends ObjectArrayAccess implements \JsonSerializable
{
    /**
     * Properties to be excluded when serializing model to json
     *
     * @var array
     */
    protected $_hidden = [];

    public function __construct(array $properties = null)
    {
        if (!empty($properties)) {
            $this->fromJson($properties, $this);
        }
    }

    public static function createFromJson($data = null, $target = null, bool $exceptionOnMissingData = true)
    {
        if (empty($data)) {
            return null;
        }

        $mapper                            = new \JsonMapper();
        $mapper->bEnforceMapType           = false;
        $mapper->bExceptionOnMissingData   = $exceptionOnMissingData;
        $mapper->bStrictObjectTypeChecking = true;

        return $mapper->map($data, $target ?? new static());
    }

    public static function createManyFromJson(
        $data = null,
        \ArrayObject &$target = null,
        bool $exceptionOnMissingData = true,
        string $className = null
    ): array {
        if (empty($data)) {
            return null;
        }

        $mapper                            = new \JsonMapper();
        $mapper->bEnforceMapType           = false;
        $mapper->bExceptionOnMissingData   = $exceptionOnMissingData;
        $mapper->bStrictObjectTypeChecking = true;

        return $mapper->mapArray($data, $target ?: [], $className ?? static::class);
    }

    public static function prepareForSerialization(array $data = null)
    {
        return $data;
    }

    public static function prepareForDeserialization(array $data = null)
    {
        return $data;
    }

    protected function fromJson($data = null, $target = null, bool $exceptionOnMissingData = true)
    {
        return static::createFromJson($data, $target, $exceptionOnMissingData);
    }

    protected function manyFromJson(
        $data = null,
        \ArrayObject &$target = null,
        bool $exceptionOnMissingData = true
    ): array {
        return static::createManyFromJson($data, $target, $exceptionOnMissingData);
    }

    public function updateFromJson($data = null, $target = null, bool $exceptionOnMissingData = true)
    {
        return $this->fromJson($data, $target, $exceptionOnMissingData);
    }

    /**
     * @inheritdoc
     */
    public function jsonSerialize()
    {
        return static::prepareForSerialization($this->toArray());
    }

    /**
     * Serializes object to array
     *
     * @return array representation of object
     */
    public function toArray(): array
    {
        $properties = (new \ReflectionObject($this))->getProperties(\ReflectionProperty::IS_PUBLIC);
        $result     = [];
        foreach ($properties as $property) {
            $propertyName = $property->name;
            if (!empty($this->_hidden) && in_array($propertyName, $this->_hidden)) {
                continue;
            }
            $result[$propertyName] = $this->$propertyName;
        }

        return $result;
    }
}
