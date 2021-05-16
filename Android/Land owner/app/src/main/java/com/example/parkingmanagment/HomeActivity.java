package com.example.parkingmanagment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityHomeBinding;

import models.BaseModel;
import models.responseModels.CancelLandResponse;
import utils.PmFragmentManager;

public class HomeActivity extends BaseActivity {

    PmFragmentManager  fragmentManager;
    HomeFragment       homeFragment;
    MyLandsFragment    myLandsFragment;
    MyEarningsFragment myEarningsFragment;
    ProfileFragment    profileFragment;

    ImageView imgHome;
    ImageView imgMyLands;
    ImageView imgMyEarnings;
    ImageView imgProfile;

    TextView tv_home;
    TextView tv_my_lands;
    TextView tv_my_earnings;
    TextView tv_profile;

    CardView btnHome;
    CardView btnMyLand;
    CardView btnMyEarnings;
    CardView btnMyProfile;

    ImageView activatedTabImg;
    TextView  activatedTabText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setActivity(this);
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof CancelLandResponse) {
            setActivated(imgMyLands, tv_my_lands);
            myLandsFragment = MyLandsFragment.newInstance();
            fragmentManager.populateFragment(myLandsFragment, myLandsFragment.getClass().toString());
        }
    }

    @Override
    void init() {
        imgHome       = findViewById(R.id.img_home);
        imgMyLands    = findViewById(R.id.img_my_land);
        imgMyEarnings = findViewById(R.id.img_earning);
        imgProfile    = findViewById(R.id.img_profile);

        tv_home        = findViewById(R.id.txt_home);
        tv_my_lands    = findViewById(R.id.txt_my_land);
        tv_my_earnings = findViewById(R.id.txt_earning);
        tv_profile     = findViewById(R.id.txt_profile);

        btnHome       = findViewById(R.id.btn_home);
        btnMyLand     = findViewById(R.id.btn_my_land);
        btnMyEarnings = findViewById(R.id.btn_earning);
        btnMyProfile  = findViewById(R.id.btn_profile);

        fragmentManager = new PmFragmentManager(this, R.id.homeFrame, null);
        setActivated(imgHome, tv_home);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }
        fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
    }

    @Override
    void setOnclickListeners() {

        btnHome.setOnClickListener(view -> {
            setActivated(imgHome, tv_home);
            if (homeFragment == null) {
                homeFragment = HomeFragment.newInstance();
            }
            fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
        });

        btnMyLand.setOnClickListener(view -> {
            setActivated(imgMyLands, tv_my_lands);
            if (myLandsFragment == null) {
                myLandsFragment = MyLandsFragment.newInstance();
            }
            fragmentManager.populateFragment(myLandsFragment, myLandsFragment.getClass().toString());
        });

        btnMyEarnings.setOnClickListener(view -> {
            setActivated(imgMyEarnings, tv_my_earnings);
            if (myEarningsFragment == null) {
                myEarningsFragment = MyEarningsFragment.newInstance();
            }
            fragmentManager.populateFragment(myEarningsFragment, myEarningsFragment.getClass().toString());
        });

        btnMyProfile.setOnClickListener(view -> {
            setActivated(imgProfile, tv_profile);
            if (profileFragment == null) {
                profileFragment = ProfileFragment.newInstance();
            }
            fragmentManager.populateFragment(profileFragment, profileFragment.getClass().toString());
        });
    }

    ColorStateList defaultTextColor;

    public void setActivated(ImageView imageView, TextView textView) {

        if (activatedTabImg != null) {
            activatedTabImg.clearColorFilter();
        }

        if (activatedTabText != null) {
            activatedTabText.setTextColor(defaultTextColor);
        } else {
            defaultTextColor = textView.getTextColors();
        }

        imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        activatedTabImg  = imageView;
        activatedTabText = textView;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case 1:
                    setActivated(imgMyLands, tv_my_lands);
                    myLandsFragment = MyLandsFragment.newInstance();
                    fragmentManager.populateFragment(myLandsFragment, myLandsFragment.getClass().toString());
                    break;
                case 2:
                    setActivated(imgHome, tv_home);
                    HomeFragment homeFragment = HomeFragment.newInstance();
                    fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
                    break;
            }

        }

    }
}