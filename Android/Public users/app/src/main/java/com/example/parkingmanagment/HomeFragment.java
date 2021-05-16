package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.FragmentHomeBinding;
import com.example.parkingmanagment.databinding.ItemBookingListBinding;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import custom_view.PmRecyclerView;
import interfaces.ModelClickListener;
import models.BaseModel;
import models.entity.Booking;
import models.responseModels.ErrorResponse;
import models.responseModels.GetBookingsResponse;
import utils.DateTimeUtil;


public class HomeFragment extends BaseFragment implements ModelClickListener {

    TextView            tv_doNotHaveLandMsg;
    FragmentHomeBinding binding;
    PmRecyclerView      rv_lands;
    GetBookingsResponse bookings;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    void init() {
        tv_doNotHaveLandMsg = getView().findViewById(R.id.tv_doNotHaveBooking);
        rv_lands            = findViewById(R.id.rv_bookings);

        controller.callApi(apis.getBookings());
    }

    @Override
    void setOnclickListeners() {
//        tv_doNotHaveLandMsg.setOnClickListener(view1 -> {
//            HomeActivity homeActivity = (HomeActivity) getActivity();
//            assert homeActivity != null;
//            homeActivity.findFragmentStack.populateNextFragment();
//            homeActivity.setActivated(homeActivity.imgFind, homeActivity.tvFind);
//        });
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetBookingsResponse) {
            bookings = (GetBookingsResponse) baseModel;

            if (bookings.getData().size() > 0) {
                tv_doNotHaveLandMsg.setVisibility(View.GONE);
                rv_lands.setVisibility(View.VISIBLE);
            } else {
                tv_doNotHaveLandMsg.setVisibility(View.VISIBLE);
                rv_lands.setVisibility(View.GONE);
            }

            rv_lands.setData(R.layout.item_booking_list, bookings.getData().size(), getActivity(), (holder, position) -> {
                ItemBookingListBinding binding = (ItemBookingListBinding) holder.binding;

                binding.setModelClickListener(this);
                binding.setBooking(bookings.getData().get(position));
            });
        }
    }

    @Override
    public void onError(ErrorResponse response) {

    }

    @Override
    public void invalidateBinding() {
        binding.invalidateAll();
    }

    @Override
    public void onBtnClick(View view, BaseModel baseModel) {
        if (baseModel instanceof Booking) {
            Booking booking = (Booking) baseModel;
            Intent intent = new Intent(getContext(), ViewBookingDetailsActivity.class);
            intent.putExtra("booking", booking);
            startActivity(intent);
        }
    }
}