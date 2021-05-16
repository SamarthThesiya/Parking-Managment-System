<?php
use Migrations\AbstractMigration;

class AddVehicleOwnerWallet extends AbstractMigration
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
        $this->table("vehicle_owner_wallet")
            ->addColumn("vehicle_owner_id", "integer", ['null' => false])
            ->addForeignKey("vehicle_owner_id", "users", "id")
            ->addColumn("wallet_balance", "decimal" , ['null'=> false, "precision" => 10, "scale" => 2])
            ->create();
    }
}
