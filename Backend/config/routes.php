<?php
/**
 * Routes configuration
 *
 * In this file, you set up routes to your controllers and their actions.
 * Routes are very important mechanism that allows you to freely connect
 * different URLs to chosen controllers and their actions (functions).
 *
 * CakePHP(tm) : Rapid Development Framework (https://cakephp.org)
 * Copyright (c) Cake Software Foundation, Inc. (https://cakefoundation.org)
 *
 * Licensed under The MIT License
 * For full copyright and license information, please see the LICENSE.txt
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright (c) Cake Software Foundation, Inc. (https://cakefoundation.org)
 * @link          https://cakephp.org CakePHP(tm) Project
 * @license       https://opensource.org/licenses/mit-license.php MIT License
 */

use Cake\Core\Plugin;
use Cake\Routing\Route\DashedRoute;
use Cake\Routing\RouteBuilder;
use Cake\Routing\Router;

/**
 * The default class to use for all routes
 *
 * The following route classes are supplied with CakePHP and are appropriate
 * to set as the default:
 *
 * - Route
 * - InflectedRoute
 * - DashedRoute
 *
 * If no call is made to `Router::defaultRouteClass()`, the class used is
 * `Route` (`Cake\Routing\Route\Route`)
 *
 * Note that `Route` does not do any inflections on URLs which will result in
 * inconsistently cased URLs when used with `:plugin`, `:controller` and
 * `:action` markers.
 *
 */
Router::defaultRouteClass(DashedRoute::class);

Router::scope('/', function (RouteBuilder $routes) {
    Router::extensions(['json', 'xml']);
    Router::prefix('v1', function (RouteBuilder $routes) {
        $routes->setExtensions(['json']);

        $routes->connect(
            '/users',
            ['controller' => 'Users', 'action' => 'index', '_method' => 'GET'],
            [
                '_name' => 'get_users',
            ]
        );

        $routes->connect(
            '/login',
            ['controller' => 'Auth', 'action' => 'login', '_method' => 'POST'],
            [
                '_name' => 'authenticate_users',
            ]
        );

        $routes->connect(
            '/sign-on',
            ['controller' => 'Auth', 'action' => 'sign_on', '_method' => 'POST'],
            [
                '_name' => 'sign_on_users',
            ]
        );

        $routes->connect(
            '/documents',
            ['controller' => 'Documents', 'action' => 'getDocuments', '_method' => 'GET'],
            [
                '_name' => 'get_documents',
            ]
        );

        $routes->connect(
            '/documents',
            ['controller' => 'Documents', 'action' => 'addDocument', '_method' => 'POST'],
            [
                '_name' => 'add_documents',
            ]
        );

        $routes->connect(
            '/document/:id',
            ['controller' => 'Documents', 'action' => 'updateDocument', '_method' => 'PATCH'],
            ['_name' => 'update-document', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/roles',
            ['controller' => 'Roles', 'action' => 'index', '_method' => 'GET'],
            [
                '_name' => 'get_roles',
            ]
        );

        $routes->connect(
            '/documents/:id/download',
            ['controller' => 'Documents', 'action' => 'downloadDocument', '_method' => 'GET'],
            ['_name' => 'upload-document', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->fallbacks(DashedRoute::class);
    });

    Router::prefix('v1/land_owner', ['path' => '/v1/land-owner'], function (RouteBuilder $routes) {
        $routes->setExtensions(['json']);

        $routes->connect(
            '/lands',
            ['controller' => 'Lands', 'action' => 'add', '_method' => 'POST'],
            ['_name' => 'land-owner-add-land']
        );

        $routes->connect(
            '/lands',
            ['controller' => 'Lands', 'action' => 'index', '_method' => 'GET'],
            ['_name' => 'land-owner-get-lands']
        );

        $routes->connect(
            '/lands/:id',
            ['controller' => 'Lands', 'action' => 'view', '_method' => 'GET'],
            ['_name' => 'land-owner-get-land', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/lands/:id',
            ['controller' => 'Lands', 'action' => 'edit', '_method' => 'PATCH'],
            ['_name' => 'land-owner-edit-land', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/lands/:id/register',
            ['controller' => 'Lands', 'action' => 'register', '_method' => 'POST'],
            ['_name' => 'land-owner-land-register', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/lands/:id/cancel',
            ['controller' => 'Lands', 'action' => 'requestCancel', '_method' => 'POST'],
            ['_name' => 'land-owner-land-cancel', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/lands/:id/deactivate',
            ['controller' => 'Lands', 'action' => 'deactivate', '_method' => 'POST'],
            ['_name' => 'land-owner-land-deactivate', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/lands/:id/reactivate',
            ['controller' => 'Lands', 'action' => 'reactivate', '_method' => 'POST'],
            ['_name' => 'land-owner-land-reactivate', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/earnings',
            ['controller' => 'LandOwnerTransactions', 'action' => 'getEarningsData', '_method' => 'GET'],
            ['_name' => 'land-owner-get-earnings']
        );

        $routes->connect(
            '/earnings/debit',
            ['controller' => 'LandOwnerTransactions', 'action' => 'debit', '_method' => 'POST'],
            ['_name' => 'land-owner-earning-debit']
        );

        $routes->fallbacks(DashedRoute::class);
    });
    Router::prefix('v1/auditor', ['path' => '/v1/auditor'], function (RouteBuilder $routes) {
        $routes->setExtensions(['json']);

        $routes->connect(
            '/lands',
            ['controller' => 'Lands', 'action' => 'index', '_method' => 'GET'],
            ['_name' => 'auditor-get-lands']
        );

        $routes->connect(
            '/lands/:id/assign-to-me',
            ['controller' => 'Lands', 'action' => 'assignToMe', '_method' => 'PATCH'],
            ['_name' => 'auditor-land-assign', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/lands/:id/audit',
            ['controller' => 'Lands', 'action' => 'auditLand', '_method' => 'PATCH'],
            ['_name' => 'auditor-land-audit', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/lands/:id',
            ['controller' => 'Lands', 'action' => 'view', '_method' => 'GET'],
            ['_name' => 'auditor-get-land', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/lands/my-audited',
            ['controller' => 'Lands', 'action' => 'myAuditedLand', '_method' => 'GET'],
            ['_name' => 'auditor-get-my-audited-land']
        );

        $routes->fallbacks(DashedRoute::class);
    });

    Router::prefix('v1/vehicle_owner', ['path' => '/v1/vehicle-owner'], function (RouteBuilder $routes) {
        $routes->setExtensions(['json']);

        $routes->connect(
            '/lands',
            ['controller' => 'Lands', 'action' => 'index', '_method' => 'GET'],
            ['_name' => 'vehicle-owner-get-lands']
        );

        $routes->connect(
            '/vehicle',
            ['controller' => 'Vehicles', 'action' => 'index', '_method' => 'GET'],
            ['_name' => 'vehicle-owner-get-vehicles']
        );

        $routes->connect(
            '/vehicle/:id',
            ['controller' => 'Vehicles', 'action' => 'view', '_method' => 'GET'],
            ['_name' => 'vehicle-owner-get-vehicle', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/vehicle',
            ['controller' => 'Vehicles', 'action' => 'add', '_method' => 'POST'],
            ['_name' => 'vehicle-owner-add-vehicle',]
        );

        $routes->connect(
            '/vehicle-types',
            ['controller' => 'VehicleTypes', 'action' => 'index', '_method' => 'GET'],
            ['_name' => 'vehicle-owner-get-vehicle-types',]
        );

        $routes->connect(
            '/booking',
            ['controller' => 'LandBookings', 'action' => 'add', '_method' => 'POST'],
            ['_name' => 'vehicle-owner-add-booking',]
        );

        $routes->connect(
            '/booking',
            ['controller' => 'LandBookings', 'action' => 'index', '_method' => 'GET'],
            ['_name' => 'vehicle-owner-get-bookings',]
        );

        $routes->connect(
            '/booking/:id',
            ['controller' => 'LandBookings', 'action' => 'view', '_method' => 'GET'],
            ['_name' => 'vehicle-owner-get-booking', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/wallet',
            ['controller' => 'Users', 'action' => 'createOrUpdateWallet', '_method' => 'PATCH'],
            ['_name' => 'vehicle-owner-update-wallet']
        );

        $routes->connect(
            '/wallet',
            ['controller' => 'Users', 'action' => 'getOrCreateWallet', '_method' => 'GET'],
            ['_name' => 'vehicle-owner-get-wallet']
        );

        $routes->fallbacks(DashedRoute::class);
    });

    Router::prefix('v1/service_person', ['path' => '/v1/service-person'], function (RouteBuilder $routes) {
        $routes->setExtensions(['json']);

        $routes->connect(
            '/booking/:booking_token',
            ['controller' => 'LandBookings', 'action' => 'view', '_method' => 'GET'],
            ['_name' => 'service-person-get-land-booking', 'id' => '\s+', 'pass' => ['booking_token']]
        );

        $routes->connect(
            '/booking',
            ['controller' => 'LandBookings', 'action' => 'index', '_method' => 'GET'],
            ['_name' => 'service-person-get-land-bookings']
        );

        $routes->connect(
            '/booking/:id/audit',
            ['controller' => 'LandBookings', 'action' => 'auditLandBooking', '_method' => 'PATCH'],
            ['_name' => 'service-person-audit-land-booking', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/land/:id/assign',
            ['controller' => 'LandServicePersons', 'action' => 'assignLand', '_method' => 'PATCH'],
            ['_name' => 'service-person-assign-land', 'id' => '\d+', 'pass' => ['id']]
        );

        $routes->connect(
            '/me',
            ['controller' => 'Users', 'action' => 'me', '_method' => 'GET'],
            ['_name' => 'service-person-get-me']
        );

        $routes->connect(
            '/lands',
            ['controller' => 'Lands', 'action' => 'index', '_method' => 'GET'],
            ['_name' => 'service-person-get-lands']
        );

        $routes->fallbacks(DashedRoute::class);
    });

    $routes->fallbacks(DashedRoute::class);
});


/**
 * Load all plugin routes. See the Plugin documentation on
 * how to customize the loading of plugin routes.
 */
Plugin::routes();
