package controllers.interceptors;

import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.isSuccessful()) {
            Response.Builder newResponse = response.newBuilder();
            String           contentType = response.header("Content-Type");
            if (TextUtils.isEmpty(contentType)) contentType = "application/json";

            JSONObject   json_response          = null;
            ResponseBody response_body          = response.body();
            String       modifiedResponseString = null;

            if (null == response_body) {
                return response;
            }

            try {
                json_response          = new JSONObject(response_body.string());
                modifiedResponseString = json_response.toString();
                Object data = json_response.get("data");

                if (data instanceof JSONObject) {
                    modifiedResponseString = json_response.getJSONObject("data").toString();
                }
            } catch (JSONException e) {
                Log.e("ResponseInterface", Objects.requireNonNull(e.getMessage()));
            } finally {
                newResponse.body(ResponseBody.create(MediaType.parse(Objects.requireNonNull(contentType)), Objects.requireNonNull(modifiedResponseString)));
            }

            return newResponse.build();
        }
        return response;
    }
}
