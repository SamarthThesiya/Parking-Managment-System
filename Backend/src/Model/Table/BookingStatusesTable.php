<?php
namespace App\Model\Table;

use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\ORM\TableRegistry;
use Cake\Validation\Validator;

/**
 * BookingStatuses Model
 *
 * @method \App\Model\Entity\BookingStatus get($primaryKey, $options = [])
 * @method \App\Model\Entity\BookingStatus newEntity($data = null, array $options = [])
 * @method \App\Model\Entity\BookingStatus[] newEntities(array $data, array $options = [])
 * @method \App\Model\Entity\BookingStatus|bool save(\Cake\Datasource\EntityInterface $entity, $options = [])
 * @method \App\Model\Entity\BookingStatus patchEntity(\Cake\Datasource\EntityInterface $entity, array $data, array $options = [])
 * @method \App\Model\Entity\BookingStatus[] patchEntities($entities, array $data, array $options = [])
 * @method \App\Model\Entity\BookingStatus findOrCreate($search, callable $callback = null, $options = [])
 */
class BookingStatusesTable extends Table
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

        $this->setTable('booking_statuses');
        $this->setDisplayField('name');
        $this->setPrimaryKey('id');
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
            ->scalar('name')
            ->requirePresence('name', 'create')
            ->notEmpty('name')
            ->add('name', 'unique', ['rule' => 'validateUnique', 'provider' => 'table']);

        $validator
            ->scalar('display_name')
            ->requirePresence('display_name', 'create')
            ->notEmpty('display_name');

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
        $rules->add($rules->isUnique(['name']));

        return $rules;
    }

    public function getBookingStatusIdFromName(string $name)
    {
        $bookingStatusTable = TableRegistry::get("BookingStatuses");

        /** @var \App\Model\Entity\BookingStatus $booking */
        $booking = $bookingStatusTable->find()
            ->where([
                $bookingStatusTable->aliasField('name') => $name,
            ])->firstOrFail();

        return $booking;
    }

    public function getBookingStatusNameFromId(int $id)
    {
        $bookingStatusTable = TableRegistry::get("BookingStatuses");

        /** @var \App\Model\Entity\BookingStatus $booking */
        $booking = $bookingStatusTable->find()
            ->where([
                $bookingStatusTable->aliasField('id') => $id,
            ])->firstOrFail();

        return $booking;
    }
}
