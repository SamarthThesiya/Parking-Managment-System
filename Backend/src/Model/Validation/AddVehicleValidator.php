<?php


namespace App\Model\Validation;


class AddVehicleValidator extends BaseValidator
{
    public function __construct()
    {
        parent::__construct();

        $this->existsInTable("image_id", "documents", "id");
        $this->existsInTable("type_id", "vehicle_types", "id");

        $this->requirePresence("type_id");
        $this->requirePresence("brand");
        $this->requirePresence("model");
        $this->requirePresence("registration_number");
        $this->requirePresence("image_id");

    }
}
