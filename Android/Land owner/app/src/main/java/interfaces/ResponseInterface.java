package interfaces;

import android.content.Context;

import models.BaseModel;
import models.responseModels.ErrorResponse;

public interface ResponseInterface {

    void onResponse(BaseModel baseModel);

    void onError(ErrorResponse response);

    Context getContext();

    void showLoading();

    void hideLoading();
}
