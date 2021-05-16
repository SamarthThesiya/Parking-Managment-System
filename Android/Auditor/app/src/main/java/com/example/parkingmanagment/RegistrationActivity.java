package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityRegistrationBinding;

import custom_view.PmButton;
import custom_view.PmEditText;
import models.BaseModel;
import models.requestModels.SignOnRequest;
import models.responseModels.ErrorResponse;
import models.responseModels.GetRolesResponse;
import models.responseModels.LoginResponse;
import services.AuthService;
import utils.Constants;
import utils.MySharedPreferences;
import utils.Validator;

public class RegistrationActivity extends BaseActivity {

    public  String      username;
    private String      phone;
    private AuthService authService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegistrationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        binding.setRegistrationActivity(this);
        phone       = getIntent().getStringExtra(MainActivity.PHONE_NUMBER);
        authService = new AuthService(this);
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetRolesResponse) {
            GetRolesResponse getRolesResponse = (GetRolesResponse) baseModel;

            SignOnRequest signOnRequest = new SignOnRequest();
            signOnRequest.setRoleId(getRolesResponse.getRoles().get(0).getId());
            signOnRequest.setPhone(phone);
            signOnRequest.setUserName(username);

            controller.callApi(apis.postSignOn(signOnRequest), true);
        } else if (baseModel instanceof LoginResponse) {
            LoginResponse loginResponse = (LoginResponse) baseModel;
            MySharedPreferences.setSharedPreferense(MySharedPreferences.ACCESS_TOKEN, loginResponse.getAccess_token(), this);

            authService.updateDisplayName(username, task -> {
                Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                startActivity(intent);
            });

        }
    }

    @Override
    public void onError(ErrorResponse response) {
        super.onError(response);

        if (authService.getSignedInUser() != null) {
            authService.signOut();
        }
    }

    @Override
    void init() {
        PmEditText et_username = findViewById(R.id.et_username);
        et_username.getValidations().add(Validator.REQUIRED);

        PmButton btn_register = findViewById(R.id.btn_register);
        btn_register.setDependent(et_username);
    }

    @Override
    void setOnclickListeners() {
        PmButton btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(view -> {
            controller.callApi(apis.getRoles(Constants.auditor), true);
        });
    }
}