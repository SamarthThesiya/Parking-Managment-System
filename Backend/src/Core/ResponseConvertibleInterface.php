<?php

namespace App\Core;

use Cake\Http\Response;

/**
 * Interface ResponseConvertibleInterface
 */
interface ResponseConvertibleInterface
{
    /**
     * Converts object to HTTP response.
     *
     * @return \Cake\Http\Response
     */
    public function toResponse(): Response;
}
