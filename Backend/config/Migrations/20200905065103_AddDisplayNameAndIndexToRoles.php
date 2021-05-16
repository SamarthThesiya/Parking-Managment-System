<?php
use Migrations\AbstractMigration;

class AddDisplayNameAndIndexToRoles extends AbstractMigration
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
        $this->table('roles')
            ->addColumn('display_name', 'text', ['null'=>false])
            ->addIndex('name', ['unique' => true])
            ->update();
    }
}
