package com.example.parkingmanagment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import custom_view.PmButton;
import interfaces.FragmentStakeCommunicator;
import interfaces.OnFragmentChangeListener;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.GetLandResponse;
import models.responseModels.GetLandStatusesResponse;
import models.responseModels.PatchLandResponse;
import models.responseModels.PostLandResponse;
import models.responseModels.RegisterLandResponse;
import utils.PmFragmentManager;

public class AddLandActivity extends BaseActivity implements FragmentStakeCommunicator, OnFragmentChangeListener {

    PmButton btn_back;
    public PmButton btn_next;

    PmFragmentManager               fragmentManager;
    PmFragmentManager.FragmentStack fragmentStack;

    ImageView imgWizardDetails;
    ImageView imgWizardPhotos;
    ImageView imgWizardLocation;
    ImageView imgWizardPricing;

    TextView tvWizardDetails;
    TextView tvWizardPhotos;
    TextView tvWizardLocation;
    TextView tvWizardPricing;

    ImageView imgDetailsToPhotos;
    ImageView imgPhotosToLocation;
    ImageView imgLocationToPricing;

    Land land = null;

    int land_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_land);
    }

    @Override
    void init() {
        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);

        imgWizardDetails  = findViewById(R.id.img_details);
        imgWizardPhotos   = findViewById(R.id.img_photo);
        imgWizardLocation = findViewById(R.id.img_location);
        imgWizardPricing  = findViewById(R.id.img_pricing);

        tvWizardDetails  = findViewById(R.id.tv_details);
        tvWizardPhotos   = findViewById(R.id.tv_photo);
        tvWizardLocation = findViewById(R.id.tv_location);
        tvWizardPricing  = findViewById(R.id.tv_pricing);

        imgDetailsToPhotos   = findViewById(R.id.img_details_to_photos);
        imgPhotosToLocation  = findViewById(R.id.img_photos_to_location);
        imgLocationToPricing = findViewById(R.id.img_location_to_pricing);

        fragmentManager = new PmFragmentManager(this, R.id.add_land_wizard_frame, null);
        fragmentStack   = fragmentManager.getFragmentStack(4, this, false);

        fragmentStack.setOnFragmentChangeListener(this);

        land_id = getIntent().getIntExtra("land_id", 0);

        if (land_id == 0) {
            land = new Land();
            fragmentStack.populateNextFragment();
        } else {
            controller.callApi(apis.getLand(land_id), true);
        }
    }

    @Override
    public void onBackPressed() {
        fragmentStack.popBackStack();
    }

    @Override
    void setOnclickListeners() {
        btn_back.setOnClickListener(view -> {
            fragmentStack.popBackStack();
        });

        btn_next.setOnClickListener(view -> {
            if (land_id == 0) {
                landOwnerController.callApi(apis.getLandStatusesByName(getString(R.string.Land_Status_Drafted)));
            } else {
                fragmentStack.getFragment(fragmentStack.getCurrentFragmentIndex()).invalidateBinding();
                land.landStatus = null;
                landOwnerController.callApi(apis.editLand(land, land_id));
            }
        });
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetLandResponse) {
            land    = (Land) baseModel;
            land_id = land.getId();
            fragmentStack.populateNextFragment();
        } else if (baseModel instanceof PatchLandResponse) {
            land = (Land) baseModel;
            btn_next.removeAllDependent();
            fragmentStack.populateNextFragment();
        } else if (baseModel instanceof PostLandResponse) {
            PostLandResponse postLandResponse = (PostLandResponse) baseModel;
            land_id = postLandResponse.getId();
            btn_next.removeAllDependent();
            fragmentStack.populateNextFragment();
        } else if (baseModel instanceof GetLandStatusesResponse) {
            GetLandStatusesResponse getLandStatusesResponse = (GetLandStatusesResponse) baseModel;
            land.statusId = getLandStatusesResponse.getData().get(0).getId();
            landOwnerController.callApi(apis.addLand(land));
        } else if (baseModel instanceof RegisterLandResponse) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("landId", land_id);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @Override
    public BaseFragment getFragmentFromByIndexNumber(int index) {

        updateLandWizard(index);
        switch (index) {
            case 0:
                return LandInfoFragment.newInstance();
            case 1:
                return LandPhotosFragment.newInstance();
            case 2:
                return LandLocationInfoFragment.newInstance();
            case 3:
                return LandPricingFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public void fragmentStackOverflowed() {

        controller.callApi(apis.registerLand(land_id));
    }

    @Override
    public boolean fragmentStackUnderFlow() {

        if (land_id == 0) {
            return true;
        }
        new AlertDialog.Builder(this)
                .setTitle("Don't wont to add land?")
                .setMessage("Your is saved as draft. Do you want to close \"Add land\" process?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    superOnBackPressed();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        return false;
    }

    ColorStateList defaultTextColor;

    public void updateLandWizard(int fragmentIndex) {

        switch (fragmentIndex) {
            case 0:
                if (defaultTextColor == null) {
                    defaultTextColor = tvWizardDetails.getTextColors();
                }
                setActiveWizard(imgWizardDetails, null, tvWizardDetails);
                resetActiveWizard(imgWizardPhotos, imgDetailsToPhotos, tvWizardPhotos);
                break;
            case 1:
                setActiveWizard(imgWizardPhotos, imgDetailsToPhotos, tvWizardPhotos);
                resetActiveWizard(imgWizardLocation, imgPhotosToLocation, tvWizardLocation);
                break;
            case 2:
                setActiveWizard(imgWizardLocation, imgPhotosToLocation, tvWizardLocation);
                resetActiveWizard(imgWizardPricing, imgLocationToPricing, tvWizardPricing);
                break;
            case 3:
                setActiveWizard(imgWizardPricing, imgLocationToPricing, tvWizardPricing);
                break;

        }

    }

    private void setActiveWizard(ImageView logo, ImageView arrow, TextView tvWizardName) {
        logo.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if (null != arrow) {
            arrow.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
        tvWizardName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    private void resetActiveWizard(ImageView logo, ImageView arrow, TextView tvWizardName) {
        logo.clearColorFilter();
        if (null != arrow) {
            arrow.clearColorFilter();
        }
        tvWizardName.setTextColor(defaultTextColor);
    }

    @Override
    public void fragmentChanged(int fragmentCount) {
        updateLandWizard(fragmentCount);
    }
}