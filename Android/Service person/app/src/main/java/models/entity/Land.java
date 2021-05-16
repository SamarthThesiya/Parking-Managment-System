package models.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import models.BaseModel;

public class Land implements BaseModel, Serializable, Cloneable {

    @SerializedName("id")
    public Integer id;

    @SerializedName("e_to_w_img")
    public Integer eToWImg;

    @SerializedName("city")
    public String city;

    @SerializedName("owner_id")
    public Integer ownerId;

    @SerializedName("latitude")
    public Double latitude;

    @SerializedName("description")
    public String description;

    @SerializedName("n_to_s_img")
    public Integer nToSImg;

    @SerializedName("title")
    public String title;

    @SerializedName("zip_code")
    public String zipCode;

    @SerializedName("w_to_e_img")
    public Integer wToEImg;

    @SerializedName("price_4w_100_to_50")
    public String price4w100To50;

    @SerializedName("status_id")
    public Integer statusId;

    @SerializedName("price_2w_50_to_10")
    public String price2w50To10;

    @SerializedName("price_2w_less_than_10")
    public String price2wLessThan10;

    @SerializedName("address_line_1")
    public String addressLine1;

    @SerializedName("price_4w_less_than_10")
    public String price4wLessThan10;

    @SerializedName("address_line_2")
    public String addressLine2;

    @SerializedName("state")
    public String state;

    @SerializedName("longitude")
    public Double longitude;

    @SerializedName("area")
    public String area;

    @SerializedName("price_4w_50_to_10")
    public String price4w50To10;

    @SerializedName("length")
    public String length;

    @SerializedName("s_to_n_img")
    public Integer sToNImg;

    @SerializedName("auditor_comment")
    public String auditorComment;

    @SerializedName("width")
    public String width;

    @SerializedName("price_2w_100_to_50")
    public String price2w100To50;

    @SerializedName("auditor_id")
    public Integer auditorId;

    @SerializedName("land_status")
    public Enums landStatus;

    @SerializedName("owner")
    public User owner;

    public User getOwner() {
        return owner;
    }

    public void setEToWImg(int eToWImg) {
        this.eToWImg = eToWImg;
    }

    public Integer getEToWImg() {
        return eToWImg;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setNToSImg(int nToSImg) {
        this.nToSImg = nToSImg;
    }

    public Integer getNToSImg() {
        return nToSImg;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setWToEImg(int wToEImg) {
        this.wToEImg = wToEImg;
    }

    public Integer getWToEImg() {
        return wToEImg;
    }

    public void setPrice4w100To50(String price4w100To50) {
        this.price4w100To50 = price4w100To50;
    }

    public String getPrice4w100To50() {
        return price4w100To50;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setPrice2w50To10(String price2w50To10) {
        this.price2w50To10 = price2w50To10;
    }

    public String getPrice2w50To10() {
        return price2w50To10;
    }

    public void setPrice2wLessThan10(String price2wLessThan10) {
        this.price2wLessThan10 = price2wLessThan10;
    }

    public String getPrice2wLessThan10() {
        return price2wLessThan10;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setPrice4wLessThan10(String price4wLessThan10) {
        this.price4wLessThan10 = price4wLessThan10;
    }

    public String getPrice4wLessThan10() {
        return price4wLessThan10;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setPrice4w50To10(String price4w50To10) {
        this.price4w50To10 = price4w50To10;
    }

    public String getPrice4w50To10() {
        return price4w50To10;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLength() {
        return length;
    }

    public void setSToNImg(int sToNImg) {
        this.sToNImg = sToNImg;
    }

    public Integer getSToNImg() {
        return sToNImg;
    }

    public void setAuditorComment(String auditorComment) {
        this.auditorComment = auditorComment;
    }

    public String getAuditorComment() {
        return auditorComment;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidth() {
        return width;
    }

    public void setPrice2w100To50(String price2w100To50) {
        this.price2w100To50 = price2w100To50;
    }

    public String getPrice2w100To50() {
        return price2w100To50;
    }

    public void setAuditorId(int auditorId) {
        this.auditorId = auditorId;
    }

    public Integer getAuditorId() {
        return auditorId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @NonNull
    @Override
    public Land clone() throws CloneNotSupportedException {
        return (Land) super.clone();
    }
}