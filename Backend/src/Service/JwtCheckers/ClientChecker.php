<?php

namespace App\Service\JwtCheckers;

use Jose\Component\Checker\ClaimChecker;
use Jose\Component\Checker\InvalidClaimException;

class ClientChecker implements ClaimChecker
{
    private const CLAIM_NAME = 'client_id';

    private $clientId;

    public function __construct(string $clientId)
    {
        if (!isset($clientId)) {
            throw new \RuntimeException('Cognito Client ID is not set');
        }
        $this->clientId = $clientId;
    }

     /**
     * When the token has the applicable claim, the value is checked.
     * If for some reason the value is not valid, an InvalidClaimException must be thrown.
     *
     * @throws InvalidClaimException
     */
    public function checkClaim($value)
    {
        if (!\is_string($value)) {
            throw new InvalidClaimException('"client_id" must be a string', self::CLAIM_NAME, $value);
        }
        if ($value !== $this->clientId) {
            throw new InvalidClaimException('Invalid "client_id" value', self::CLAIM_NAME, $value);
        }
    }

    /**
     * The method returns the claim to be checked.
     */
    public function supportedClaim(): string
    {
        return self::CLAIM_NAME;
    }
}
