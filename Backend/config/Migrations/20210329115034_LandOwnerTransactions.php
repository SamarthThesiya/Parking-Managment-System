<?php
use Migrations\AbstractMigration;

class LandOwnerTransactions extends AbstractMigration
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
        $this->table("land_owner_transactions")
            ->addColumn("land_owner_id", "integer", ['null' => false])
            ->addForeignKey("land_owner_id", "users", "id")
            ->addColumn("transaction_type", "string", ['null' => false])
            ->addColumn("amount", "decimal", ['null' => false, "precision" => 10, "scale" => 2])
            ->addColumn('created_at', 'timestamp', [
                'default' => 'CURRENT_TIMESTAMP',
                'limit' => null,
                'null' => false,
            ])
            ->create();
    }
}
