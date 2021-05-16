package models.entity;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class MonthlyEarnings implements BaseModel {

    @SerializedName("month")
    String month;

    @SerializedName("earning")
    float  earnings;

    public String getMonth() {
        return month;
    }

    public String getEarnings() {
        return String.valueOf(earnings);
    }
}
