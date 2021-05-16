package com.example.parkingmanagment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import models.BaseModel;
import models.entity.Booking;
import utils.ViewUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends BaseFragment implements ZXingScannerView.ResultHandler {

    public ScanFragment() {
    }

    ZXingScannerView scannerView;
    ToggleButton     cameraToggle;

    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    void init() {
        scannerView  = getView().findViewById(R.id.scanner_view);
        cameraToggle = getView().findViewById(R.id.camera_toggle);

        scannerView.setResultHandler(this);

        cameraToggle.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                scannerView.startCamera();
                scannerView.resumeCameraPreview(this);
            } else {
                scannerView.stopCamera();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scannerView.startCamera();
                    scannerView.resumeCameraPreview(this);
                } else {
                    ViewUtils.toastMessage("Access is required to turn on camera", getContext(), Toast.LENGTH_LONG);
                }
                return;
            }
        }
    }

    @Override
    void setOnclickListeners() {

    }

    @Override
    public void invalidateBinding() {

    }

    @Override
    public void onResponse(BaseModel baseModel) {

        if (baseModel instanceof Booking) {
            scannerView.stopCamera();
            cameraToggle.setChecked(false);
            Booking booking = (Booking) baseModel;
            Intent intent = new Intent(getContext(), ViewBookingDetailsActivity.class);
            intent.putExtra("booking", booking);
            startActivity(intent);
        }
    }

    @Override
    public void handleResult(Result result) {
        controller.callApi(apis.getBooking(result.getText()));
    }
}