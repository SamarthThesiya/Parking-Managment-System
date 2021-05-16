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

import interfaces.FragmentStakeCommunicator;
import models.BaseModel;
import models.entity.vo.BookingData;
import models.responseModels.AddBookingResponse;
import utils.PmFragmentManager;

public class HomeActivity extends BaseActivity implements FragmentStakeCommunicator {

    PmFragmentManager               fragmentManager;
    public PmFragmentManager.FragmentStack findFragmentStack;

    public  ImageView imgBookings;
    public  ImageView imgFind;
    private ImageView imgWallet;
    private ImageView imgProfile;

    public  TextView tvBooking;
    public  TextView tvFind;
    private TextView tvWallet;
    private TextView tvProfile;

    private CardView btnBooking;
    private CardView btnFind;
    private CardView btnWallet;
    private CardView btnProfile;

    ImageView activatedTabImg;
    TextView  activatedTabText;

    public BookingData bookingData;

    private HomeFragment        homeFragment;
    private FindParkingFragment findParkingFragment;
    private WalletFragment      walletFragment;
    private ProfileFragment     profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setActivity(this);
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof AddBookingResponse) {

            initFragmentStackForFindFragment();
            BaseFragment homeFragment = HomeFragment.newInstance();
            fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
            setActivated(imgBookings, tvBooking);
        }
    }

    @Override
    void init() {
        imgBookings = findViewById(R.id.img_booking);
        imgFind     = findViewById(R.id.img_find);
        imgWallet   = findViewById(R.id.img_wallet);
        imgProfile  = findViewById(R.id.img_profile);

        tvBooking = findViewById(R.id.tv_booking);
        tvFind    = findViewById(R.id.tv_find);
        tvWallet  = findViewById(R.id.tv_wallet);
        tvProfile = findViewById(R.id.tv_profile);

        btnBooking = findViewById(R.id.btn_booking);
        btnFind    = findViewById(R.id.btn_find);
        btnWallet  = findViewById(R.id.btn_wallet);
        btnProfile = findViewById(R.id.btn_profile);

        fragmentManager = new PmFragmentManager(this, R.id.homeFrame, null);

        initFragmentStackForFindFragment();

        setActivated(imgBookings, tvBooking);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }
        fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
    }

    private void initFragmentStackForFindFragment() {
        findFragmentStack = fragmentManager.getFragmentStack(3, this, false);
    }

    @Override
    void setOnclickListeners() {
        btnBooking.setOnClickListener(view -> {
            setActivated(imgBookings, tvBooking);
            if (homeFragment == null) {
                homeFragment = HomeFragment.newInstance();
            }
            fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
        });

        btnFind.setOnClickListener(view -> {
            setActivated(imgFind, tvFind);

            int currentFragmentIndex = findFragmentStack.getCurrentFragmentIndex();
            if (currentFragmentIndex == -1) {
                findFragmentStack.populateNextFragment();
            } else {
                BaseFragment fragment = findFragmentStack.getFragment(currentFragmentIndex);
                fragmentManager.populateFragment(fragment, fragment.getClass().toString());
            }

        });

        btnWallet.setOnClickListener(view -> {
            setActivated(imgWallet, tvWallet);
            if (walletFragment == null) {
                walletFragment = WalletFragment.newInstance();
            }
            fragmentManager.populateFragment(walletFragment, walletFragment.getClass().toString());
        });

        btnProfile.setOnClickListener(view -> {
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
    public BaseFragment getFragmentFromByIndexNumber(int index) {

        if (bookingData == null) {
            bookingData = new BookingData();
        }

        switch (index) {
            case 0:
                return FindParkingFragment.newInstance(bookingData);
            case 1:
                return ViewLandsInMapFragment.newInstance(bookingData);
            case 2:
                return ReviewBookingFragment.newInstance(bookingData);
        }
        return null;
    }

    @Override
    public void fragmentStackOverflowed() {
        controller.callApi(apis.bookLand(bookingData.getBookingRequest()));
    }

    @Override
    public boolean fragmentStackUnderFlow() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}