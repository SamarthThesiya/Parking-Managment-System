<?php


namespace App\Controller;


use App\Service\ServiceAwareTrait;
use Cake\Datasource\Paginator;
use Cake\Event\Event;
use Cake\Http\Response;
use Cake\Http\ServerRequest;
use Crud\Controller\ControllerTrait;

/**
 * Class ApiController
 *
 * @package App\Controller
 * @property \App\Controller\Component\RequestValidatorComponent $RequestValidator
 * @property \Crud\Controller\Component\CrudComponent            $Crud
 */
class ApiController extends AppController
{
    use ServiceAwareTrait;
    use ControllerTrait;

    public $paginate = [
        'page' => 1,
        'limit' => 10,
        'maxLimit' => 100,
    ];

    /**
     * @inheritdoc
     */
    public function __construct(
        ServerRequest $request = null,
        Response $response = null,
        $name = null,
        $eventManager = null,
        $components = null
    ) {
        parent::__construct($request, $response, $name, $eventManager, $components);

        $this->_loadServices();
    }

    /**
     * Initialization hook method.
     *
     * @return void
     */
    public function initialize()
    {
        parent::initialize();

        $this->RequestHandler->renderAs($this, 'ajax');
        $this->RequestHandler->respondAs('json');

        $this->loadComponent('Auth', [
            'loginAction'          => null,
            'loginRedirect'        => null,
            'logoutRedirect'       => null,
            'unauthorizedRedirect' => false,
            'storage'              => 'Memory',
            'checkAuthIn'          => 'Controller.initialize',
        ]);
        $this->loadComponent('RequestValidator');

        $this->Auth->setConfig('authenticate', [
            'OAuth',
        ]);
        $this->Auth->setConfig('authorize', ['Controller']);

        $this->loadComponent('Crud.Crud', [
            'actions'   => [
                'Crud.Index',
                'Crud.View',
                'Crud.Add',
                'Crud.Delete',
                'Crud.Edit',
            ],
        ]);

        $this->Crud->addListener('Crud.Api');
        $this->Crud->addListener('Crud.ApiPagination');

//        $this->loadComponent('RBAC.RBAC');
    }

    /**
     * beforeFilter callback.
     *
     * @param \Cake\Event\Event $event Event.
     *
     * @return \Cake\Network\Response|null|void
     */
    public function beforeFilter(Event $event)
    {
    }

    public function isAuthorized($user)
    {
        return true;
    }

    /**
     * afterFilter callback.
     *
     * @param \Cake\Event\Event $event Event.
     *
     * @return \Cake\Network\Response|null|void
     */
    public function afterFilter(Event $event)
    {
    }

    /**
     * To paginate the query
     *
     * @param array $data
     *
     * @return array
     */
    protected function doPaginate($data)
    {
        /* @var \Cake\Datasource\QueryInterface $query */
        $query = $data['query'];

        $config = [
            'page'  => 1,
            'limit' => 10,
        ];

        if (array_key_exists('page', $data) && is_numeric($data['page'])) {
            $config['page'] = $data['page'];
        }
        if (array_key_exists('limit', $data) && is_numeric($data['limit'])) {
            $config['limit'] = $data['limit'];
        }

        $paginator = new Paginator();
        $paginator->setConfig($config);
        $data         = $paginator->paginate($query)->toArray();
        $pagingParams = $paginator->getPagingParams()[$query->repository()->getRegistryAlias()];
        $pagination   = [
            "page_count"    => $pagingParams['pageCount'],
            "current_page"  => $pagingParams['page'],
            "has_next_page" => $pagingParams['nextPage'],
            "has_prev_page" => $pagingParams['prevPage'],
            "count"         => $pagingParams['count'],
            "limit"         => $pagingParams['perPage'],
        ];

        return ["data" => $data, "pagination" => $pagination];
    }

    /**
     * beforeRender callback.
     *
     * @param \Cake\Event\Event $event Event.
     * @return \Cake\Network\Response|null|void
     */
    public function beforeRender(Event $event)
    {
        parent::beforeRender($event);

        $this->viewBuilder()->setClassName('Json');
    }
}
