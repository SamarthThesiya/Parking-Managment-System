<?php


namespace App\Service;

use App\Exceptions\NotAllowedException;
use Cake\I18n\Time;

/**
 * Class LandOwnerTransactionService
 *
 * @package App\Service
 *
 * @property \App\Model\Table\LandOwnerTransactionsTable $LandOwnerTransactions
 */
class LandOwnerTransactionService extends Service
{
    public function initialize(): void
    {
        parent::initialize();

        $this->loadModel("LandOwnerTransactions");
    }

    public function credit(int $landOwnerId, float $amount)
    {
        $landOwnerTransaction = $this->LandOwnerTransactions->newEntity();

        $landOwnerTransaction->land_owner_id    = $landOwnerId;
        $landOwnerTransaction->amount           = $amount;
        $landOwnerTransaction->transaction_type = 'credit';

        $this->LandOwnerTransactions->saveOrFail($landOwnerTransaction);

        return $landOwnerTransaction;
    }

    public function debit(int $landOwnerId, float $amount)
    {
        if ($amount > $this->getBalance($landOwnerId)) {
            throw new NotAllowedException("Insufficient amount in wallet");
        }
        $landOwnerTransaction = $this->LandOwnerTransactions->newEntity();

        $landOwnerTransaction->land_owner_id    = $landOwnerId;
        $landOwnerTransaction->amount           = $amount;
        $landOwnerTransaction->transaction_type = 'debit';

        $this->LandOwnerTransactions->saveOrFail($landOwnerTransaction);

        return $landOwnerTransaction;
    }

    public function getTotalEarnings(int $landOwnerId)
    {
        return $this->LandOwnerTransactions->find()
            ->where([
                $this->LandOwnerTransactions->aliasField("land_owner_id")    => $landOwnerId,
                $this->LandOwnerTransactions->aliasField("transaction_type") => 'credit',
            ])->sumOf("amount");
    }

    public function getTotalWithdrawals(int $landOwnerId)
    {
        return $this->LandOwnerTransactions->find()
            ->where([
                $this->LandOwnerTransactions->aliasField("land_owner_id")    => $landOwnerId,
                $this->LandOwnerTransactions->aliasField("transaction_type") => 'debit',
            ])->sumOf("amount");
    }

    public function getBalance(int $landOwnerId)
    {
        return $this->getTotalEarnings($landOwnerId) - $this->getTotalWithdrawals($landOwnerId);
    }

    public function getLast5monthsEarnings(int $landOwnerId)
    {
        $result = [];
        for ($i = 0; $i < 5; $i++) {

            $year  = Time::now()->subMonth($i)->year;
            $month = Time::now()->subMonth($i)->month;

            $dateObj   = \DateTime::createFromFormat('!m', $month);
            $monthName = $dateObj->format('F');

            $result[] = [
                "month"   => $monthName . ' ' . $year,
                "earning" => $this->getTransactionsByMonth(
                    $landOwnerId,
                    $month,
                    $year
                ),
            ];
        }

        return $result;
    }

    public function getTransactionsByMonth(int $landOwnerId, int $month, int $year, string $type = 'credit')
    {
        return $this->LandOwnerTransactions->find()
            ->where([
                $this->LandOwnerTransactions->aliasField("land_owner_id")    => $landOwnerId,
                $this->LandOwnerTransactions->aliasField("transaction_type") => $type,
                "EXTRACT(MONTH FROM created_at) = "                          => $month,
                "EXTRACT(YEAR FROM created_at) = "                           => $year,
            ])->sumOf("amount");
    }

}
