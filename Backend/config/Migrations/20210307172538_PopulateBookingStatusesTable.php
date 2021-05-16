<?php
use Migrations\AbstractMigration;

class PopulateBookingStatusesTable extends AbstractMigration
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
        $this->execute("

        INSERT INTO public.booking_statuses (\"name\",display_name) VALUES ('booked','Booked');
        INSERT INTO public.booking_statuses (\"name\",display_name) VALUES ('parked','Parked');
        INSERT INTO public.booking_statuses (\"name\",display_name) VALUES ('completed','Completed');
        INSERT INTO public.booking_statuses (\"name\",display_name) VALUES ('cancelled','Cancelled');

        ");
    }
}
