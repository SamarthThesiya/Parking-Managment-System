package models.entity.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import models.BaseModel;
import models.entity.Booking;
import models.entity.Enums;
import models.entity.Land;
import models.entity.User;
import models.entity.Vehicle;
import models.requestModels.BookingRequest;
import utils.DateTimeUtil;

public class BookingData implements BaseModel, Serializable {
    String  date;
    String  startTime;
    String  endTime;
    Integer landId;
    Float   totalCharge;
    Integer walletBalance;
    Long    dateTimestamp;
    Land    selectedLand;
    Vehicle selectedVehicle;
    String  error;
    Integer bookingId;
    User    vehicleOwner;
    Enums   bookingStatus;
    String  rejectionReason;

    public BookingData() {

    }

    public BookingData(Booking booking) {

        String[] splittedStartTime = booking.getConvertedStartTime().split(" ");

        String date = DateTimeUtil.convertDateTimFormat(splittedStartTime[0], "yyyy-MM-dd", "dd/MM/yyyy");

        String startTime = booking.getConvertedStartTime().substring(date.length() + 1);
        String endTime   = booking.getConvertedEndTime().substring(date.length() + 1);

        setDate(date);
        setStartTime(startTime);
        setEndTime(endTime);
        setSelectedLand(booking.getLand());
        setSelectedVehicle(booking.getVehicle());
        setTotalCharge(Float.valueOf(booking.getBookingFee()));
        setWalletBalance(1000);
        setError(booking.getError());
        setBookingId(booking.getId());
        setVehicleOwner(booking.getVehicleOwner());
        setBookingStatus(booking.getStatus());
        setRejectionReason(booking.getDenialReason());
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setBookingStatus(Enums bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Enums getBookingStatus() {
        return bookingStatus;
    }

    public void setVehicleOwner(User vehicleOwner) {
        this.vehicleOwner = vehicleOwner;
    }

    public User getVehicleOwner() {
        return vehicleOwner;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedLand(Land selectedLand) {
        this.selectedLand = selectedLand;
    }

    public Land getSelectedLand() {
        return selectedLand;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateTimestamp(Long date) {
        this.dateTimestamp = date;
    }

    public Long getDateTimestamp() {
        return dateTimestamp;
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

    public Integer getLandId() {
        return landId;
    }

    public void setLandId(Integer landId) {
        this.landId = landId;
    }

    public String getTotalCharge() {
        return String.valueOf(totalCharge);
    }

    public void setTotalCharge(Float totalCharge) {
        this.totalCharge = totalCharge;
    }

    public Integer getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Integer walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Timestamp getStartTimeTimeStamp() {
        return DateTimeUtil.getTimestampFromDateTime(getStartDateTime().replace(" : ", ":"));
    }

    public Timestamp getEndTimeTimeStamp() {
        return DateTimeUtil.getTimestampFromDateTime(getEndDateTime().replace(" : ", ":"));
    }

    public String getDuration() {
        return String.valueOf(DateTimeUtil.getHourDifferenceBetweenTimeStamps(getStartTimeTimeStamp(), getEndTimeTimeStamp()));
    }

    public BookingRequest getBookingRequest() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setBookingFee(getTotalCharge());
        bookingRequest.setLandId(getSelectedLand().getId());
        bookingRequest.setVehicleId(getSelectedVehicle().getId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        bookingRequest.setStartTime(sdf.format(getStartTimeTimeStamp()));
        bookingRequest.setEndTime(sdf.format(getEndTimeTimeStamp()));

        return bookingRequest;
    }

    public String getStartDateTime() {
        return date + " " + startTime;
    }

    public String getEndDateTime() {
        return date + " " + endTime;
    }

}
