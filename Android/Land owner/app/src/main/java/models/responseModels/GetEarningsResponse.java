package models.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import models.BaseModel;
import models.entity.MonthlyEarnings;

public class GetEarningsResponse implements BaseModel {

    @SerializedName("total_earnings")
    float totalEarnings;

    @SerializedName("balance")
    float balance;

    @SerializedName("history")
    List<MonthlyEarnings> history;

    public String getTotalEarnings() {
        return String.valueOf(totalEarnings);
    }

    public String  getBalance() {
        return String.valueOf(balance);
    }

    public List<MonthlyEarnings> getHistory() {
        return history;
    }
}
