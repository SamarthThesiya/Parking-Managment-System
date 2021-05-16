<?php
namespace App\Model\Table;

use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\ORM\TableRegistry;
use Cake\Validation\Validator;

/**
 * LandBookings Model
 *
 * @property \App\Model\Table\UsersTable|\Cake\ORM\Association\BelongsTo $Users
 * @property \App\Model\Table\LandsTable|\Cake\ORM\Association\BelongsTo $Lands
 * @property \App\Model\Table\BookingStatusesTable|\Cake\ORM\Association\BelongsTo $BookingStatuses
 * @property \App\Model\Table\VehiclesTable|\Cake\ORM\Association\BelongsTo $Vehicles
 *
 * @method \App\Model\Entity\LandBooking get($primaryKey, $options = [])
 * @method \App\Model\Entity\LandBooking newEntity($data = null, array $options = [])
 * @method \App\Model\Entity\LandBooking[] newEntities(array $data, array $options = [])
 * @method \App\Model\Entity\LandBooking|bool save(\Cake\Datasource\EntityInterface $entity, $options = [])
 * @method \App\Model\Entity\LandBooking patchEntity(\Cake\Datasource\EntityInterface $entity, array $data, array $options = [])
 * @method \App\Model\Entity\LandBooking[] patchEntities($entities, array $data, array $options = [])
 * @method \App\Model\Entity\LandBooking findOrCreate($search, callable $callback = null, $options = [])
 */
class LandBookingsTable extends Table
{

    /**
     * Initialize method
     *
     * @param array $config The configuration for the Table.
     * @return void
     */
    public function initialize(array $config)
    {
        parent::initialize($config);

        $this->setTable('land_bookings');
        $this->setDisplayField('id');
        $this->setPrimaryKey('id');

        $this->belongsTo('Users', [
            'foreignKey' => 'user_id',
            'joinType' => 'INNER'
        ]);
        $this->belongsTo('Lands', [
            'foreignKey' => 'land_id',
            'joinType' => 'INNER'
        ]);
        $this->belongsTo('BookingStatuses', [
            'foreignKey' => 'status_id',
            'joinType' => 'INNER'
        ]);
        $this->belongsTo('Vehicles', [
            'foreignKey' => 'vehicle_id',
            'joinType' => 'INNER'
        ]);
    }

    /**
     * Default validation rules.
     *
     * @param \Cake\Validation\Validator $validator Validator instance.
     * @return \Cake\Validation\Validator
     */
    public function validationDefault(Validator $validator)
    {
        $validator
            ->integer('id')
            ->allowEmpty('id', 'create');

        $validator
            ->dateTime('start_time')
            ->requirePresence('start_time', 'create')
            ->notEmpty('start_time');

        $validator
            ->dateTime('end_time')
            ->requirePresence('end_time', 'create')
            ->notEmpty('end_time');

        return $validator;
    }

    /**
     * Returns a rules checker object that will be used for validating
     * application integrity.
     *
     * @param \Cake\ORM\RulesChecker $rules The rules object to be modified.
     * @return \Cake\ORM\RulesChecker
     */
    public function buildRules(RulesChecker $rules)
    {
        $rules->add($rules->existsIn(['user_id'], 'Users'));
        $rules->add($rules->existsIn(['land_id'], 'Lands'));
        $rules->add($rules->existsIn(['status_id'], 'BookingStatuses'));
        $rules->add($rules->existsIn(['vehicle_id'], 'Vehicles'));

        return $rules;
    }


}
