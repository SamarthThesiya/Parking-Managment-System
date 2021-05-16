<?php


namespace App\Service;

use App\Model\Entity\Land;
use App\Model\Enums\VehicleTypes;

/**
 * Class BookingService
 *
 * @package App\Service
 *
 * @property \App\Model\Table\LandsTable        $Lands
 * @property \App\Model\Table\LandBookingsTable $LandBookings
 */
class BookingService extends Service
{
    public function initialize(): void
    {
        parent::initialize();
        $this->loadModel("Lands");
        $this->loadModel("LandBookings");
    }

    public function getCapacityOfLand(Land $land)
    {
        return $land->width * $land->length;
    }

    public function getOccupiedAreaOfLand(Land $land, string $startTime, string $endTime)
    {
        $areaRequiredForSingleCar  = 80; // In square feet
        $areaRequiredForSingleBike = 15; // In square feet

        $occupiedSpaceByVehicle = 0;

        /** @var \App\Model\Entity\LandBooking[] $landBookings */
        $landBookings = $this->LandBookings->find()
            ->contain("Vehicles.VehicleTypes")
            ->where([
                $this->LandBookings->aliasField("land_id") => $land->id,
                'or'                                       => [
                    [
                        $this->LandBookings->aliasField("start_time >=") => $startTime,
                        $this->LandBookings->aliasField("end_time < ")    => $endTime,
                    ],
                    [
                        $this->LandBookings->aliasField("start_time <=") => $startTime,
                        $this->LandBookings->aliasField("end_time > ")    => $endTime,
                    ]
                ],
            ])->all()->toArray();

        foreach ($landBookings as $landBooking) {
            if ($landBooking->vehicle->vehicle_type->name == VehicleTypes::TWO_WHEELER) {
                $occupiedSpaceByVehicle += $areaRequiredForSingleBike;
            } elseif ($landBooking->vehicle->vehicle_type->name == VehicleTypes::FOUR_WHEELER) {
                $occupiedSpaceByVehicle += $areaRequiredForSingleCar;
            }
        }

        return $occupiedSpaceByVehicle * 2;
    }

    public function getPercentageOfOccupiedAreaOfLand(Land $land, string $startTime, string $endTime)
    {
        return ($this->getOccupiedAreaOfLand($land, $startTime, $endTime) / $this->getCapacityOfLand($land)) * 100;
    }

    public function getDynamicPriceOfLand(Land $land, string $startTime, string $endTime, string $vehicleType)
    {
        $occupiedLand = $this->getPercentageOfOccupiedAreaOfLand($land, $startTime, $endTime);

        $twoWheelerPrice  = 0;
        $fourWheelerPrice = 0;

        $freeLand = 100 - $occupiedLand;

        if ($freeLand >= 50) {
            $twoWheelerPrice  = $land->price_2w_100_to_50;
            $fourWheelerPrice = $land->price_4w_100_to_50;
        } elseif ($freeLand >= 10) {
            $twoWheelerPrice  = $land->price_2w_50_to_10;
            $fourWheelerPrice = $land->price_4w_50_to_10;
        } else {
            $twoWheelerPrice  = $land->price_2w_less_than_10;
            $fourWheelerPrice = $land->price_4w_less_than_10;
        }

        if ($vehicleType == VehicleTypes::TWO_WHEELER) {
            return $twoWheelerPrice;
        } elseif ($vehicleType == VehicleTypes::FOUR_WHEELER) {
            return $fourWheelerPrice;
        }
    }
}
