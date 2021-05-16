package models.responseModels;

import com.google.gson.annotations.SerializedName;

public abstract class PostResponse extends ApiOkResponse {

    @SerializedName("id")
    Integer id;

    public Integer getId() {
        return id;
    }
}
