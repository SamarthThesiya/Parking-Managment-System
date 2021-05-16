<?php


namespace App\Controller\V1;


use App\Controller\ApiController;
use App\Model\Response\ApiErrorResponse;
use App\Model\Response\ApiOKResponse;
use App\Model\Response\Error\FailedDependencyResponse;
use App\Model\Response\Error\UnauthorizedResponse;
use App\Model\Validation\LoginValidator;
use App\Model\Validation\SignOnValidation;
use App\Service\AuthService;
use Cake\ORM\Exception\PersistenceFailedException;

/**
 * Class AuthController
 *
 * @package App\Controller\v1
 * @property \App\Service\AuthService $AuthService
 */
class AuthController extends ApiController
{
    public $services = [
        'AuthService' => [
            'className' => AuthService::class,
        ],
    ];
    public function initialize()
    {
        parent::initialize();
        $this->Auth->allow(['login', 'signOn']);

        $this->RequestValidator->addRequestDataValidators([
            'login'  => LoginValidator::class,
            'signOn' => SignOnValidation::class,
        ]);
    }

    public function login() {

        $request_data = $this->request->getData();

        $jwt_token = $this->AuthService->authenticate($request_data);

        if ($jwt_token == false) {
            return new UnauthorizedResponse('Invalid credentials');
        }

        return new ApiOKResponse(['access_token' => $jwt_token]);
    }

    public function signOn()
    {
        try {
            $request_data = $this->request->getData();

            $jwt_token = $this->AuthService->do_sign_on(
                $request_data['phone'],
                $request_data['username'],
                $request_data['role_id']
            );

            if ($jwt_token == false) {
                return new UnauthorizedResponse('Invalid credentials');
            }

            return new ApiOKResponse(['access_token' => $jwt_token]);
        } catch (PersistenceFailedException $exception) {
            return new FailedDependencyResponse($exception->getMessage());
        }
    }
}
