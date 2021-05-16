package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkingmanagment.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseUser;

import custom_view.PmButton;
import models.BaseModel;
import models.responseModels.ErrorResponse;
import services.AuthService;
import utils.MySharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {

    FragmentProfileBinding binding;
    public String name;
    public String phone;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setFragment(this);

        return binding.getRoot();
    }

    @Override
    void init() {
        AuthService authService = new AuthService(getBaseActivity());
        FirebaseUser user = authService.getSignedInUser();
        name = user.getDisplayName();
        phone = user.getPhoneNumber();
    }

    @Override
    void setOnclickListeners() {
        PmButton btn_logout = findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(view1 -> {
            BaseActivity baseActivity = (BaseActivity) getActivity();
            baseActivity.showLoading();
            AuthService authService = new AuthService(getContext());
            authService.signOut();
            baseActivity.hideLoading();

            MySharedPreferences.clear(baseActivity.getContext());
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });
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