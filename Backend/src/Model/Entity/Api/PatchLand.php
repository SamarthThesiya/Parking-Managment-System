<?php


namespace App\Model\Entity\Api;


use App\Model\Entity\Land;

class PatchLand extends Land implements \JsonSerializable
{
    protected $_hidden = [
        'id'
    ];

    public function toArray()
    {
        $data = parent::toArray();

        foreach ($data as $key => $value) {
            if ($value == null) {
                unset($data[$key]);
            }
        }

        foreach ($data as $key => $value) {
            if ($value === '') {
                $data[$key] = null;
            }
        }
    }
}
