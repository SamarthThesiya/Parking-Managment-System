<?php
use Migrations\AbstractMigration;

class AddUsersTable extends AbstractMigration
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
        $this->table('users')
            ->addColumn('username', 'text', ['null' => false])
            ->addColumn('password', 'text', ['null' => false])
            ->addColumn('role', 'text', ['null' => false])
            ->create();
    }
}
