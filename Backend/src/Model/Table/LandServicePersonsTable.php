<?php
namespace App\Model\Table;

use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\Validation\Validator;

/**
 * LandServicePersons Model
 *
 * @property \App\Model\Table\LandsTable|\Cake\ORM\Association\BelongsTo $Lands
 * @property \App\Model\Table\UsersTable|\Cake\ORM\Association\BelongsTo $Users
 *
 * @method \App\Model\Entity\LandServicePerson get($primaryKey, $options = [])
 * @method \App\Model\Entity\LandServicePerson newEntity($data = null, array $options = [])
 * @method \App\Model\Entity\LandServicePerson[] newEntities(array $data, array $options = [])
 * @method \App\Model\Entity\LandServicePerson|bool save(\Cake\Datasource\EntityInterface $entity, $options = [])
 * @method \App\Model\Entity\LandServicePerson patchEntity(\Cake\Datasource\EntityInterface $entity, array $data, array $options = [])
 * @method \App\Model\Entity\LandServicePerson[] patchEntities($entities, array $data, array $options = [])
 * @method \App\Model\Entity\LandServicePerson findOrCreate($search, callable $callback = null, $options = [])
 */
class LandServicePersonsTable extends Table
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

        $this->setTable('land_service_persons');
        $this->setDisplayField('id');
        $this->setPrimaryKey('id');

        $this->belongsTo('Lands', [
            'foreignKey' => 'land_id',
            'joinType' => 'INNER'
        ]);
        $this->belongsTo('Users', [
            'foreignKey' => 'service_person_id',
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
        $rules->add($rules->existsIn(['land_id'], 'Lands'));
        $rules->add($rules->existsIn(['service_person_id'], 'Users'));

        return $rules;
    }
}
