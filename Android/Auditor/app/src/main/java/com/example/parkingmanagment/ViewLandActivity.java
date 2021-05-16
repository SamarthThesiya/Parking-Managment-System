package com.example.parkingmanagment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.example.parkingmanagment.databinding.ActivityViewLandBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import custom_view.PmButton;
import models.BaseModel;
import models.entity.Land;
import models.requestModels.PatchAuditLandRequest;
import models.responseModels.GetLandResponse;
import models.responseModels.PatchAuditLandResponse;
import utils.Constants;
import utils.ViewUtils;
import view_pager.ImageSlider;

public class ViewLandActivity extends BaseActivity {

    TextView                landStatusTv;
    PmButton                actionBtn;
    PmButton                backBtn;
    Land                    land = null;
    ActivityViewLandBinding binding;
    ImageView               locationBtn;
    AlertDialog             auditDialog;

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


        land = (Land) getIntent().getSerializableExtra(HomeFragment.LAND);
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

    private void reloadActionAndStatus() {
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
            if (land.landStatus.name.equals(Constants.pending_approval)) {
                showAuditBox(true);
            } else if (
                    land.landStatus.name.equals(Constants.approved_and_activated) ||
                            land.landStatus.name.equals(Constants.approved_and_deactivated) ||
                            land.landStatus.name.equals(Constants.rejected)
            ) {
                showAuditBox(false);
            }
        });

        backBtn.setOnClickListener(view -> {
            onBackPressed();
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("landId", land.id);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void showAuditBox(boolean isForAudit) {
        LayoutInflater factory         = LayoutInflater.from(this);
        View           auditDialogView = factory.inflate(R.layout.dialog_audit_land, null);

        auditDialog = new AlertDialog.Builder(this).create();
        auditDialog.setView(auditDialogView);

        RadioGroup auditResponse = auditDialogView.findViewById(R.id.rg_audit_land);
        EditText et_comment = auditDialogView.findViewById(R.id.et_comment);

        if (!TextUtils.isEmpty(land.auditorComment)) {
            et_comment.setText(land.auditorComment);
        }

        if (land.landStatus.name.equals(Constants.approved_and_activated) ||
                land.landStatus.name.equals(Constants.approved_and_deactivated) ||
                land.landStatus.name.equals(Constants.pending_approval)
        ) {
            ((RadioButton)auditDialogView.findViewById(R.id.rb_approve)).setChecked(true);
            ((RadioButton)auditDialogView.findViewById(R.id.rb_reject)).setChecked(false);
        } else if (land.landStatus.name.equals(Constants.rejected)) {
            ((RadioButton)auditDialogView.findViewById(R.id.rb_approve)).setChecked(false);
            ((RadioButton)auditDialogView.findViewById(R.id.rb_reject)).setChecked(true);
        }

        if (!isForAudit) {
            auditResponse.getChildAt(0).setEnabled(false);
            auditResponse.getChildAt(1).setEnabled(false);
        }

        auditDialogView.findViewById(R.id.btn_close).setOnClickListener(v -> auditDialog.dismiss());

        auditDialogView.findViewById(R.id.btn_response).setOnClickListener(v -> {
            int checkedRadioButtonId = auditResponse.getCheckedRadioButtonId();

            if (checkedRadioButtonId == R.id.rb_reject) {
                if (TextUtils.isEmpty(et_comment.getText())) {
                    et_comment.setError("Enter reason of rejection");
                    return;
                }
            } else {
                et_comment.setError(null);
            }

            PatchAuditLandRequest patchAuditLandRequest = new PatchAuditLandRequest();
            patchAuditLandRequest.setResponse((String) auditDialogView.findViewById(checkedRadioButtonId).getTag());
            if (!TextUtils.isEmpty(et_comment.getText())) patchAuditLandRequest.setComment(et_comment.getText().toString());
            controller.callApi(apis.auditLand(land.id, patchAuditLandRequest));
            auditDialog.dismiss();
        });

        auditDialog.show();
    }

    private static boolean isPackageExisted(Context context, String targetPackage) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof PatchAuditLandResponse) {
            controller.callApi(apis.getLand(land.id));
        } else if (baseModel instanceof GetLandResponse) {
            land = (GetLandResponse) baseModel;
            binding.setLand(land);
            binding.invalidateAll();
            reloadActionAndStatus();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
//            controller.callApi(apis.getLand(land.id));
        }
    }
}