package interfaces;

import java.util.Map;

import models.entity.Booking;
import models.entity.User;
import models.requestModels.AuditBookingRequest;
import models.requestModels.LoginRequest;
import models.requestModels.SignOnRequest;
import models.responseModels.AuditBookingResponse;
import models.responseModels.GetBookingsResponse;
import models.responseModels.GetLandsResponse;
import models.responseModels.GetMeResponse;
import models.responseModels.GetRolesResponse;
import models.responseModels.GetUsersResponse;
import models.responseModels.LandAssignResponse;
import models.responseModels.LoginResponse;
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

    String servicePersonPrefix = "service-person/";

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

    @GET(servicePersonPrefix + "booking/{bookingToken}")
    Call<Booking> getBooking(@Path("bookingToken") String bookingToken);

    @PATCH(servicePersonPrefix + "booking/{id}/audit")
    Call<AuditBookingResponse> auditBooking(@Path("id") int id, @Body AuditBookingRequest auditBookingRequest);

    @GET(servicePersonPrefix + "booking")
    Call<GetBookingsResponse> getBookings(@Query("filter") String filter, @Query("page") Integer page, @Query("limit") Integer limit);

    @GET(servicePersonPrefix + "booking")
    Call<GetBookingsResponse> getBookings(@Query("filter") String filter, @Query("page") Integer page, @Query("limit") Integer limit, @Query("vehicle-number") String vehicleNumber);

    @GET(servicePersonPrefix + "me")
    Call<GetMeResponse> getMe();

    @GET(servicePersonPrefix + "lands")
    Call<GetLandsResponse> getLands(@Query("page") Integer page, @Query("limit") Integer limit);

    @GET(servicePersonPrefix + "lands")
    Call<GetLandsResponse> getLands(@Query("title") String title, @Query("page") Integer page, @Query("limit") Integer limit);

    @PATCH(servicePersonPrefix + "land/{id}/assign")
    Call<LandAssignResponse> assignLand(@Path("id") Integer landId);
}
