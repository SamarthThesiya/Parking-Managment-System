<?php
namespace App\Test\Fixture;

use Cake\TestSuite\Fixture\TestFixture;

/**
 * VehicleOwnerWalletFixture
 *
 */
class VehicleOwnerWalletFixture extends TestFixture
{

    /**
     * Table name
     *
     * @var string
     */
    public $table = 'vehicle_owner_wallet';

    /**
     * Fields
     *
     * @var array
     */
    // @codingStandardsIgnoreStart
    public $fields = [
        'id' => ['type' => 'integer', 'length' => 10, 'autoIncrement' => true, 'default' => null, 'null' => false, 'comment' => null, 'precision' => null, 'unsigned' => null],
        'vehicle_owner_id' => ['type' => 'integer', 'length' => 10, 'default' => null, 'null' => false, 'comment' => null, 'precision' => null, 'unsigned' => null, 'autoIncrement' => null],
        'wallet_balance' => ['type' => 'decimal', 'length' => 10, 'default' => null, 'null' => false, 'comment' => null, 'precision' => 2, 'unsigned' => null],
        '_constraints' => [
            'primary' => ['type' => 'primary', 'columns' => ['id'], 'length' => []],
            'vehicle_owner_wallet_vehicle_owner_id' => ['type' => 'foreign', 'columns' => ['vehicle_owner_id'], 'references' => ['users', 'id'], 'update' => 'noAction', 'delete' => 'noAction', 'length' => []],
        ],
    ];
    // @codingStandardsIgnoreEnd

    /**
     * Init method
     *
     * @return void
     */
    public function init()
    {
        $this->records = [
            [
                'id' => 1,
                'vehicle_owner_id' => 1,
                'wallet_balance' => 1.5
            ],
        ];
        parent::init();
    }
}
