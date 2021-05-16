package models.responseModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class LoginResponse implements BaseModel {

    @SerializedName("access_token")
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

}
