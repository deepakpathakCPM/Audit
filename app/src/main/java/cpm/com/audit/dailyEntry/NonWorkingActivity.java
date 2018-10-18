package cpm.com.audit.dailyEntry;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cpm.com.audit.R;
import cpm.com.audit.Retrofit_method.UploadImageWithRetrofit;
import cpm.com.audit.Retrofit_method.upload.UploadWithoutWaitActivity;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.delegates.CoverageBean;
import cpm.com.audit.getterSetter.JourneyPlan;
import cpm.com.audit.getterSetter.NonWorkingReason;
import cpm.com.audit.utilities.AlertandMessages;
import cpm.com.audit.utilities.CommonFunctions;
import cpm.com.audit.utilities.CommonString;

public class NonWorkingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    ArrayList<NonWorkingReason> reasondata = new ArrayList<>();
    private Spinner reasonspinner;
    private RBGTDatabase database;
    String reasonname = "", reasonid = "", entry_allow = "", image_allow = "", image, intime = "";
    Button save;
    private ArrayAdapter<CharSequence> reason_adapter;
    protected String _path;
    protected String _pathforcheck = "";
    private String image1 = "";
    private SharedPreferences preferences;
    String _UserId, visit_date, app_ver = "", store_id = "0", campaign_id = "0";
    protected boolean status = true;
    AlertDialog alert;
    ImageButton camera;
    RelativeLayout rel_cam;
    ArrayList<JourneyPlan> jcp;
    boolean update_flag = false;
    GoogleApiClient mGoogleApiClient;
    double lat = 0.0, lon = 0.0;
    ProgressDialog loading;
    Context context;
    ArrayList<JourneyPlan> storelist;
    ArrayList<JourneyPlan> journeyPlan;
    boolean nonflag;
    JourneyPlan jcpGetset;
    private LocationRequest mLocationRequest;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    boolean enabled;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_working);
        declaration();
        database.open();
        storelist = database.getStoreListByCampaignId(jcpGetset);
        journeyPlan = database.getSpecificStoreData(store_id, campaign_id);

        for (int i = 0; i < storelist.size(); i++) {
            if (!storelist.get(i).getUploadStatus().equalsIgnoreCase("N") || !storelist.get(i).getUploadStatus().equalsIgnoreCase("N")) {
                nonflag = true;
                break;
            } else {
                nonflag = false;
            }
        }

        if (nonflag) {
            reasondata = database.getNonWorkingEntryAllowData();
        } else {
            reasondata = database.getNonWorkingData();
        }

        reason_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        reason_adapter.add(getResources().getString(R.string.select_reason));
        for (int i = 0; i < reasondata.size(); i++) {
            reason_adapter.add(reasondata.get(i).getReason());
        }
        reasonspinner.setAdapter(reason_adapter);
        reason_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonspinner.setOnItemSelectedListener(this);
        reason_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        } else {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _pathforcheck = store_id + "_" + campaign_id + "_" + "_NONWORKING-" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(context, _path, null, false);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetIsAvailable()) {
                    if (validatedata()) {
                        if (imageAllowed()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(R.string.title_activity_save_data)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {
                                                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                    if (entry_allow.equalsIgnoreCase("false")) {

                                                        CoverageBean cdata = new CoverageBean();

                                                        cdata.setStoreId(store_id);
                                                        cdata.setCampaignId(campaign_id);
                                                        cdata.setVisitDate(visit_date);
                                                        cdata.setUserId(_UserId);
                                                        cdata.setReason(reasonname);
                                                        cdata.setReasonid(reasonid);
                                                        cdata.setLatitude(String.valueOf(lat));
                                                        cdata.setLongitude(String.valueOf(lon));
                                                        cdata.setImage(image1);
                                                        cdata.setCkeckout_image(image1);
                                                        //cdata.setRemark(text.getText().toString().replaceAll("[&^<>{}'$]", " "));
                                                        cdata.setStatus(CommonString.STORE_STATUS_LEAVE);

                                                        new CoverageNonWorkingUpload(cdata).execute();

                                                    } else {

                                                        ArrayList<CoverageBean> coverageBeanList = new ArrayList<>();

                                                        CoverageBean cdata = new CoverageBean();
                                                        cdata.setStoreId(store_id);
                                                        cdata.setCampaignId(campaign_id);
                                                        cdata.setVisitDate(visit_date);
                                                        cdata.setUserId(_UserId);
                                                        //cdata.setInTime(intime);
                                                        //cdata.setOutTime(getCurrentTime());
                                                        cdata.setReason(reasonname);
                                                        cdata.setReasonid(reasonid);
                                                        cdata.setLatitude(String.valueOf(lat));
                                                        cdata.setLongitude(String.valueOf(lon));
                                                        cdata.setImage(image1);
                                                        cdata.setRemark("");
                                                        //cdata.setRemark(text.getText().toString().replaceAll("[&^<>{}'$]", " "));
                                                        cdata.setStatus(CommonString.STORE_STATUS_LEAVE);

                                                        coverageBeanList.add(cdata);

                                                        if (database.InsertCoverageData(cdata) > 0) {
                                                            if (database.updateStoreStatusOnLeave(store_id, campaign_id, visit_date, CommonString.STORE_STATUS_LEAVE) > 0) {
                                                                SharedPreferences.Editor editor = preferences.edit();
                                                                editor.putString(CommonString.KEY_STOREVISITED_STATUS + store_id, "No");
                                                                editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                                                editor.putString(CommonString.KEY_STORE_IN_TIME, "");
                                                                editor.putString(CommonString.KEY_LATITUDE, "");
                                                                editor.putString(CommonString.KEY_LONGITUDE, "");
                                                                editor.commit();
                                                                new GeoTagUpload(coverageBeanList).execute();

                                                            } else {
                                                                AlertandMessages.showToastMsg(context, "Store status not updated!!");
                                                            }
                                                        } else {
                                                            AlertandMessages.showToastMsg(context, "Coverage not saved!!");
                                                        }
                                                    }
                                                    //  finish();
                                                }
                                            })
                                    .setNegativeButton(R.string.closed,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            alert = builder.create();
                            alert.show();
                        } else {
                            AlertandMessages.showToastMsg(context, getResources().getString(R.string.clickimage));
                        }
                    } else {
                        AlertandMessages.showToastMsg(context, getResources().getString(R.string.please_select_reason));
                    }
                } else {
                    AlertandMessages.showToastMsg(context, getResources().getString(R.string.nonetwork));
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.gps));
            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.gpsebale));
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });
            // Showing Alert Message
            alertDialog.show();
        }
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        int UPDATE_INTERVAL = 500;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        int FATEST_INTERVAL = 100;
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int DISPLACEMENT = 5;
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }


    @SuppressWarnings("deprecation")
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                AlertandMessages.showToastMsg(context, getResources().getString(R.string.notsuppoted));
                finish();
            }
            return false;
        }
        return true;
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub

        switch (arg0.getId()) {
            case R.id.spinner2:
                if (position != 0) {
                    reasonname = reasondata.get(position - 1).getReason();
                    reasonid = reasondata.get(position - 1).getReasonId().toString();
                    entry_allow = reasondata.get(position - 1).getEntryAllow().toString();
                    image_allow = reasondata.get(position - 1).getImageAllow().toString();
                    if (image_allow.equalsIgnoreCase("true")) {
                        rel_cam.setVisibility(View.VISIBLE);
                    } else {
                        rel_cam.setVisibility(View.GONE);
                    }
                } else {
                    reasonname = "";
                    reasonid = "";
                    image_allow = "";
                    entry_allow = "";
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;
            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    try {
                        if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                            Bitmap bmp = BitmapFactory.decodeFile(CommonString.FILE_PATH + _pathforcheck);
                            Bitmap dest = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
                            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                            String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

                            Canvas cs = new Canvas(dest);
                            Paint tPaint = new Paint();
                            tPaint.setTextSize(70);
                            tPaint.setColor(Color.RED);
                            tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                            cs.drawBitmap(bmp, 0f, 0f, null);
                            float height = tPaint.measureText("yY");
                            cs.drawText(dateTime, 20f, height + 15f, tPaint);
                            try {
                                dest.compress(Bitmap.CompressFormat.JPEG, 100,
                                        new FileOutputStream(new File(CommonString.FILE_PATH + _pathforcheck)));
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            camera.setImageDrawable(getResources().getDrawable(R.mipmap.camera_green));
                            image1 = _pathforcheck;
                            _pathforcheck = "";
                        }
                    } catch (Resources.NotFoundException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public boolean imageAllowed() {
        boolean result = true;
        if (image_allow.equalsIgnoreCase("true")) {
            if (image1.equalsIgnoreCase("")) {
                result = false;
            }
        }
        return result;
    }

    private boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public boolean validatedata() {
        boolean result = false;
        if (reasonid != null && !reasonid.equalsIgnoreCase("")) {
            result = true;
        }
        return result;
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!update_flag) {
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                dialog.dismiss();
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public class CoverageNonWorkingUpload extends AsyncTask<Void, Void, String> {

        private CoverageBean coverageBean;
        Dialog dialog;
        boolean ResultFlag = false;
        String strflag;

        CoverageNonWorkingUpload(CoverageBean coverageBean) {
            this.coverageBean = coverageBean;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle(getResources().getString(R.string.dialog_title));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                UploadImageWithRetrofit upload = new UploadImageWithRetrofit(context);

                JSONObject jsonObject;
                String jsonString2 = "", result = "5";

                //region Coverage Data
                jsonObject = new JSONObject();
                jsonObject.put("StoreId", coverageBean.getStoreId());
                jsonObject.put("CampaignId", coverageBean.getCampaignId());
                jsonObject.put("VisitDate", coverageBean.getVisitDate());
                jsonObject.put("Latitude", coverageBean.getLatitude());
                jsonObject.put("Longitude", coverageBean.getLongitude());
                jsonObject.put("ReasonId", coverageBean.getReasonid());
                jsonObject.put("Remark", coverageBean.getRemark());
                jsonObject.put("ImageName", coverageBean.getImage());
                jsonObject.put("AppVersion", app_ver);
                jsonObject.put("UploadStatus", CommonString.KEY_U);
                jsonObject.put("UserId", _UserId);
                jsonObject.put("TSE_Id", 0);

                jsonString2 = jsonObject.toString();
                result = upload.downloadDataUniversal(jsonString2, CommonString.COVERAGE_NONWORKING);

                if (result.equalsIgnoreCase(CommonString.MESSAGE_NO_RESPONSE_SERVER)) {
                    throw new SocketTimeoutException();
                } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                    throw new IOException();
                } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_INVALID_JSON)) {
                    throw new JsonSyntaxException("non_working");
                } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                    throw new Exception();
                } else {
                    int mid = 0;
                    try {
                        mid = Integer.parseInt(result);
                        if (mid > 0) {
                            ResultFlag = true;
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        throw new NumberFormatException();
                    }

                }


            } catch (MalformedURLException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_EXCEPTION;
            } catch (SocketTimeoutException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_SOCKETEXCEPTION;
            } catch (InterruptedIOException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_EXCEPTION;
            } catch (NumberFormatException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_NUMBER_FORMATE_EXEP;
            } catch (IOException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_SOCKETEXCEPTION;
            } catch (XmlPullParserException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_XmlPull;
            } catch (Exception e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_EXCEPTION;
            }
            if (ResultFlag) {
                return CommonString.KEY_SUCCESS;
            } else {
                return strflag;
            }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
               /* if (coverageBeanList.size() > 1) {
                    //database.deleteAllTables();
                }*/
                jcp = database.getStoreListByCampaignId(jcpGetset);
                for (int i = 0; i < jcp.size(); i++) {
                    String storeid = String.valueOf(jcp.get(i).getStoreId());
                    String campaignId = String.valueOf(jcp.get(i).getCampaignId());
                    if (database.updateStoreStatusOnLeave(storeid, campaignId, visit_date, CommonString.STORE_STATUS_LEAVE) > 0) {
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString(CommonString.KEY_STOREVISITED_STATUS + storeid, "No");
                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                        editor.putString(CommonString.KEY_STORE_IN_TIME, "");
                        editor.putString(CommonString.KEY_LATITUDE, "");
                        editor.putString(CommonString.KEY_LONGITUDE, "");
                        editor.commit();

                        AlertandMessages.showAlert((Activity) context, getString(R.string.data_uploaded), true);

                    } else {
                        AlertandMessages.showSnackbarMsg(context, "Store status not updated!!");
                        break;
                    }
                }
                dialog.dismiss();
                finish();
            } else {
              /*  GSKGTMerDB db = new GSKGTMerDB(NonWorkingActivity.this);
                db.open();
                dialog.dismiss();
                db.deleteTableWithStoreID(store_id);
                if (tag_from != null) {
                    if (tag_from.equalsIgnoreCase(CommonString.TAG_FROM_PJP)) {
                        db.updateStoreStatusPJP(store_id, visit_date, CommonString.KEY_N);
                    } else if (tag_from.equalsIgnoreCase(CommonString.TAG_FROM_JCP)) {
                        db.updateStoreStatus(store_id, visit_date, CommonString.KEY_N);
                    }
                }*/
                AlertandMessages.showAlert((Activity) context, getString(R.string.datanotfound) + " " + result, false);
            }
        }

    }

    public class GeoTagUpload extends AsyncTask<Void, Void, String> {

        private ArrayList<CoverageBean> coverageBeanList;
        Dialog dialog;
        boolean ResultFlag = true;
        String strflag = "";

        GeoTagUpload(ArrayList<CoverageBean> coverageBeanList) {
            this.coverageBeanList = coverageBeanList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle(getResources().getString(R.string.dialog_title));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                for (int i = 0; i < coverageBeanList.size(); i++) {
                    UploadImageWithRetrofit upload = new UploadImageWithRetrofit(context);

                    JSONObject jsonObject;
                    String jsonString2 = "", result = "5";

                    //region Coverage Data
                    jsonObject = new JSONObject();
                    jsonObject.put("StoreId", coverageBeanList.get(i).getStoreId());
                    jsonObject.put("Campaign_Id", coverageBeanList.get(i).getCampaignId());
                    jsonObject.put("VisitDate", coverageBeanList.get(i).getVisitDate());
                    jsonObject.put("Latitude", coverageBeanList.get(i).getLatitude());
                    jsonObject.put("Longitude", coverageBeanList.get(i).getLongitude());
                    jsonObject.put("ReasonId", coverageBeanList.get(i).getReasonid());
                    jsonObject.put("SubReasonId", "0");
                    jsonObject.put("Remark", coverageBeanList.get(i).getRemark());
                    jsonObject.put("ImageName", coverageBeanList.get(i).getImage());
                    jsonObject.put("AppVersion", app_ver);
                    jsonObject.put("UploadStatus", CommonString.STORE_STATUS_LEAVE);
                    jsonObject.put("Checkout_Image", coverageBeanList.get(i).getImage());
                    jsonObject.put("UserId", _UserId);

                    jsonString2 = jsonObject.toString();
                    result = upload.downloadDataUniversal(jsonString2, CommonString.COVERAGE_DETAIL);

                    if (result.equalsIgnoreCase(CommonString.MESSAGE_NO_RESPONSE_SERVER)) {
                        throw new SocketTimeoutException();
                    } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                        throw new IOException();
                    } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_INVALID_JSON)) {
                        throw new JsonSyntaxException("non_working");
                    } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                        throw new Exception();
                    } else {
                        int mid = 0;
                        try {
                            mid = Integer.parseInt(result);
                            if (mid > 0) {
                                ResultFlag = true;
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            throw new NumberFormatException();
                        }

                    }

                }


            } catch (MalformedURLException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_EXCEPTION;
            } catch (SocketTimeoutException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_SOCKETEXCEPTION;
            } catch (InterruptedIOException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_EXCEPTION;
            } catch (NumberFormatException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_NUMBER_FORMATE_EXEP;
            } catch (IOException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_SOCKETEXCEPTION;
            } catch (XmlPullParserException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_XmlPull;
            } catch (Exception e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_EXCEPTION;
            }
            if (ResultFlag) {
                return CommonString.KEY_SUCCESS;
            } else {
                return strflag;
            }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
               /* if (coverageBeanList.size() > 1) {
                    //database.deleteAllTables();
                }*/
                coverageBeanList.clear();
                dialog.dismiss();
                Intent i = new Intent(context, UploadWithoutWaitActivity.class);
                startActivity(i);
                finish();
            } else {
                coverageBeanList.clear();
                dialog.dismiss();
                database.open();
                database.deleteTableWithStoreID(store_id, campaign_id);
                database.updateStoreStatus(store_id, campaign_id, visit_date, CommonString.KEY_N);
                AlertandMessages.showAlert((Activity) context, getString(R.string.datanotfound) + " " + result, false);
            }
        }

    }


    @Override
    public void onConnected(Bundle bundle) {

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();
            }
        }
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    void declaration() {
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reasonspinner = (Spinner) findViewById(R.id.spinner2);
        camera = (ImageButton) findViewById(R.id.imgcam);
        save = (Button) findViewById(R.id.save);
        rel_cam = (RelativeLayout) findViewById(R.id.relimgcam);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, null);

        //Intent data
        if (getIntent().getSerializableExtra(CommonString.TAG_OBJECT) != null) {
            jcpGetset = (JourneyPlan) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
            store_id = jcpGetset.getStoreId().toString();
            campaign_id = jcpGetset.getCampaignId().toString();
        }

        app_ver = preferences.getString(CommonString.KEY_VERSION, "");
        getSupportActionBar().setTitle("Non Working -" + visit_date);
        database = new RBGTDatabase(context);
    }

}
