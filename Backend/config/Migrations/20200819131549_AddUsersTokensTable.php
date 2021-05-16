<?php
use Migrations\AbstractMigration;

class AddUsersTokensTable extends AbstractMigration
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
        $this->table('user_tokens')
            ->addColumn('token_type', 'text', ['null' => false])
            ->addColumn('user_id', 'integer', ['null' => false])
            ->addForeignKey('user_id', 'users', 'id',
                [
                    'update' => 'CASCADE',
                    'delete' => 'CASCADE',
                ])
            ->addColumn('token_value', 'text', ['null' => false])
            ->addColumn('created_at', 'timestamp', ['default' => 'CURRENT_TIMESTAMP', 'null' => false])
            ->addColumn('modified_at', 'timestamp', ['default' => 'CURRENT_TIMESTAMP', 'null' => false])
            ->addColumn('expired_at', 'timestamp', ['null' => true])
            ->create();
    }
}
