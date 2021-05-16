<?php


namespace App\Model\PM_models;


use App\Model\Base\JsonModel;

class DocumentInfo extends JsonModel
{
    /** @var string */
    public $filePath;

    /** @var string */
    public $fileName;

    /** @var string */
    public $fileExtension;

    /** @var string */
    public $fileSize;

    /** @var int|null */
    public $ownerId;

    /** @var int|null */
    public $creatorId;

    /** @var int|null */
    public $appointmentId;

    /** @var int|null */
    public $documentTypeId;

    /** @var int|null */
    public $documentSourceId;

    /** @var string|null */
    public $documentClass;

    /** @var string|null */
    public $patientNotes;

    /** @var string|null */
    public $actionNotes;

    /** @var string|null */
    public $description;

    /** @var int|null */
    public $providerId;
}
