<?php
namespace App\Test\TestCase\Model\Table;

use App\Model\Table\LandServicePersonsTable;
use Cake\ORM\TableRegistry;
use Cake\TestSuite\TestCase;

/**
 * App\Model\Table\LandServicePersonsTable Test Case
 */
class LandServicePersonsTableTest extends TestCase
{

    /**
     * Test subject
     *
     * @var \App\Model\Table\LandServicePersonsTable
     */
    public $LandServicePersons;

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'app.land_service_persons',
        'app.lands',
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
        $config = TableRegistry::exists('LandServicePersons') ? [] : ['className' => LandServicePersonsTable::class];
        $this->LandServicePersons = TableRegistry::get('LandServicePersons', $config);
    }

    /**
     * tearDown method
     *
     * @return void
     */
    public function tearDown()
    {
        unset($this->LandServicePersons);

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
