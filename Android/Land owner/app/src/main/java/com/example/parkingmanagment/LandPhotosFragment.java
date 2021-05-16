package com.example.parkingmanagment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.parkingmanagment.databinding.FragmentLandPhotosBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.Objects;

import controllers.BaseController;
import custom_view.PmButton;
import custom_view.PmImageView;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.DocumentResponse;
import models.responseModels.ErrorResponse;
import models.responseModels.GetDocumentTypesResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import utils.MySharedPreferences;
import utils.ViewUtils;

import static utils.MySharedPreferences.ACCESS_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandPhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandPhotosFragment extends BaseFragment {

    public Land land;
    FragmentLandPhotosBinding binding;
    PmImageView               imgSouthToNorth, imgNorthToSouth, imgEastToWest, imgWestToEast, currentImageView;
    PmButton viewImgSouthToNorth, viewImgNorthToSouth, viewImgEastToWest, viewImgWestToEast;
    TextView viewMsgSouthToNorth;

    int landImagesDocTypeId = 0;

    public LandPhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LandPhotosFragment.
     */
    public static LandPhotosFragment newInstance() {
        return new LandPhotosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_land_photos, container, false);
        binding.setFragment(this);

        return binding.getRoot();
    }

    @Override
    void init() {
        imgSouthToNorth = findViewById(R.id.img_s_to_n);
        imgNorthToSouth = findViewById(R.id.img_n_to_s);
        imgEastToWest   = findViewById(R.id.img_e_to_w);
        imgWestToEast   = findViewById(R.id.img_w_to_e);

        viewMsgSouthToNorth = findViewById(R.id.tv_viewMsg);

        imgSouthToNorth.setMsgTextView(viewMsgSouthToNorth);

//        viewImgSouthToNorth = findViewById(R.id.btn_view_img_s_to_n);
//        viewImgNorthToSouth = findViewById(R.id.btn_view_img_n_to_s);
//        viewImgEastToWest   = findViewById(R.id.btn_view_img_e_to_w);
//        viewImgWestToEast   = findViewById(R.id.btn_view_img_w_to_e);

        land = ((AddLandActivity) Objects.requireNonNull(getActivity())).land;

        loadLandImages(imgSouthToNorth, land.sToNImg);
        loadLandImages(imgNorthToSouth, land.nToSImg);
        loadLandImages(imgEastToWest, land.eToWImg);
        loadLandImages(imgWestToEast, land.wToEImg);

        controller.callApi(apis.getDocumentTypeByName("land_imgs"));

    }

    @Override
    void setOnclickListeners() {
        imgSouthToNorth.setOnClickListener(view1 -> {
            populateImage(imgSouthToNorth);
        });

        imgNorthToSouth.setOnClickListener(view1 -> {
            populateImage(imgNorthToSouth);
        });

        imgEastToWest.setOnClickListener(view1 -> {
            populateImage(imgEastToWest);
        });

        imgWestToEast.setOnClickListener(view1 -> {
            populateImage(imgWestToEast);
        });

//        viewImgNorthToSouth.setOnClickListener(view1 -> {
//            openImageInGallery(imgNorthToSouth);
//        });
//
//        viewImgSouthToNorth.setOnClickListener(view1 -> {
//            openImageInGallery(imgSouthToNorth);
//        });
//
//        viewImgEastToWest.setOnClickListener(view1 -> {
//            openImageInGallery(imgEastToWest);
//        });
//
//        viewImgWestToEast.setOnClickListener(view1 -> {
//            openImageInGallery(imgWestToEast);
//        });
    }

    private void openImageInGallery(ImageView imageView) {
        AlertDialog.Builder ImageDialog = new AlertDialog.Builder(getBaseActivity());
        ImageDialog.setTitle("Title");
        ImageView imageView1 = new ImageView(getBaseActivity());
        imageView1.setImageDrawable(imageView.getDrawable());
        ImageDialog.setView(imageView1);

        ImageDialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        ImageDialog.show();
    }

    private void populateImage(PmImageView imageView) {
        currentImageView = imageView;
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
            currentImageView.setImageURI(fileUri);
//            showViewButton(currentImageView);

            File               file        = new File(Objects.requireNonNull(filePath));
            MultipartBody.Part fileRequest = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(file, MediaType.parse("image/*")));

            this.controller.callApi(apis.addDocument(fileRequest, landImagesDocTypeId), true);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            if (null != data) {
                ViewUtils.toastMessage(data.getStringExtra(ImagePicker.EXTRA_ERROR), getBaseActivity());
            }
        } else {
            ViewUtils.toastMessage("Task Cancelled", getBaseActivity());
        }
    }

    private void showViewButton(ImageView imageView) {
        if (imageView == imgSouthToNorth) {
            viewImgSouthToNorth.setVisibility(View.VISIBLE);
        } else if (imageView == imgNorthToSouth) {
            viewImgNorthToSouth.setVisibility(View.VISIBLE);
        } else if (imageView == imgEastToWest) {
            viewImgEastToWest.setVisibility(View.VISIBLE);
        } else if (imageView == imgWestToEast) {
            viewImgWestToEast.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof DocumentResponse) {
            DocumentResponse documentResponse = (DocumentResponse) baseModel;

            switch (currentImageView.getId()) {
                case R.id.img_s_to_n:
                    land.sToNImg = documentResponse.getId();
                    break;
                case R.id.img_n_to_s:
                    land.nToSImg = documentResponse.getId();
                    break;
                case R.id.img_e_to_w:
                    land.eToWImg = documentResponse.getId();
                    break;
                case R.id.img_w_to_e:
                    land.wToEImg = documentResponse.getId();
                    break;
            }
        } else if (baseModel instanceof GetDocumentTypesResponse) {
            GetDocumentTypesResponse response = (GetDocumentTypesResponse) baseModel;
            landImagesDocTypeId = response.getData().get(0).getId();
        }
    }

    @Override
    public void onError(ErrorResponse response) {
        if (response.getAction().equalsIgnoreCase("addDocument")) {
            currentImageView.setImageDrawable(getContext().getDrawable(R.drawable.upload_failed));
        }
    }

    @Override
    public void invalidateBinding() {

    }

    private void loadLandImages(ImageView imageView, Integer documentId) {

        if (documentId == null) return;

        ViewUtils.loadImage(getBaseActivity(), imageView, ViewUtils.getDocumentUrl(documentId, getBaseActivity()));
    }
}