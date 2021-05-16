<?php

use Migrations\AbstractMigration;

class PopulateLandStatuses extends AbstractMigration
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
        $roles_table = \Cake\ORM\TableRegistry::get('land_statuses');
        $roles       = [
            'pending_approval'         => 'Pending Approval',
            'approved_and_activated'   => 'Approved and Activated',
            'approved_and_deactivated' => 'Approved and Deactivated',
            'rejected'                 => 'Rejected',
            'drafted'                  => 'Drafted',
            'request_cancelled'        => 'Request cancelled',
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
