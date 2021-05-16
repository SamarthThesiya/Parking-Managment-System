package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingmanagment.R;
import com.example.parkingmanagment.databinding.ItemHomeLandListBinding;
import com.example.parkingmanagment.databinding.ItemLandListBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import interfaces.ModelClickListener;
import models.entity.Land;
import utils.ViewUtils;

public class HomeLandListAdaptor extends RecyclerView.Adapter<HomeLandListAdaptor.HomeLandListAdaptorViewHolder>{

    List<Land>         lands = null;
    ModelClickListener modelClickListener;
    Context            context;

    public HomeLandListAdaptor(ModelClickListener modelClickListener, Context context) {
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
    public HomeLandListAdaptor.HomeLandListAdaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeLandListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_home_land_list, parent, false);

        return new HomeLandListAdaptor.HomeLandListAdaptorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeLandListAdaptor.HomeLandListAdaptorViewHolder holder, int position) {

        Land land = lands.get(position);

        JSONObject landStatus;
        try {
            landStatus = ViewUtils.loadLandStatus(land.landStatus.name, context);
            holder.itemLandListBinding.setLand(land);
            holder.itemLandListBinding.setModelClickListener(modelClickListener);
            holder.itemLandListBinding.executePendingBindings();

            holder.tv_status.setTextColor(Color.parseColor(landStatus.getString("status_color")));
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

    public class HomeLandListAdaptorViewHolder extends RecyclerView.ViewHolder {

        public ItemHomeLandListBinding itemLandListBinding;
        public TextView            tv_status;
        public ImageView           img_view_land;

        public HomeLandListAdaptorViewHolder(ItemHomeLandListBinding itemLandListBinding) {
            super(itemLandListBinding.getRoot());
            this.itemLandListBinding = itemLandListBinding;
            tv_status                = itemLandListBinding.getRoot().findViewById(R.id.tv_landStatus);
            img_view_land            = itemLandListBinding.getRoot().findViewById(R.id.btn_view_details);
        }
    }
}
