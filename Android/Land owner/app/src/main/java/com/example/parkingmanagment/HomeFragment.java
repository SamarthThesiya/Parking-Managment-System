package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingmanagment.databinding.FragmentHomeBinding;

import java.util.HashMap;
import java.util.Map;

import adapter.HomeLandListAdaptor;
import adapter.LandListAdaptor;
import interfaces.ModelClickListener;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.ErrorResponse;
import models.responseModels.GetLandStatusesResponse;
import models.responseModels.GetLandsResponse;
import utils.Constants;

import static com.example.parkingmanagment.MyLandsFragment.LAND;

public class HomeFragment extends BaseFragment implements ModelClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView            tv_doNotHaveLandMsg;
    FragmentHomeBinding binding;
    GetLandsResponse    lands;
    RecyclerView        rv_lands;

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

        landOwnerController.callApi(apis.getLandStatusesByName(Constants.approved_and_activated));
    }

    @Override
    void setOnclickListeners() {
        tv_doNotHaveLandMsg.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddLandActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetLandStatusesResponse) {
            GetLandStatusesResponse getLandStatusesResponse = (GetLandStatusesResponse) baseModel;
            int                     status_id               = getLandStatusesResponse.getData().get(0).getId();
            Map<String, Object>     filter                  = new HashMap<>();
            filter.put("status_id", status_id);
            landOwnerController.callApi(apis.getLands(filter));
        } else if (baseModel instanceof GetLandsResponse) {
            lands = (GetLandsResponse) baseModel;
            if (lands.getData().size() > 0) {
                rv_lands.setVisibility(View.VISIBLE);
                tv_doNotHaveLandMsg.setVisibility(View.GONE);
                HomeLandListAdaptor adaptor = new HomeLandListAdaptor(this, getContext());
                adaptor.setOrUpdateList(lands.getData());
                rv_lands.setAdapter(adaptor);
                rv_lands.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                rv_lands.setVisibility(View.GONE);
                tv_doNotHaveLandMsg.setVisibility(View.VISIBLE);
            }
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

        Land land = null;
        if (baseModel instanceof Land) {
            land = (Land) baseModel;
        }

        switch (view.getId()) {
            case R.id.btn_view_details:
                Intent intent = new Intent(getActivity(), ViewLandActivity.class);
                intent.putExtra(LAND, land);
                startActivityForResult(intent, 2);
                break;

        }
    }
}