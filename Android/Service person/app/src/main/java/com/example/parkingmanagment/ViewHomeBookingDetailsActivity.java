package com.example.parkingmanagment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityViewHomeBookingDetailsBinding;

import models.BaseModel;
import models.entity.vo.BookingData;
import utils.ViewUtils;

public class ViewHomeBookingDetailsActivity extends BaseActivity {

    ActivityViewHomeBookingDetailsBinding binding;
    BookingData                           bookingData;
    Button                                btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_home_booking_details);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_home_booking_details);

        bookingData = (BookingData) getIntent().getSerializableExtra("booking");
        binding.setBookingData(bookingData);
    }

    @Override
    void init() {
        LinearLayout ll_reason = findViewById(R.id.ll_reason);
        if (TextUtils.isEmpty(bookingData.getRejectionReason())) {
            ll_reason.setVisibility(View.GONE);
        } else {
            ll_reason.setVisibility(View.VISIBLE);
        }

        btnBack = findViewById(R.id.btn_back);

        ImageView imgVehicle = findViewById(R.id.img_vehicle);
        ViewUtils.loadImage(this, imgVehicle, ViewUtils.getDocumentUrl(bookingData.getSelectedVehicle().getImageId(), this));
    }

    @Override
    void setOnclickListeners() {
        btnBack.setOnClickListener(view -> super.onBackPressed());
    }

    @Override
    public void onResponse(BaseModel baseModel) {

    }
}