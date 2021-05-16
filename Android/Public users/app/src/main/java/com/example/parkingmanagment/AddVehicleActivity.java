package com.example.parkingmanagment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityAddVehicleBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.Objects;

import custom_view.PmButton;
import custom_view.PmImageView;
import models.BaseModel;
import models.entity.Vehicle;
import models.responseModels.AddVehicleResponse;
import models.responseModels.DocumentResponse;
import models.responseModels.GetDocumentTypesResponse;
import models.responseModels.GetVehicleTypesResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import utils.ViewUtils;

public class AddVehicleActivity extends BaseActivity {

    PmButton    btnAddVehicle;
    PmImageView imgVehicle;
    RadioGroup  rgVehicleType;

    ActivityAddVehicleBinding binding;

    Vehicle vehicle;

    int vehicleImagesDocTypeId = 0;
    int vehicleImageDocumentId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_vehicle);

        vehicle = new Vehicle();
        binding.setVehicle(vehicle);
    }

    @Override
    void init() {
        btnAddVehicle = findViewById(R.id.btn_addVehicle);
        imgVehicle    = findViewById(R.id.img_vehicle);
        rgVehicleType = findViewById(R.id.rg_vehicle_type);

        controller.callApi(apis.getDocumentTypeByName("vehicle_img"));
    }

    @Override
    void setOnclickListeners() {

        imgVehicle.setOnClickListener(view -> {
            populateImage();
        });
        btnAddVehicle.setOnClickListener(view -> {
            switch (rgVehicleType.getCheckedRadioButtonId()) {
                case R.id.rb_2Wheeler:
                    controller.callApi(apis.getVehicleTypes("2_wheeler"));
                    break;
                case R.id.rb_4Wheeler:
                    controller.callApi(apis.getVehicleTypes("4_wheeler"));
                    break;
            }
        });
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetDocumentTypesResponse) {
            GetDocumentTypesResponse response = (GetDocumentTypesResponse) baseModel;
            vehicleImagesDocTypeId = response.getData().get(0).id;
        } else if (baseModel instanceof DocumentResponse) {
            DocumentResponse response = (DocumentResponse) baseModel;
            vehicle.setImageId(response.getId());
        } else if (baseModel instanceof GetVehicleTypesResponse) {
            GetVehicleTypesResponse response = (GetVehicleTypesResponse) baseModel;
            int vehicleTypeId = response.getData().get(0).getId();
            vehicle.setTypeId(vehicleTypeId);

            controller.callApi(apis.addVehicle(vehicle));
        } else if (baseModel instanceof AddVehicleResponse) {
            AddVehicleResponse addVehicleResponse = (AddVehicleResponse) baseModel;
            controller.callApi(apis.getVehicle(addVehicleResponse.getId()));
        } else if (baseModel instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) baseModel;
            Intent data = new Intent();
            data.putExtra("vehicle",vehicle);
            setResult(RESULT_OK,data);
            finish();
        }
    }

    private void populateImage() {
        ImagePicker.Companion.with(this)
                .crop()
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data && null != data.getData()) {
            Uri    fileUri  = data.getData();
            String filePath = data.getStringExtra(ImagePicker.EXTRA_FILE_PATH);
            imgVehicle.setImageURI(fileUri);

            File               file        = new File(Objects.requireNonNull(filePath));
            MultipartBody.Part fileRequest = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(file, MediaType.parse("image/*")));

            this.controller.callApi(apis.addDocument(fileRequest, vehicleImagesDocTypeId), true);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            if (null != data) {
                ViewUtils.toastMessage(data.getStringExtra(ImagePicker.EXTRA_ERROR), this);
            }
        } else {
            ViewUtils.toastMessage("Task Cancelled", this);
        }
    }
}