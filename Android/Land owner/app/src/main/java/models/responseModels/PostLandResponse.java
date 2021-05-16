package models.responseModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class PostLandResponse implements BaseModel {

    @SerializedName("id")
    public int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
