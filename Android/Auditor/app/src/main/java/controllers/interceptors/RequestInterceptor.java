package controllers.interceptors;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import utils.MySharedPreferences;

import static utils.MySharedPreferences.ACCESS_TOKEN;

public class RequestInterceptor implements Interceptor {

    private Context context;

    public RequestInterceptor(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        String access_token = MySharedPreferences.getSharedPreference(ACCESS_TOKEN, context);
        Log.d("request", chain.request().toString());

        Request.Builder authorisedRequest = chain.request().newBuilder()
                .addHeader("Accept", "application/json");
        if (TextUtils.isEmpty(access_token)) {
            return chain.proceed(chain.request());
        }

        authorisedRequest.addHeader("access-token", access_token);

        return chain.proceed(authorisedRequest.build());
    }
}
