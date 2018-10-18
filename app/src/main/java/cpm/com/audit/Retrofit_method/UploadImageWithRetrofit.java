package cpm.com.audit.Retrofit_method;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cpm.com.audit.Retrofit_method.upload.ToStringConverterFactory;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.delegates.CoverageBean;
import cpm.com.audit.getterSetter.AuditAnswerMasterGetterSetter;
import cpm.com.audit.getterSetter.AuditDataGetterSetter;
import cpm.com.audit.getterSetter.AuditQuestionMasterGetterSetter;
import cpm.com.audit.getterSetter.GeotaggingBeans;
import cpm.com.audit.getterSetter.JCPGetterSetter;
import cpm.com.audit.getterSetter.JourneyPlan;
import cpm.com.audit.getterSetter.NonWorkingReasonGetterSetter;
import cpm.com.audit.getterSetter.ReferenceVariablesForDownloadActivity;
import cpm.com.audit.getterSetter.TableStructure;
import cpm.com.audit.getterSetter.TableStructureGetterSetter;
import cpm.com.audit.utilities.AlertandMessages;
import cpm.com.audit.utilities.CommonString;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by deepakp on 5/22/2017.
 */

public class UploadImageWithRetrofit extends ReferenceVariablesForDownloadActivity {

    boolean isvalid;
    RequestBody body1;
    private Retrofit adapter;
    Context context;
    public int totalFiles = 0;
    public static int uploadedFiles = 0;
    public int listSize = 0;
    int status = 0;
    RBGTDatabase db;
    ProgressDialog pd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String _UserId, date, app_ver, rightname;
    String[] jj;
    boolean statusUpdated = true;
    int from;

    public UploadImageWithRetrofit(Context context) {
        this.context = context;
    }

    public UploadImageWithRetrofit(Context context, RBGTDatabase db, ProgressDialog pd, int from) {
        this.context = context;
        this.db = db;
        this.pd = pd;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        this.from = from;
        _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        date = preferences.getString(CommonString.KEY_DATE, null);
        rightname = preferences.getString(CommonString.KEY_RIGHTNAME, null);
        try {
            app_ver = String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db.open();
    }

    public String downloadDataUniversal(final String jsonString, int type) {
        try {
            status = 0;
            isvalid = false;
            final String[] data_global = {""};
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();

            adapter = new Retrofit.Builder()
                    .baseUrl(CommonString.URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            PostApi api = adapter.create(PostApi.class);
            Call<ResponseBody> call = null;
            if (type == CommonString.LOGIN_SERVICE) {
                call = api.getLogindetail(jsonData);
            } else if (type == CommonString.DOWNLOAD_ALL_SERVICE) {
                call = api.getDownloadAll(jsonData);
            } else if (type == CommonString.COVERAGE_DETAIL) {
                call = api.getCoverageDetail(jsonData);
            } else if (type == CommonString.COVERAGE_DETAIL_CLIENT) {
                call = api.getCoverageDetailClient(jsonData);
            } else if (type == CommonString.UPLOADJCPDetail) {
                call = api.getUploadJCPDetail(jsonData);
            } else if (type == CommonString.UPLOADJsonDetail) {
                call = api.getUploadJsonDetail(jsonData);
            } else if (type == CommonString.COVERAGEStatusDetail) {
                call = api.getCoverageStatusDetail(jsonData);
            } else if (type == CommonString.CHECKOUTDetail) {
                call = api.getCheckout(jsonData);
            } else if (type == CommonString.CHECKOUTDetail_CLIENT) {
                call = api.getCheckoutClient(jsonData);
            } else if (type == CommonString.DELETE_COVERAGE) {
                call = api.deleteCoverageData(jsonData);
            } else if (type == CommonString.COVERAGE_NONWORKING) {
                call = api.setCoverageNonWorkingData(jsonData);
            }


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            if (data.equalsIgnoreCase("")) {
                                data_global[0] = "";
                                isvalid = true;
                                status = 1;
                            } else {
                                data = data.substring(1, data.length() - 1).replace("\\", "");
                                data_global[0] = data;
                                isvalid = true;
                                status = 1;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            isvalid = true;
                            status = -2;
                        }
                    } else {
                        isvalid = true;
                        status = -1;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isvalid = true;
                    if (t instanceof SocketTimeoutException) {
                        status = 3;
                    } else if (t instanceof IOException) {
                        status = 3;
                    } else {
                        status = 3;
                    }

                }
            });

            while (isvalid == false) {
                synchronized (this) {
                    this.wait(25);
                }
            }
            if (isvalid) {
                synchronized (this) {
                    this.notify();
                }
            }
            if (status == 1) {
                return data_global[0];
            } else if (status == 2) {
                return CommonString.MESSAGE_NO_RESPONSE_SERVER;
            } else if (status == 3) {
                return CommonString.MESSAGE_SOCKETEXCEPTION;
            } else if (status == -2) {
                return CommonString.MESSAGE_INVALID_JSON;
            } else {
                return CommonString.KEY_FAILURE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonString.KEY_FAILURE;
        }
    }

    public File saveBitmapToFileSmaller(File file) {
        File file2 = file;
        try {
            int inWidth = 0;
            int inHeight = 0;

            InputStream in = new FileInputStream(file2);
            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(file2);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / 800, inHeight / 500);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, 800, 500);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);
            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
            // save image
            FileOutputStream out = new FileOutputStream(file2);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

        } catch (Exception e) {
            Log.e("Image", e.toString(), e);
            return file;
        }
        return file2;
    }

    public void UploadImageRecursive(final Context context) {
        try {
            String filename = null, foldername = null;
            int totalfiles = 0;
            File f = new File(CommonString.FILE_PATH);
            File file[] = f.listFiles();
            if (file.length > 0) {
                filename = "";
                totalfiles = f.listFiles().length;
                for (int i = 0; i < file.length; i++) {
                    if (new File(CommonString.FILE_PATH + file[i].getName()).exists()) {
                        if (file[i].getName().contains("_StoreImg-")) {
                            foldername = "CoverageImages";
                        } else if (file[i].getName().contains("_NONWORKING-")) {
                            foldername = "CoverageImages";
                        } else if (file[i].getName().contains("_GeoTag-")) {
                            foldername = "GeoTagImages";
                        } else if (file[i].getName().contains("_AuditAnsPic-")) {
                            foldername = "AuditImages";
                        } else {
                            foldername = "BulkImages";
                        }
                        filename = file[i].getName();
                    }
                    break;
                }


                status = 0;
                File originalFile = new File(CommonString.FILE_PATH + filename);
                final File finalFile = saveBitmapToFileSmaller(originalFile);
                String date;
                if (false) {
                    date = getParsedDate(filename);
                } else {
                    date = this.date;
                }
                isvalid = false;

                OkHttpClient.Builder b = new OkHttpClient.Builder();
                b.connectTimeout(20, TimeUnit.SECONDS);
                b.readTimeout(20, TimeUnit.SECONDS);
                b.writeTimeout(20, TimeUnit.SECONDS);
                OkHttpClient client = b.build();
                pd.setMessage("uploading images (" + uploadedFiles + "/" + totalFiles + ")");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), finalFile);
                //com.squareup.okhttp3.RequestBody requestFile = com.squareup.okhttp.RequestBody.create(com.squareup.okhttp.MediaType.parse("application/octet-stream"), finalFile);
                // MultipartBody.Part body = MultipartBody.Part.createFormData("file", filename, requestFile);
                // MultipartBody.Part body = MultipartBody.Part.createFormData("file", filename, requestFile).createFormData("Foldername", foldername);
                //  RequestBody name = RequestBody.create(MediaType.parse("application/octet-stream"), "upload_test");
                // add another part within the multipart request
                body1 = new MultipartBody.Builder()
                        .setType(MediaType.parse("multipart/form-data"))
                        .addFormDataPart("file", finalFile.getName(), requestFile)
                        .addFormDataPart("Foldername", foldername)
                        .addFormDataPart("Path", date)
                        .build();

                /*body1 = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("file", finalFile.getName(), requestFile)
                        .addFormDataPart("FolderName", foldername)
                        .build();*/
                adapter = new Retrofit.Builder()
                        .baseUrl(CommonString.URL3)
                        .addConverterFactory(new ToStringConverterFactory())
                        .client(client)
                        .build();

                PostApi api = adapter.create(PostApi.class);
                Call<String> observable = api.getUploadImage(body1);
                observable.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body().contains("Success")) {
                            finalFile.delete();
                            uploadedFiles++;
                            status = 1;
                        } else {
                            status = 0;
                        }
                        if (status == 0) {
                            pd.dismiss();
                            if (!((Activity) context).isFinishing()) {
                                AlertandMessages.showAlert((Activity) context, "Image not uploaded." + "\n" + uploadedFiles + " images uploaded out of " + totalFiles, true);
                            }
                        } else {
                            UploadImageRecursive(context);
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                            status = -1;
                            //uploadedFiles = 0;
                            pd.dismiss();
                            // AlertandMessages.showAlert((Activity) context, "Network Error in upload", false);
                            if (!((Activity) context).isFinishing()) {
                                AlertandMessages.showAlert((Activity) context, "Network Error in upload." + "\n" + uploadedFiles + " images uploaded out of " + totalFiles, true);
                            } else {

                            }
                        }

                    }
                });

            } else {
                if (totalFiles == uploadedFiles) {
                    //region Coverage upload status Data
                    new StatusUpload(this.date).execute();
                    //endregion
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getParsedDate(String filename) {
        String testfilename = filename;
        testfilename = testfilename.substring(testfilename.indexOf("-") + 1);
        testfilename = testfilename.substring(0, testfilename.indexOf("-"));
        return testfilename;
    }

    public void uploadDataWithoutWait(final ArrayList<String> keyList, final int keyIndex, final ArrayList<CoverageBean> coverageList, final int coverageIndex) {
        try {
            status = 0;
            isvalid = false;
            final String[] data_global = {""};
            String jsonString = "";
            int type = 0;
            JSONObject jsonObject;

            //region Creating json data
            switch (keyList.get(keyIndex)) {
                case "CoverageDetail_latest":
                    //region Coverage Data
                    db.open();
                    jsonObject = new JSONObject();
                    jsonObject.put("StoreId", coverageList.get(coverageIndex).getStoreId());
                    jsonObject.put("Campaign_Id", coverageList.get(coverageIndex).getCampaignId());
                    jsonObject.put("VisitDate", coverageList.get(coverageIndex).getVisitDate());
                    jsonObject.put("Latitude", coverageList.get(coverageIndex).getLatitude());
                    jsonObject.put("Longitude", coverageList.get(coverageIndex).getLongitude());
                    jsonObject.put("ReasonId", coverageList.get(coverageIndex).getReasonid());
                    jsonObject.put("SubReasonId", "0");
                    jsonObject.put("Remark", "");
                    jsonObject.put("ImageName", coverageList.get(coverageIndex).getImage());
                    jsonObject.put("AppVersion", app_ver);
                    jsonObject.put("UploadStatus", CommonString.KEY_P);
                    jsonObject.put("Checkout_Image", coverageList.get(coverageIndex).getCkeckout_image());
                    jsonObject.put("UserId", coverageList.get(coverageIndex).getUserId());

                    jsonString = jsonObject.toString();
                    type = CommonString.COVERAGE_DETAIL;
                    //endregion
                    break;

                case "Audit_data":
                    //region Category_DBSR_data
                    db.open();
                    ArrayList<AuditDataGetterSetter> auditDataList = db.getAfterSaveAuditQuestionAnswerData(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getCampaignId());
                    if (auditDataList.size() > 0) {
                        JSONArray compArray = new JSONArray();
                        for (int j = 0; j < auditDataList.size(); j++) {

                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", coverageList.get(coverageIndex).getUserId());
                            obj.put("Campaign_Id", coverageList.get(coverageIndex).getCampaignId());
                            obj.put("Store_Id", coverageList.get(coverageIndex).getStoreId());
                            obj.put("Question_Id", auditDataList.get(j).getQUESTION_ID());
                            obj.put("Answer", auditDataList.get(j).getANSWER());
                            obj.put("Answer_Id", auditDataList.get(j).getANSWER_ID());
                            obj.put("Answer_Image", auditDataList.get(j).getCAM_IMAGE());
                            compArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "Audit_data");
                        jsonObject.put("JsonData", compArray.toString());
                        jsonObject.put("UserId", coverageList.get(coverageIndex).getUserId());

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "GeoTag":
                    //region GeoTag
                    ArrayList<GeotaggingBeans> geotaglist = db.getinsertGeotaggingData(coverageList.get(coverageIndex).getStoreId(), "N");
                    if (geotaglist.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < geotaglist.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put(CommonString.KEY_STORE_ID, geotaglist.get(j).getStoreid());
                            obj.put(CommonString.KEY_VISIT_DATE, coverageList.get(coverageIndex).getVisitDate());
                            obj.put(CommonString.KEY_LATITUDE, geotaglist.get(j).getLatitude());
                            obj.put(CommonString.KEY_LONGITUDE, geotaglist.get(j).getLongitude());
                            obj.put("FRONT_IMAGE", geotaglist.get(j).getImage());
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "GeoTag");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", coverageList.get(coverageIndex).getUserId());

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;


            }
            //endregion

            final int[] finalJsonIndex = {keyIndex};
            final String finalKeyName = keyList.get(keyIndex);

            if (jsonString != null && !jsonString.equalsIgnoreCase("")) {

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .build();

                pd.setMessage("Uploading (" + keyIndex + "/" + keyList.size() + ") \n" + keyList.get(keyIndex) + "\n Store uploading " + (coverageIndex + 1) + "/" + coverageList.size());
                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL2).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
                PostApi api = adapter.create(PostApi.class);
                Call<ResponseBody> call = null;

                if (type == CommonString.COVERAGE_DETAIL) {
                    call = api.getCoverageDetail(jsonData);
                } else if (type == CommonString.UPLOADJsonDetail) {
                    call = api.getUploadJsonDetail(jsonData);
                }


                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                if (data.equalsIgnoreCase("")) {
                                    data_global[0] = "";
                                    AlertandMessages.showAlert((Activity) context, "Invalid Data : problem occured at " + keyList.get(keyIndex), true);
                                } else {
                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                    data_global[0] = data;

                                    if (finalKeyName.equalsIgnoreCase("CoverageDetail_latest")) {
                                        try {
                                            coverageList.get(coverageIndex).setMID(Integer.parseInt(data_global[0]));
                                        } catch (NumberFormatException ex) {
                                            AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);
                                        }
                                    } else if (data_global[0].contains(CommonString.KEY_SUCCESS)) {

                                        if (finalKeyName.equalsIgnoreCase("GeoTag")) {
                                            db.open();
                                            db.updateInsertedGeoTagStatus(coverageList.get(coverageIndex).getStoreId(), CommonString.KEY_Y);
                                            db.updateStatus(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getCampaignId(), CommonString.KEY_Y);
                                        }
                                    } else {
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName + " : " + data_global[0], true);
                                    }


                                    finalJsonIndex[0]++;
                                    if (finalJsonIndex[0] != keyList.size()) {
                                        uploadDataWithoutWait(keyList, finalJsonIndex[0], coverageList, coverageIndex);
                                    } else {
                                        pd.setMessage("updating status :" + coverageIndex);
                                        //uploading status D for current store from coverageList
                                        updateStatus(coverageList, coverageIndex, CommonString.KEY_D);
                                    }
                                }

                            } catch (Exception e) {
                                pd.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);
                            }
                        } else {
                            pd.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        isvalid = true;
                        pd.dismiss();
                        if (t instanceof SocketTimeoutException) {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        } else if (t instanceof IOException) {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        } else if (t instanceof SocketException) {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        } else {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        }

                    }
                });

            } else {
                finalJsonIndex[0]++;
                if (finalJsonIndex[0] != keyList.size()) {
                    uploadDataWithoutWait(keyList, finalJsonIndex[0], coverageList, coverageIndex);
                } else {
                    pd.setMessage("updating status :" + coverageIndex);
                    //uploading status D for current store from coverageList
                    updateStatus(coverageList, coverageIndex, CommonString.KEY_D);

                }
            }
        } catch (Exception ex) {

        }

    }

    void updateStatus(final ArrayList<CoverageBean> coverageList, final int coverageIndex, String status) {
        if (coverageList.get(coverageIndex) != null) {
            try {
                final int[] tempcoverageIndex = {coverageIndex};
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("StoreId", coverageList.get(coverageIndex).getStoreId());
                jsonObject.put("VisitDate", coverageList.get(coverageIndex).getVisitDate());
                jsonObject.put("UserId", _UserId);
                jsonObject.put("Status", status);

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .build();

                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL2).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
                PostApi api = adapter.create(PostApi.class);
                Call<ResponseBody> call = null;

                call = api.getCoverageStatusDetail(jsonData);

                pd.setMessage("Uploading store status " + (coverageIndex + 1) + "/" + coverageList.size());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                if (data.equalsIgnoreCase("")) {
                                    pd.dismiss();
                                    AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                                } else {
                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                    if (data.contains("1")) {
                                        db.open();
                                        db.updateCheckoutStatus(coverageList.get(tempcoverageIndex[0]).getStoreId(), coverageList.get(tempcoverageIndex[0]).getCampaignId(), CommonString.KEY_D, CommonString.TABLE_Journey_Plan);
                                        tempcoverageIndex[0]++;
                                        if (tempcoverageIndex[0] != coverageList.size()) {
                                            //updateStatus(coverageList, tempcoverageIndex[0], CommonString.KEY_D);
                                            uploadDataUsingCoverageRecursive(coverageList, tempcoverageIndex[0]);
                                        } else {
                                            pd.setMessage("uploading images");
                                            String coverageDate = null;
                                            if (coverageList.size() > 0) {
                                                coverageDate = coverageList.get(0).getVisitDate();
                                            } else {
                                                coverageDate = date;
                                            }
                                            //UploadImageFileJsonList(context, coverageDate);
                                            uploadImage(coverageDate);
                                        }

                                    } else {
                                        pd.dismiss();
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                                    }
                                }
                                // jsonStringList.remove(finalJsonIndex);
                                // KeyNames.remove(finalJsonIndex);
                            } catch (Exception e) {
                                pd.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                            }
                        } else {
                            pd.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        isvalid = true;
                        pd.dismiss();
                        if (t instanceof SocketTimeoutException) {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        } else if (t instanceof IOException) {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        } else if (t instanceof SocketException) {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        } else {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        }

                    }
                });

            } catch (JSONException ex) {

            }
        }

    }

    public void uploadDataUsingCoverageRecursive(ArrayList<CoverageBean> coverageList, int coverageIndex) {
        try {
            ArrayList<String> keyList = new ArrayList<>();
            keyList.clear();
            String store_id = coverageList.get(coverageIndex).getStoreId();
            String campaignId = coverageList.get(coverageIndex).getCampaignId();
            String status = null;
            pd.setMessage("Uploading store " + (coverageIndex + 1) + "/" + coverageList.size());
            db.open();

            ArrayList<JourneyPlan> journeyPlans = db.getSpecificStoreData(store_id, campaignId);
            if (journeyPlans.size() > 0) {
                status = journeyPlans.get(0).getUploadStatus();
            } else {
                status = null;
            }

            if (status != null && !status.equalsIgnoreCase(CommonString.KEY_D) && !coverageList.get(coverageIndex).getReasonid().equalsIgnoreCase("11")) {
                keyList.add("CoverageDetail_latest");
                keyList.add("Audit_data");
                //keyList.add("GeoTag");
            }

            if (keyList.size() > 0) {
                UploadImageWithRetrofit upload = new UploadImageWithRetrofit(context, db, pd, from);
                upload.uploadDataWithoutWait(keyList, 0, coverageList, coverageIndex);
            } else {

                if (++coverageIndex != coverageList.size()) {
                    uploadDataUsingCoverageRecursive(coverageList, coverageIndex);
                } else {
                    String coverageDate = null;
                    if (coverageList.size() > 0) {
                        coverageDate = coverageList.get(0).getVisitDate();
                    } else {
                        coverageDate = date;
                    }
                    //UploadImageFileJsonList(context, coverageDate);
                    uploadImage(coverageDate);
                }
            }
            //endregion
        } catch (Exception e) {
            e.printStackTrace();
            pd.dismiss();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
        }

    }

    void uploadImage(String coverageDate) {

        File f = new File(CommonString.FILE_PATH);
        File file[] = f.listFiles();
        if (file.length > 0) {
            uploadedFiles = 0;
            totalFiles = file.length;
            UploadImageRecursive(context);
        } else {
            uploadedFiles = 0;
            totalFiles = file.length;
            new StatusUpload(coverageDate).execute();
        }
    }

    //region StatusUpload
    class StatusUpload extends AsyncTask<String, String, String> {
        String coverageDate;

        StatusUpload(String coverageDate) {
            this.coverageDate = coverageDate;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                db = new RBGTDatabase(context);
                db.open();
                ArrayList<JourneyPlan> storeList = db.getStoreData(coverageDate);
                for (int i = 0; i < storeList.size(); i++) {
                    if (storeList.get(i).getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("StoreId", storeList.get(i).getStoreId());
                        jsonObject.put("Campaign_Id", storeList.get(i).getCampaignId());
                        jsonObject.put("VisitDate", coverageDate);
                        jsonObject.put("UserId", _UserId);
                        jsonObject.put("Status", CommonString.KEY_U);

                        UploadImageWithRetrofit upload = new UploadImageWithRetrofit(context);
                        String jsonString2 = jsonObject.toString();
                        String result = upload.downloadDataUniversal(jsonString2, CommonString.COVERAGEStatusDetail);

                        if (result.equalsIgnoreCase(CommonString.MESSAGE_NO_RESPONSE_SERVER)) {
                            statusUpdated = false;
                            throw new SocketTimeoutException();
                        } else if (result.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                            statusUpdated = false;
                            throw new IOException();
                        } else if (result.equalsIgnoreCase(CommonString.MESSAGE_INVALID_JSON)) {
                            statusUpdated = false;
                            throw new JsonSyntaxException("Coverage Status Detail");
                        } else if (result.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                            statusUpdated = false;
                            throw new Exception();
                        } else {
                            statusUpdated = true;
                            if (db.updateCheckoutStatus(String.valueOf(storeList.get(i).getStoreId()), String.valueOf(storeList.get(i).getCampaignId()), CommonString.KEY_U, CommonString.TABLE_Journey_Plan) > 0) {
                                db.deleteTableWithStoreID(String.valueOf(storeList.get(i).getStoreId()), String.valueOf(storeList.get(i).getCampaignId()));
                                //AlertandMessages.show
                                // Alert((Activity) context, "All Image Uploaded Successfully", false);
                            }

                            if (db.updateCheckoutStatus(String.valueOf(storeList.get(i).getStoreId()), String.valueOf(storeList.get(i).getCampaignId()), CommonString.KEY_U, CommonString.TABLE_Journey_Plan_DBSR_Saved) > 0) {
                                db.deleteTableWithStoreID(String.valueOf(storeList.get(i).getStoreId()), String.valueOf(storeList.get(i).getCampaignId()));
                                //AlertandMessages.show
                                // Alert((Activity) context, "All Image Uploaded Successfully", false);
                            }
                        }
                    }
                }

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
            } catch (IOException e) {
                e.printStackTrace();
                AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON, true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            if (statusUpdated) {
                return CommonString.KEY_SUCCESS;
            } else {
                return CommonString.KEY_FAILURE;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                if (totalFiles == uploadedFiles && statusUpdated) {
                    AlertandMessages.showAlert((Activity) context, "All images uploaded Successfully", true);
                } else if (totalFiles == uploadedFiles && !statusUpdated) {
                    AlertandMessages.showAlert((Activity) context, "All images uploaded Successfully, but status not updated", true);
                } else {
                    AlertandMessages.showAlert((Activity) context, "Some images not uploaded", true);
                }
            }
        }
    }
    //endregion

    //region DownloadImageTask
    /*class DownloadImageTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                downloadImages();
                return CommonString.KEY_SUCCESS;
            } catch (FileNotFoundException ex) {
                return CommonString.KEY_FAILURE;
            } catch (IOException ex) {
                return CommonString.KEY_FAILURE;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                pd.dismiss();
                AlertandMessages.showAlert((Activity) context, "All data downloaded Successfully", true);
            } else {
                pd.dismiss();
                AlertandMessages.showAlert((Activity) context, "Error in downloading", true);
            }

        }

    }*/
    //endregion

    //region downloadImages
    /*void downloadImages() throws IOException, FileNotFoundException {
        //region JCP Image Download
        if (jcpObject != null) {
            for (int i = 0; i < jcpObject.getJourneyPlan().size(); i++) {

                String image_name = jcpObject.getJourneyPlan().get(i).getImageName();
                if (image_name != null && !image_name.equalsIgnoreCase("NA")
                        && !image_name.equalsIgnoreCase("")) {
                    URL url = new URL(jcpObject.getJourneyPlan().get(i).getImagePath() + image_name);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.getResponseCode();
                    c.setConnectTimeout(20000);
                    c.connect();

                    if (c.getResponseCode() == 200) {
                        int length = c.getContentLength();
                        String size = new DecimalFormat("##.##")
                                .format((double) ((double) length / 1024))
                                + " KB";
                               *//* String PATH = Environment
                                        .getExternalStorageDirectory()
                                        + "/GT_GSK_Images/";*//*
                        File file = new File(CommonString.FILE_PATH_Downloaded);
                        file.mkdirs();
                        if (!new File(CommonString.FILE_PATH_Downloaded
                                + image_name).exists()
                                && !size.equalsIgnoreCase("0 KB")) {

                            jj = image_name.split("\\/");
                            image_name = jj[jj.length - 1];
                            File outputFile = new File(file, image_name);
                            FileOutputStream fos = null;
                            fos = new FileOutputStream(outputFile);
                            InputStream is1 = (InputStream) c.getInputStream();
                            int bytes = 0;
                            byte[] buffer = new byte[1024];
                            int len1 = 0;

                            while ((len1 = is1.read(buffer)) != -1) {
                                bytes = (bytes + len1);
                                // data.value = (int) ((double) (((double)
                                // bytes) / length) * 100);
                                fos.write(buffer, 0, len1);
                            }

                            fos.close();
                            is1.close();

                        }
                    }
                }
            }

        }
        //endregion

        //region Category Images
        if (categoryObject != null) {
            for (int i = 0; i < categoryObject.getCategoryMaster().size(); i++) {

                String image_name = categoryObject.getCategoryMaster().get(i).getIcon();
                if (image_name != null && !image_name.equalsIgnoreCase("NA")
                        && !image_name.equalsIgnoreCase("")) {
                    URL url = new URL(categoryObject.getCategoryMaster().get(i).getImagePath() + image_name);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.getResponseCode();
                    c.setConnectTimeout(20000);
                    c.connect();
                    if (c.getResponseCode() == 200) {
                        int length = c.getContentLength();
                        String size = new DecimalFormat("##.##")
                                .format((double) ((double) length / 1024))
                                + " KB";
                               *//* String PATH = Environment
                                        .getExternalStorageDirectory()
                                        + "/GT_GSK_Images/";*//*
                        File file = new File(CommonString.FILE_PATH_Downloaded);
                        file.mkdirs();
                        if (!new File(CommonString.FILE_PATH_Downloaded + image_name).exists()
                                && !size.equalsIgnoreCase("0 KB")) {

                            jj = image_name.split("\\/");
                            image_name = jj[jj.length - 1];
                            File outputFile = new File(file, image_name);
                            FileOutputStream fos = new FileOutputStream(outputFile);
                            InputStream is1 = (InputStream) c.getInputStream();
                            int bytes = 0;
                            byte[] buffer = new byte[1024];
                            int len1 = 0;

                            while ((len1 = is1.read(buffer)) != -1) {
                                bytes = (bytes + len1);
                                // data.value = (int) ((double) (((double)
                                // bytes) / length) * 100);
                                fos.write(buffer, 0, len1);
                            }
                            fos.close();
                            is1.close();

                        }
                    }
                }


                String image_name2 = categoryObject.getCategoryMaster().get(i).getIconDone();
                if (image_name2 != null && !image_name2.equalsIgnoreCase("NA")
                        && !image_name2.equalsIgnoreCase("")) {
                    URL url = new URL(categoryObject.getCategoryMaster().get(i).getImagePath() + image_name2);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.getResponseCode();
                    c.setConnectTimeout(20000);
                    c.connect();

                    if (c.getResponseCode() == 200) {

                        int length = c.getContentLength();

                        String size = new DecimalFormat("##.##")
                                .format((double) ((double) length / 1024))
                                + " KB";

                                *//*String PATH = Environment
                                        .getExternalStorageDirectory()
                                        + "/GT_GSK_Images/";*//*
                        File file = new File(CommonString.FILE_PATH_Downloaded);
                        file.mkdirs();

                        if (!new File(CommonString.FILE_PATH_Downloaded
                                + image_name2).exists()
                                && !size.equalsIgnoreCase("0 KB")) {

                            jj = image_name2.split("\\/");
                            image_name2 = jj[jj.length - 1];

                            File outputFile = new File(file,
                                    image_name2);
                            FileOutputStream fos = new FileOutputStream(
                                    outputFile);
                            InputStream is1 = (InputStream) c
                                    .getInputStream();

                            int bytes = 0;
                            byte[] buffer = new byte[1024];
                            int len1 = 0;

                            while ((len1 = is1.read(buffer)) != -1) {

                                bytes = (bytes + len1);

                                // data.value = (int) ((double) (((double)
                                // bytes) / length) * 100);

                                fos.write(buffer, 0, len1);
                            }
                            fos.close();
                            is1.close();
                        }
                    }
                }
            }

        }
        //endregion

        //region mapping window Images
        if (mappingWObject != null) {

            for (int i = 0; i < mappingWObject.getMappingWindow().size(); i++) {

                String image_name = mappingWObject.getMappingWindow().get(i).getPlanogramImage();
                if (image_name != null && !image_name.equalsIgnoreCase("NA")
                        && !image_name.equalsIgnoreCase("")) {
                    URL url = new URL(mappingWObject.getMappingWindow().get(i).getImagePath() + image_name);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.getResponseCode();
                    c.setConnectTimeout(20000);
                    c.connect();

                    if (c.getResponseCode() == 200) {
                        int length = c.getContentLength();
                        String size = new DecimalFormat("##.##")
                                .format((double) ((double) length / 1024))
                                + " KB";

                        String PATH = Environment
                                .getExternalStorageDirectory()
                                + "/GT_GSK_Images/";
                        File file = new File(CommonString.FILE_PATH_Downloaded);
                        file.mkdirs();

                        if (!new File(CommonString.FILE_PATH_Downloaded
                                + image_name).exists()
                                && !size.equalsIgnoreCase("0 KB")) {

                            jj = image_name.split("\\/");
                            image_name = jj[jj.length - 1];

                            File outputFile = new File(file,
                                    image_name);
                            FileOutputStream fos = new FileOutputStream(
                                    outputFile);
                            InputStream is1 = (InputStream) c
                                    .getInputStream();

                            int bytes = 0;
                            byte[] buffer = new byte[1024];
                            int len1 = 0;

                            while ((len1 = is1.read(buffer)) != -1) {

                                bytes = (bytes + len1);

                                // data.value = (int) ((double) (((double)
                                // bytes) / length) * 100);

                                fos.write(buffer, 0, len1);

                            }

                            fos.close();
                            is1.close();

                        }
                    }
                }
            }

        }
        //endregion

    }*/
    //endregion

    String createTable(TableStructureGetterSetter tableGetSet) {
        List<TableStructure> tableList = tableGetSet.getTableStructure();
        for (int i = 0; i < tableList.size(); i++) {
            String table = tableList.get(i).getSqlText();
            if (db.createtable(table) == 0) {
                return table;
            }
        }
        return CommonString.KEY_SUCCESS;
    }

    //region downloadDataUniversalWithoutWait
    public void downloadDataUniversalWithoutWait(final ArrayList<String> jsonStringList, final ArrayList<String> KeyNames, int downloadindex, int type) {
        status = 0;
        isvalid = false;
        final String[] data_global = {""};
        String jsonString = "", KeyName = "";
        int jsonIndex = 0;

        if (jsonStringList.size() > 0) {

            jsonString = jsonStringList.get(downloadindex);
            KeyName = KeyNames.get(downloadindex);
            jsonIndex = downloadindex;

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();

            pd.setMessage("Downloading (" + downloadindex + "/" + listSize + ") \n" + KeyName + "");
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
            adapter = new Retrofit.Builder().baseUrl(CommonString.URL2)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PostApi api = adapter.create(PostApi.class);
            Call<ResponseBody> call = null;

            if (type == CommonString.LOGIN_SERVICE) {
                call = api.getLogindetail(jsonData);
            } else if (type == CommonString.DOWNLOAD_ALL_SERVICE) {
                call = api.getDownloadAll(jsonData);
            } else if (type == CommonString.COVERAGE_DETAIL) {
                call = api.getCoverageDetail(jsonData);
            } else if (type == CommonString.UPLOADJCPDetail) {
                call = api.getUploadJCPDetail(jsonData);
            } else if (type == CommonString.UPLOADJsonDetail) {
                call = api.getUploadJsonDetail(jsonData);
            } else if (type == CommonString.COVERAGEStatusDetail) {
                call = api.getCoverageStatusDetail(jsonData);
            } else if (type == CommonString.CHECKOUTDetail) {
                call = api.getCheckout(jsonData);
            } else if (type == CommonString.DELETE_COVERAGE) {
                call = api.deleteCoverageData(jsonData);
            }

            final int[] finalJsonIndex = {jsonIndex};
            final String finalKeyName = KeyName;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            if (data.equalsIgnoreCase("")) {
                                data_global[0] = "";

                            } else {
                                data = data.substring(1, data.length() - 1).replace("\\", "");
                                data_global[0] = data;
                                if (finalKeyName.equalsIgnoreCase("Table_Structure")) {
                                    editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                                    editor.apply();
                                    tableStructureObj = new Gson().fromJson(data, TableStructureGetterSetter.class);
                                    String isAllTableCreated = createTable(tableStructureObj);
                                    if (isAllTableCreated != CommonString.KEY_SUCCESS) {
                                        pd.dismiss();
                                        AlertandMessages.showAlert((Activity) context, isAllTableCreated + " not created", true);
                                    }
                                } else {
                                    editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                                    editor.apply();
                                    //region Description
                                    switch (finalKeyName) {
                                        case "Journey_Plan":
                                            if (!data.contains("No Data")) {
                                                jcpObject = new Gson().fromJson(data, JCPGetterSetter.class);
                                                if (jcpObject != null && !db.insertJCPData(jcpObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "JCP data data not saved");
                                                }
                                            } else {
                                                throw new Exception();
                                            }
                                            break;
                                        case "Non_Working_Reason":
                                            if (!data.contains("No Data")) {
                                                nonWorkingObj = new Gson().fromJson(data, NonWorkingReasonGetterSetter.class);
                                                if (nonWorkingObj != null && !db.insertNonWorkingData(nonWorkingObj)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Non Working Reason data not saved");
                                                }
                                            } else {
                                                throw new Exception();
                                            }
                                            break;
                                        case "Audit_Question_Master":
                                            if (!data.contains("No Data")) {
                                                auditQuestionMasterGetSet = new Gson().fromJson(data, AuditQuestionMasterGetterSetter.class);
                                                if (auditQuestionMasterGetSet != null && !db.insertAuditQuestionMasterData(auditQuestionMasterGetSet)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Audit Question Master data not saved");
                                                }
                                            } else {
                                                throw new Exception();
                                            }
                                            break;
                                        case "Audit_Answer_Master":
                                            if (!data.contains("No Data")) {
                                                auditAnswerMasterGetSet = new Gson().fromJson(data, AuditAnswerMasterGetterSetter.class);
                                                if (auditAnswerMasterGetSet != null && !db.insertAuditAnswerMasterData(auditAnswerMasterGetSet)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Audit Answer Master data not saved");
                                                }
                                            } else {
                                                throw new Exception();
                                            }
                                            break;
                                    }
                                    //endregion
                                }
                            }
                            // jsonStringList.remove(finalJsonIndex);
                            // KeyNames.remove(finalJsonIndex);
                            finalJsonIndex[0]++;
                            if (finalJsonIndex[0] != KeyNames.size()) {
                                editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                                editor.apply();
                                downloadDataUniversalWithoutWait(jsonStringList, KeyNames, finalJsonIndex[0], CommonString.DOWNLOAD_ALL_SERVICE);
                            } else {
                                editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
                                editor.apply();
                                pd.dismiss();
                                AlertandMessages.showAlert((Activity) context, "All data downloaded Successfully", true);
                                //downloadImages();
                                //pd.setMessage("Downloading Images");
                                //new DownloadImageTask().execute();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                            editor.apply();
                            pd.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in downloading Data at " + finalKeyName, true);
                        }
                    } else {
                        editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                        editor.apply();
                        pd.dismiss();
                        AlertandMessages.showAlert((Activity) context, "Error in downloading Data at " + finalKeyName, true);

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isvalid = true;
                    pd.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                    } else if (t instanceof IOException) {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                    } else if (t instanceof SocketException) {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                    } else {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                    }

                }
            });

        } else {
            editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
            editor.apply();
            // pd.dismiss();
            // AlertandMessages.showAlert((Activity) context, "All data downloaded Successfully", true);
            //pd.setMessage("Downloading Images");
            //new DownloadImageTask().execute();
        }

    }
    //endregion

}
