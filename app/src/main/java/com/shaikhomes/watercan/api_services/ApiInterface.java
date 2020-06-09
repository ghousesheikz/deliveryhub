package com.shaikhomes.watercan.api_services;


import com.shaikhomes.watercan.pojo.CategoryPojo;
import com.shaikhomes.watercan.pojo.DeliveryhubOffersPojo;
import com.shaikhomes.watercan.pojo.EmployeeDetailsPojo;
import com.shaikhomes.watercan.pojo.ItemPojo;
import com.shaikhomes.watercan.pojo.OrderDelivery;
import com.shaikhomes.watercan.pojo.PostResponsePojo;
import com.shaikhomes.watercan.pojo.UpdateOrderPojo;
import com.shaikhomes.watercan.pojo.UpdateWalletPojo;
import com.shaikhomes.watercan.pojo.UserRegistrationPojo;
import com.shaikhomes.watercan.ui.ordercalculation.ItemQueriesPojo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api/UserRegistration?")
    Call<UserRegistrationPojo> GetUserbyNumber(@Query("isadmin") String isadmin, @Query("number") String number);

    @GET("api/Values?")
    Call<UserRegistrationPojo> GetUserbyuserid(@Query("userid") String userid);


    @POST("api/UserRegistration?")
    Call<PostResponsePojo> PostUserDetails(@Body UserRegistrationPojo.UserData writeoffPost);

    @POST("api/ItemMaster")
    Call<PostResponsePojo> PostItem(@Body ItemPojo.Item item);

    @GET("api/ItemMaster?")
    Call<ItemPojo> GetItemList(@Query("vendorid") String vendorid, @Query("active") String active);

    @GET("api/ItemFilter?")
    Call<ItemPojo> GetItemListByCategory(@Query("categoryid") String categoryid, @Query("active") String active);

    @GET("api/UpdateItem?")
    Call<ItemPojo> GetItemListBySearch(@Query("itemname") String itemname, @Query("active") String active);

    @POST("api/OrderMaster?")
    Call<PostResponsePojo> PostOrder(@Body OrderDelivery.OrderList item);

    @GET("api/OrderMaster?")
    Call<OrderDelivery> GetOrderDetails(@Query("mobilenumber") String mobilenumber, @Query("orderdate") String orderdate);

    @POST("api/UpdateItem?")
    Call<PostResponsePojo> UpdateItemDetails(@Body ItemPojo.Item item);

    @GET("api/UpdateAddress?")
    Call<PostResponsePojo> UpdateAddress(@Query("address") String address, @Query("id") String id);

    @POST("api/Category?")
    Call<PostResponsePojo> PostCategories(@Body CategoryPojo.CategoryDetail item);

    @GET("api/Category?")
    Call<CategoryPojo> GetCategoryDetails(@Query("message") String message);

    @GET("api/UpdateOrder?")
    Call<OrderDelivery> GetVendorOrderDetails(@Query("vendorid") String vendorid, @Query("orderdate") String orderdate);

    @POST("api/UpdateOrder?")
    Call<PostResponsePojo> UpdateOrderDetails(@Body UpdateOrderPojo updateOrderPojo);

    @POST("api/UpdateWallet?")
    Call<PostResponsePojo> UpdateWalletDetails(@Body UpdateWalletPojo.WalletDetail updateOrderPojo);

    @GET("api/UpdateWallet?")
    Call<UpdateWalletPojo> GetWalletDetails(@Query("vendorid") String vendorid);

    @GET("api/EmployeeDetails?")
    Call<ResponseBody> GetEmployeeDetails(@Query("employeeid") String employeeid, @Query("vendorid") String vendorid, @Query("orderdate") String orderdate);

    @GET("api/UpdateVendor?")
    Call<PostResponsePojo> UpdateVendor(@Query("vendorid") String vendorid, @Query("status") String status);

    @GET("api/AppOffers?")
    Call<DeliveryhubOffersPojo> GetAppOffers(@Query("type") String type);

    @POST("api/AppOffers?")
    Call<PostResponsePojo> UpdateOffers(@Body DeliveryhubOffersPojo.OffersList updateOrderPojo);

    @POST("api/ItemQueries?")
    Call<PostResponsePojo> PostItemQuery(@Body ItemQueriesPojo.QueryList postquery);

    @GET("api/ItemQueries?")
    Call<ItemQueriesPojo> getQueries(@Query("vendorid") String vendorid, @Query("userid") String userid, @Query("querydate") String querydate,@Query("flag") String flag);


  /*  @POST("api/LeadsRegistration")
    Call<PostResponsePojo> PostLeads(@Body AddLeadsPojo writeoffPost);

    @POST("api/ServiceRegistration")
    Call<PostResponsePojo> ServiceRegistration(@Body PostRegistrationServicce writeoffPost);

    @POST("api/RentPayments")
    Call<PostResponsePojo> RentPayment(@Body PostRentPayment postRentPayment);

    @GET("api/UserRegistration?")
    Call<ListUserDetails> gettingUsers(@Query("isadmin") String isadmin);

    @POST("api/UserRegistration")
    Call<PostResponsePojo> PostUsers(@Body UserRegistrationPojo userRegistrationPojo);

    @GET("api/ServiceRegistration?")
    Call<ListServiceDetails> getServiceListDetails(@Query("number") String number, @Query("status") String status, @Query("assignstatus") String assignstatus);

    @GET("api/UpdateServiceRegister?")
    Call<PostResponsePojo> updateservicestatus(@Query("number") String number, @Query("regid") String regid);

    @GET("api/LeadsRegistration?")
    Call<LeadsList> getLeadDetails();

    @GET("api/UpdateUserStatus?")
    Call<PostResponsePojo> updateUserStatus(@Query("number") String number, @Query("name") String name, @Query("status") String status);

    @GET("api/UpdateLeadsData?")
    Call<PostResponsePojo> updateLeadsStatus(@Query("id") String id, @Query("status") String status, @Query("leadstatus") String leadstatus, @Query("date") String date);

    @POST("api/ApartmentRegistration")
    Call<PostResponsePojo> PostApartments(@Body ApartmentRegistrationPojo apartmentRegistrationPojo);

    @POST("api/ApartmentNames")
    Call<PostResponsePojo> PostApartmentNames(@Body ApartmentNamePojo apartmentNamePojo);

    @GET("api/ApartmentNames?")
    Call<ListApartmentName> getApartmentList(@Query("message") String message);

    @GET("api/ApartmentRegistration?")
    Call<ListApartments> getApartmentRegistration(@Query("apartmentname") String apartmentname);

    @POST("api/PaymentRegistration")
    Call<PostResponsePojo> PostPayments(@Body PaymentsRegistration paymentsRegistration);

    @GET("api/PaymentRegistration?")
    Call<ListPaymentsRegistration> getPayments(@Query("number") String number);

    @GET("api/UserPayments?")
    Call<UserPayments> getUserPayments(@Query("isadmin") String isadmin, @Query("number") String number);

    @GET("api/UserAttandance?")
    Call<ListAttandance> getAttandanceDetailsbydate(@Query("fromdate") String fromdate, @Query("todate") String todate);

    @GET("api/GetUserAttandanceDetails?")
    Call<ListAttandance> getUserAttandanceDetails(@Query("username") String username);

    @POST("api/UserAttandance")
    Call<PostResponsePojo> PostUserAttandance(@Body AttandancePojo attandancePojo);

    @GET("api/LiveTracking?")
    Call<ListTraceLocation> getTraceLocations(@Query("name") String name);

    @POST("api/LiveTracking")
    Call<PostResponsePojo> PostTraceLocation(@Body TraceLocationPojo traceLocationPojo);

    @GET("api/AppVersion?")
    Call<AppVersionPojo> getAppVersion();

    @POST("api/EmployeeWallet?")
    Call<PostResponsePojo> PostWallet(@Body PostWalletDetails postWalletDetails);

    @GET("api/EmployeeWallet?")
    Call<EmployeeWalletList> getEmployeeWalletDetails(@Query("number") String number);

    @GET("api/UserJob?")
    Call<UserJobPojo> getUserJobList(@Query("fromdate") String fromdate, @Query("todate") String todate);

    @POST("api/UserJob?")
    Call<PostResponsePojo> PostUserJobs(@Body UserJobPojo.UserJob userJob);

    @GET("api/LeadRemainder?")
    Call<LeadAppointmentPojo> getLeadsAppointment(@Query("number") String number, @Query("status") String statius, @Query("date") String date);

    @POST("api/LeadRemainder?")
    Call<PostResponsePojo> PostAppointmentData(@Body LeadAppointmentPojo.Data leaddata);

    @POST("api/ApartmentDetails?")
    Call<PostResponsePojo> PostApartmentDetails(@Body ApartmentDetails.ApartmentList apartmentList);

    @GET("api/ApartmentDetails?")
    Call<ApartmentDetails> getApartmentDetailsList(@Query("message") String message);

    @POST("api/ServiceMoniter")
    Call<PostResponsePojo> ServiceMoniter(@Body PostMoniterServicce postMoniterServicce);

    @GET("api/ServiceMoniter?")
    Call<ServiceMoniterDetails> getServiceMoniterDetailsList(@Query("number") String number, @Query("status") String status, @Query("id") String id);
   *//* @GET("top-headlines?")
    Call<LoginResponse> GetNewsUpdates(@Query("country") String country, @Query("apiKey") String apiKey);

    @GET("GetVehicleSeries/?")
    Call<VehicleListResponse> GetVehicleSeries(@Query("imei") String imei, @Query("subCategoryID") int subCategoryID);

    @GET("GetProductList/?")
    Call<ProductListResponse> GetProductList(@Query("imei") String imei, @Query("custType") int custType, @Query("categoryID") int categoryID, @Query("offset") int offset, @Query("seriesID") int seriesID, @Query("searchText") String searchText);
*/
}
