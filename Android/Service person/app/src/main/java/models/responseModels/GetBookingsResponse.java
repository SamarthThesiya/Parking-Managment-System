package models.responseModels;

import android.widget.LinearLayout;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import models.BaseModel;
import models.entity.Booking;

public class GetBookingsResponse implements BaseModel {

    @SerializedName("data")
    List<Booking> data;

    @SerializedName("pagination")
    Pagination pagination;

    public List<Booking> getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
