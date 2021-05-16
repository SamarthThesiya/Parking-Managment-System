package com.example.parkingmanagment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import controllers.BaseController;
import controllers.LandOwnerController;
import interfaces.ResponseInterface;
import interfaces.Urls;
import models.responseModels.ErrorResponse;
import utils.ViewUtils;

public abstract class BaseFragment extends Fragment implements ResponseInterface {

    Urls                apis;
    BaseController      controller;
    LandOwnerController landOwnerController;
    private View progressBar;

    abstract void init();

    abstract void setOnclickListeners();

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        controller          = new BaseController(this);
        landOwnerController = new LandOwnerController(this);
        apis                = controller.getApis();

        init();
        setOnclickListeners();
    }

    public <T extends View> T findViewById(int id) {
        return getView().findViewById(id);
    }

    public void showLoading() {
        if (null == progressBar) {
            createProgressBar();
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        if (null != progressBar) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void createProgressBar() {
        RelativeLayout layout         = new RelativeLayout(getBaseActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getBaseActivity());
        progressBar = layoutInflater.inflate(R.layout.loading_view, null, false);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        layout.setElevation(200);

        FrameLayout frameLayout = findViewById(R.id.main);
        frameLayout.addView(layout, 0, params);
    }

    @Override
    public Context getContext() {
        return getBaseActivity();
    }

    @Override
    public void onError(ErrorResponse response) {
        ViewUtils.toastMessage(response.getError_message(), getBaseActivity());
    }

    abstract public void invalidateBinding();
}
