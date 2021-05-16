<?php
namespace App\Test\TestCase\Model\Table;

use App\Model\Table\LandBookingsTable;
use Cake\ORM\TableRegistry;
use Cake\TestSuite\TestCase;

/**
 * App\Model\Table\LandBookingsTable Test Case
 */
class LandBookingsTableTest extends TestCase
{

    /**
     * Test subject
     *
     * @var \App\Model\Table\LandBookingsTable
     */
    public $LandBookings;

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'app.land_bookings',
        'app.users',
        'app.lands',
        'app.booking_statuses',
        'app.vehicles'
    ];

    /**
     * setUp method
     *
     * @return void
     */
    public function setUp()
    {
        parent::setUp();
        $config = TableRegistry::exists('LandBookings') ? [] : ['className' => LandBookingsTable::class];
        $this->LandBookings = TableRegistry::get('LandBookings', $config);
    }

    /**
     * tearDown method
     *
     * @return void
     */
    public function tearDown()
    {
        unset($this->LandBookings);

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
