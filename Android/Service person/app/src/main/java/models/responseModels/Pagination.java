package models.responseModels;

import com.google.gson.annotations.SerializedName;

import models.BaseModel;

public class Pagination implements BaseModel {

    @SerializedName("page_count")
    Integer pageCount;

    @SerializedName("current_page")
    Integer currentPage;

    @SerializedName("has_next_page")
    Boolean hasNextPage;

    @SerializedName("has_prev_page")
    Boolean hasPreviousPage;

    @SerializedName("count")
    Integer count;

    @SerializedName("limit")
    Integer limit;

    public Integer getPageCount() {
        return pageCount;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public int getCount() {
        return count;
    }

    public int getLimit() {
        return limit;
    }
}
