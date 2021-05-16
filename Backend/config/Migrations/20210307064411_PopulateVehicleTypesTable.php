<?php
use Migrations\AbstractMigration;

class PopulateVehicleTypesTable extends AbstractMigration
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
        $this->execute("INSERT INTO public.vehicle_types (\"name\") VALUES ('2_wheeler');
                             INSERT INTO public.vehicle_types (\"name\") VALUES ('4_wheeler');
                             ");
    }
}
