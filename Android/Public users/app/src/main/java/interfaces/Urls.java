package interfaces;

import models.entity.Vehicle;
import models.requestModels.BookingRequest;
import models.requestModels.CreateOrUpdateWalletRequest;
import models.requestModels.LoginRequest;
import models.requestModels.SignOnRequest;
import models.responseModels.AddBookingResponse;
import models.responseModels.AddVehicleResponse;
import models.responseModels.CreateOrUpdateWalletResponse;
import models.responseModels.DocumentResponse;
import models.responseModels.GetBookingsResponse;
import models.responseModels.GetDocumentTypesResponse;
import models.responseModels.GetLandsResponse;
import models.responseModels.GetOrCreateWalletResponse;
import models.responseModels.GetRolesResponse;
import models.responseModels.GetUsersResponse;
import models.responseModels.GetVehicleTypesResponse;
import models.responseModels.GetVehiclesResponse;
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

    String vehicleOwnerPrefix = "vehicle-owner/";

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

    @GET("document-types")
    Call<GetDocumentTypesResponse> getDocumentTypeByName(@Query("name") String name);

    @Multipart
    @POST("documents")
    Call<DocumentResponse> addDocument(@Part MultipartBody.Part file, @Part("document_type_id") int documentTypeId);

    @GET(vehicleOwnerPrefix + "lands")
    Call<GetLandsResponse> getLands(@Query("startTime") String startTime, @Query("endTime") String endTime);

    @GET(vehicleOwnerPrefix + "vehicle-types")
    Call<GetVehicleTypesResponse> getVehicleTypes(@Query("display_name") String name);

    @POST(vehicleOwnerPrefix + "vehicle")
    Call<AddVehicleResponse> addVehicle(@Body Vehicle vehicle);

    @GET(vehicleOwnerPrefix + "vehicle")
    Call<GetVehiclesResponse> getVehicles();

    @GET(vehicleOwnerPrefix + "vehicle/{id}")
    Call<Vehicle> getVehicle(@Path("id") int id);

    @POST(vehicleOwnerPrefix + "booking")
    Call<AddBookingResponse> bookLand(@Body BookingRequest bookingRequest);

    @GET(vehicleOwnerPrefix + "booking")
    Call<GetBookingsResponse> getBookings();

    @PATCH(vehicleOwnerPrefix + "wallet")
    Call<CreateOrUpdateWalletResponse> createOrUpdateWallet(@Body CreateOrUpdateWalletRequest request);

    @GET(vehicleOwnerPrefix + "wallet")
    Call<GetOrCreateWalletResponse> getOrCreateWallet();
}
