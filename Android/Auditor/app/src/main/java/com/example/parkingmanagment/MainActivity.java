package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

import interfaces.OtpSentInterface;
import custom_view.PmButton;
import custom_view.PmEditText;
import models.BaseModel;
import models.requestModels.LoginRequest;
import services.AuthService;
import utils.MySharedPreferences;
import utils.Validator;

public class MainActivity extends BaseActivity implements OtpSentInterface {

    public static final String       OTP_TOKEN    = "OTP_TOKEN";
    public static final String       PHONE_NUMBER = "PHONE_NUMBER";
    public              LoginRequest login;
    public              String       phone;
    private             AuthService  authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        login = new LoginRequest();
        binding.setMainActivity(this);

        authService = new AuthService(this);
        FirebaseUser user = authService.getSignedInUser();
        if (null != user) {
            if (MySharedPreferences.getSharedPreference(MySharedPreferences.ACCESS_TOKEN, this) != null) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            } else {
                String phone  = user.getPhoneNumber();
                Intent intent = new Intent(this, RegistrationActivity.class);
                intent.putExtra(MainActivity.PHONE_NUMBER, phone);
                startActivity(intent);
            }
        }

    }

    @Override
    public void otpSentSuccess(String token) {
        hideLoading();
        Intent intent = new Intent(this, OtpVerificationActivity.class);
        intent.putExtra(PHONE_NUMBER, phone);
        intent.putExtra(OTP_TOKEN, token);
        startActivity(intent);
    }

    @Override
    public void otpSentFailure(FirebaseException e) {
        hideLoading();
        Log.e("Failed to send otp", Objects.requireNonNull(e.getMessage()));
    }

    @Override
    public void onResponse(BaseModel baseModel) {

    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    void init() {
        PmEditText et_phone = findViewById(R.id.et_phone);
        et_phone.getValidations().add(Validator.REQUIRED, null, null);
        et_phone.getValidations().add(Validator.MIN_LENGTH, 10, "Invalid format");
//        et_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        PmButton loginButton = findViewById(R.id.login_button);
        loginButton.setDependent(et_phone);
    }

    @Override
    void setOnclickListeners() {
        PmButton loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            showLoading();
            CountryCodePicker countryCodePicker = findViewById(R.id.ccp);
            String            country_code      = countryCodePicker.getSelectedCountryCode();
            phone = "+" + country_code + phone;
            phone = phone.replace("(","").replace(")","").replace("-","").replace(" ", "");
            authService.sendOtp(phone, MainActivity.this);
        });
    }
}
