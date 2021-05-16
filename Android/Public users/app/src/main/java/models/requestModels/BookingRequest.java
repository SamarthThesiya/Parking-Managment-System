package models.requestModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class BookingRequest implements BaseModel {

    @SerializedName("land_id")
    @Expose
    private Integer landId;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("booking_fee")
    @Expose
    private String bookingFee;
    @SerializedName("vehicle_id")
    @Expose
    private Integer vehicleId;

    public Integer getLandId() {
        return landId;
    }

    public void setLandId(Integer landId) {
        this.landId = landId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBookingFee() {
        return bookingFee;
    }

    public void setBookingFee(String bookingFee) {
        this.bookingFee = bookingFee;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }
}
