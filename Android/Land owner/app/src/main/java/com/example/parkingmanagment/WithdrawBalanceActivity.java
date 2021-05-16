package com.example.parkingmanagment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityWithdrawBalanceBinding;

import custom_view.PmButton;
import custom_view.PmEditText;
import models.BaseModel;
import models.requestModels.WithdrawAmountRequest;
import models.responseModels.GetEarningsResponse;
import models.responseModels.WithdrawAmountResponse;
import utils.Validator;

public class WithdrawBalanceActivity extends BaseActivity {
    ActivityWithdrawBalanceBinding binding;
    PmEditText                     et_withdrawAmount;
    PmButton                       btn_back, btn_withdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdraw_balance);
    }

    @Override
    void init() {
        et_withdrawAmount = findViewById(R.id.et_withdraw_amount);
        et_withdrawAmount.getValidations().add(Validator.REQUIRED);

        btn_back          = findViewById(R.id.btn_back);
        btn_withdraw      = findViewById(R.id.btn_withdraw);
        btn_withdraw.setDependent(et_withdrawAmount);

        controller.callApi(apis.getEarnings());
    }

    @Override
    void setOnclickListeners() {
        btn_back.setOnClickListener(view -> finish());
        btn_withdraw.setOnClickListener(view -> {

            WithdrawAmountRequest request = new WithdrawAmountRequest();
            request.setAmount(Float.parseFloat(et_withdrawAmount.getText().toString()));

            controller.callApi(apis.withdrawEarning(request));
        });
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetEarningsResponse) {
            GetEarningsResponse response = (GetEarningsResponse) baseModel;
            binding.setEarning(response);
        } else if (baseModel instanceof WithdrawAmountResponse) {
            WithdrawAmountResponse response = (WithdrawAmountResponse) baseModel;
            if (response.getSuccess()) {
                controller.callApi(apis.getEarnings());
            }
        }
    }
}