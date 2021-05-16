package models.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import models.BaseModel;
import models.entity.Vehicle;

public class GetVehiclesResponse implements BaseModel {

    @SerializedName("data")
    private List<Vehicle> data;

    public List<Vehicle> getData() {
        return data;
    }

}
