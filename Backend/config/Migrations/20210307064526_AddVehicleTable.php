<?php
use Migrations\AbstractMigration;

class AddVehicleTable extends AbstractMigration
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
        $this->table("vehicles")
            ->addColumn("type_id", "integer", ['null' => false])
            ->addForeignKey("type_id", "vehicle_types", "id")
            ->addColumn("brand", "text", ['null' => false])
            ->addColumn("model", "text", ['null' => false])
            ->addColumn("registration_number", "text", ['null' => false])
            ->addColumn("image_id", "integer", ['null' => false])
            ->addForeignKey("image_id", "documents", "id")
            ->addColumn("owner_id", "integer", ['null' => false])
            ->addForeignKey("owner_id", "users", "id")
            ->create();
    }
}
