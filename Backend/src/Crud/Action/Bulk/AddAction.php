<?php
namespace App\Crud\Action\Bulk;

use Cake\Controller\Controller;
use Cake\Network\Request;
use Cake\ORM\Query;
use Crud\Action\Bulk\BaseAction;
use Crud\Error\Exception\ValidationException;

/**
 * Handles 'Add' Crud actions
 */
class AddAction extends BaseAction
{
    const REQUEST_KEY = 'bulkAdd';

    public function __construct(Controller $Controller, $config = [])
    {
        $this->_defaultConfig['api'] = [
            'success' => [
                'code' => 201
            ]
        ];

        parent::__construct($Controller, $config);
    }

    /**
     * Handle a bulk event
     *
     * @param \Cake\ORM\Query|null $query The query to act upon
     *
     * @return bool
     */
    protected function _bulk(Query $query = null)
    {
        /** @var Request */
        $request = $this->_request();
        $data = $request->getData(self::REQUEST_KEY);

        if (!empty($data)) {
            foreach ($data as $entityData) {
                $newEntity = $this->_entity($entityData);
                $this->_table()->save($newEntity);

                if (!empty($newEntity->getErrors())) {
                    throw new ValidationException($newEntity);
                }
            }
        }
        return true;
    }

    /**
     * @inheritdoc
     */
    protected function _processIds()
    {
        return [];
    }
}
