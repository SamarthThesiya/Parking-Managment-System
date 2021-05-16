package com.example.parkingmanagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.parkingmanagment.databinding.FragmentWalletBinding;

import custom_view.PmButton;
import custom_view.PmEditText;
import models.BaseModel;
import models.requestModels.CreateOrUpdateWalletRequest;
import models.responseModels.CreateOrUpdateWalletResponse;
import models.responseModels.GetOrCreateWalletResponse;
import utils.Validator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends BaseFragment {

    TextView              tv_walletAmount;
    PmEditText            et_addAmount;
    PmButton              btn_addAmount;
    FragmentWalletBinding binding;

    public WalletFragment() {
    }

    public static WalletFragment newInstance() {
        return new WalletFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false);
        return binding.getRoot();
    }

    @Override
    void init() {
        tv_walletAmount = getView().findViewById(R.id.tv_wallet_balance);
        et_addAmount    = getView().findViewById(R.id.et_add_amount);
        btn_addAmount   = getView().findViewById(R.id.btn_add_amount);

        et_addAmount.getValidations().add(Validator.REQUIRED);
        btn_addAmount.setDependent(et_addAmount);

        controller.callApi(apis.getOrCreateWallet());
    }

    @Override
    void setOnclickListeners() {
        btn_addAmount.setOnClickListener(view -> {
            CreateOrUpdateWalletRequest request = new CreateOrUpdateWalletRequest();
            request.setAddAmount(Float.parseFloat(et_addAmount.getText().toString()));

            et_addAmount.setText(null);

            controller.callApi(apis.createOrUpdateWallet(request));
        });
    }

    @Override
    public void invalidateBinding() {

    }

    private void updateWalletBalance(String amount) {
        tv_walletAmount.setText(amount + " Rs.");
    }

    @Override
    public void onResponse(BaseModel baseModel) {

        if (baseModel instanceof GetOrCreateWalletResponse) {
            GetOrCreateWalletResponse response = (GetOrCreateWalletResponse) baseModel;

            updateWalletBalance(String.valueOf(response.getWalletBalance()));
        } else if (baseModel instanceof CreateOrUpdateWalletResponse) {
            CreateOrUpdateWalletResponse response = (CreateOrUpdateWalletResponse) baseModel;

            updateWalletBalance(String.valueOf(response.getWalletBalance()));
        }
    }
}