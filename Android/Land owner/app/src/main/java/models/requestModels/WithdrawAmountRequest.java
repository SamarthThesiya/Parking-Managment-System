package models.requestModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class WithdrawAmountRequest implements BaseModel {

    @SerializedName("amount")
    float amount;

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
