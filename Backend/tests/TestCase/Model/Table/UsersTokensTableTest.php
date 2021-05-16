<?php
namespace App\Test\TestCase\Model\Table;

use App\Model\Table\UsersTokensTable;
use Cake\ORM\TableRegistry;
use Cake\TestSuite\TestCase;

/**
 * App\Model\Table\UsersTokensTable Test Case
 */
class UsersTokensTableTest extends TestCase
{

    /**
     * Test subject
     *
     * @var \App\Model\Table\UsersTokensTable
     */
    public $UsersTokens;

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'app.users_tokens',
        'app.users'
    ];

    /**
     * setUp method
     *
     * @return void
     */
    public function setUp()
    {
        parent::setUp();
        $config = TableRegistry::exists('UsersTokens') ? [] : ['className' => UsersTokensTable::class];
        $this->UsersTokens = TableRegistry::get('UsersTokens', $config);
    }

    /**
     * tearDown method
     *
     * @return void
     */
    public function tearDown()
    {
        unset($this->UsersTokens);

        parent::tearDown();
    }

    /**
     * Test initialize method
     *
     * @return void
     */
    public function testInitialize()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }

    /**
     * Test validationDefault method
     *
     * @return void
     */
    public function testValidationDefault()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }

    /**
     * Test buildRules method
     *
     * @return void
     */
    public function testBuildRules()
    {
        $this->markTestIncomplete('Not implemented yet.');
    }
}
