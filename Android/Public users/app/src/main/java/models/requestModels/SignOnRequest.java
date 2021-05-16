package models.requestModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class SignOnRequest implements BaseModel {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("username")
    @Expose
    private String userName;
    @SerializedName("role_id")
    @Expose
    private Integer roleId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }


}
