<?php

use Migrations\AbstractMigration;

class AddRolesToRolesTable extends AbstractMigration
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
        $roles_table = \Cake\ORM\TableRegistry::get('roles');
        $roles       = [
            'land_owner'    => 'Land Owner',
            'vehicle_owner' => 'Vehicle Owner',
            'auditor'       => 'Auditor',
            'pass_checker'  => 'Pass Checker',
        ];

        $entities = [];

        foreach ($roles as $key => $value) {
            $entities[] = [
                'name'         => $key,
                'display_name' => $value,
            ];
        }

        $entities_objs = $roles_table->newEntities($entities);

        $roles_table->saveMany($entities_objs);
    }
}
