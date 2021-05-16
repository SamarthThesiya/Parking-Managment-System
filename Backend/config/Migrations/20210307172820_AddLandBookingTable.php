<?php
use Migrations\AbstractMigration;

class AddLandBookingTable extends AbstractMigration
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
        $this->table("land_bookings")
            ->addColumn("user_id", "integer", ['null' => false])
            ->addForeignKey("user_id", "users", "id")
            ->addColumn("land_id", "integer", ["null" => false])
            ->addForeignKey("land_id", "lands", "id")
            ->addColumn("start_time", "timestamp", ["null" => false])
            ->addColumn("end_time", "timestamp", ["null" => false])
            ->addColumn("booking_fee", "text", ["null" => false])
            ->addColumn("status_id", "integer", ["null" => false])
            ->addForeignKey("status_id", "booking_statuses", "id")
            ->addColumn("vehicle_id", "integer", ["null" => false])
            ->addForeignKey("vehicle_id", "vehicles", "id")
            ->create();

    }
}
