<?php


namespace App\Model\Entity\Api;


use App\Model\Entity\Land;
use App\Model\Enums\VehicleTypes;
use App\Service\BookingService;
use Cake\Routing\Router;

class GetVehicleOwnerLand extends Land
{
    protected $_hidden = [
        "price_2w_100_to_50",
        "price_4w_100_to_50",
        "price_2w_50_to_10",
        "price_4w_50_to_10",
        "price_2w_less_than_10",
        "price_4w_less_than_10",
    ];

    protected $_virtual = ["2_wheeler_price", "4_wheeler_price", "is_land_available"];

    public function _get2WheelerPrice()
    {
        $request   = Router::getRequest();
        $startTime = $request->getQuery("startTime");
        $endTime   = $request->getQuery("endTime");

        $service = new BookingService();
        $service->initialize();
        return $service->getDynamicPriceOfLand($this, $startTime, $endTime, VehicleTypes::TWO_WHEELER);
    }

    public function _get4WheelerPrice()
    {
        $request   = Router::getRequest();
        $startTime = $request->getQuery("startTime");
        $endTime   = $request->getQuery("endTime");

        $service = new BookingService();
        $service->initialize();
        return $service->getDynamicPriceOfLand($this, $startTime, $endTime, VehicleTypes::FOUR_WHEELER);
    }

    public function _getIsLandAvailable() {

        $request   = Router::getRequest();
        $startTime = $request->getQuery("startTime");
        $endTime   = $request->getQuery("endTime");

        $service = new BookingService();
        $service->initialize();
        $occupiedArea = $service->getPercentageOfOccupiedAreaOfLand($this, $startTime, $endTime);

        $freeArea = 100 - $occupiedArea;

        if ($freeArea < 5) {
            return false;
        }
        return true;
    }
}
