package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.parkingmanagment.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

import controllers.BaseController;

import static utils.MySharedPreferences.ACCESS_TOKEN;

public class ViewUtils {

    public static void toastMessage(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void loadImage(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .timeout(60000)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .placeholder(R.drawable.spinner).into(imageView);
    }

    public static JSONObject loadLandStatus(String statusName, Context context) throws JSONException {
        try {
            JSONObject landStatusButtonMapping = new JSONObject(Objects.requireNonNull(Files.loadJSONFromAsset(context, "land_status_button_mapping")));
            return landStatusButtonMapping.getJSONObject(statusName);
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static JSONObject getLandStatusMappingByActionText(String actionText, Context context) throws JSONException {
        try {
            JSONObject landStatusButtonMapping = new JSONObject(Objects.requireNonNull(Files.loadJSONFromAsset(context, "land_status_button_mapping")));

            Iterator<String> keys = landStatusButtonMapping.keys();
            while (keys.hasNext()) {
                try {
                    JSONObject mapping = landStatusButtonMapping.getJSONObject(keys.next());
                    if (mapping.getString("btn_text").equalsIgnoreCase(actionText)) return mapping;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    public static String getDocumentUrl(int documentId, Context context) {
        return BaseController.BASE_URL + "documents/" + documentId + "/download?_token=" + MySharedPreferences.getSharedPreference(ACCESS_TOKEN, context);
    }

    public static ViewDataBinding getDataBinding(int layout, ViewGroup parent) {
        return DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                layout, parent, false);
    }
}
