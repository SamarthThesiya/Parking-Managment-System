<?php
use Migrations\AbstractMigration;

class PopulateDisplayNameToVehicleTypes extends AbstractMigration
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
        $this->execute("UPDATE public.vehicle_types SET display_name='2 Wheeler' WHERE name = '2_wheeler';
                             UPDATE public.vehicle_types SET display_name='4 Wheeler' WHERE name = '4_wheeler';
                             ");

        $this->table("vehicle_types")
            ->changeColumn("display_name", "text", ['null' => false])
            ->update();
    }
}
