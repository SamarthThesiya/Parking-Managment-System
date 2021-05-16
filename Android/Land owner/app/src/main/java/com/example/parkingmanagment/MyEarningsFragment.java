package com.example.parkingmanagment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.parkingmanagment.databinding.FragmentMyEarningsBinding;
import com.example.parkingmanagment.databinding.ItemMonthlyEarningBinding;

import custom_view.PmButton;
import custom_view.PmRecyclerView;
import models.BaseModel;
import models.responseModels.ErrorResponse;
import models.responseModels.GetEarningsResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyEarningsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEarningsFragment extends BaseFragment {

    FragmentMyEarningsBinding binding;
    PmRecyclerView            rv_earnings;
    PmButton                  btn_withdrawEarning;

    public MyEarningsFragment() {
    }

    public static MyEarningsFragment newInstance() {
        return new MyEarningsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_earnings, container, false);

        return binding.getRoot();
    }

    @Override
    void init() {
        btn_withdrawEarning = findViewById(R.id.btn_withdraw);
        rv_earnings         = findViewById(R.id.rv_monthly_earnings);
        controller.callApi(apis.getEarnings());
    }

    @Override
    void setOnclickListeners() {
        btn_withdrawEarning.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), WithdrawBalanceActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetEarningsResponse) {
            GetEarningsResponse response = (GetEarningsResponse) baseModel;
            binding.setEarnings(response);

            rv_earnings.setData(R.layout.item_monthly_earning, response.getHistory().size(), getContext(), (holder, position) -> {
                ItemMonthlyEarningBinding binding = (ItemMonthlyEarningBinding) holder.binding;

                binding.setEarning(response.getHistory().get(position));
                binding.setPosition(position);
            });
        }
    }

    @Override
    public void onError(ErrorResponse response) {

    }

    @Override
    public void invalidateBinding() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        controller.callApi(apis.getEarnings());
    }
}