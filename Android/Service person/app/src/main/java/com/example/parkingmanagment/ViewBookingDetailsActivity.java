package com.example.parkingmanagment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityViewBookingDetailsBinding;

import models.BaseModel;
import models.entity.Booking;
import models.entity.vo.BookingData;
import models.requestModels.AuditBookingRequest;
import models.responseModels.AuditBookingResponse;
import utils.ViewUtils;

public class ViewBookingDetailsActivity extends BaseActivity {

    ActivityViewBookingDetailsBinding binding;
    Button                            btnScanAgain, btnDeny, btnAllow;
    BookingData bookingData;
    ImageView   imgVehicle;
    private AlertDialog auditDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_booking_details);
        Booking booking = (Booking) getIntent().getSerializableExtra("booking");
        bookingData = new BookingData(booking);
        binding.setBookingData(bookingData);
    }

    @Override
    void init() {
        imgVehicle   = findViewById(R.id.img_vehicle);
        btnScanAgain = findViewById(R.id.btn_back);
        btnDeny      = findViewById(R.id.btn_deny);
        btnAllow     = findViewById(R.id.btn_allow);

        TextView tvError = findViewById(R.id.tv_error);

        if (!TextUtils.isEmpty(bookingData.getError())) {
            btnAllow.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
        } else {
            btnAllow.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
        }

        ViewUtils.loadImage(this, imgVehicle, ViewUtils.getDocumentUrl(bookingData.getSelectedVehicle().getImageId(), this));
    }

    @Override
    void setOnclickListeners() {
        btnScanAgain.setOnClickListener(view -> onBackPressed());

        btnDeny.setOnClickListener(view -> {
            showRejectionBox();
        });

        btnAllow.setOnClickListener(view -> {
            AuditBookingRequest auditBookingRequest = new AuditBookingRequest();
            auditBookingRequest.setResult("allow");
            controller.callApi(apis.auditBooking(bookingData.getBookingId(), auditBookingRequest));
        });

    }

    private void showRejectionBox() {
        LayoutInflater factory         = LayoutInflater.from(this);
        View           auditDialogView = factory.inflate(R.layout.dialog_reject_booking, null);

        auditDialog = new AlertDialog.Builder(this).create();
        auditDialog.setView(auditDialogView);

        EditText et_comment = auditDialogView.findViewById(R.id.et_comment);

        if (!TextUtils.isEmpty(bookingData.getRejectionReason())) {
            et_comment.setText(bookingData.getRejectionReason());
        }

        auditDialogView.findViewById(R.id.btn_close).setOnClickListener(v -> auditDialog.dismiss());

        auditDialogView.findViewById(R.id.btn_response).setOnClickListener(v -> {

            if (TextUtils.isEmpty(et_comment.getText())) {
                et_comment.setError("Enter reason of rejection");
                return;
            } else {
                et_comment.setError(null);
            }

            AuditBookingRequest auditBookingRequest = new AuditBookingRequest();
            auditBookingRequest.setResult("deny");
            auditBookingRequest.setDenialReason(et_comment.getText().toString());
            controller.callApi(apis.auditBooking(bookingData.getBookingId(), auditBookingRequest));
            auditDialog.dismiss();
        });

        auditDialog.show();
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof AuditBookingResponse) {
            ViewUtils.toastMessage("Success", getContext());
            finish();
        }
    }
}