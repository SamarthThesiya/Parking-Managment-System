package models.responseModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class ApiOkResponse implements BaseModel {

    @SerializedName("success")
    private boolean success;

    public boolean getSuccess(){
        return success;
    }
}
