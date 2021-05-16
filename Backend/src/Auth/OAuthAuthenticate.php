<?php


namespace App\Auth;


use App\Exceptions\NotFoundException;
use App\Exceptions\UnauthorizedException;
use App\Service\JwtHelperService;
use Cake\Auth\BaseAuthenticate;
use Cake\Http\Response;
use Cake\Http\ServerRequest;
use Cake\ORM\TableRegistry;

class OAuthAuthenticate extends BaseAuthenticate
{

    /**
     * @inheritDoc
     */
    public function authenticate(ServerRequest $request, Response $response)
    {
        return $this->getUser($request);
    }

    public function getUser(ServerRequest $request)
    {
        $access_token = $request->getHeaderLine('access-token');
        if (empty($access_token)) {
            $access_token = $request->getQuery('_token');

            if (empty($access_token)) {
                throw new UnauthorizedException('Access token is missing');
            }
        }

        $jwt = new JwtHelperService();
        $payload = $jwt->decodeToken($access_token);

        if ($payload === null) {
            throw new UnauthorizedException('Access token is invalid');
        }

        $table = TableRegistry::get('Users');
        $user = $table->find()->where([
            'id' => $payload['id']
        ])->first();

        if (!$user) {
            throw new NotFoundException('User who accessing this app is not found on server');
        }
        return $user->toArray();
    }

}
