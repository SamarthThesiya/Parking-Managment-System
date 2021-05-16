package interfaces;

import java.util.Map;

import models.requestModels.LoginRequest;
import models.requestModels.PatchAuditLandRequest;
import models.requestModels.SignOnRequest;
import models.responseModels.GetLandResponse;
import models.responseModels.GetLandsResponse;
import models.responseModels.GetRolesResponse;
import models.responseModels.GetUsersResponse;
import models.responseModels.LoginResponse;
import models.responseModels.PatchAssignToMeResponse;
import models.responseModels.PatchAuditLandResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface Urls {

    String auditorPrefix = "auditor/";

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

    @GET(auditorPrefix + "lands")
    Call<GetLandsResponse> getLands();

    @GET(auditorPrefix + "lands/{id}")
    Call<GetLandResponse> getLand(@Path("id") int landId);

    @GET(auditorPrefix + "lands")
    Call<GetLandsResponse> getLands(@QueryMap Map<String, Object> queryParams);

    @PATCH(auditorPrefix + "lands/{id}/assign-to-me")
    Call<PatchAssignToMeResponse> assignToMe(@Path("id") int landId);

    @PATCH(auditorPrefix + "lands/{id}/audit")
    Call<PatchAuditLandResponse> auditLand(@Path("id") int landId, @Body PatchAuditLandRequest patchAuditLand);

    @GET(auditorPrefix + "lands/my-audited")
    Call<GetLandsResponse> getMyAuditedLands();
}
