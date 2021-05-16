<?php
use Migrations\AbstractMigration;

class AddNewBookingStatus extends AbstractMigration
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
        $sql = "INSERT INTO public.booking_statuses (\"name\",display_name) VALUES ('cancelled_by_service_person','Cencelled by Service Person');";

        $this->execute($sql);
    }
}
