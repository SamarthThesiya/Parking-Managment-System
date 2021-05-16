package models.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import models.BaseModel;
import models.entity.Booking;

public class GetBookingsResponse implements BaseModel {


    @SerializedName("data")
    List<Booking> data;

    public List<Booking> getData() {
        return data;
    }
}
