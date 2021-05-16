<?php


namespace App\Service;


use App\Model\Entity\LandStatus;
use App\Model\Enums\LandStatuses;

/**
 * Class LandService
 *
 * @package App\Service
 *
 * @property \App\Model\Table\LandsTable        $Lands
 * @property \App\Model\Table\LandStatusesTable $LandStatuses
 */
class LandService extends Service
{
    public function initialize(): void
    {
        parent::initialize();
        $this->loadModel('Lands');
        $this->loadModel('LandStatuses');
    }

    public function changeLandStatus(int $landId, string $status)
    {
        $land = $this->Lands->get($landId);

        $land->status_id = $this->LandStatuses->find()
            ->where([
                'name' => $status,
            ])->first()->id;

        $this->Lands->saveOrFail($land);
    }

    public function getLandStatusFromName(string $status) : LandStatus {

        /** @var LandStatus $landStatus */
        $landStatus = $this->LandStatuses->find()->where(['name' => $status])->firstOrFail();

        return $landStatus;
    }
}
