package interfaces;

import java.util.Map;

import models.entity.Land;
import models.requestModels.LoginRequest;
import models.requestModels.SignOnRequest;
import models.requestModels.WithdrawAmountRequest;
import models.responseModels.CancelLandResponse;
import models.responseModels.DocumentResponse;
import models.responseModels.GetDocumentTypesResponse;
import models.responseModels.GetEarningsResponse;
import models.responseModels.GetLandResponse;
import models.responseModels.GetLandStatusesResponse;
import models.responseModels.PostLandResponse;
import models.responseModels.GetRolesResponse;
import models.responseModels.GetUsersResponse;
import models.responseModels.PatchLandResponse;
import models.responseModels.GetLandsResponse;
import models.responseModels.LoginResponse;
import models.responseModels.RegisterLandResponse;
import models.responseModels.WithdrawAmountResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface Urls {

    String landOwnerPrefix = "land-owner/";

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

    @Multipart
    @POST("documents")
    Call<DocumentResponse> addDocument(@Part MultipartBody.Part file, @Part("document_type_id") int documentTypeId);

    @GET("document-types")
    Call<GetDocumentTypesResponse> getDocumentTypeByName(@Query("name") String name);

    @GET(landOwnerPrefix + "lands/{id}")
    Call<GetLandResponse> getLand(@Path("id") int landId);

    @GET(landOwnerPrefix + "lands")
    Call<GetLandsResponse> getLands();

    @GET(landOwnerPrefix + "lands")
    Call<GetLandsResponse> getLands(@QueryMap(encoded = true) Map<String, Object> queryParams);

    @POST(landOwnerPrefix + "lands")
    Call<PostLandResponse> addLand(@Body Land land);

    @PATCH(landOwnerPrefix + "lands/{id}")
    Call<PatchLandResponse> editLand(@Body Land land, @Path("id") int landId);

    @GET(landOwnerPrefix + "land-statuses")
    Call<GetLandStatusesResponse> getLandStatusesByName(@Query("name") String name);

    @GET(landOwnerPrefix + "land-statuses")
    Call<GetLandStatusesResponse> getLandStatuses();

    @POST(landOwnerPrefix + "lands/{id}/register")
    Call<RegisterLandResponse> registerLand(@Path("id") int landId);

    @POST(landOwnerPrefix + "lands/{id}/cancel")
    Call<CancelLandResponse> cancelLand(@Path("id") int landId);

    @POST(landOwnerPrefix + "lands/{id}/deactivate")
    Call<CancelLandResponse> deactivateLand(@Path("id") int landId);

    @POST(landOwnerPrefix + "lands/{id}/reactivate")
    Call<CancelLandResponse> reactivateLand(@Path("id") int landId);

    @GET(landOwnerPrefix + "earnings")
    Call<GetEarningsResponse> getEarnings();

    @POST(landOwnerPrefix + "earnings/debit")
    Call<WithdrawAmountResponse> withdrawEarning(@Body WithdrawAmountRequest request);
}
