package models.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import models.BaseModel;

public class GetRolesResponse implements BaseModel {

    @SerializedName("roles")
    private List<RolesItem> roles;

    public List<RolesItem> getRoles() {
        return roles;
    }

    public static class RolesItem {

        @SerializedName("name")
        private String name;

        @SerializedName("id")
        private int id;

        @SerializedName("display_name")
        private String displayName;

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}