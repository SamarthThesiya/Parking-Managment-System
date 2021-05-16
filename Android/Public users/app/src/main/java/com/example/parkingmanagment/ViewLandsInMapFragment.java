package com.example.parkingmanagment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.FragmentViewLandsInMapBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import custom_view.PmButton;
import models.BaseModel;
import models.entity.Land;
import models.entity.vo.BookingData;
import models.responseModels.GetLandsResponse;
import utils.ViewUtils;

public class ViewLandsInMapFragment extends BaseFragment implements OnMapReadyCallback {

    private MapView   mapView;
    private Bundle    savedInstanceState;
    private GoogleMap googleMap;

    FragmentViewLandsInMapBinding binding;

    Land selectedLand = null;

    PmButton btnBack, btnNext;

    public BookingData bookingData;

    public ViewLandsInMapFragment() {
    }

    public static ViewLandsInMapFragment newInstance(BookingData bookingData) {
        ViewLandsInMapFragment viewLandsInMapFragment = new ViewLandsInMapFragment();
        viewLandsInMapFragment.bookingData = bookingData;
        return viewLandsInMapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        binding                 = DataBindingUtil.inflate(inflater, R.layout.fragment_view_lands_in_map, container, false);
        return binding.getRoot();
    }

    @Override
    void init() {
        mapView = findViewById(R.id.map_view);

        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(ViewLandsInMapFragment.this);
    }

    @Override
    void setOnclickListeners() {
        btnBack.setOnClickListener(view -> {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.findFragmentStack.popBackStack();
        });

        btnNext.setOnClickListener(view -> {

            if (selectedLand == null) {
                ViewUtils.toastMessage("Please select land marker", getContext());
                return;
            }
            bookingData.setLandId(selectedLand.id);
            bookingData.setTotalCharge(getTotalChargeFromLand());
            bookingData.setSelectedLand(selectedLand);

            HomeActivity homeActivity = (HomeActivity) getActivity();

            homeActivity.findFragmentStack.populateNextFragment();
        });
    }

    private Float getTotalChargeFromLand() {
        if (bookingData.getSelectedVehicle().getVehicleType().name.equals("2_wheeler")) {
            return Float.parseFloat(bookingData.getDuration()) * (Float.parseFloat(selectedLand.twoWheelerPrice));
        } else if (bookingData.getSelectedVehicle().getVehicleType().name.equals("4_wheelers")) {
            return Float.parseFloat(bookingData.getDuration()) * (Float.parseFloat(selectedLand.fourWheelerPrice));
        }

        return null;
    }

    @Override
    public void invalidateBinding() {

    }

    HashMap<Marker, Land> landIdMarkerMapping;

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetLandsResponse) {
            GetLandsResponse landsResponse = (GetLandsResponse) baseModel;

            landIdMarkerMapping = new HashMap<>();

            for (Land land : landsResponse.getData()) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(land.latitude, land.longitude));

                Marker marker = googleMap.addMarker(markerOptions);
                landIdMarkerMapping.put(marker, land);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {

            googleMap.setMyLocationEnabled(true);
        }

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View     view             = getLayoutInflater().inflate(R.layout.item_info_window_land_marker, null);
                TextView title            = view.findViewById(R.id.tv_title);
                TextView twoWheelerPrice  = view.findViewById(R.id.tv_2w_price);
                TextView fourWheelerPrice = view.findViewById(R.id.tv_4w_price);

                Land land = landIdMarkerMapping.get(marker);
                title.setText(land.getTitle());
                twoWheelerPrice.setText(land.getTwoWheelerPrice());
                fourWheelerPrice.setText(land.getFourWheelerPrice());
                return view;
            }
        });

        googleMap.setOnMarkerClickListener(marker -> {
            selectedLand = landIdMarkerMapping.get(marker);
            return false;
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        controller.callApi(
                apis.getLands(
                        sdf.format(bookingData.getStartTimeTimeStamp()),
                        sdf.format(bookingData.getEndTimeTimeStamp())
                )
        );
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
}