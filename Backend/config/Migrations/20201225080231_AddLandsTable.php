<?php
use Migrations\AbstractMigration;

class AddLandsTable extends AbstractMigration
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
        $table = $this->table('lands')
            ->addColumn('title', 'text', ['null' => false])
            ->addColumn('length', 'text', ['null' => false])
            ->addColumn('width', 'text', ['null' => false])
            ->addColumn('description', 'text', ['null' => false])
            ->addColumn('s_to_n_img', 'integer', ['null' => true])
            ->addForeignKey('s_to_n_img', 'documents', 'id')
            ->addColumn('n_to_s_img', 'integer', ['null' => true])
            ->addForeignKey('n_to_s_img', 'documents', 'id')
            ->addColumn('e_to_w_img', 'integer', ['null' => true])
            ->addForeignKey('e_to_w_img', 'documents', 'id')
            ->addColumn('w_to_e_img', 'integer', ['null' => true])
            ->addForeignKey('w_to_e_img', 'documents', 'id')
            ->addColumn('latitude', 'decimal', ['null' => true, 'precision' => 8, 'scale'=> 6])
            ->addColumn('longitude', 'decimal', ['null' => true, 'precision' => 9, 'scale'=> 6])
            ->addColumn('address_line_1', 'text', ['null' => true])
            ->addColumn('address_line_2', 'text', ['null' => true])
            ->addColumn('area', 'text', ['null' => true])
            ->addColumn('city', 'text', ['null' => true])
            ->addColumn('state', 'text', ['null' => true])
            ->addColumn('zip_code', 'text', ['null' => true])
            ->addColumn('price_4w_100_to_50', 'text', ['null' => true])
            ->addColumn('price_4w_50_to_10', 'text', ['null' => true])
            ->addColumn('price_4w_less_than_10', 'text', ['null' => true])
            ->addColumn('price_2w_100_to_50', 'text', ['null' => true])
            ->addColumn('price_2w_50_to_10', 'text', ['null' => true])
            ->addColumn('price_2w_less_than_10', 'text', ['null' => true])
            ->addColumn('status_id', 'integer', ['null' => false])
            ->addForeignKey('status_id', 'land_statuses', 'id')
            ->addColumn('owner_id', 'integer', ['null' => false])
            ->addForeignKey('owner_id', 'users', 'id')
            ->addColumn('auditor_id', 'integer', ['null' => true])
            ->addForeignKey('auditor_id', 'users', 'id')
            ->addColumn('auditor_comment', 'text', ['null' => true])
            ->addColumn('created_at', 'timestamp', ['default' => 'CURRENT_TIMESTAMP', 'null' => false])
            ->addColumn('modified_at', 'timestamp', ['default' => 'CURRENT_TIMESTAMP', 'null' => false])
            ->create();
    }
}
