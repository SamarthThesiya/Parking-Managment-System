package models.responseModels;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class GetUsersResponse implements BaseModel {

    @SerializedName("users")
    private List<UsersItem> users;

    public List<UsersItem> getUsers() {
        return users;
    }

    public static class UsersItem {

        @SerializedName("role")
        private String role;

        @SerializedName("id")
        private int id;

        @SerializedName("username")
        private String username;

        public String getRole() {
            return role;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
    }
}