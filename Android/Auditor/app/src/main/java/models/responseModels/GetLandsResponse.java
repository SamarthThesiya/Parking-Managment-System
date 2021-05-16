package models.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import models.BaseModel;
import models.entity.Land;

public class GetLandsResponse implements BaseModel {

    @SerializedName("data")
    private List<Land> data;

    public List<Land> getData() {
        return data;
    }
}
