<?php
use Migrations\AbstractMigration;

class AddUniqueConstraintInUsers extends AbstractMigration
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
            ->addIndex(['role_id', 'phone'], ['unique' => true])
            ->update();
    }
}
