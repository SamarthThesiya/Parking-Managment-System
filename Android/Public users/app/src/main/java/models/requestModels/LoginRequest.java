package models.requestModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class LoginRequest implements BaseModel {

    @SerializedName("user_name")
    public String user_name;

    @SerializedName("password")
    public String password;

    public String getUser_name() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
