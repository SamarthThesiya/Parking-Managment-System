package models.entity;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import models.BaseModel;

public class LandServicePerson implements BaseModel, Serializable {

    @SerializedName("id")
    Integer id;

    @SerializedName("service_person_id")
    Integer servicePersonId;

    @SerializedName("land_id")
    Integer landId;

    public Integer getLandId() {
        return landId;
    }
}
