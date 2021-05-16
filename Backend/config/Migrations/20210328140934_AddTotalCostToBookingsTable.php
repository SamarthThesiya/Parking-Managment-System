<?php
use Migrations\AbstractMigration;

class AddTotalCostToBookingsTable extends AbstractMigration
{
    /**
     * Change Method.
     *
     * More information on this method is available here:
     * http://docs.phinx.org/en/latest/migrations.html#the-change-method
     * @return void
     */
    public function change()
    {
        $this->table('land_bookings')
            ->addColumn("total_cost", "text", ['null' => true])
            ->update();
    }
}
