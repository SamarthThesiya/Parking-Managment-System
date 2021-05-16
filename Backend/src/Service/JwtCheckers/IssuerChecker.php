<?php

namespace App\Service\JwtCheckers;

use Jose\Component\Checker\ClaimChecker;
use Jose\Component\Checker\InvalidClaimException;

class IssuerChecker implements ClaimChecker
{
    private const CLAIM_NAME = 'iss';

    /**
     * @var Cognito region.
     */
    private $region;

    /**
     * @var user pool ID.
     */
    private $userPool;
    
    public function __construct(string $userPool, string $region)
    {
        $this->userPool = $userPool;
        $this->region = $region;
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
            throw new IvalidClaimException('"iss" must be a string', self::CLAIM_NAME, $value);
        }
        $expectedValue = 'https://cognito-idp.' . $this->region . '.amazonaws.com/' . $this->userPool;
        if ($value !== $expectedValue) {
            throw new InvalidClaimException('Invalid "iss" value', self::CLAIM_NAME, $value);
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
