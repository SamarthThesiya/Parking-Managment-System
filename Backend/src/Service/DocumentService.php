<?php


namespace App\Service;


use App\Model\Entity\Document;
use App\Model\PM_models\DocumentInfo;
use Cake\Http\Response;
use Cake\Utility\Security;

/**
 * Class DocumentService
 *
 * @property \App\Model\Table\DocumentsTable $Documents
 *
 * @property \App\Service\GoogleDriveService $GoogleDrive
 *
 * @package App\Service
 */
class DocumentService extends Service
{
    public $services = [
        'GoogleDrive',
    ];

    private static $extensionToMimeMap = [
        'txt'  => 'text/plain',
        'htm'  => 'text/html',
        'html' => 'text/html',
        'php'  => 'text/html',
        'css'  => 'text/css',
        'js'   => 'application/javascript',
        'json' => 'application/json',
        'xml'  => 'application/xml',
        'swf'  => 'application/x-shockwave-flash',
        'flv'  => 'video/x-flv',

        // images
        'png'  => 'image/png',
        'jpe'  => 'image/jpeg',
        'jpeg' => 'image/jpeg',
        'jpg'  => 'image/jpeg',
        'gif'  => 'image/gif',
        'bmp'  => 'image/bmp',
        'ico'  => 'image/vnd.microsoft.icon',
        'tiff' => 'image/tiff',
        'tif'  => 'image/tiff',
        'svg'  => 'image/svg+xml',
        'svgz' => 'image/svg+xml',

        // archives
        'zip'  => 'application/zip',
        'rar'  => 'application/x-rar-compressed',
        'exe'  => 'application/x-msdownload',
        'msi'  => 'application/x-msdownload',
        'cab'  => 'application/vnd.ms-cab-compressed',

        // audio/video
        'mp3'  => 'audio/mpeg',
        'qt'   => 'video/quicktime',
        'mov'  => 'video/quicktime',

        // adobe
        'pdf'  => 'application/pdf',
        'psd'  => 'image/vnd.adobe.photoshop',
        'ai'   => 'application/postscript',
        'eps'  => 'application/postscript',
        'ps'   => 'application/postscript',

        // ms office
        'doc'  => 'application/msword',
        'docx' => 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'rtf'  => 'application/rtf',
        'xls'  => 'application/vnd.ms-excel',
        'xlsx' => 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'ppt'  => 'application/vnd.ms-powerpoint',

        // open office
        'odt'  => 'application/vnd.oasis.opendocument.text',
        'ods'  => 'application/vnd.oasis.opendocument.spreadsheet',
    ];

    public function initialize(): void
    {
        parent::initialize();

        $this->loadModel('Documents');
    }

    /**
     * Handles single document upload by moving file to temp directory and
     * returning its metadata.
     *
     * @param string $fileField Name of field which contains file in $_FILES array.
     *
     * @return DocumentInfo Uploaded document info.
     * @throws \RuntimeException When error occurs during file upload.
     *
     */
    public function handleSingleDocumentUpload(string $fileField = 'file'): DocumentInfo
    {
        if (!isset($_FILES[$fileField])) {
            throw new \RuntimeException('File is either not provided or could not be uploaded.');
        }

        if (0 !== $_FILES[$fileField]['error']) {
            throw new \RuntimeException($_FILES[$fileField]['error']);
        }

        $result = new DocumentInfo();

        $result->filePath      = TMP . sha1(Security::randomBytes(40));
        $result->fileName      = strtolower($_FILES[$fileField]['name']);
        $result->fileExtension = pathinfo($result->fileName, PATHINFO_EXTENSION);
        $result->fileSize      = $_FILES[$fileField]['size'];

        if (!move_uploaded_file($_FILES[$fileField]['tmp_name'], $result->filePath)) {
            throw new \RuntimeException('Provided file could not be uploaded.');
        }

        return $result;
    }

    public function saveDocument(DocumentInfo $documentInfo)
    {
        $g_driveId = $this->GoogleDrive->uploadFile($documentInfo);

        $document                       = $this->Documents->newEntity();
        $document->document_type_id     = $documentInfo->documentTypeId;
        $document->owner_id             = $documentInfo->ownerId;
        $document->file_name            = $documentInfo->fileName;
        $document->file_size            = $documentInfo->fileSize;
        $document->external_storage_url = $g_driveId;
        $document->extension            = $documentInfo->fileExtension;

        $this->Documents->saveOrFail($document);

        return $document;
    }

    public function downloadDocument(Document $document)
    {
        /** @var \GuzzleHttp\Psr7\Response $g_drive_response */
        $g_drive_response = $this->GoogleDrive->downloadDocument($document->external_storage_url);

        $response = new Response([
            'status'  => 200,
            'type'    => self::inferUserDocumentMimeContentType($document),
            'charset' => 'utf-8',
            'body'    => $g_drive_response->getBody(),
        ]);

        $response->withHeader('Content-length', $document->file_size);
        $response->withHeader('Content-disposition', 'attachment; filename="' . $document->file_name . '"');
        return $response;
    }

    /**
     * Infers and returns document mime-type. This method tries to figure out mime-type
     * by extension without physically accessing the file and if it fails
     * to find correspondent mime-type, then it uses SPL's mime_content_type method
     * to identify the mime-type.
     *
     * @param \App\Model\Entity\Document $document User document.
     *
     * @return string
     */
    public static function inferUserDocumentMimeContentType(Document $document): string
    {
        return self::getMimeContentTypeByExtension($document->extension);
    }

    /**
     * Returns mime-type associated with specified file extension.
     *
     * @param string $extension File extension.
     *
     * @return string Correspondent mime type of application/octet-stream if no correspondent type found.
     */
    public static function getMimeContentTypeByExtension(string $extension): string
    {
        return self::$extensionToMimeMap[$extension] ?? 'application/octet-stream';
    }
}
