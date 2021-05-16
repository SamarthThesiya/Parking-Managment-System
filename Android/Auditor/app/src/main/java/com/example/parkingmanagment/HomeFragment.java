package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.FragmentHomeBinding;
import com.example.parkingmanagment.databinding.ItemHomeLandListBinding;

import java.util.HashMap;
import java.util.Map;

import custom_view.PmRecyclerView;
import interfaces.ModelClickListener;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.ErrorResponse;
import models.responseModels.GetLandsResponse;
import utils.JwtUtils;
import utils.MySharedPreferences;
import utils.ViewUtils;


public class HomeFragment extends BaseFragment implements ModelClickListener {

    public static final String LAND  = "LAND";

    TextView            tv_doNotHaveLandMsg;
    FragmentHomeBinding binding;
    PmRecyclerView      rv_lands;
    GetLandsResponse    lands;

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
        tv_doNotHaveLandMsg = getView().findViewById(R.id.tv_doNotHaveLandMsg);
        rv_lands            = findViewById(R.id.rv_lands);

        String userId = JwtUtils.getValueFromJwtToken(MySharedPreferences.getSharedPreference(MySharedPreferences.ACCESS_TOKEN, getActivity()), "id");
        Map<String, Object> filter = new HashMap<>();
        filter.put("auditor_id", Integer.parseInt(userId));
        controller.callApi(apis.getLands(filter));
    }

    @Override
    void setOnclickListeners() {
        tv_doNotHaveLandMsg.setOnClickListener(view1 -> {
            AllRequestsFragment allRequestsFragment = AllRequestsFragment.newInstance();
            HomeActivity homeActivity = (HomeActivity) getActivity();
            assert homeActivity != null;
            homeActivity.fragmentManager.populateFragment(allRequestsFragment, allRequestsFragment.getClass().toString());
            homeActivity.setActivated(homeActivity.imgAllRequest, homeActivity.tvAllRequest);
        });
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetLandsResponse) {
            lands = (GetLandsResponse) baseModel;
            if (lands.getData().size() > 0) {
                rv_lands.setVisibility(View.VISIBLE);
                tv_doNotHaveLandMsg.setVisibility(View.GONE);

                rv_lands.setData(R.layout.item_home_land_list, lands.getData().size(), getActivity(), (holder, position) -> {
                    ItemHomeLandListBinding binding = (ItemHomeLandListBinding) holder.binding;
                    binding.setLand(lands.getData().get(position));
                    binding.setModelClickListener(this);
                });

            } else {
                rv_lands.setVisibility(View.GONE);
                tv_doNotHaveLandMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onError(ErrorResponse response) {
        ViewUtils.toastMessage(response.getError_message(), getContext());
    }

    @Override
    public void invalidateBinding() {
        binding.invalidateAll();
    }

    @Override
    public void onBtnClick(View view, BaseModel baseModel) {

        Land land = null;
        if (baseModel instanceof Land) {
            land = (Land) baseModel;
        }

        switch (view.getId()) {
            case R.id.btn_view_details:
                Intent intent = new Intent(getContext(), ViewLandActivity.class);
                intent.putExtra(LAND, land);
                getActivity().startActivityForResult(intent, 1);
                break;

        }
    }
}