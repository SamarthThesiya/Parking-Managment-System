<?php
namespace App\Test\TestCase\Model\Table;

use App\Model\Table\LandOwnerTransactionsTable;
use Cake\ORM\TableRegistry;
use Cake\TestSuite\TestCase;

/**
 * App\Model\Table\LandOwnerTransactionsTable Test Case
 */
class LandOwnerTransactionsTableTest extends TestCase
{

    /**
     * Test subject
     *
     * @var \App\Model\Table\LandOwnerTransactionsTable
     */
    public $LandOwnerTransactions;

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'app.land_owner_transactions',
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
        $config = TableRegistry::exists('LandOwnerTransactions') ? [] : ['className' => LandOwnerTransactionsTable::class];
        $this->LandOwnerTransactions = TableRegistry::get('LandOwnerTransactions', $config);
    }

    /**
     * tearDown method
     *
     * @return void
     */
    public function tearDown()
    {
        unset($this->LandOwnerTransactions);

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
