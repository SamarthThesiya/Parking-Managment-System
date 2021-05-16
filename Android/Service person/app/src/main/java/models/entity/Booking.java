package models.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import models.BaseModel;
import utils.DateTimeUtil;

public class Booking implements BaseModel, Serializable {

    @SerializedName("id")
    Integer id;

    @SerializedName("user_id")
    Integer userId;

    @SerializedName("land_id")
    Integer landId;

    @SerializedName("start_time")
    String  startTime;

    @SerializedName("end_time")
    String  endTime;

    @SerializedName("booking_fee")
    String  bookingFee;

    @SerializedName("status_id")
    Integer statusId;

    @SerializedName("vehicle_id")
    Integer vehicleId;

    @SerializedName("land")
    Land land;

    @SerializedName("booking_token")
    String bookingToken;

    @SerializedName("vehicle")
    Vehicle vehicle;

    @SerializedName("booking_status")
    Enums status;

    @SerializedName("error")
    String error;

    @SerializedName("user")
    User vehicleOwner;

    @SerializedName("denial_reason")
    String denialReason;

    public void setDenialReason(String denialReason) {
        this.denialReason = denialReason;
    }

    public String getDenialReason() {
        return denialReason;
    }

    public User getVehicleOwner() {
        return vehicleOwner;
    }

    public String getError() {
        return error;
    }

    public void setBookingToken(String bookingToken) {
        this.bookingToken = bookingToken;
    }

    public String getBookingToken() {
        return bookingToken;
    }

    public Enums getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getConvertedStartTime() {
        return DateTimeUtil.convertServerToClientDateTime(getStartTime());
    }

    public String getConvertedEndTime() {
        return DateTimeUtil.convertServerToClientDateTime(getEndTime());
    }
}
