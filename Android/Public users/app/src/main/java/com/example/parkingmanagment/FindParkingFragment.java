package com.example.parkingmanagment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.FragmentFindParkingBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import custom_view.PmButton;
import custom_view.PmEditText;
import models.BaseModel;
import models.entity.Vehicle;
import models.entity.vo.BookingData;
import models.responseModels.GetVehiclesResponse;
import utils.Validator;
import utils.ViewUtils;

public class FindParkingFragment extends BaseFragment {

    FragmentFindParkingBinding binding;

    PmEditText etStartTime, etEndTime;
    PmButton btnAddVehicle, btnNext;

    CalendarView calendarView;

    BookingData bookingData;

    Spinner spVehicles;

    ArrayAdapter<String> spVehiclesAdaptor;

    List<Vehicle> vehicles;

    int selectedVehiclePosition;

    ArrayList<String> vehicleRegNumbers;

    public FindParkingFragment() {

    }

    public static FindParkingFragment newInstance(BookingData bookingData) {
        FindParkingFragment findParkingFragment = new FindParkingFragment();
        findParkingFragment.bookingData = bookingData;
        return findParkingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_parking, container, false);
        binding.setBookingData(bookingData);
        return binding.getRoot();
    }

    @Override
    void init() {
        spVehicles  = findViewById(R.id.sp_vehicles);
        etStartTime = findViewById(R.id.et_startTime);
        etEndTime   = findViewById(R.id.et_endTime);

        etStartTime.getValidations().add(Validator.REQUIRED);
        etEndTime.getValidations().add(Validator.REQUIRED);

        btnAddVehicle = findViewById(R.id.btn_addVehicle);
        btnNext       = findViewById(R.id.btn_next);

        btnNext.setDependent(etStartTime, etEndTime);

        calendarView = findViewById(R.id.cal_booking);

        if (vehicles == null || vehicles.size() == 0) {
            controller.callApi(apis.getVehicles());
        } else {
            updateSpinner();
            spVehicles.setSelection(selectedVehiclePosition);
        }


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                bookingData.setDateTimestamp(cal.getTimeInMillis());
                bookingData.setDate(sdf.format(cal.getTimeInMillis()));
            }
        });

        if (bookingData.getDateTimestamp() != null) {
            calendarView.setDate(bookingData.getDateTimestamp(), true, true);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().getActualMinimum(Calendar.HOUR_OF_DAY));
            long date = calendar.getTime().getTime();
            calendarView.setDate(date, true, true);
            bookingData.setDateTimestamp(date);
            bookingData.setDate(sdf.format(date));
        }

        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());

        binding.invalidateAll();
    }

    @Override
    void setOnclickListeners() {
        etStartTime.setOnClickListener(view -> setTime((PmEditText) view));
        etEndTime.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etStartTime.getText())) {
                ViewUtils.toastMessage("Set start time first", getContext());
                return;
            }
            setTime((PmEditText) view);
        });

        btnAddVehicle.setOnClickListener(view -> startActivityForResult(new Intent(getContext(), AddVehicleActivity.class), 1));
        btnNext.setOnClickListener(view -> {

            binding.invalidateAll();
            if (spVehicles.getSelectedItem().toString().equals("-")) {
                ViewUtils.toastMessage("Please add vehicle.", getContext());
                return;
            }
            selectedVehiclePosition = spVehicles.getSelectedItemPosition();
            bookingData.setSelectedVehicle(getVehicleFromList());

            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.findFragmentStack.populateNextFragment();
        });
    }

    private Vehicle getVehicleFromList() {
        return vehicles.get(selectedVehiclePosition);
    }

    Integer startHours, startMinute, endHour, endMinute;

    private void setTime(PmEditText pmEditText) {
        Calendar mCurrentTime = Calendar.getInstance();
        int      hour         = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int      minute       = mCurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String am_pm           = " AM";
                int    hoursIn24Format = selectedHour;
                if (selectedHour > 12) {
                    selectedHour -= 12;
                    am_pm = " PM";
                }

                String selectedHourStr = String.valueOf(selectedHour), selectedMinuteStr = String.valueOf(selectedMinute);

                if (selectedHourStr.length() == 1) {
                    selectedHourStr = "0" + selectedHourStr;
                }

                if (selectedMinuteStr.length() == 1) {
                    selectedMinuteStr = "0" + selectedMinuteStr;
                }

                if (pmEditText == etStartTime) {
                    startHours  = hoursIn24Format;
                    startMinute = selectedMinute;
                } else if (pmEditText == etEndTime) {
                    endHour   = hoursIn24Format;
                    endMinute = selectedMinute;
                }

                if (isValidTime()) {
                    pmEditText.setText(selectedHourStr + " : " + selectedMinuteStr + am_pm);
                    pmEditText.setError(null);
                } else {
                    pmEditText.setText(null);
                    ViewUtils.toastMessage("End time is not greater than start time", getContext());
                    if (pmEditText == etEndTime) {
                        endHour   = null;
                        endMinute = null;
                    } else if (pmEditText == etStartTime) {
                        startHours  = null;
                        startMinute = null;
                    }
                }
            }
        }, hour, minute, false);

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private boolean isValidTime() {

        if (startHours == null || startMinute == null || endHour == null || endMinute == null) {
            return true;
        }
        return (startHours < endHour || (startHours.equals(endHour) && startMinute < endMinute));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null) {
            Vehicle selectedVehicle = (Vehicle) data.getSerializableExtra("vehicle");
            if (selectedVehicle != null) {

                bookingData.setSelectedVehicle(selectedVehicle);
                vehicles.add(selectedVehicle);
                updateSpinner();
                spVehicles.setSelection(vehicleRegNumbers.size() - 1);
            }
        }
    }

    @Override
    public void invalidateBinding() {

    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetVehiclesResponse) {
            GetVehiclesResponse response = (GetVehiclesResponse) baseModel;

            vehicles = response.getData();
            updateSpinner();
        }
    }

    private void updateSpinner() {

        vehicleRegNumbers = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            vehicleRegNumbers.add(vehicle.getRegistrationNumber());
        }

        if (vehicleRegNumbers.size() == 0) {
            vehicleRegNumbers.add("-");
        }

        if (getContext() != null) {
            spVehiclesAdaptor = new ArrayAdapter<>(getContext(), R.layout.item_spinner_with_bigger_text, vehicleRegNumbers);
            spVehicles.setAdapter(spVehiclesAdaptor);

            spVehiclesAdaptor.notifyDataSetChanged();
        }
    }
}