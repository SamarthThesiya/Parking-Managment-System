<?php


namespace App\Model\Entity\Api;


use App\Model\Entity\Land;
use App\Model\Enums\BookingStatuses;
use App\Service\BookingService;
use Cake\Datasource\ModelAwareTrait;
use Cake\I18n\Time;

/**
 * Class LandOwnerGetLands
 *
 * @package App\Model\Entity\Api
 *
 * @property \App\Model\Table\LandBookingsTable    $LandBookings
 * @property \App\Model\Table\BookingStatusesTable $BookingStatuses
 */
class LandOwnerGetLands extends Land
{
    use ModelAwareTrait;

    public function __construct(array $properties = [], array $options = [])
    {
        parent::__construct($properties, $options);
        $this->loadModel('LandBookings');
        $this->loadModel('BookingStatuses');
    }

    protected $_virtual = [
        'availability',
        'current_month_earnings',
        'total_earnings',
    ];

    public function _getAvailability()
    {
        $service = new BookingService();
        $service->initialize();

        $startTime    = Time::now();
        $endTime      = (clone $startTime)->addMinutes(1);
        $occupiedArea = $service->getPercentageOfOccupiedAreaOfLand($this, $startTime, $endTime);

        return 100 - $occupiedArea;
    }

    public function _getCurrentMonthEarnings()
    {
        $currentTime = Time::now();
        $startTime   = (clone $currentTime)->subDays($currentTime->day-1)->format('Y-m-d');

        return $this->LandBookings->find()
            ->contain("BookingStatuses")
            ->where([
                $this->BookingStatuses->aliasField('name')    => BookingStatuses::PARKED,
                $this->LandBookings->aliasField('start_time >=') => $startTime,
                $this->LandBookings->aliasField('land_id')    => $this->_properties['id'],
            ])->sumOf('booking_fee');
    }

    public function _getTotalEarnings()
    {
        return $this->LandBookings->find()
            ->contain("BookingStatuses")
            ->where([
                $this->BookingStatuses->aliasField('name') => BookingStatuses::PARKED,
                $this->LandBookings->aliasField('land_id') => $this->_properties['id'],
            ])->sumOf('booking_fee');
    }
}
