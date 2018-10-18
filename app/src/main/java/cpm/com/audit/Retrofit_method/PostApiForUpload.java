package cpm.com.audit.Retrofit_method;

/**
 * Created by deepakp on 10/4/2017.
 */

import com.squareup.okhttp.RequestBody;

import retrofit.http.Body;
import retrofit.http.POST;

public interface PostApiForUpload {

    @POST("Uploadimageswithpath")
    retrofit.Call<String> getUploadImageRetrofitOne(@Body RequestBody body1);

    @POST("Uploadimages")
    retrofit.Call<String> getUploadImages(@Body RequestBody body1);
}
