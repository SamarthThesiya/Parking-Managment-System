<?php

namespace App\Model\Table;

use App\Model\Enums\LandStatuses;
use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\Validation\Validator;

/**
 * Lands Model
 *
 * @property \App\Model\Table\LandStatusesTable|\Cake\ORM\Association\BelongsTo $LandStatuses
 * @property \App\Model\Table\UsersTable|\Cake\ORM\Association\BelongsTo        $Users
 *
 * @method \App\Model\Entity\Land get($primaryKey, $options = [])
 * @method \App\Model\Entity\Land newEntity($data = null, array $options = [])
 * @method \App\Model\Entity\Land[] newEntities(array $data, array $options = [])
 * @method \App\Model\Entity\Land|bool save(\Cake\Datasource\EntityInterface $entity, $options = [])
 * @method \App\Model\Entity\Land patchEntity(\Cake\Datasource\EntityInterface $entity, array $data, array $options = [])
 * @method \App\Model\Entity\Land[] patchEntities($entities, array $data, array $options = [])
 * @method \App\Model\Entity\Land findOrCreate($search, callable $callback = null, $options = [])
 */
class LandsTable extends Table
{

    /**
     * Initialize method
     *
     * @param array $config The configuration for the Table.
     *
     * @return void
     */
    public function initialize(array $config)
    {
        parent::initialize($config);

        $this->setTable('lands');
        $this->setDisplayField('title');
        $this->setPrimaryKey('id');

        $this->belongsTo('LandStatuses', [
            'foreignKey' => 'status_id',
            'joinType'   => 'INNER',
        ]);
        $this->belongsTo('Owner', [
            'joinType'     => 'INNER',
            'className'    => 'Users',
            'foreignKey'   => 'owner_id',
            'propertyName' => 'owner',
        ]);
        $this->belongsTo('Auditor', [
            'className'    => 'Users',
            'foreignKey'   => 'auditor_id',
            'propertyName' => 'Auditor',
        ]);

        $this->addBehavior('Timestamp', [
            'events' => [
                'Model.beforeSave' => [
                    'created_at'  => 'new',
                    'modified_at' => 'always',
                ],
            ],
        ]);
    }

    /**
     * Default validation rules.
     *
     * @param \Cake\Validation\Validator $validator Validator instance.
     *
     * @return \Cake\Validation\Validator
     */
    public function validationDefault(Validator $validator)
    {
        $validator
            ->integer('id')
            ->allowEmpty('id', 'create');

        $validator
            ->scalar('title')
            ->requirePresence('title', 'create')
            ->notEmpty('title');

        $validator
            ->scalar('length')
            ->requirePresence('length', 'create')
            ->notEmpty('length');

        $validator
            ->scalar('width')
            ->requirePresence('width', 'create')
            ->notEmpty('width');

        $validator
            ->scalar('description')
            ->requirePresence('description', 'create')
            ->notEmpty('description');

        $validator
            ->integer('s_to_n_img')
            ->allowEmpty('s_to_n_img');

        $validator
            ->integer('n_to_s_img')
            ->allowEmpty('n_to_s_img');

        $validator
            ->integer('e_to_w_img')
            ->allowEmpty('e_to_w_img');

        $validator
            ->integer('w_to_e_img')
            ->allowEmpty('w_to_e_img');

        $validator
            ->decimal('latitude')
            ->allowEmpty('latitude');

        $validator
            ->decimal('longitude')
            ->allowEmpty('longitude');

        $validator
            ->scalar('address_line_1')
            ->allowEmpty('address_line_1');

        $validator
            ->scalar('address_line_2')
            ->allowEmpty('address_line_2');

        $validator
            ->scalar('area')
            ->allowEmpty('area');

        $validator
            ->scalar('city')
            ->allowEmpty('city');

        $validator
            ->scalar('state')
            ->allowEmpty('state');

        $validator
            ->scalar('zip_code')
            ->allowEmpty('zip_code');

        $validator
            ->scalar('price_4w_100_to_50')
            ->allowEmpty('price_4w_100_to_50');

        $validator
            ->scalar('price_4w_50_to_10')
            ->allowEmpty('price_4w_50_to_10');

        $validator
            ->scalar('price_4w_less_than_10')
            ->allowEmpty('price_4w_less_than_10');

        $validator
            ->scalar('price_2w_100_to_50')
            ->allowEmpty('price_2w_100_to_50');

        $validator
            ->scalar('price_2w_50_to_10')
            ->allowEmpty('price_2w_50_to_10');

        $validator
            ->scalar('price_2w_less_than_10')
            ->allowEmpty('price_2w_less_than_10');

        $validator
            ->scalar('auditor_comment')
            ->allowEmpty('auditor_comment');

        $validator
            ->dateTime('created_at')
            ->notEmpty('created_at');

        $validator
            ->dateTime('modified_at')
            ->notEmpty('modified_at');

        return $validator;
    }

    /**
     * Returns a rules checker object that will be used for validating
     * application integrity.
     *
     * @param \Cake\ORM\RulesChecker $rules The rules object to be modified.
     *
     * @return \Cake\ORM\RulesChecker
     */
    public function buildRules(RulesChecker $rules)
    {
        $rules->add($rules->existsIn(['status_id'], 'LandStatuses'));

        return $rules;
    }

    public function findAuditorAll(Query $query, array $options)
    {
        if (isset($options['status'])) {
            $query->where([
                $this->LandStatuses->aliasField('name') => $options['status'],
            ]);
            unset($options['status']);
        } else {
            $query->where([
                $this->LandStatuses->aliasField('name') => LandStatuses::PENDING_APPROVAL,
            ]);
        }

        if (isset($options['auditor_id']) && $options['auditor_id'] == 'null') {
            $query->andWhere([
                $this->aliasField('auditor_id') . ' is ' => null
            ]);
            unset($options['auditor_id']);
        }

        if (!empty($options)) {
            $query->andWhere($options);
        }

        return $query;
    }
}
