package cpm.com.audit.dailyEntry;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cpm.com.audit.R;
import cpm.com.audit.Retrofit_method.UploadImageWithRetrofit;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.delegates.CoverageBean;
import cpm.com.audit.getterSetter.JourneyPlan;
import cpm.com.audit.utilities.AlertandMessages;
import cpm.com.audit.utilities.CommonFunctions;
import cpm.com.audit.utilities.CommonString;

/**
 * Created by ashishc on 31-05-2016.
 */
public class StoreimageActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    ImageView img_cam, img_clicked;
    Button btn_save;
    private Dialog dialog;
    private TextView percentage, message;
    private ProgressBar pb;
    String _pathforcheck, _path, str;
    String visit_date, visit_date_formatted, username, intime, date, app_ver;
    private SharedPreferences preferences;
    AlertDialog alert;
    String img_str, strflag;
    private RBGTDatabase database;
    double lat, lon;
    GoogleApiClient mGoogleApiClient;
    Toolbar toolbar;
    boolean ResultFlag = true;
    JourneyPlan jcpGetset;
    LocationManager locationManager;
    boolean enabled;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 500; // 5 sec
    private static int FATEST_INTERVAL = 100; // 1 sec
    private static int DISPLACEMENT = 5; // 10 meters
    private Location mLastLocation;
    Context context;
    TextView txt_label;
    String tag_from = "";
    private static final String TAG = StoreimageActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeimage);
        declaration();

        if (getIntent().getSerializableExtra(CommonString.TAG_OBJECT) != null) {
            jcpGetset = (JourneyPlan) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
        }

        try {
            app_ver = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        img_cam.setOnClickListener(this);
        img_clicked.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }

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

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // NavUtils.navigateUpFromSameTask(this);
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.notsuppoted)
                        , Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();
            }
        }
        // if (mRequestingLocationUpdates) {
        startLocationUpdates();
        // }
        // startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.img_cam_selfie:

                _pathforcheck = jcpGetset.getStoreId() + "_" + username.replace(".", "") + "_StoreImg-" + visit_date_formatted + "-" + CommonFunctions.getCurrentTimeHHMMSS() + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                intime = getCurrentTime();
                //CommonFunctions.startCameraActivity((Activity) context, _path);
                CommonFunctions.startAnncaCameraActivity(context, _path, null, false);
                break;
            case R.id.btn_save_selfie:

                if (img_str != null)
                // if (true)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(getResources().getString(R.string.title_activity_save_data))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                    CoverageBean cdata = new CoverageBean();

                                    cdata.setStoreId(jcpGetset.getStoreId().toString());
                                    cdata.setCampaignId(jcpGetset.getCampaignId().toString());
                                    cdata.setVisitDate(visit_date);
                                    cdata.setUserId(username);
                                    cdata.setReason("");
                                    cdata.setReasonid("0");
                                    cdata.setLatitude(String.valueOf(lat));
                                    cdata.setLongitude(String.valueOf(lon));
                                    cdata.setImage(img_str);
                                    cdata.setRemark("");
                                    cdata.setCkeckout_image("");

                                    new GeoTagUpload(cdata).execute();
                                    //CoverageUpload(cdata);

                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    alert = builder.create();
                    alert.show();

                } else {
                    Toast.makeText(context, getResources().getString(R.string.clickimage), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {

            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {
                        try {
                            //Bitmap bmp = BitmapFactory.decodeFile(str + _pathforcheck);
                            Bitmap bmp = convertBitmap(str + _pathforcheck);
                            img_cam.setImageBitmap(bmp);
                        } catch (OutOfMemoryError ex) {
                            CommonFunctions.setScaledImage(img_cam, str + _pathforcheck);
                        }
                        img_clicked.setVisibility(View.GONE);
                        img_cam.setVisibility(View.VISIBLE);

                        img_str = _pathforcheck;
                        _pathforcheck = "";
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());

        return cdate;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

    }

    public static Bitmap convertBitmap(String path) {
        Bitmap bitmap = null;
        BitmapFactory.Options ourOptions = new BitmapFactory.Options();
        ourOptions.inDither = false;
        ourOptions.inPurgeable = true;
        ourOptions.inInputShareable = true;
        ourOptions.inTempStorage = new byte[32 * 1024];
        File file = new File(path);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fs != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, ourOptions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }



    /*protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }*/

    @Override
    public void onLocationChanged(Location location) {

    }


    //region GeoTagUpload
    public class GeoTagUpload extends AsyncTask<Void, Void, String> {

        private CoverageBean cdata;

        GeoTagUpload(CoverageBean cdata) {
            this.cdata = cdata;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle(getResources().getString(R.string.dialog_title));
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                RBGTDatabase db = new RBGTDatabase(context);
                db.open();

                //coverage = db.getCoverageWithStoreID_Data(store_id);
                UploadImageWithRetrofit upload = new UploadImageWithRetrofit(context);
                JSONObject jsonObject;
                String jsonString2 = "", result = "5";

                //region Coverage Data
                jsonObject = new JSONObject();
                jsonObject.put("StoreId", cdata.getStoreId());
                jsonObject.put("Campaign_Id", cdata.getCampaignId());
                jsonObject.put("VisitDate", cdata.getVisitDate());
                jsonObject.put("Latitude", cdata.getLatitude());
                jsonObject.put("Longitude", cdata.getLongitude());
                jsonObject.put("ReasonId", cdata.getReasonid());
                jsonObject.put("SubReasonId", "0");
                jsonObject.put("Remark", cdata.getRemark());
                jsonObject.put("ImageName", cdata.getImage());
                jsonObject.put("AppVersion", app_ver);
                jsonObject.put("UploadStatus", CommonString.KEY_CHECK_IN);
                jsonObject.put("Checkout_Image", cdata.getCkeckout_image());
                jsonObject.put("UserId", username);

                jsonString2 = jsonObject.toString();
                result = upload.downloadDataUniversal(jsonString2, CommonString.COVERAGE_DETAIL);

                if (result.equalsIgnoreCase(CommonString.MESSAGE_NO_RESPONSE_SERVER)) {
                    throw new SocketTimeoutException();
                } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                    throw new IOException();
                } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_INVALID_JSON)) {
                    throw new JsonSyntaxException("Mapping_Mydb_Posm");
                } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                    throw new Exception();
                } else {
                    int mid = 0;
                    try {
                        mid = Integer.parseInt(result);
                        if (mid > 0) {
                            return CommonString.KEY_SUCCESS;
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

            } catch (IOException e) {

                ResultFlag = false;
                strflag = CommonString.MESSAGE_SOCKETEXCEPTION;

            } catch (NumberFormatException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_NUMBER_FORMATE_EXEP;

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
                dialog.dismiss();
                database.open();
                if (database.InsertCoverageData(cdata) > 0) {
                    if (database.updateCheckoutStatus(jcpGetset.getStoreId().toString(),jcpGetset.getCampaignId().toString(), CommonString.KEY_CHECK_IN, CommonString.TABLE_Journey_Plan) > 0) {
                        Intent in = new Intent(context, AuditActivity.class);
                        in.putExtra(CommonString.TAG_OBJECT, jcpGetset);
                        startActivity(in);
                        finish();
                    }
                } else {
                    AlertandMessages.showToastMsg(context, "Error in saving data");
                }
            } else {
                RBGTDatabase db = new RBGTDatabase(context);
                db.open();
                dialog.dismiss();
                db.deleteTableWithStoreID(jcpGetset.getStoreId().toString(),jcpGetset.getCampaignId().toString());
                showAlert(getString(R.string.datanotfound) + " " + result);
            }
        }

    }
    //endregion

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(StoreimageActivity.this);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void declaration() {
        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        img_cam = (ImageView) findViewById(R.id.img_selfie);
        img_clicked = (ImageView) findViewById(R.id.img_cam_selfie);
        btn_save = (Button) findViewById(R.id.btn_save_selfie);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");
        visit_date_formatted = preferences.getString(CommonString.KEY_YYYYMMDD_DATE, "");
        str = CommonString.FILE_PATH;
        txt_label = (TextView) findViewById(R.id.txt_label);
        getSupportActionBar().setTitle("");
        txt_label.setText("Store Image - " + date);
        database = new RBGTDatabase(this);
        database.open();
    }
}
