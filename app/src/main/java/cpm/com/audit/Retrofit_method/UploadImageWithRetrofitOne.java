package cpm.com.audit.Retrofit_method;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import cpm.com.audit.R;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.utilities.AlertandMessages;
import cpm.com.audit.utilities.CommonString;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by deepakp on 10/4/2017.
 */

public class UploadImageWithRetrofitOne {

    RequestBody body1;
    PostApiForUpload api;
    retrofit.Call<String> call;
    private Retrofit adapter;
    int status = 0;
    int count = 0;
    public static int uploadedFiles = 0;
    public static int totalFiles = 0;
    boolean isvalid = false, statusUpdated = true;
    Context context;
    String visitDate, userID, uploadStatus;
    int storeId = 0;
    RBGTDatabase db;
    ProgressDialog pd;
   // ArrayList<JourneyPlan> storeList, storeList_deviation;

    public UploadImageWithRetrofitOne(Context context) {
        this.context = context;
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setTitle("Please wait");
        pd.setMessage("Uploading images");
        pd.show();
    }

    public UploadImageWithRetrofitOne() {
    }

    public UploadImageWithRetrofitOne(Context context, String msg) {
        this.context = context;
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setTitle("Please wait");
        pd.setMessage(msg);
        pd.show();
    }


    public UploadImageWithRetrofitOne(String visitDate, String userId, Context context) {
        this.visitDate = visitDate;
        this.userID = userId;
        this.context = context;
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setTitle("Please wait");
        pd.setMessage("Uploading images");
        pd.show();
    }


    //region UploadImageRecursive
   /* public void UploadImageRecursive(final Context context) {
        try {

            status = 0;
            String filename = null, foldername = null;
            int totalfiles = 0;
            File f = new File(CommonString.FILE_PATH);
            File file[] = f.listFiles();
            count = file.length;
            if (file.length > 0) {
                filename = "";
                totalfiles = f.listFiles().length;
                pd.setMessage("Uploading images" + "(" + uploadedFiles + "/" + totalFiles + ")");
                for (int i = 0; i < file.length; i++) {
                    if (new File(CommonString.FILE_PATH + file[i].getName()).exists()) {
                        if (file[i].getName().contains("_StoreImg-")) {
                            foldername = "Coverage";
                        } else if (file[i].getName().contains("_NonWorking-")) {
                            foldername = "Coverage";
                        } else if (file[i].getName().contains("_GeoTag-")) {
                            foldername = "GeoTag";
                        } else if (file[i].getName().contains("_MyPOSM-")) {
                            foldername = "MyPosm";
                        } else if (file[i].getName().contains("_Visitor_Intime-") || file[i].getName().contains("_Visitor_Outtime-")) {
                            foldername = "VisitorLogin";
                        } else if (file[i].getName().contains("_Backwall_Topup-")
                                || file[i].getName().contains("_Shelf1_Topup-")
                                || file[i].getName().contains("_Shelf2_Topup-")
                                || file[i].getName().contains("_Dealer_Board_Topup-")
                                || file[i].getName().contains("_POSM_Topup-")) {
                            foldername = "Topup";
                        } else if (file[i].getName().contains("_SS_Promo-")) {
                            foldername = "SS_Promotion";
                        } else if (file[i].getName().contains("_SS_PrimaryBay-") || file[i].getName().contains("_CategoryPic_SS-")) {
                            foldername = "SS_Primary";
                        } else if (file[i].getName().contains("_SS_Sec_Display-")) {
                            foldername = "SS_Secondary";
                        } else if (file[i].getName().contains("_SS_Posm-")) {
                            foldername = "SS_Touchpoint";
                        } else if (file[i].getName().contains("_SS_Comptetion-")) {
                            foldername = "SS_Competition";
                        } else if (file[i].getName().contains("_CANVAS_Primary-")) {
                            foldername = "PrimaryWindow";
                        } else if (file[i].getName().contains("_CANVAS_Sec_Window-")) {
                            foldername = "SecondaryWindow";
                        } else if (file[i].getName().contains("_CANVAS_Sec_Backwall-")) {
                            foldername = "Backwall";
                        } else if (file[i].getName().contains("_CANVAS_POSM-")) {
                            foldername = "Touchpoint";
                        } else if (file[i].getName().contains("_Store_Profile-") || file[i].getName().contains("_GST_Img-") || file[i].getName().contains("_PAN_Img-")) {
                            foldername = "StoreProfile";
                        } else {
                            foldername = "Bulkimages";
                        }

                        filename = file[i].getName();
                    }
                    break;
                }


                File originalFile = new File(CommonString.FILE_PATH + filename);
                final File finalFile = saveBitmapToFileSmaller(originalFile);
                String date;
                if (filename.contains("-")) {
                    date = getParsedDate(filename);
                } else {
                    date = visitDate;
                }

                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
                okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
                okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);

                com.squareup.okhttp.RequestBody photo = com.squareup.okhttp.RequestBody.create(com.squareup.okhttp.MediaType.parse("application/octet-stream"), finalFile);

                if (filename.contains("_Store_Profile-")) {
                    body1 = new MultipartBuilder()
                            .type(MultipartBuilder.FORM)
                            .addFormDataPart("file", finalFile.getName(), photo)
                            .addFormDataPart("FolderName", foldername)
                            .build();
                    adapter = new retrofit.Retrofit.Builder()
                            .baseUrl(CommonString.URL3)
                            .client(okHttpClient)
                            .addConverterFactory(new StringConverterFactory())
                            .build();
                    api = adapter.create(PostApiForUpload.class);
                    call = api.getUploadImages(body1);
                } else {

                    body1 = new MultipartBuilder()
                            .type(MultipartBuilder.FORM)
                            .addFormDataPart("file", finalFile.getName(), photo)
                            .addFormDataPart("FolderName", foldername)
                            .addFormDataPart("Path", date)
                            .build();

                    adapter = new retrofit.Retrofit.Builder()
                            .baseUrl(CommonString.URL3)
                            .client(okHttpClient)
                            .addConverterFactory(new StringConverterFactory())
                            .build();
                    api = adapter.create(PostApiForUpload.class);
                    call = api.getUploadImageRetrofitOne(body1);
                }

                call.enqueue(new retrofit.Callback<String>() {
                    @Override
                    public void onResponse(retrofit.Response<String> response) {
                        if (response.isSuccess() && response.body().contains("Success")) {
                            finalFile.delete();
                            status = 1;
                            uploadedFiles++;
                        } else {
                            status = 0;
                            //uploadedFiles = 0;
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
                    public void onFailure(Throwable t) {
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
                    new StatusUpload().execute();
                    //endregion
                }

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON, true);
        } catch (Exception e) {
            e.printStackTrace();
            if (totalFiles == uploadedFiles && !statusUpdated) {
                AlertandMessages.showAlert((Activity) context, "All images uploaded but status not updated", true);
            } else {
                AlertandMessages.showAlert((Activity) context, CommonString.KEY_FAILURE, true);
            }
        }

    }*/
    //endregion

    String getParsedDate(String filename) {
        String testfilename = filename;
        testfilename = testfilename.substring(testfilename.indexOf("-") + 1);
        testfilename = testfilename.substring(0, testfilename.indexOf("-"));
        return testfilename;
    }


    //region StatusUpload
    /*class StatusUpload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                db = new GSKGTMerDB(context);
                db.open();
                storeList = db.getStoreData(visitDate);
                for (int i = 0; i < storeList.size(); i++) {
                    if (storeList.get(i).getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("StoreId", storeList.get(i).getStoreId());
                        jsonObject.put("VisitDate", visitDate);
                        jsonObject.put("UserId", userID);
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
                            db.open();
                            if (db.updateCheckoutStatus(String.valueOf(storeList.get(i).getStoreId()), CommonString.KEY_U, CommonString.TABLE_Journey_Plan) > 0) {
                                db.deleteTableWithStoreID(String.valueOf(storeList.get(i).getStoreId()));
                                //AlertandMessages.show
                                // Alert((Activity) context, "All Image Uploaded Successfully", false);
                            } else {
                                //AlertandMessages.showAlert((Activity) context, "Store status not updated", false);
                            }
                        }
                    }
                }

                db.open();
                storeList_deviation = db.getPJPDeviationList(visitDate);
                for (int i = 0; i < storeList_deviation.size(); i++) {
                    if (storeList_deviation.get(i).getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("StoreId", storeList_deviation.get(i).getStoreId());
                        jsonObject.put("VisitDate", visitDate);
                        jsonObject.put("UserId", userID);
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
                            db.open();
                            if (db.updateCheckoutStatus(String.valueOf(storeList_deviation.get(i).getStoreId()), CommonString.KEY_U, CommonString.TABLE_Deviation_Journey_Plan) > 0) {
                                db.deleteTableWithStoreID(String.valueOf(storeList_deviation.get(i).getStoreId()));
                                //AlertandMessages.show
                                // Alert((Activity) context, "All Image Uploaded Successfully", false);
                            } else {
                                //AlertandMessages.showAlert((Activity) context, "Store status not updated", false);
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
    }*/
    //endregion


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

    class StringConverterFactory implements Converter.Factory {
        private StringConverterFactory() {
        }

        @Override
        public Converter<String> get(Type type) {
            Class<?> cls = (Class<?>) type;
            if (String.class.isAssignableFrom(cls)) {
                return new StringConverter();
            }
            return null;
        }
    }

    private static class StringConverter implements Converter<String> {
        private static final MediaType PLAIN_TEXT = MediaType.parse("text/plain; charset=UTF-8");

        @Override
        public String fromBody(ResponseBody body) throws IOException {
            return new String(body.bytes());
        }

        @Override
        public RequestBody toBody(String value) {
            return RequestBody.create(PLAIN_TEXT, convertToBytes(value));
        }

        private static byte[] convertToBytes(String string) {
            try {
                return string.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public String UploadImage2(final String filename, String foldername, String folderPath) {
        try {
            status = 0;
            File originalFile = new File(folderPath + filename);
            final File finalFile = saveBitmapToFileSmaller(originalFile);
            isvalid = false;
            String date;
            if (filename.contains("-")) {
                date = getParsedDate(filename);
            } else {
                date = visitDate;
            }
            // RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), finalFile);
            // MultipartBody.Part body = MultipartBody.Part.createFormData("image", filename, requestFile);
            // add another part within the multipart request
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);

            com.squareup.okhttp.RequestBody photo = com.squareup.okhttp.RequestBody.create(com.squareup.okhttp.MediaType.parse("application/octet-stream"), finalFile);
            body1 = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("file", finalFile.getName(), photo)
                    .addFormDataPart("FolderName", foldername)
                    .addFormDataPart("Path", date)
                    .build();

            adapter = new retrofit.Retrofit.Builder()
                    .baseUrl(CommonString.URL3)
                    .client(okHttpClient)
                    .addConverterFactory(new StringConverterFactory())
                    .build();
            PostApiForUpload api = adapter.create(PostApiForUpload.class);

            retrofit.Call<String> call = api.getUploadImageRetrofitOne(body1);
            // Call<ResponseBody> observable = api.getUploadImage(body);
            call.enqueue(new retrofit.Callback<String>() {

                @Override
                public void onResponse(Response<String> response) {
                    if (response.isSuccess() && response.body().contains("Success")) {
                        finalFile.delete();
                        uploadedFiles++;
                        isvalid = true;
                        status = 1;
                    } else {
                        isvalid = true;
                        status = 0;
                        uploadedFiles++;
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    isvalid = true;
                    //Toast.makeText(context, finalFile.getName() + " not uploaded", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                        status = -1;
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
                return CommonString.KEY_SUCCESS;
            } else if (status == -1) {
                return CommonString.MESSAGE_SOCKETEXCEPTION;
            } else {
                return CommonString.KEY_FAILURE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonString.KEY_FAILURE;

        }
    }


    public void UploadBackup(final String filename, String foldername, String folderPath) {
        try {
            status = 0;
            final File finalFile = new File(folderPath + filename);
            isvalid = false;
            String date;
            if (filename.contains("-")) {
                date = getParsedDate(filename);
            } else {
                date = visitDate;
            }
            com.squareup.okhttp.RequestBody photo = com.squareup.okhttp.RequestBody.create(com.squareup.okhttp.MediaType.parse("application/octet-stream"), finalFile);
            body1 = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("file", finalFile.getName(), photo)
                    .addFormDataPart("Foldername", foldername)
                    .addFormDataPart("Path", date)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);

            //.addNetworkInterceptor(networkInterceptor)

            adapter = new retrofit.Retrofit.Builder()
                    .baseUrl(CommonString.URL3)
                    .client(okHttpClient)
                    .addConverterFactory(new StringConverterFactory())
                    .build();
            PostApiForUpload api = adapter.create(PostApiForUpload.class);
            retrofit.Call<String> call = api.getUploadImageRetrofitOne(body1);
            call.enqueue(new retrofit.Callback<String>() {

                @Override
                public void onResponse(Response<String> response) {
                    if (response.isSuccess() && response.body().contains("Success")) {
                        finalFile.delete();
                        pd.dismiss();
                        AlertandMessages.showToastMsg(context, context.getString(R.string.data_uploaded_successfully));
                    } else {
                        pd.dismiss();
                        AlertandMessages.showAlert((Activity) context, context.getString(R.string.database_not_uploaded), true);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                        pd.dismiss();
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
                    } else {
                        pd.dismiss();
                        AlertandMessages.showAlert((Activity) context, context.getString(R.string.errordatabase_not_uploaded), true);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            pd.dismiss();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_EXCEPTION, true);
        }
    }

}
