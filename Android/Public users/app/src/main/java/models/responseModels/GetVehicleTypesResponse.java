package models.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import models.BaseModel;
import models.entity.Enums;

public class GetVehicleTypesResponse implements BaseModel {

    @SerializedName("data")
    private List<Enums> data;

    public List<Enums> getData() {
        return data;
    }
}
