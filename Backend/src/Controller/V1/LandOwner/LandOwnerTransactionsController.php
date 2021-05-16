<?php


namespace App\Controller\V1\LandOwner;


use App\Controller\ApiController;
use App\Model\Response\ApiOKResponse;
use App\Model\Validation\LandOwnerWithdrawValidator;

/**
 * Class LandOwnerTransactionsController
 *
 * @package App\Controller\V1\LandOwner
 *
 * @property \App\Service\LandOwnerTransactionService $LandOwnerTransaction
 */
class LandOwnerTransactionsController extends ApiController
{
    public $services = [
        "LandOwnerTransaction",
    ];

    public function initialize()
    {
        parent::initialize();

        $this->RequestValidator->addRequestDataValidators([
            "debit" => LandOwnerWithdrawValidator::class,
        ]);
    }

    public function getEarningsData()
    {
        $userId = $this->Auth->user('id');

        $response = [];

        $response["total_earnings"] = $this->LandOwnerTransaction->getTotalEarnings($userId);
        $response["balance"]        = $this->LandOwnerTransaction->getBalance($userId);
        $response["history"]        = $this->LandOwnerTransaction->getLast5monthsEarnings($userId);

        return new ApiOKResponse($response);
    }

    public function debit()
    {
        $userId         = $this->Auth->user('id');
        $withdrawAmount = $this->request->getData("amount");

        $this->LandOwnerTransaction->debit($userId, $withdrawAmount);

        return new ApiOKResponse();
    }
}
