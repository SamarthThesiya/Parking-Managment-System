<?php
return [
    'debug' => filter_var(env('DEBUG', false), FILTER_VALIDATE_BOOLEAN),
    'Cors' => [
        'origins' => [
            'http://localhost',
            'http://localhost:8080',
            'http://127.0.0.1',
            'http://127.0.0.1:8080',
        ],
    ],

    'Datasources' => [
        'default' => [
            'driver'     => \Cake\Database\Driver\Postgres::class,
            'persistent' => false,
            'host'       => env('DB_HOSTNAME', 'localhost'),
            'port'       => env('DB_PORT', 6432),
            'username'   => env('DB_USERNAME', 'Demo'),
            'password'   => env('DB_PASSWORD', 'Demo'),
            'database'   => env('DB_NAME', 'DemoApp2'),
        ],
        'test'    => [
            'driver'     => \Cake\Database\Driver\Postgres::class,
            'persistent' => false,
            'host'       => env('DB_HOSTNAME', 'localhost'),
            'port'       => env('DB_PORT', 6432),
            'username'   => env('DB_USERNAME', 'Demo'),
            'password'   => env('DB_PASSWORD', 'Demo'),
            'database'   => env('DB_NAME', 'DemoApp2'),
        ],
    ],

    'Error' => [
        'exceptionRenderer' => \App\Error\AppExceptionRenderer::class,
        'skipLog'           => [
            'Cake\Network\Exception\UnauthorizedException',
            'Cake\Network\Exception\NotFoundException',
            'Cake\Routing\Exception\MissingRouteException',
            'Cake\Routing\Exception\MissingControllerException',
            'Cake\Controller\Exception\MissingActionException',
            'Cake\Controller\Exception\MissingActionException',
            'App\Exception\ApiProblemException',
        ],
    ],

    'g_drive' => [
        'document_folder_id' => '1y1Y5ilBYSs_gaLy2N_e5g-QIwLRUZm-N'
    ]

];
