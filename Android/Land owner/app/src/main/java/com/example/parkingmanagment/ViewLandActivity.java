package com.example.parkingmanagment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.example.parkingmanagment.databinding.ActivityViewLandBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

import custom_view.PmButton;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.CancelLandResponse;
import models.responseModels.GetLandResponse;
import utils.LandStatusActions;
import utils.ViewUtils;
import view_pager.ImageSlider;

public class ViewLandActivity extends BaseActivity {

    TextView                landStatusTv;
    PmButton                actionBtn;
    PmButton                backBtn;
    Land                    land = null;
    ActivityViewLandBinding binding;
    ImageView               locationBtn;

    ImageView vp_indexer_1;
    ImageView vp_indexer_2;
    ImageView vp_indexer_3;
    ImageView vp_indexer_4;

    ImageView current_indexer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_land);
    }

    @Override
    void init() {

        landStatusTv = findViewById(R.id.tv_landStatus);
        actionBtn    = findViewById(R.id.btn_action);
        backBtn      = findViewById(R.id.btn_back);
        locationBtn  = findViewById(R.id.btn_location);

        vp_indexer_1 = findViewById(R.id.img_1st_photo);
        vp_indexer_2 = findViewById(R.id.img_2nd_photo);
        vp_indexer_3 = findViewById(R.id.img_3rd_photo);
        vp_indexer_4 = findViewById(R.id.img_4th_photo);

        current_indexer = vp_indexer_1;


        land = (Land) getIntent().getSerializableExtra(MyLandsFragment.LAND);
        binding.setLand(land);

        ViewPager   viewPager   = findViewById(R.id.vp_landPhotos);
        ImageSlider imageSlider = new ImageSlider(this, getLandPhotoUrls());
        viewPager.setAdapter(imageSlider);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateIndexer(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        reloadActionAndStatus();
    }

    private void reloadActionAndStatus(){
        try {
            JSONObject statusConfig = ViewUtils.loadLandStatus(Objects.requireNonNull(land).landStatus.name, this);
            landStatusTv.setText(statusConfig.getString("status_value"));
            landStatusTv.setTextColor(Color.parseColor(statusConfig.getString("status_color")));
            actionBtn.setText(statusConfig.getString("btn_text"));
            actionBtn.setTextColor(Color.parseColor(statusConfig.getString("btn_text_color")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void updateIndexer(int position) {
        current_indexer.setImageDrawable(ContextCompat.getDrawable(ViewLandActivity.this, R.drawable.white_solid_circle));
        ImageView next_indexer = null;
        switch (position) {
            case 0:
                next_indexer = vp_indexer_1;
                break;
            case 1:
                next_indexer = vp_indexer_2;
                break;
            case 2:
                next_indexer = vp_indexer_3;
                break;
            case 3:
                next_indexer = vp_indexer_4;
                break;
        }
        Objects.requireNonNull(next_indexer).setImageDrawable(ContextCompat.getDrawable(ViewLandActivity.this, R.drawable.grey_solid_circle));
        current_indexer = next_indexer;
    }

    private String[] getLandPhotoUrls() {

        return new String[]{
                ViewUtils.getDocumentUrl(land.wToEImg, this),
                ViewUtils.getDocumentUrl(land.eToWImg, this),
                ViewUtils.getDocumentUrl(land.nToSImg, this),
                ViewUtils.getDocumentUrl(land.sToNImg, this)
        };
    }

    @Override
    void setOnclickListeners() {
        actionBtn.setOnClickListener(view -> {
            LandStatusActions landStatusActions = new LandStatusActions(this, land.id);
            landStatusActions.execute(land.landStatus.name);
        });

        backBtn.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("landId", land.id);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        locationBtn.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:" + land.latitude + "," + land.longitude));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Only if initiating from a Broadcast Receiver
            String mapsPackageName = "com.google.android.apps.maps";
            if (isPackageExisted(this, mapsPackageName)) {
                i.setClassName(mapsPackageName, "com.google.android.maps.MapsActivity");
                i.setPackage(mapsPackageName);
            }
            startActivity(i);
        });
    }

    private static boolean isPackageExisted(Context context, String targetPackage){
        PackageManager pm=context.getPackageManager();
        try {
            PackageInfo info =pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof CancelLandResponse) {
            controller.callApi(apis.getLand(land.id));
        } else if (baseModel instanceof GetLandResponse) {
            land = (Land) baseModel;
            binding.setLand(land);
            binding.invalidateAll();
            reloadActionAndStatus();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            controller.callApi(apis.getLand(land.id));
        }
    }
}