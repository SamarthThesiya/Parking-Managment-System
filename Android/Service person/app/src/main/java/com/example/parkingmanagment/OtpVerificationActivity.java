package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityOtpVerificationBinding;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import interfaces.OtpVerificationInterface;
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

public class OtpVerificationActivity extends BaseActivity implements OtpVerificationInterface {

    public  String       phone = "Phone";
    public  String       otp;
    public  String       otp_token;
    private AuthService  authService;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOtpVerificationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verification);
        binding.setOtpActivity(this);

        otp_token = getIntent().getStringExtra(MainActivity.OTP_TOKEN);
        phone     = getIntent().getStringExtra(MainActivity.PHONE_NUMBER);

        authService = new AuthService(this);
    }

    @Override
    public void onOtpSuccess(FirebaseUser firebaseUser) {
        hideLoading();
        MySharedPreferences.setSharedPreferense(MySharedPreferences.FIREBASE_USER_ID, firebaseUser.getUid(), this);
        if (firebaseUser.getDisplayName() == null) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            intent.putExtra(MainActivity.PHONE_NUMBER, phone);
            startActivity(intent);
        } else {
            this.firebaseUser = firebaseUser;
            controller.callApi(apis.getRoles(Constants.vehicle_owner), true);
        }

    }

    @Override
    public void onOtpError(Exception exception) {
        hideLoading();
        Log.e("Failed to verify otp", Objects.requireNonNull(exception.getMessage()));
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetRolesResponse) {
            GetRolesResponse getRolesResponse = (GetRolesResponse) baseModel;

            SignOnRequest signOnRequest = new SignOnRequest();
            signOnRequest.setRoleId(getRolesResponse.getRoles().get(0).getId());
            signOnRequest.setPhone(phone);
            signOnRequest.setUserName(firebaseUser.getDisplayName());

            controller.callApi(apis.postSignOn(signOnRequest), true);
        } else if (baseModel instanceof LoginResponse) {
            LoginResponse loginResponse = (LoginResponse) baseModel;
            MySharedPreferences.setSharedPreferense(MySharedPreferences.ACCESS_TOKEN, loginResponse.getAccess_token(), this);

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
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
        PmEditText otp_et = findViewById(R.id.et_otp);
        otp_et.getValidations().add(Validator.REQUIRED);
        otp_et.getValidations().add(Validator.MIN_LENGTH, 6);

        PmButton otpBtn = findViewById(R.id.verify_otp_button);
        otpBtn.setDependent(otp_et);
    }

    @Override
    void setOnclickListeners() {
        PmButton otpBtn = findViewById(R.id.verify_otp_button);
        otpBtn.setOnClickListener(view -> {
            showLoading();
            authService.verify(otp, otp_token, this);
        });
    }
}