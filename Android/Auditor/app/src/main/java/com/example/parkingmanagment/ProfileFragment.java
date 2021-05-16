package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;

import custom_view.PmButton;
import models.BaseModel;
import services.AuthService;
import utils.MySharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View   view;

    public String name;
    public String phone;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle          args     = new Bundle();
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

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        init();
        setOnclickListeners();
        return view;
    }

    @Override
    void init() {
        AuthService  authService = new AuthService(getBaseActivity());
        FirebaseUser user        = authService.getSignedInUser();
        name  = user.getDisplayName();
        phone = user.getPhoneNumber();

        TextView tv_username = view.findViewById(R.id.tv_username);
        tv_username.setText(name);

        TextView tv_phone = view.findViewById(R.id.tv_phone);
        tv_phone.setText(phone);

    }

    @Override
    void setOnclickListeners() {
        PmButton btn_logout = view.findViewById(R.id.btn_logout);

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
    public void invalidateBinding() {
        
    }

    @Override
    public void onResponse(BaseModel baseModel) {

    }
}