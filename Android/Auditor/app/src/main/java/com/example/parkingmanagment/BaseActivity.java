package com.example.parkingmanagment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import controllers.AuditorController;
import controllers.BaseController;
import interfaces.ResponseInterface;
import interfaces.Urls;
import models.responseModels.ErrorResponse;
import utils.ViewUtils;

abstract public class BaseActivity extends AppCompatActivity implements ResponseInterface {

    Urls              apis;
    BaseController    controller;
    AuditorController auditorController;
    View              progressBar;

    public BaseActivity() {
        controller        = new BaseController(this);
        auditorController = new AuditorController(this);
        apis              = controller.getApis();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        init();
        setOnclickListeners();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void createProgressBar() {
        RelativeLayout layout         = new RelativeLayout(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        progressBar = layoutInflater.inflate(R.layout.loading_view, null, false);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        layout.setElevation(200);

        ConstraintLayout constraintLayout = findViewById(R.id.main);
        constraintLayout.addView(layout, 0, params);
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

    abstract void init();

    abstract void setOnclickListeners();

    @Override
    public void onError(ErrorResponse response) {
        ViewUtils.toastMessage(response.getError_message(), this);
    }

    protected void exit() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void superOnBackPressed() {
        super.onBackPressed();
    }
}
