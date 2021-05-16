package com.example.parkingmanagment;

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
import models.responseModels.GetMeResponse;
import utils.PmFragmentManager;

public class HomeActivity extends BaseActivity {

    PmFragmentManager fragmentManager;
    ProfileFragment   profileFragment;
    ScanFragment      scanFragment;
    HomeFragment      homeFragment;

    private ImageView imgHome;
    private ImageView imgScan;
    private ImageView imgProfile;

    private TextView tv_home;
    private TextView tv_scan;
    private TextView tv_profile;

    private CardView btnHome;
    private CardView btnScan;
    private CardView btnMyProfile;

    ImageView activatedTabImg;
    TextView  activatedTabText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setActivity(this);

        controller.callApi(apis.getMe());
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetMeResponse) {
            GetMeResponse response = (GetMeResponse) baseModel;
            if (response.getLandServicePerson() != null) {
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
                }
                fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
            }
            else {
                Intent intent = new Intent(this, AssignLandActivity.class);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    void init() {
        imgHome    = findViewById(R.id.img_home);
        imgScan    = findViewById(R.id.img_scan);
        imgProfile = findViewById(R.id.img_profile);

        tv_home    = findViewById(R.id.txt_home);
        tv_scan    = findViewById(R.id.txt_scan);
        tv_profile = findViewById(R.id.txt_profile);

        btnHome      = findViewById(R.id.btn_home);
        btnScan      = findViewById(R.id.btn_scan);
        btnMyProfile = findViewById(R.id.btn_profile);

        fragmentManager = new PmFragmentManager(this, R.id.homeFrame, null);
        setActivated(imgHome, tv_home);
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

        btnScan.setOnClickListener(view -> {
            setActivated(imgScan, tv_scan);
            if (scanFragment == null) {
                scanFragment = ScanFragment.newInstance();
            }
            fragmentManager.populateFragment(scanFragment, scanFragment.getClass().toString());
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (homeFragment == null) {
                homeFragment = HomeFragment.newInstance();
            }
            fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
        }
    }
}