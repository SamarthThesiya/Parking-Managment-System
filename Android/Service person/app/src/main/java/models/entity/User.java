package models.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import models.BaseModel;

public class User implements BaseModel, Serializable {

    @SerializedName("id")
    Integer id;

    @SerializedName("username")
    String  userName;

    @SerializedName("role_id")
    Integer roleId;

    @SerializedName("phone")
    String  phone;

    @SerializedName("land_service_person")
    LandServicePerson landServicePerson;

    public LandServicePerson getLandServicePerson() {
        return landServicePerson;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
