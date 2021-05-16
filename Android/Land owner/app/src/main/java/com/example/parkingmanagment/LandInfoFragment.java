package com.example.parkingmanagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.parkingmanagment.databinding.FragmentLandInfoBinding;

import java.util.Objects;

import custom_view.PmButton;
import custom_view.PmEditText;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.ErrorResponse;
import utils.Validator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandInfoFragment extends BaseFragment {

    public  Land   land;
    private Bundle savedInstanceState;
    FragmentLandInfoBinding binding;

    PmEditText et_title, et_width, et_length, et_description;

    public LandInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LandInfoFragment.
     */
    public static LandInfoFragment newInstance() {
        return new LandInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_land_info, container, false);
        binding.setFragment(this);
        this.savedInstanceState = savedInstanceState;

        return binding.getRoot();
    }

    @Override
    void init() {
        land = ((AddLandActivity) Objects.requireNonNull(getActivity())).land;
        PmButton nextButton = ((AddLandActivity) Objects.requireNonNull(getActivity())).btn_next;

        et_title       = findViewById(R.id.et_title);
        et_width       = findViewById(R.id.et_width);
        et_length      = findViewById(R.id.et_length);
        et_description = findViewById(R.id.et_description);

        et_title.getValidations().add(Validator.REQUIRED);
        et_width.getValidations().add(Validator.REQUIRED);
        et_length.getValidations().add(Validator.REQUIRED);
        et_description.getValidations().add(Validator.REQUIRED);

        nextButton.setDependent(et_title, et_width, et_length, et_description);
    }

    @Override
    void setOnclickListeners() {

    }

    @Override
    public void onResponse(BaseModel baseModel) {

    }

    @Override
    public void onError(ErrorResponse response) {

    }

    @Override
    public void invalidateBinding() {

    }
}