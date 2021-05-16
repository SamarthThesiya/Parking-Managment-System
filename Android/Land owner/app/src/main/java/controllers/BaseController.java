package controllers;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import controllers.interceptors.RequestInterceptor;
import controllers.interceptors.ResponseInterceptor;
import interfaces.ResponseInterface;
import interfaces.Urls;
import models.BaseModel;
import models.responseModels.ErrorResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Invocation;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseController implements Callback<BaseModel> {

    public static final String  BASE_URL    = "http://192.168.1.5:81/v1/";
    private static final Integer FAILER_CODE = 5000;
    protected static     String  prefix      = "";

    private Urls              apis;
    private ResponseInterface responseInterface;

    public BaseController(ResponseInterface responseInterface) {
        this.responseInterface = responseInterface;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor(responseInterface.getContext()))
                .addInterceptor(new ResponseInterceptor())
                .addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

//        Gson gson = new GsonBuilder()
//                .serializeNulls()
//                .create();

        Retrofit retrofitClient = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL + prefix)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apis = retrofitClient.create(Urls.class);
    }

    public void callApi(Call call, boolean showProgressBar) {
        call.enqueue(this);
        if (showProgressBar) {
            responseInterface.showLoading();
        }
    }

    public void callApi(Call call) {
        call.enqueue(this);
        responseInterface.showLoading();
    }


    public Urls getApis() {
        return apis;
    }

    @Override
    public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
        responseInterface.hideLoading();
        if (response.isSuccessful()) {
            responseInterface.onResponse(response.body());
        } else {
            JSONObject    errorObject;
            ErrorResponse errorResponse = null;
            try {
                errorObject   = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                errorResponse = new ErrorResponse();
                errorResponse.setMessage(errorObject.get("message").toString());
                errorResponse.setCode(errorObject.get("code").toString());
                errorResponse.setStatus_code(Integer.parseInt(errorObject.get("status_code").toString()));
                errorResponse.setAction(call.request().tag(Invocation.class).method().getName());

                if (errorObject.has("fields")) {
                    errorResponse.setFieldValidation(errorObject.getJSONObject("fields"));
                }
                Log.e("Error response", errorResponse.getError_message());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            responseInterface.onError(errorResponse);
        }
    }

    @Override
    public void onFailure(@NotNull Call<BaseModel> call, Throwable t) {
        responseInterface.hideLoading();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Something went wrong");
        errorResponse.setCode(t.getMessage());
        errorResponse.setStatus_code(FAILER_CODE);
        errorResponse.setAction(call.request().tag(Invocation.class).method().getName());
//        errorResponse.setMethodName((Execut)call);

        Log.e("Error response", errorResponse.getError_message());
        responseInterface.onError(errorResponse);
    }
}
