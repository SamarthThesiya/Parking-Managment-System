<?php
use Migrations\AbstractMigration;

class AddDenialReasonInLandBooking extends AbstractMigration
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
            ->addColumn("denial_reason", 'text', ['null' => true])
            ->update();
    }
}
