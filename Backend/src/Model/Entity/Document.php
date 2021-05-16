<?php

namespace App\Model\Entity;

use Cake\ORM\Entity;

/**
 * Document Entity
 *
 * @property int                            $id
 * @property int                            $document_type_id
 * @property int                            $owner_id
 * @property string                         $file_name
 * @property int                            $file_size
 * @property string                         $external_storage_url
 * @property \Cake\I18n\FrozenTime          $created_at
 * @property \Cake\I18n\FrozenTime          $modified_at
 * @property \Cake\I18n\FrozenTime          $deleted_at
 * @property string                         $extension
 *
 * @property \App\Model\Entity\DocumentType $document_type
 * @property \App\Model\Entity\User         $user
 */
class Document extends Entity
{

    /**
     * Fields that can be mass assigned using newEntity() or patchEntity().
     *
     * Note that when '*' is set to true, this allows all unspecified fields to
     * be mass assigned. For security purposes, it is advised to set '*' to false
     * (or remove it), and explicitly make individual fields accessible as needed.
     *
     * @var array
     */
    protected $_accessible = [
        'document_type_id'     => true,
        'owner_id'             => true,
        'file_name'            => true,
        'file_size'            => true,
        'external_storage_url' => true,
        'created_at'           => true,
        'modified_at'          => true,
        'deleted_at'           => true,
        'extension'            => true,
        'document_type'        => true,
        'user'                 => true,
    ];
}
