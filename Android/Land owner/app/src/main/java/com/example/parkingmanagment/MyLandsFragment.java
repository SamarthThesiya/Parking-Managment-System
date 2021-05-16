package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingmanagment.databinding.FragmentMyLandsBinding;

import adapter.LandListAdaptor;
import custom_view.PmButton;
import interfaces.ModelClickListener;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.ErrorResponse;
import models.responseModels.GetLandsResponse;
import utils.LandStatusActions;

public class MyLandsFragment extends BaseFragment implements ModelClickListener {

    public static final String LAND = "LAND";

    public  String text = "Hello My Lands Fragment";
    FragmentMyLandsBinding binding;
    GetLandsResponse       lands;

    RecyclerView rv_lands;
    PmButton     btn_addLand;

    public MyLandsFragment() {

    }

    public static MyLandsFragment newInstance() {
        MyLandsFragment fragment = new MyLandsFragment();
        Bundle          args     = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_lands, container, false);

        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    void init() {

        rv_lands    = findViewById(R.id.rv_lands);
        btn_addLand = findViewById(R.id.btn_addLand);

        controller.callApi(apis.getLands());
    }

    @Override
    void setOnclickListeners() {

        btn_addLand.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddLandActivity.class);
            startActivityForResult(intent, 2);
        });
    }

    @Override
    public void onResponse(BaseModel baseModel) {

        if (baseModel instanceof GetLandsResponse) {
            lands = (GetLandsResponse) baseModel;
            LandListAdaptor adaptor = new LandListAdaptor(this, getContext());
            adaptor.setOrUpdateList(lands.getData());
            rv_lands.setAdapter(adaptor);
            rv_lands.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    @Override
    public void onError(ErrorResponse response) {

    }

    @Override
    public void invalidateBinding() {

    }

    @Override
    public void onBtnClick(View view, BaseModel baseModel) {
        Land land = null;
        if (baseModel instanceof Land) {
            land = (Land) baseModel;
        }
        switch (view.getId()) {
            case R.id.btn_action:
                if (land != null) {
                    LandStatusActions landStatusActions = new LandStatusActions(getActivity(), land.id);
                    landStatusActions.execute(land.landStatus.name);
                }
                break;
            case R.id.btn_view_details:
                Intent intent = new Intent(getActivity(), ViewLandActivity.class);
                intent.putExtra(LAND, land);
                startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.onActivityResult(requestCode, resultCode, data);
    }
}