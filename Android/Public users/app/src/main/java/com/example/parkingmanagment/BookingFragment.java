package com.example.parkingmanagment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkingmanagment.databinding.FragmentHomeBinding;

import models.BaseModel;

public class BookingFragment extends BaseFragment {

    TextView            tv_doNotHaveLandMsg;
    FragmentHomeBinding binding;
    RecyclerView        rv_lands;

    public BookingFragment() {

    }

    public static BookingFragment newInstance() {
        return new BookingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking, container, false);
        return binding.getRoot();
    }

    @Override
    void init() {
        tv_doNotHaveLandMsg = getView().findViewById(R.id.tv_doNotHaveBooking);
        rv_lands            = findViewById(R.id.rv_bookings);
    }

    @Override
    void setOnclickListeners() {
        tv_doNotHaveLandMsg.setOnClickListener(view1 -> {

        });
    }

    @Override
    public void invalidateBinding() {

    }

    @Override
    public void onResponse(BaseModel baseModel) {

    }
}