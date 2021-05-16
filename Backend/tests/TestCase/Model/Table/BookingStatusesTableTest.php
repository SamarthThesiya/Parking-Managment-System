<?php
namespace App\Test\TestCase\Model\Table;

use App\Model\Table\BookingStatusesTable;
use Cake\ORM\TableRegistry;
use Cake\TestSuite\TestCase;

/**
 * App\Model\Table\BookingStatusesTable Test Case
 */
class BookingStatusesTableTest extends TestCase
{

    /**
     * Test subject
     *
     * @var \App\Model\Table\BookingStatusesTable
     */
    public $BookingStatuses;

    /**
     * Fixtures
     *
     * @var array
     */
    public $fixtures = [
        'app.booking_statuses'
    ];

    /**
     * setUp method
     *
     * @return void
     */
    public function setUp()
    {
        parent::setUp();
        $config = TableRegistry::exists('BookingStatuses') ? [] : ['className' => BookingStatusesTable::class];
        $this->BookingStatuses = TableRegistry::get('BookingStatuses', $config);
    }

    /**
     * tearDown method
     *
     * @return void
     */
    public function tearDown()
    {
        unset($this->BookingStatuses);

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
