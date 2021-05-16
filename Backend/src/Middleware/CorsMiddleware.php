<?php
namespace App\Middleware;

use Cake\Core\Configure;
use Cake\Core\InstanceConfigTrait;
use Cake\Http\Response;
use Cake\Http\ServerRequest;
use Psr\Http\Message\ResponseInterface;
use Psr\Http\Message\ServerRequestInterface;

/**
 * Cross-Origin Resource Sharing Middleware.
 *
 * Handles the pre-flight requests.
 */
class CorsMiddleware
{
    use InstanceConfigTrait;

    /**
     * Default configuration values.
     *
     * - `origins` List of origins to be allowed for cross-domain requests (Access-Control-Allow-Origin header).
     *
     *   ```
     *   'origins' => ['localhost']
     *   ```
     *
     * - `methods` List of HTTP methods to be allowed for cross-domain requests (Access-Control-Allow-Methods header).
     *   Example:
     *
     *   ```
     *   'methods' => ['GET', 'POST',]
     *   ```
     *
     * - `headers` List of HTTP headers to be allowed for cross-domain requests (Access-Control-Allow-Headers header).
     *   Example:
     *
     *   ```
     *   'headers' => ['origin', 'content-type']
     *   ```
     *
     * @var array
     */
    protected $_defaultConfig = [
        'origins' => [],
        'methods' => [],
        'headers' => [],
    ];

    /**
     * Constructor
     *
     * @param array $config Configuration options to use. If empty, `Configure::read('Cors')`
     *                      will be used.
     */
    public function __construct(array $config = [])
    {
        $config = $config ?: Configure::read('Cors');
        $this->setConfig($config);
    }

    /**
     * Either pass the request to remaining middleware or handle the pre-flight OPTIONS requests.
     *
     * @param \Psr\Http\Message\ServerRequestInterface $request  The request.
     * @param \Psr\Http\Message\ResponseInterface      $response The response.
     * @param callable                                 $next     Callback to invoke the next middleware.
     * @return \Psr\Http\Message\ResponseInterface A response
     */
    public function __invoke(ServerRequestInterface $request, ResponseInterface $response, $next)
    {
        /** @var Response $response */
        /** @var ServerRequest $request */

        if ($request->getMethod() != 'OPTIONS') {
            $response = $next($request, $response);
            $response = $response->cors($request, $this->getConfig('origins'))
                ->allowCredentials(['true'])
                ->build();

            return $response
                ->withoutHeader('Access-Control-Allow-Methods')
                ->withoutHeader('Access-Control-Allow-Headers');
        }

        $response = $response->cors(
            $request,
            $this->getConfig('origins'),
            $this->getConfig('methods'),
            $this->getConfig('headers'))
            ->allowCredentials(['true'])
            ->build();

        return $response;
    }
}
