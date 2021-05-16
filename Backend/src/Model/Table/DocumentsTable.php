<?php

namespace App\Model\Table;

use Cake\ORM\Query;
use Cake\ORM\RulesChecker;
use Cake\ORM\Table;
use Cake\Validation\Validator;

/**
 * Documents Model
 *
 * @property \App\Model\Table\DocumentTypesTable|\Cake\ORM\Association\BelongsTo $DocumentTypes
 * @property \App\Model\Table\UsersTable|\Cake\ORM\Association\BelongsTo         $Users
 *
 * @method \App\Model\Entity\Document get($primaryKey, $options = [])
 * @method \App\Model\Entity\Document newEntity($data = null, array $options = [])
 * @method \App\Model\Entity\Document[] newEntities(array $data, array $options = [])
 * @method \App\Model\Entity\Document|bool save(\Cake\Datasource\EntityInterface $entity, $options = [])
 * @method \App\Model\Entity\Document patchEntity(\Cake\Datasource\EntityInterface $entity, array $data, array $options = [])
 * @method \App\Model\Entity\Document[] patchEntities($entities, array $data, array $options = [])
 * @method \App\Model\Entity\Document findOrCreate($search, callable $callback = null, $options = [])
 */
class DocumentsTable extends Table
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

        $this->setTable('documents');
        $this->setDisplayField('id');
        $this->setPrimaryKey('id');

        $this->belongsTo('DocumentTypes', [
            'foreignKey' => 'document_type_id',
            'joinType'   => 'INNER',
        ]);
        $this->belongsTo('Users', [
            'foreignKey' => 'owner_id',
            'joinType'   => 'INNER',
        ]);

        $this->addBehavior('Timestamp', [
            'events' => [
                'Model.beforeSave'    => [
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
            ->scalar('file_name')
            ->requirePresence('file_name', 'create')
            ->notEmpty('file_name');

        $validator
            ->requirePresence('file_size', 'create')
            ->notEmpty('file_size');

        $validator
            ->scalar('external_storage_url')
            ->requirePresence('external_storage_url', 'create')
            ->notEmpty('external_storage_url');

        $validator
            ->dateTime('created_at')
            ->notEmpty('created_at');

        $validator
            ->dateTime('modified_at')
            ->notEmpty('modified_at');

        $validator
            ->dateTime('deleted_at')
            ->allowEmpty('deleted_at');

        $validator
            ->scalar('extension')
            ->requirePresence('extension', 'create')
            ->notEmpty('extension');

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
        $rules->add($rules->existsIn(['document_type_id'], 'DocumentTypes'));
        $rules->add($rules->existsIn(['owner_id'], 'Users'));

        return $rules;
    }
}
