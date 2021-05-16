package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.parkingmanagment.databinding.FragmentAllRequestsBinding;
import com.example.parkingmanagment.databinding.ItemAllLandListBinding;
import com.example.parkingmanagment.databinding.ItemHomeLandListBinding;

import java.util.HashMap;
import java.util.Map;

import custom_view.PmRecyclerView;
import interfaces.ModelClickListener;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.GetLandsResponse;
import models.responseModels.PatchAssignToMeResponse;
import utils.JwtUtils;
import utils.MySharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllRequestsFragment extends BaseFragment implements ModelClickListener {

    public static final String LAND = "LAND";

    TextView                   tv_doNotHaveLandMsg;
    FragmentAllRequestsBinding binding;
    PmRecyclerView             rv_lands;
    GetLandsResponse           lands;

    public AllRequestsFragment() {
    }

    public static AllRequestsFragment newInstance() {
        AllRequestsFragment fragment = new AllRequestsFragment();
        Bundle              args     = new Bundle();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_requests, container, false);
        return binding.getRoot();
    }

    @Override
    void init() {
        tv_doNotHaveLandMsg = getView().findViewById(R.id.tv_doNotHaveLandMsg);
        rv_lands            = findViewById(R.id.rv_lands);
        loadLands();
    }

    private void loadLands() {
        Map<String, Object> filter = new HashMap<>();
        filter.put("auditor_id", "null");
        controller.callApi(apis.getLands(filter));
    }

    @Override
    void setOnclickListeners() {

    }

    @Override
    public void invalidateBinding() {

    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetLandsResponse) {
            lands = (GetLandsResponse) baseModel;
            if (lands.getData().size() > 0) {
                rv_lands.setVisibility(View.VISIBLE);
                tv_doNotHaveLandMsg.setVisibility(View.GONE);

                rv_lands.setData(R.layout.item_all_land_list, lands.getData().size(), getActivity(), (holder, position) -> {
                    ItemAllLandListBinding binding = (ItemAllLandListBinding) holder.binding;
                    binding.setLand(lands.getData().get(position));
                    binding.setModelClickListener(this);
                });

            } else {
                rv_lands.setVisibility(View.GONE);
                tv_doNotHaveLandMsg.setVisibility(View.VISIBLE);
            }
        }
        else if (baseModel instanceof PatchAssignToMeResponse) {
            loadLands();
        }
    }

    @Override
    public void onBtnClick(View view, BaseModel baseModel) {
        Land land = null;
        if (baseModel instanceof Land) {
            land = (Land) baseModel;
        }

        switch (view.getId()) {
            case R.id.btn_assign_to_me:
                assert land != null;
                auditorController.callApi(apis.assignToMe(land.id));
                break;

        }
    }
}