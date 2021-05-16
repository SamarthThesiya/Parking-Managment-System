<?php
use Migrations\AbstractMigration;

class AddVehicleTypesTable extends AbstractMigration
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
        $this->table("vehicle_types")
            ->addColumn("name", "text", ['null' => false])
            ->addIndex("name", ['unique' => true])
            ->create();


    }
}
