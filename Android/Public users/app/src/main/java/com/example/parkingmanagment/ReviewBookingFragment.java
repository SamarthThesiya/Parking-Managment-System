package com.example.parkingmanagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.FragmentReviewBookingBinding;

import custom_view.PmButton;
import models.BaseModel;
import models.entity.vo.BookingData;
import models.responseModels.GetOrCreateWalletResponse;

public class ReviewBookingFragment extends BaseFragment {

    FragmentReviewBookingBinding binding;
    public ReviewBookingFragment() {
    }

    PmButton btnBack, btnNext;
    TextView tv_rent;

    public BookingData bookingData;

    public static ReviewBookingFragment newInstance(BookingData bookingData) {
        ReviewBookingFragment reviewBookingFragment = new ReviewBookingFragment();
        reviewBookingFragment.bookingData = bookingData;
        return reviewBookingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_booking, container, false);
        binding.setBookingData(bookingData);
        return binding.getRoot();
    }

    @Override
    void init() {
        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);
        tv_rent = findViewById(R.id.tv_rent);

        if (bookingData.getSelectedVehicle().getVehicleType().name.equals("2_wheeler")) {
            tv_rent.setText(bookingData.getSelectedLand().twoWheelerPrice);
        } else if (bookingData.getSelectedVehicle().getVehicleType().name.equals("4_wheeler")) {
            tv_rent.setText(bookingData.getSelectedLand().fourWheelerPrice);
        }

        controller.callApi(apis.getOrCreateWallet());
    }

    @Override
    void setOnclickListeners() {
        btnBack.setOnClickListener(view -> {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.findFragmentStack.popBackStack();
        });

        btnNext.setOnClickListener(view -> {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.findFragmentStack.populateNextFragment();
        });
    }

    @Override
    public void invalidateBinding() {

    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetOrCreateWalletResponse) {
            GetOrCreateWalletResponse response = (GetOrCreateWalletResponse) baseModel;

            TextView tvWalletBalance = findViewById(R.id.tv_wallet_balance);
            tvWalletBalance.setText(String.valueOf(response.getWalletBalance()));
        }
    }
}