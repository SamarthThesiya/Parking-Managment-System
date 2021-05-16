<?php

namespace App\Service;

use App\Model\Base\JsonModel;
use App\Model\Entity\User;
use App\Service\JwtCheckers\IssuerChecker;
use App\Service\JwtCheckers\ClientChecker;
use Cake\Http\Client;
use Cake\Http\Client\Request;
use Cake\Http\Client\Response;
use Cake\I18n\Time;
use Cake\Log\Log;
use Cake\ORM\TableRegistry;
use Cake\Utility\Security;
use Jose\Component\Checker\AlgorithmChecker;
use Jose\Component\Checker\ClaimCheckerManager;
use Jose\Component\Checker\ExpirationTimeChecker;
use Jose\Component\Checker\HeaderCheckerManager;
use Jose\Component\Checker\IssuedAtChecker;
use Jose\Component\Core\AlgorithmManager;
use Jose\Component\Core\Converter\JsonConverter;
use Jose\Component\Core\Converter\StandardConverter;
use Jose\Component\Core\JWK;
use Jose\Component\Core\JWKSet;
use Jose\Component\KeyManagement\JWKFactory;
use Jose\Component\Signature\Algorithm\HS256;
use Jose\Component\Signature\Algorithm\RS256;
use Jose\Component\Signature\JWSBuilder;
use Jose\Component\Signature\JWSTokenSupport;
use Jose\Component\Signature\JWSVerifier;
use Jose\Component\Signature\Serializer\CompactSerializer;
use Jose\Component\Signature\Serializer\Serializer;

/**
 * Class JwtHelperService
 *
 * @package App\Service
 */
class JwtHelperService extends Service
{
    /**
     * Creates and returns JWK.
     *
     * @param string|null $key The shared secret. If not provided, Security::getSalt will be used.
     *
     * @see \Cake\Utility\Security::getSalt
     *
     * @return \Jose\Component\Core\JWK
     */
    private function getJWK(string $key = null): JWK
    {
        return JWKFactory::createFromSecret(
            $key ?? Security::getSalt(),
            [
                'alg' => (new HS256())->name(),
                'use' => 'sig',
            ]
        );
    }

    /**
     * Creates and returns algo manager with used algos.
     *
     * @return \Jose\Component\Core\AlgorithmManager
     */
    private function getAlgorithmManager(): AlgorithmManager
    {
        return AlgorithmManager::create([new HS256(),]);
    }

    /**
     * Return default Json Converter.
     *
     * @return \Jose\Component\Core\Converter\JsonConverter
     */
    private function getDefaultJsonConverter(): JsonConverter
    {
        return new StandardConverter();
    }

    /**
     * Returns default serializer instance for a converter.
     *
     * @param \Jose\Component\Core\Converter\JsonConverter|null $converter Json converter to be used. If not specified,
     *                                                                     when the one returned by
     *                                                                     getDefaultJsonConverter method will be used.
     *
     * @return \Jose\Component\Signature\Serializer\Serializer
     */
    private function getDefaultSignatureSerializer(JsonConverter $converter = null): Serializer
    {
        return new CompactSerializer($converter ?? $this->getDefaultJsonConverter());
    }

    /**
     * Decodes JWT token and returns its contents in case of success or null otherwise.
     *
     * @param string $token     Token to be decoded.
     * @param string $className String representing class name of payload object for json mapper.
     * @param string $salt      String representing salt for decoding a token.
     *
     * @return array|object|null Decoded data.
     */
    public function decodeToken(string $token, string $className = null, string $salt = null)
    {
        if (empty($token)) {
            return null;
        }

        try {
            $converter  = $this->getDefaultJsonConverter();
            $serializer = $this->getDefaultSignatureSerializer($converter);
            $jws        = $serializer->unserialize($token);

            $jwsVerifier = new JWSVerifier($this->getAlgorithmManager());

            $isSignatureValid = $jwsVerifier->verifyWithKey($jws, $this->getJWK(), 0);
            $payload          = $isSignatureValid ? $converter->decode($jws->getPayload()) : null;

            // In general, we only check expiration if it is set in payload.

            if ($isSignatureValid && isset($payload['exp'])) {
                $claimCheckerManager = ClaimCheckerManager::create(
                    [
                        new ExpirationTimeChecker(),
                    ]
                );
                $claimCheckerManager->check($payload);
            }
        } catch (\Exception $e) {
            $payload = null;
        }
        if (!empty($className)) {
            if (!$className || !class_exists($className)) {
                throw new \RuntimeException(sprintf(
                    'Class "%s" was not found.',
                    $className
                ));
            }
            $payload = JsonModel::createFromJson($payload, new $className());
        }

        return $payload;
    }

    /**
     * Encodes an array an returns it as the signed JWT token string.
     *
     * @param array|object $payload Array or object to encode.
     * @param string       $salt    String representing salt for encoding a token.
     *
     * @return null|string JWT token string.
     *
     * @throws \Exception
     */
    public function encodeToken($payload, string $salt = null): string
    {
        if (empty($payload)) {
            return null;
        }

        $converter = $this->getDefaultJsonConverter();

        $jwsBuilder = new JWSBuilder($converter, $this->getAlgorithmManager());
        $payload    = $converter->encode($payload);

        $jws = $jwsBuilder
            ->create()
            ->withPayload($payload)
            ->addSignature($this->getJWK($salt), ['alg' => (new HS256())->name()])
            ->build();

        $serializer = $this->getDefaultSignatureSerializer($converter);

        return $serializer->serialize($jws, 0);
    }

    /**
     * Issues new authentication token for accessing auth-protected APIs. If no expiration provided,
     * a default expiration will be used (6 months).
     *
     * @param User $user       User entity.
     * @param Time $expiration Token expiration date in unix epoch format.
     *
     * @return null|string Access token or null if not user was provided.
     *
     * @throws \Exception
     */
    public function issueAuthToken(User $user, Time $expiration = null): string
    {
        if (!$user) {
            return null;
        }
        if (!$expiration) {
            $expiration = Time::now()->addMonths(6);
        }
        $usersTable = TableRegistry::get('users');
        $user       = $usersTable->loadInto($user, ['Partners']);
        $payload = [
            'id'     => $user->email,
            'is_scp' => $user->is_scp,
            'iat'    => time(),
            'exp'    => intval($expiration->toUnixString()),
        ];

        return $this->encodeToken($payload);
    }

    /**
     * Decodes and verifies authentication token, in case of successful verification returns decoded payload.
     *
     * @param string $token JWT token.
     *
     * @return array Associative array containing decoded payload.
     *
     * @throws \Jose\Component\Checker\InvalidClaimException
     * @throws \Jose\Component\Checker\InvalidHeaderException
     * @throws \Jose\Component\Checker\MissingMandatoryClaimException
     * @throws \Jose\Component\Checker\MissingMandatoryHeaderParameterException
     * @throws \Exception
     */
    public function verifyAuthToken(string $token): array
    {
        $algoManager = $this->getAlgorithmManager();

        $converter  = $this->getDefaultJsonConverter();
        $serializer = $this->getDefaultSignatureSerializer($converter);

        // Converting to JWT

        $jws = $serializer->unserialize($token);

        // Checking header

        $headerCheckerManager = HeaderCheckerManager::create(
            [
                new AlgorithmChecker($algoManager->list()),
            ],
            [
                new JWSTokenSupport(),
            ]
        );
        $headerCheckerManager->check($jws, 0);

        // Checking claim

        $claimCheckerManager = ClaimCheckerManager::create(
            [
                new IssuedAtChecker(),
                new ExpirationTimeChecker(),
            ]
        );
        $claims              = $converter->decode($jws->getPayload());
        $claimCheckerManager->check($claims);

        // Checking signature

        $jwsVerifier      = new JWSVerifier($algoManager);
        $isSignatureValid = $jwsVerifier->verifyWithKey($jws, $this->getJWK(), 0);

        if (!$isSignatureValid) {
            throw new \RuntimeException('Invalid signature');
        }

        return $converter->decode($jws->getPayload());
    }

    public function verifyCognitoToken(string $token,
                                       string $cognitoClientId,
                                       string $cognitoRegion,
                                       string $cognitoUserPool): array
    {
        $algoManager = AlgorithmManager::create([new RS256(),]);

        $converter  = $this->getDefaultJsonConverter();
        $serializer = $this->getDefaultSignatureSerializer($converter);

        // Converting to JWT

        $jws = $serializer->unserialize($token);

        // Checking header

        $headerCheckerManager = HeaderCheckerManager::create(
            [
                new AlgorithmChecker($algoManager->list()),
            ],
            [
                new JWSTokenSupport(),
            ]
        );
        $headerCheckerManager->check($jws, 0);

        // Checking claim

        $claimCheckerManager = ClaimCheckerManager::create(
            [
                new IssuedAtChecker(),
                new ExpirationTimeChecker(),
                new IssuerChecker($cognitoUserPool, $cognitoRegion),
                new ClientChecker($cognitoClientId),
            ]
        );
        $claims = $converter->decode($jws->getPayload());
        $claimCheckerManager->check($claims);

        // Checking signature
        $keySet = $this->loadKeySetFromUrl($this->getCognitoJWKSUri($cognitoUserPool));
        $jwsVerifier = new JWSVerifier($algoManager);
        $isSignatureValid = $jwsVerifier->verifyWithKeySet($jws, $keySet, 0);

        if (!$isSignatureValid) {
            throw new \RuntimeException('Invalid signature');
        }

        return $converter->decode($jws->getPayload());

    }

    public function decodeCognitoToken(string $token)
    {
        if (empty($token)) {
            return null;
        }

        try {
            $converter  = $this->getDefaultJsonConverter();
            $serializer = $this->getDefaultSignatureSerializer($converter);
            // Converting to JWT
            $jws        = $serializer->unserialize($token);
            $payload = $converter->decode($jws->getPayload());

        } catch (\Exception $e) {
            Log::error('Unable to decode cognito token: '.$e);
            $payload = null;
        }
        return $payload;

    }

    /**
     * Retrieves a new "system" token (used for service-to-service communications) from
     * Cognito user pool.
     *
     * @return token string if successful, null if not.
     */
    public function getCognitoSystemToken(string $cognitoDomainName,
                                          string $cognitoClientId,
                                          string $cognitoClientSecret): string
    {
        $url = 'https://' . $cognitoDomainName . '.auth.us-east-2.amazoncognito.com/oauth2/token';
        $headers =  ['Authorization' => 'Basic ' . base64_encode(implode(':', [$cognitoClientId, $cognitoClientSecret])),
        'Content-Type' => 'application/x-www-form-urlencoded'];

        $body = ['grant_type' => 'client_credentials'];

        $request = new Request($url, Request::METHOD_POST, $headers, $body);
        $client = new Client();
        $response = $client->send($request);

        if (!$response->isOK()) {
            Log::error('Failed to get system Cognito token, response code: ' . $reponse->getStatusCode());
            return null;
        }
        $jsonResponse = $response->json;
        $token = $jsonResponse['access_token'];

        return $token;

    }

    private function getCognitoJWKSUri(string $cognitoUserPool): string
    {
        if (!$cognitoUserPool) {
            throw new \RuntimeException('AWS Cognito User Pool ID is missing while getting JWKS URI');
        }
        $uri = 'https://cognito-idp.us-east-2.amazonaws.com/' . $cognitoUserPool . '/.well-known/jwks.json';
        return $uri;
    }

    /**
     * This method will try to fetch the url a retrieve the key set.
     * Throws an exception in case of failure.
     *
     * @throws \InvalidArgumentException
     */
    private function loadKeySetFromUrl(string $url): JWKSet
    {
        $content = $this->getContentFromUrl($url);
        if (!\is_array($content)) {
            throw new \RuntimeException('Invalid content.');
        }
        return JWKSet::createFromKeyData($content);
    }

    private function getContentFromUrl(string $url)
    {
        $request = new Request($url, Request::METHOD_GET);
        $client = new Client();
        $response = $client->send($request);

        if (!$response->isOK()) {
            throw new \RuntimeException('Failed to load JWKS from Cognito');
        }

        return $response->json;
    }
}
