package com.example.parkingmanagment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.example.parkingmanagment.databinding.ActivityAssignLandBinding;
import com.example.parkingmanagment.databinding.ItemAllLandListBinding;

import custom_view.PmRecyclerView;
import interfaces.ModelClickListener;
import models.BaseModel;
import models.entity.Land;
import models.responseModels.GetLandsResponse;
import models.responseModels.LandAssignResponse;

public class AssignLandActivity extends BaseActivity implements ModelClickListener {

    ActivityAssignLandBinding binding;
    Integer                   currentPage = 1;
    Button                    btn_next, btn_previous, btn_search;
    PmRecyclerView rvLands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_land);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assign_land);
    }

    @Override
    void init() {

        btn_next     = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);
        btn_search   = findViewById(R.id.btn_search);
        rvLands      = findViewById(R.id.rv_lands);
        getLands();
    }

    @Override
    void setOnclickListeners() {

        btn_search.setOnClickListener(view -> getLands());
        btn_next.setOnClickListener(view -> {
            currentPage++;
            getLands();
        });
        btn_previous.setOnClickListener(view -> {
            currentPage--;
            getLands();
        });
    }

    @Override
    public void onResponse(BaseModel baseModel) {
        if (baseModel instanceof GetLandsResponse) {
            GetLandsResponse response = (GetLandsResponse) baseModel;

            if (response.getData().size() > 0) {
                rvLands.setData(R.layout.item_all_land_list, response.getData().size(), this, (holder, position) -> {
                    ItemAllLandListBinding binding = (ItemAllLandListBinding) holder.binding;
                    binding.setLand(response.getData().get(position));
                    binding.setModelClickListener(this);
                });
            }

            binding.setPagination(response.getPagination());
            btn_next.setEnabled(response.getPagination().getHasNextPage());
            btn_previous.setEnabled(response.getPagination().getHasPreviousPage());
        } else if (baseModel instanceof LandAssignResponse) {
            LandAssignResponse response = (LandAssignResponse) baseModel;
            if (response.getSuccess()) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        }
    }

    private void getLands() {
        EditText etSearch = findViewById(R.id.et_land_search);

        if (TextUtils.isEmpty(etSearch.getText())) {
            controller.callApi(apis.getLands(currentPage, 5));
        } else {
            controller.callApi(apis.getLands(etSearch.getText().toString(), currentPage, 5));
        }
    }

    @Override
    public void onBtnClick(View view, BaseModel baseModel) {
        if (baseModel instanceof Land) {
            Land land = (Land) baseModel;
            controller.callApi(apis.assignLand(land.getId()));
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}