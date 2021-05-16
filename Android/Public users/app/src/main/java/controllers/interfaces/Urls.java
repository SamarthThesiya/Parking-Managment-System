package controllers.interfaces;

import models.requestModels.LoginRequest;
import models.requestModels.SignOnRequest;
import models.responseModels.GetRolesResponse;
import models.responseModels.GetUsersResponse;
import models.responseModels.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Urls {

    @POST("login")
    Call<LoginResponse> postLogin(@Body LoginRequest login);

    @GET("users")
    Call<GetUsersResponse> getUsers();

    @GET("roles")
    Call<GetRolesResponse> getRoles(@Query("name") String role);

    @GET("roles")
    Call<GetRolesResponse> getRoles();

    @POST("sign-on")
    Call<LoginResponse> postSignOn(@Body SignOnRequest signOnRequest);
}
