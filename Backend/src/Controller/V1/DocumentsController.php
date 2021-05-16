<?php


namespace App\Controller\V1;


use App\Controller\ApiController;
use App\Model\Response\ApiOKResponse;
use App\Model\Response\Error\NotFoundResponse;
use App\Service\DocumentService;
use Cake\Http\Response;

/**
 * Class DocumentsController
 *
 * @package App\Controller\v1
 *
 * @property \App\Model\Table\DocumentsTable     $Documents
 * @property \App\Model\Table\DocumentTypesTable $DocumentTypes
 *
 * @property DocumentService                     $DocumentService
 */
class DocumentsController extends ApiController
{
    public $services = [
        'DocumentService' => ['className' => DocumentService::class],
    ];

    public function initialize()
    {
        parent::initialize();

        $this->loadModel('Documents');
        $this->loadModel('DocumentTypes');
    }

    public function getDocuments()
    {

        $documents = $this->Documents->find()
            ->contain(['DocumentTypes', 'Users'])
            ->all();

        return new ApiOKResponse(['documents' => $documents]);
    }

    public function addDocument()
    {
        $documentData = $this->DocumentService->handleSingleDocumentUpload();

        $documentData->ownerId        = $this->Auth->user('id');
        $documentData->documentTypeId = $this->request->getData('document_type_id');

        $document = $this->DocumentService->saveDocument($documentData);

        return new ApiOKResponse($document);
    }

    public function updateDocument(int $id)
    {
        /** @var \App\Model\Entity\Document $documentEntity */
        $documentEntity = $this->Documents->find()
            ->where([
                'id' => $id,
            ])->first();

        $data = $this->request->getData();

        $this->Documents->patchEntity($documentEntity, $data, []);

        $this->Documents->saveOrFail($documentEntity);

        $this->Documents->delete($documentEntity);

        return new ApiOKResponse($documentEntity);
    }

    public function getDocumentTypes()
    {
        return new ApiOKResponse($this->DocumentTypes->find()->all());
    }

    public function downloadDocument(int $id)
    {
        /** @var \App\Model\Entity\Document $document */
        $document = $this->Documents->find()
            ->where([
                $this->Documents->aliasField('id') => $id,
            ])->first();

        if (empty($document)) {
            return new NotFoundResponse('Document not found');
        }

        return $this->DocumentService->downloadDocument($document);
    }
}
