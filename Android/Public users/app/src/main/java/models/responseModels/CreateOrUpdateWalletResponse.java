package models.responseModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class CreateOrUpdateWalletResponse implements BaseModel {

    @SerializedName("wallet_balance")
    float walletBalance;

    public float getWalletBalance() {
        return walletBalance;
    }
}
