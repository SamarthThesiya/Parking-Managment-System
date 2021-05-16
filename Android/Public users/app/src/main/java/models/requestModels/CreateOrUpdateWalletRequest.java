package models.requestModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class CreateOrUpdateWalletRequest implements BaseModel {

    @SerializedName("add_amount")
    float addAmount;

    public void setAddAmount(float addAmount) {
        this.addAmount = addAmount;
    }
}
