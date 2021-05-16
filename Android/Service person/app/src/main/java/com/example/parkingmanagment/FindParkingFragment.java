package com.example.parkingmanagment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkingmanagment.databinding.ActivityMainBinding;
import com.example.parkingmanagment.databinding.FragmentFindParkingBinding;

import custom_view.PmButton;
import models.BaseModel;
import utils.ViewUtils;

public class FindParkingFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public String text = "Hello find parking";

    FragmentFindParkingBinding binding;

    public FindParkingFragment() {

    }

    public static FindParkingFragment newInstance() {
        FindParkingFragment fragment = new FindParkingFragment();
        Bundle              args     = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_parking, container, false);

        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    void init() {

    }

    @Override
    void setOnclickListeners() {

    }

    @Override
    public void invalidateBinding() {

    }

    @Override
    public void onResponse(BaseModel baseModel) {

    }
}