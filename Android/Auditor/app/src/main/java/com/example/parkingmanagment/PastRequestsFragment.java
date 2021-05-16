package com.example.parkingmanagment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.parkingmanagment.databinding.FragmentPastRequestsBinding;
import com.example.parkingmanagment.databinding.ItemHomeLandListBinding;
import com.example.parkingmanagment.databinding.ItemPastLandListBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import custom_view.PmRecyclerView;
import interfaces.ModelClickListener;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.GetLandsResponse;
import utils.JwtUtils;
import utils.MySharedPreferences;
import utils.ViewUtils;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PastRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastRequestsFragment extends BaseFragment implements ModelClickListener {

    public static final String LAND = "LAND";

    TextView                    tv_doNotHaveLandMsg;
    FragmentPastRequestsBinding binding;
    PmRecyclerView              rv_lands;
    GetLandsResponse            lands;

    public PastRequestsFragment() {
    }

    public static PastRequestsFragment newInstance() {
        PastRequestsFragment fragment = new PastRequestsFragment();
        Bundle               args     = new Bundle();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_past_requests, container, false);
        return binding.getRoot();
    }

    @Override
    void init() {
        tv_doNotHaveLandMsg = getView().findViewById(R.id.tv_doNotHaveLandMsg);
        rv_lands            = findViewById(R.id.rv_lands);

        controller.callApi(apis.getMyAuditedLands());
    }

    @Override
    void setOnclickListeners() {
        tv_doNotHaveLandMsg.setOnClickListener(view1 -> {
            HomeFragment homeFragment = HomeFragment.newInstance();
            HomeActivity homeActivity = (HomeActivity) getActivity();
            assert homeActivity != null;
            homeActivity.fragmentManager.populateFragment(homeFragment, homeFragment.getClass().toString());
            homeActivity.setActivated(homeActivity.imgHome, homeActivity.tvHome);
        });
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

                rv_lands.setData(R.layout.item_past_land_list, lands.getData().size(), getActivity(), (holder, position) -> {
                    ItemPastLandListBinding binding = (ItemPastLandListBinding) holder.binding;
                    Land land = lands.getData().get(position);
                    binding.setLand(land);
                    binding.setModelClickListener(this);

                    TextView landStatusTv = holder.itemView.findViewById(R.id.tv_landStatus);

                    try {
                        JSONObject statusConfig = ViewUtils.loadLandStatus(Objects.requireNonNull(land).landStatus.name, getActivity());
                        landStatusTv.setText(statusConfig.getString("status_value"));
                        landStatusTv.setTextColor(Color.parseColor(statusConfig.getString("status_color")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            } else {
                rv_lands.setVisibility(View.GONE);
                tv_doNotHaveLandMsg.setVisibility(View.VISIBLE);
            }
        }
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
                getActivity().startActivityForResult(intent, 3);
                break;

        }
    }
}