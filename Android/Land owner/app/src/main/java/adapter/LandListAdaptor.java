package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingmanagment.R;
import com.example.parkingmanagment.databinding.ItemLandListBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import interfaces.ModelClickListener;
import models.entity.Land;
import utils.Constants;
import utils.ViewUtils;

public class LandListAdaptor extends RecyclerView.Adapter<LandListAdaptor.LandListAdaptorViewHolder> {

    List<Land>         lands = null;
    ModelClickListener modelClickListener;
    Context            context;

    public LandListAdaptor(ModelClickListener modelClickListener, Context context) {
        this.modelClickListener = modelClickListener;
        this.context            = context;
    }

    public void setOrUpdateList(List<Land> itemList) {
        if (lands != null) {
            lands.clear();
            lands.addAll(itemList);
            notifyDataSetChanged();
        } else {
            lands = itemList;
        }
    }

    @NonNull
    @Override
    public LandListAdaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLandListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_land_list, parent, false);

        return new LandListAdaptorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LandListAdaptorViewHolder holder, int position) {

        Land land = lands.get(position);

        if (land.landStatus.name.equalsIgnoreCase(Constants.drafted)) {
            holder.img_view_land.setVisibility(View.GONE);
        } else {
            holder.img_view_land.setVisibility(View.VISIBLE);
        }

        JSONObject landStatus;
        try {
            landStatus = ViewUtils.loadLandStatus(land.landStatus.name, context);
            holder.itemLandListBinding.setLand(land);
            holder.itemLandListBinding.setModelClickListener(modelClickListener);
            holder.itemLandListBinding.executePendingBindings();

            holder.tv_status.setTextColor(Color.parseColor(landStatus.getString("status_color")));
            holder.btn_action.setText(landStatus.getString("btn_text"));
            holder.btn_action.setTextColor(Color.parseColor(landStatus.getString("btn_text_color")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lands.size();
    }

    public class LandListAdaptorViewHolder extends RecyclerView.ViewHolder {

        public ItemLandListBinding itemLandListBinding;
        public TextView            tv_status;
        public Button              btn_action;
        public ImageView           img_view_land;

        public LandListAdaptorViewHolder(ItemLandListBinding itemLandListBinding) {
            super(itemLandListBinding.getRoot());
            this.itemLandListBinding = itemLandListBinding;
            tv_status                = itemLandListBinding.getRoot().findViewById(R.id.tv_landStatus);
            btn_action               = itemLandListBinding.getRoot().findViewById(R.id.btn_action);
            img_view_land            = itemLandListBinding.getRoot().findViewById(R.id.btn_view_details);
        }
    }

}
