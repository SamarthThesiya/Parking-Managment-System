package com.example.parkingmanagment;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.parkingmanagment.databinding.FragmentLandLocationInfoBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.schibstedspain.leku.LocationPickerActivity;

import java.util.Objects;

import models.BaseModel;
import models.entity.Land;
import models.responseModels.ErrorResponse;

import static com.schibstedspain.leku.LocationPickerActivityKt.ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandLocationInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandLocationInfoFragment extends BaseFragment implements OnMapReadyCallback {

    FragmentLandLocationInfoBinding binding;
    MapView                         mapView;
    Bundle                          savedInstanceState;
    GoogleMap                       googleMap;
    LinearLayout                    map_layout;

    public Land land;

    public LandLocationInfoFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LandLocationInfoFragment.
     */
    public static LandLocationInfoFragment newInstance() {
        return new LandLocationInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_land_location_info, container, false);
        this.savedInstanceState = savedInstanceState;
        return binding.getRoot();
    }

    @Override
    void init() {
        mapView    = findViewById(R.id.map_view);
        map_layout = findViewById(R.id.map_layout);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(LandLocationInfoFragment.this);

        land = ((AddLandActivity) Objects.requireNonNull(getActivity())).land;
        binding.setLand(land);
    }

    @Override
    void setOnclickListeners() {

    }

    Address fullAddress;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == 1) {
                land.latitude  = data.getDoubleExtra(LATITUDE, 0.0);
                land.longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                fullAddress    = data.getParcelableExtra(ADDRESS);

                if (null != fullAddress) {
                    land.addressLine2 = fullAddress.getFeatureName();
                    land.area         = fullAddress.getSubLocality();
                    land.city         = fullAddress.getLocality();
                    land.state        = fullAddress.getAdminArea();
                    land.zipCode      = fullAddress.getPostalCode();
                }

                LatLng latLng = new LatLng(land.latitude, land.longitude);
                setSmallMap(latLng);

                binding.invalidateAll();
            } else if (requestCode == 2) {

            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng latLng = null;
        if (null != land && null != land.longitude && null != land.latitude) {
            latLng = new LatLng(land.latitude, land.longitude);

            setSmallMap(latLng);
        }

        final LatLng finalLatLng = latLng;
        googleMap.setOnMapClickListener(view1 -> {
            Intent build = (new LocationPickerActivity.Builder())
                    .withLegacyLayout()
                    .withLocation(finalLatLng)
                    .build(getBaseActivity());

            startActivityForResult(build, 1);
        });

        googleMap
                .getUiSettings()
                .setAllGesturesEnabled(false);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
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

    Marker smallMapMarker;
    private void setSmallMap(LatLng latLng) {
        MarkerOptions position = new MarkerOptions().position(latLng);

        CameraPosition camPos = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));

        if (null != smallMapMarker) {
            smallMapMarker.remove();
        }
        smallMapMarker = googleMap.addMarker(position);
    }
}