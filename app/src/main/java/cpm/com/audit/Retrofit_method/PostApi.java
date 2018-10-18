package cpm.com.audit.Retrofit_method;


import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/*import retrofit.http.Body;
import retrofit.http.POST;*/


/**
 * Created by upendrak on 16-05-2017.
 */

public interface PostApi {

    @POST("LoginDetaillatest")
    Call<ResponseBody> getLogindetail(@Body RequestBody request);
   /* @Multipart
    @POST("Uploadimages")
    Call<ResponseBody> getUploadImage(@Body RequestBody request);*/

    @POST("Uploadimages")
    Call<String> getUploadImage(@Body RequestBody request);

    @POST("DownloadAll")
    Call<ResponseBody> getDownloadAll(@Body RequestBody request);

    @POST("CoverageDetail_latest")
    Call<ResponseBody> getCoverageDetail(@Body RequestBody request);

    @POST("CoverageDetail_latest_client")
    Call<ResponseBody> getCoverageDetailClient(@Body RequestBody request);

    @POST("UploadJCPDetail")
    Call<ResponseBody> getUploadJCPDetail(@Body RequestBody request);

    @POST("UploadJsonDetail")
    Call<ResponseBody> getUploadJsonDetail(@Body RequestBody request);

    @POST("UploadJsonDetail")
    Call<JSONObject> getUploadJsonDetailForFileList(@Body RequestBody request);

    @POST("CoverageStatusDetail")
    Call<ResponseBody> getCoverageStatusDetail(@Body RequestBody request);

    @POST("Upload_StoreGeoTag_IMAGES")
    Call<ResponseBody> getGeoTagImage(@Body RequestBody request);

    @POST("CheckoutDetail")
    Call<ResponseBody> getCheckout(@Body RequestBody request);

    @POST("CheckoutDetail_client")
    Call<ResponseBody> getCheckoutClient(@Body RequestBody request);

    @POST("DeleteCoverage")
    Call<ResponseBody> deleteCoverageData(@Body RequestBody request);

    @POST("CoverageNonworking")
    Call<ResponseBody> setCoverageNonWorkingData(@Body RequestBody request);
}
