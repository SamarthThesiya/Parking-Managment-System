<?php


namespace App\Service;

use Google\Client;

/**
 * Class GoogleApiService
 *
 * @property \Google\Client $googleApiClient
 *
 * @package App\Service
 */
abstract class GoogleApiService extends Service
{
    public function __construct() {

        $this->googleApiClient = new Client();
        $this->googleApiClient->useApplicationDefaultCredentials();
    }
}
