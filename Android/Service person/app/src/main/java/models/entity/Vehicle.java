package models.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import models.BaseModel;

public class Vehicle implements BaseModel, Serializable {

    @SerializedName("type_id")
    @Expose
    private Integer typeId;
    @SerializedName("brand")
    @Expose
    private String  brand;
    @SerializedName("model")
    @Expose
    private String  model;
    @SerializedName("registration_number")
    @Expose
    private String  registrationNumber;
    @SerializedName("image_id")
    @Expose
    private Integer imageId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("vehicle_type")
    @Expose
    private Enums   vehicleType;

    public Enums getVehicleType() {
        return vehicleType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
