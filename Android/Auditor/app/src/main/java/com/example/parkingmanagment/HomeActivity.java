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
import utils.PmFragmentManager;

public class HomeActivity extends BaseActivity {

    PmFragmentManager fragmentManager;

    ProfileFragment      profileFragment;
    AllRequestsFragment  allRequestsFragment;
    PastRequestsFragment pastRequestsFragment;
    HomeFragment         homeFragment;

    public  ImageView imgHome;
    public  ImageView imgAllRequest;
    private ImageView imgPastRequests;
    private ImageView imgProfile;

    public  TextView tvHome;
    public  TextView tvAllRequest;
    private TextView tvPastRequest;
    private TextView tvProfile;

    private CardView btnHome;
    private CardView btnAllRequests;
    private CardView btnPastRequests;
    private CardView btnMyProfile;

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

    }

    @Override
    void init() {
        imgHome         = findViewById(R.id.img_home);
        imgAllRequest   = findViewById(R.id.img_all_requests);
        imgPastRequests = findViewById(R.id.img_past_request);
        imgProfile      = findViewById(R.id.img_profile);

        tvHome        = findViewById(R.id.txt_home);
        tvAllRequest  = findViewById(R.id.txt_my_land);
        tvPastRequest = findViewById(R.id.txt_earning);
        tvProfile     = findViewById(R.id.txt_profile);

        btnHome         = findViewById(R.id.btn_home);
        btnAllRequests  = findViewById(R.id.btn_all_request);
        btnPastRequests = findViewById(R.id.btn_past_requests);
        btnMyProfile    = findViewById(R.id.btn_profile);

        fragmentManager = new PmFragmentManager(this, R.id.homeFrame, null);
        setActivated(imgHome, tvHome);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }
        fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
    }

    @Override
    void setOnclickListeners() {
        btnHome.setOnClickListener(view -> {
            setActivated(imgHome, tvHome);
            if (homeFragment == null) {
                homeFragment = HomeFragment.newInstance();
            }
            fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
        });

        btnAllRequests.setOnClickListener(view -> {
            setActivated(imgAllRequest, tvAllRequest);
            if (allRequestsFragment == null) {
                allRequestsFragment = AllRequestsFragment.newInstance();
            }
            fragmentManager.populateFragment(allRequestsFragment, allRequestsFragment.getClass().toString());
        });

        btnPastRequests.setOnClickListener(view -> {
            setActivated(imgPastRequests, tvPastRequest);
            if (pastRequestsFragment == null) {
                pastRequestsFragment = PastRequestsFragment.newInstance();
            }
            fragmentManager.populateFragment(pastRequestsFragment, pastRequestsFragment.getClass().toString());
        });

        btnMyProfile.setOnClickListener(view -> {
            setActivated(imgProfile, tvProfile);
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

        if (requestCode == 1) {
            HomeFragment homeFragment = HomeFragment.newInstance();
            fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
        } else if (requestCode == 2) {
            AllRequestsFragment allRequestsFragment = AllRequestsFragment.newInstance();
            fragmentManager.populateFragment(allRequestsFragment, allRequestsFragment.getClass().toString());
        } else if (requestCode == 3) {
            PastRequestsFragment pastRequestsFragment = PastRequestsFragment.newInstance();
            fragmentManager.populateFragment(pastRequestsFragment, pastRequestsFragment.getClass().toString());
        }
    }
}