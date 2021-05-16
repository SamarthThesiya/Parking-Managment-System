<?php
use Migrations\AbstractMigration;

class AddBookingStatusTable extends AbstractMigration
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
        $this->table('booking_statuses')
            ->addColumn('name', 'text', ['null' => false])
            ->addIndex('name', ['unique' => true])
            ->addColumn('display_name', 'text', ['null' => false])
            ->create();

    }
}
