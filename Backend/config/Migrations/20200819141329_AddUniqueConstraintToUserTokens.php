<?php
use Migrations\AbstractMigration;

class AddUniqueConstraintToUserTokens extends AbstractMigration
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
            ->addIndex(['user_id', 'token_type'], ['unique' => true])
            ->update();
    }
}
