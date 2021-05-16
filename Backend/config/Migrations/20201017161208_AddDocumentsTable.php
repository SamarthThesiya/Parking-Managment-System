<?php

use Migrations\AbstractMigration;

class AddDocumentsTable extends AbstractMigration
{
    /**
     * Change Method.
     *
     * More information on this method is available here:
     * http://docs.phinx.org/en/latest/migrations.html#the-change-method
     *
     * @return void
     */
    public function change()
    {
        $this->table('documents')
            ->addColumn('document_type_id', 'integer', ['null' => false])
            ->addForeignKey('document_type_id', 'document_types', 'id',
                [
                    'update' => 'RESTRICT',
                    'delete' => 'RESTRICT',
                ])
            ->addColumn('owner_id', 'integer', ['null' => false])
            ->addForeignKey('owner_id', 'users', 'id',
                [
                    'update' => 'RESTRICT',
                    'delete' => 'RESTRICT',
                ])
            ->addColumn('file_name', 'text', ['null' => false])
            ->addColumn('file_size', 'biginteger', ['null' => false])
            ->addColumn('external_storage_url', 'text', ['null' => false])
            ->addColumn('created_at', 'timestamp', ['default' => 'CURRENT_TIMESTAMP', 'null' => false])
            ->addColumn('modified_at', 'timestamp', ['default' => 'CURRENT_TIMESTAMP', 'null' => false])
            ->addColumn('deleted_at', 'timestamp', ['null' => true])
            ->addColumn('extension', 'text', ['null' => false])
            ->create();
    }
}
