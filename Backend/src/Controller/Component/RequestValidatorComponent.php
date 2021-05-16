<?php

namespace App\Controller\Component;

use App\Exception\ApiProblemException;
use App\Model\Response\ApiOKResponse;
use App\Model\Response\Error\ValidationErrorResponse;
use Cake\Controller\Component;
use Cake\Controller\Controller;
use Cake\Core\App;
use Cake\Event\Event;
use Cake\Utility\Hash;
use Cake\Validation\Validator;

class RequestValidatorComponent extends Component
{
    /**
     * An array which defines the request data validators for controller actions.
     *
     * @var array
     */
    protected $validators;

    /**
     * @inheritdoc
     */
    public function implementedEvents()
    {
        return [
            'Controller.startup' => 'startup'
        ];
    }

    /**
     * @inheritdoc
     */
    public function initialize(array $config)
    {
        $this->validators = [];
        $this->addValidators($config ?? []);
    }

    /**
     * Component startup hook
     *
     * @param Event $event event that triggered this hook call
     * @return ValidationErrorResponse|null
     */
    public function startup(Event $event)
    {
        /** @var \Cake\Http\ServerRequest $request */
        $request = $event->getSubject()->request;

        if (!empty($request->getBody()->getSize()) && JSON_ERROR_NONE !== json_last_error()) {
            throw new ApiProblemException('Malformed request', 400);
        }
        if (empty($this->validators)) {
            return null;
        }
        return $this->validateRequest($event);
    }

    public function addRequestDataValidators(array $config) : void
    {
        $this->addValidators($config);
    }

    protected function addValidators(array $config) : void
    {
        if (empty($config)) {
            return;
        }
        foreach ($config as $actionName => $actionConfig) {
            if (is_array($actionConfig)) {
                $this->mapValidatorToAction(
                    $actionName,
                    Hash::get($actionConfig, 'validator'),
                    Hash::get($actionConfig, 'method')
                );
            } elseif (is_string($actionConfig)) {
                $this->mapValidatorToAction(
                    $actionName,
                    $actionConfig
                );
            }
        }
    }

    /**
     * Returns string to be used as key for mapping validator to specific action (and request method if any)
     *
     * @param string $action controller action
     * @param string|null $requestMethod request method
     * @return string key to be used for mapping validator to specific action (and request method if any)
     */
    protected function getActionToValidatorMapKey(string $action, string $requestMethod = null) : string
    {
        return empty($requestMethod) ? $action : $action.':'.$requestMethod;
    }

    /**
     * Maps specific validator to controller action. Request data validation will be performed before the controller
     * action called. If request method defined, then specified validator will be mapped only to this request method
     * in case when action handles multiple request methods.
     *
     * @param string $action controller action name
     * @param string $validator validator class
     * @param string|null $requestMethod request method
     */
    protected function mapValidatorToAction(string $action, string $validator, string $requestMethod = null) : void
    {
        $className = App::className($validator, 'Validation', 'Validation');
        if (!$className || !class_exists($className)) {
            throw new \RuntimeException(sprintf(
                'Validation "%s" was not found.',
                $validator
            ));
        }
        $key = $this->getActionToValidatorMapKey($action, $requestMethod);
        $this->validators[$key] = $className;
    }

    /**
     * Removes validator(s) from controller action. If request method is supplied, when only validator
     *
     * @param string $action controller action name
     * @param string|null $requestMethod request method
     */
    protected function removeActionValidator(string $action, string $requestMethod = null) : void
    {
        $key = $this->getActionToValidatorMapKey($action, $requestMethod);
        unset($this->validators[$key]);
    }

    /**
     * Handles startup event and checks if request needs to be validated with validator mapped to request action.
     * In case of validation error returns ValidationErrorResponse with invalid fields.
     *
     * @param Event $event event that triggered component startup hook call
     * @return ValidationErrorResponse|null
     */
    protected function validateRequest(Event $event)
    {
        /** @var Controller $controller */
        $controller = $event->getSubject();
        $request = $controller->request;
        $action = $request->getParam('action');
        $method = $request->getMethod();

        $validator = null;

        // Check for method specific validator first

        $methodSpecific = $this->getActionToValidatorMapKey($action, $method);
        $actionWide = $this->getActionToValidatorMapKey($action);

        $validator = $this->validators[$methodSpecific] ?? $this->validators[$actionWide] ?? null;
        if (!$validator) {
            return null;
        }

        /** @var Validator $instance */
        $instance = new $validator;
        $errors = $instance->errors($method === 'GET' ? $request->getQueryParams() : $request->getParsedBody());
        if (!empty($errors)) {
            return new ValidationErrorResponse($errors, null, null, $controller->response->getType());
        }
        return null;
    }
}
