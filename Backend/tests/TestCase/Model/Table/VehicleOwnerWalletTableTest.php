<?php
namespace App\Test\TestCase\Model\Table;

use App\Model\Table\VehicleOwnerWalletsTable;
use Cake\ORM\TableRegistry;
use Cake\TestSuite\TestCase;

/**
 * App\Model\Table\VehicleOwnerWalletsTable Test Case
 */
class VehicleOwnerWalletTableTest extends TestCase
{

    /**
     * Test subject
     *
     * @var \App\Model\Table\VehicleOwnerWalletsTable
     */
    public $VehicleOwnerWallet;

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'app.vehicle_owner_wallet',
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
        $config = TableRegistry::exists('VehicleOwnerWallet') ? [] : ['className' => VehicleOwnerWalletsTable::class];
        $this->VehicleOwnerWallet = TableRegistry::get('VehicleOwnerWallet', $config);
    }

    /**
     * tearDown method
     *
     * @return void
     */
    public function tearDown()
    {
        unset($this->VehicleOwnerWallet);

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
