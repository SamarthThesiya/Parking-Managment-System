package models.requestModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class PatchAuditLandRequest implements BaseModel {

    @SerializedName("response")
    String response;

    @SerializedName("comment")
    String comment;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
