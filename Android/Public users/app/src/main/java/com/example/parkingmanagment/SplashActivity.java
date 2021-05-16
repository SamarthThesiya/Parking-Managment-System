package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


import com.google.firebase.auth.FirebaseUser;

import models.BaseModel;
import services.AuthService;
import utils.MySharedPreferences;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


    @Override
    void init() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            AuthService  authService = new AuthService(SplashActivity.this);
            FirebaseUser user        = authService.getSignedInUser();

            if (MySharedPreferences.getSharedPreference(MySharedPreferences.ACCESS_TOKEN, SplashActivity.this) != null) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                if (user != null) {
                    authService.signOut();
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

    @Override
    void setOnclickListeners() {

    }

    @Override
    public void onResponse(BaseModel baseModel) {

    }
}