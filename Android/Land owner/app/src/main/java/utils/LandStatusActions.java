package utils;

import android.content.Context;
import android.content.Intent;

import com.example.parkingmanagment.AddLandActivity;
import com.example.parkingmanagment.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import controllers.LandOwnerController;
import interfaces.Urls;

public class LandStatusActions {

    Context             context;
    int                 landId;
    LandOwnerController controller;
    public Urls apis;

    public LandStatusActions(Context context, int landId) {
        this.context = context;
        this.landId  = landId;
        if (context instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) context;
            controller = baseActivity.landOwnerController;
            apis       = baseActivity.apis;
        }
    }

    public void execute(String currentStatus) {
        JSONObject statusMapping = null;
        try {
            statusMapping = ViewUtils.loadLandStatus(currentStatus, context);
            this.getClass().getDeclaredMethod(Objects.requireNonNull(statusMapping).getString("action")).invoke(this);
        } catch (JSONException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // Called by execute method
    private void edit_and_resent() {
        Intent intent = new Intent(context, AddLandActivity.class);
        intent.putExtra("land_id", landId);
        ((BaseActivity)context).startActivityForResult(intent, 1);
    }

    // Called by execute method
    private void cancel_request() {
        controller.callApi(apis.cancelLand(landId));
    }

    // Called by execute method
    private void deactivate() {
        controller.callApi(apis.deactivateLand(landId));
    }

    // Called by execute method
    private void activate() {
        controller.callApi(apis.reactivateLand(landId));
    }
}
