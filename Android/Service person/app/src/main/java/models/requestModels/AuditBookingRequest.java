package models.requestModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class AuditBookingRequest implements BaseModel {

    @SerializedName("result")
    String result;

    @SerializedName("denial_reason")
    String denialReason;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDenialReason() {
        return denialReason;
    }

    public void setDenialReason(String denialReason) {
        this.denialReason = denialReason;
    }
}
