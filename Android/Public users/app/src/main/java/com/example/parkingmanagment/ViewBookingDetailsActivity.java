package com.example.parkingmanagment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityViewBookingDetailsBinding;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import custom_view.PmButton;
import models.BaseModel;
import models.entity.Booking;
import models.entity.vo.BookingData;
import models.responseModels.GetOrCreateWalletResponse;
import utils.ViewUtils;

public class ViewBookingDetailsActivity extends BaseActivity {

    Booking booking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityViewBookingDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_view_booking_details);

        booking = (Booking) getIntent().getSerializableExtra("booking");

        assert booking != null;
        BookingData bookingData = new BookingData(booking);
        binding.setBookingData(bookingData);
    }

    @Override
    void init() {

        ImageView imgQrCode = findViewById(R.id.img_qr_code);
        try {
            ViewUtils.setQrCodeInImageView(booking.getBookingToken(), imgQrCode, 200);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        controller.callApi(apis.getOrCreateWallet());
    }

    @Override
    void setOnclickListeners() {
        PmButton backButton = findViewById(R.id.btn_back);

        backButton.setOnClickListener(view -> {
            onBackPressed();
        });
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