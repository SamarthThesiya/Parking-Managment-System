<?php


namespace App\Service;


use App\Model\PM_models\DocumentInfo;
use Cake\Core\Configure;
use Cake\I18n\Time;
use Google_Service_Drive_DriveFile;

/**
 * Class GoogleDriveService
 *
 * @property \Google_Service_Drive $g_drive
 *
 * @package App\Service
 */
class GoogleDriveService extends GoogleApiService
{
    public function __construct()
    {
        parent::__construct();
        $this->googleApiClient->setScopes(\Google_Service_Drive::DRIVE);
        $this->g_drive = new \Google_Service_Drive($this->googleApiClient);
    }

    public function uploadFile(DocumentInfo $documentInfo)
    {
        $g_drive_folder_id = Configure::read('g_drive.document_folder_id');
        $file = new Google_Service_Drive_DriveFile();
        $file->setName(Time::now()->timestamp);
        $file->setMimeType(DocumentService::getMimeContentTypeByExtension($documentInfo->fileExtension));
        $file->setParents([$g_drive_folder_id]);

        $data = file_get_contents($documentInfo->filePath);

        $createdFile = $this->g_drive->files->create($file, [
            'data' => $data,
        ]);

        return $createdFile->id;
    }

    public function downloadDocument($id)
    {
        return $this->g_drive->files->get($id, ["alt" => "media"]);
    }
}
