package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.FragmentHomeBinding;
import com.example.parkingmanagment.databinding.ItemBookingListBinding;

import java.util.ArrayList;

import custom_view.PmRecyclerView;
import interfaces.ModelClickListener;
import models.BaseModel;
import models.entity.Booking;
import models.entity.vo.BookingData;
import models.responseModels.ErrorResponse;
import models.responseModels.GetBookingsResponse;
import models.responseModels.Pagination;


public class HomeFragment extends BaseFragment implements ModelClickListener {

    TextView            tv_doNotHaveLandMsg;
    FragmentHomeBinding binding;
    PmRecyclerView      rv_lands;
    Pagination          pagination;

    EditText et_vehicleNumber;

    Button btnNextPage, btnPreviousPage, btnSearch;

    String  currentFilter;
    Integer requestedPageCount = 1;

    private static final String CURRENT_BOOKINGS = "Current Bookings";
    private static final String PAST_BOOKINGS    = "Past Bookings";
    private static final String ALL_BOOKINGS     = "All Bookings";

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
    }

    @Override
    void init() {

        btnPreviousPage = getView().findViewById(R.id.btn_previous);
        btnNextPage     = getView().findViewById(R.id.btn_next);
        btnSearch       = getView().findViewById(R.id.btn_search);

        tv_doNotHaveLandMsg = getView().findViewById(R.id.tv_doNotHaveLandMsg);
        rv_lands            = findViewById(R.id.rv_lands);

        Spinner           spFilter = findViewById(R.id.sp_filter);
        ArrayList<String> filters  = new ArrayList<>();
        filters.add(CURRENT_BOOKINGS);
        filters.add(PAST_BOOKINGS);
        filters.add(ALL_BOOKINGS);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner_with_bigger_text, filters);
        spFilter.setAdapter(arrayAdapter);

        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentFilter = filters.get(i);
                getBookings(currentFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_vehicleNumber = getView().findViewById(R.id.et_vehicle_number);
    }

    @Override
    void setOnclickListeners() {
        tv_doNotHaveLandMsg.setOnClickListener(view1 -> {
        });

        btnPreviousPage.setOnClickListener(view -> {
            requestedPageCount--;
            getBookings(currentFilter);
        });

        btnNextPage.setOnClickListener(view -> {
            requestedPageCount++;
            getBookings(currentFilter);
        });

        btnSearch.setOnClickListener(view -> {
            getBookings(currentFilter);
        });
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetBookingsResponse) {
            GetBookingsResponse response = (GetBookingsResponse) baseModel;
            rv_lands.setData(R.layout.item_booking_list, response.getData().size(), getContext(), (holder, position) -> {
                ItemBookingListBinding binding = (ItemBookingListBinding) holder.binding;
                binding.setBookingData(new BookingData(response.getData().get(position)));
                binding.setModelClickListener(this);
            });
            pagination         = response.getPagination();
            requestedPageCount = pagination.getCurrentPage();
            binding.setPagination(pagination);

            btnNextPage.setEnabled(pagination.getHasNextPage());
            btnPreviousPage.setEnabled(pagination.getHasPreviousPage());
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

        if (baseModel instanceof BookingData) {
            BookingData booking = (BookingData) baseModel;
            Intent intent = new Intent(getContext(), ViewHomeBookingDetailsActivity.class);
            intent.putExtra("booking", booking);
            startActivity(intent);
        }
    }

    private void getBookings(String filter) {

        String reqFilter = "all_bookings";
        switch (filter) {
            case CURRENT_BOOKINGS:
                reqFilter = "current_bookings";
                break;
            case PAST_BOOKINGS:
                reqFilter = "past_bookings";
                break;
            case ALL_BOOKINGS:
                reqFilter = "all_bookings";
                break;

        }

        if (TextUtils.isEmpty(et_vehicleNumber.getText())) {
            controller.callApi(apis.getBookings(reqFilter, requestedPageCount, 3));
        } else {
            controller.callApi(apis.getBookings(reqFilter, requestedPageCount, 3, et_vehicleNumber.getText().toString()));
        }
    }
}