package com.example.parkingmanagment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkingmanagment.databinding.FragmentPricingBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import models.BaseModel;
import models.entity.Land;
import models.responseModels.ErrorResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandPricingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandPricingFragment extends BaseFragment {

    FragmentPricingBinding binding;
    public Land land;

    public LandPricingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LandPricingFragment.
     */
    public static LandPricingFragment newInstance() {
        return new LandPricingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pricing, container, false);
        return binding.getRoot();
    }

    @Override
    void init() {
        land = ((AddLandActivity) Objects.requireNonNull(getActivity())).land;
        binding.setLand(land);
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