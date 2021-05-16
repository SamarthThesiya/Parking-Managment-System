<?php


namespace App\Service;

use App\Model\Enums\UserTokenTypes;
use Cake\Test\Fixture\ThingsFixture;

/**
 * Class AuthService
 *
 * @package App\Service
 * @property \App\Model\Table\UsersTable      $Users
 * @property \App\Model\Table\UserTokensTable $UserTokens
 *
 * @property \App\Service\JwtHelperService    $JwtHelper
 */
class AuthService extends Service
{
    public $services = [
        'JwtHelper',
    ];

    public function __construct()
    {
        $this->loadModel('Users');
        $this->loadModel('UserTokens');
    }

    // Not usable yet.
    public function authenticate(array $login_data)
    {
//        $user_name = $login_data['user_name'];
//        $password  = $login_data['password'];
//
//        /** @var \App\Model\Entity\User $user */
//        $user = $this->Users->find()
//            ->where([
//                $this->Users->aliasField('username') => $user_name,
//                $this->Users->aliasField('password') => $password,
//            ])->first();
//
//        if (empty($user)) {
//            return false;
//        }
//
//        $payload = [
//            'id'   => $user->id,
//            'role' => $user->role,
//        ];
//
//        $jwt = $this->JwtHelper->encodeToken($payload);
//
//        $user_token = $this->UserTokens->find()
//            ->where([
//                $this->UserTokens->aliasField('user_id')    => $user->id,
//                $this->UserTokens->aliasField('token_type') => UserTokenTypes::ACCESS_TOKEN,
//            ])->first();
//
//        if (empty($user_token)) {
//            $user_token             = $this->UserTokens->newEntity();
//            $user_token->user       = $user;
//            $user_token->token_type = UserTokenTypes::ACCESS_TOKEN;
//        }
//        $user_token->token_value = $jwt;
//        $user_token->expired_at  = null;
//
//        $this->UserTokens->saveOrFail($user_token);

        return null;
    }

    public function do_sign_on(string $phone, string $user_name, int $role_id)
    {
        /** @var \App\Model\Entity\User $user */
        $user = $this->Users->find()
            ->where([
                'Lower(' . $this->Users->aliasField('username') . ')' => strtolower($user_name),
                $this->Users->aliasField('phone')                     => $phone,
                $this->Users->aliasField('role_id')                   => $role_id,
            ])->first();

        if (empty($user)) {
            $user = $this->Users->newEntity([
                'username'     => $user_name,
                'phone'        => $phone,
                'role_id'      => $role_id,
            ]);
            $this->Users->save($user);
        }

        $this->Users->loadInto($user, ['Roles']);

        $role = $user->role->name;

        $payload = [
            'id'   => $user->id,
            'role' => $role,
        ];

        $jwt = $this->JwtHelper->encodeToken($payload);

        $user_token = $this->UserTokens->find()
            ->where([
                $this->UserTokens->aliasField('user_id')    => $user->id,
                $this->UserTokens->aliasField('token_type') => UserTokenTypes::ACCESS_TOKEN,
            ])->first();

        if (empty($user_token)) {
            $user_token             = $this->UserTokens->newEntity();
            $user_token->user       = $user;
            $user_token->token_type = UserTokenTypes::ACCESS_TOKEN;
        }
        $user_token->token_value = $jwt;
        $user_token->expired_at  = null;

        $this->UserTokens->saveOrFail($user_token);

        return $jwt;
    }
}
