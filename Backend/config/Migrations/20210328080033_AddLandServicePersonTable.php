<?php
use Migrations\AbstractMigration;

class AddLandServicePersonTable extends AbstractMigration
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
        $this->table("land_service_persons")
            ->addColumn("land_id", "integer", ['null' => false])
            ->addForeignKey("land_id", "lands", 'id')
            ->addColumn("service_person_id", "integer", ['null' => false])
            ->addForeignKey("service_person_id", "users", 'id')
            ->create();

    }
}
