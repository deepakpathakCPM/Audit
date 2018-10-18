package cpm.com.audit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import cpm.com.audit.Retrofit_method.upload.PreviousDataUploadActivity;
import cpm.com.audit.Retrofit_method.upload.UploadWithoutWaitActivity;
import cpm.com.audit.dailyEntry.CampaignListActivity;
import cpm.com.audit.dailyEntry.ServiceActivity;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.delegates.CoverageBean;
import cpm.com.audit.download.DownloadActivity;
import cpm.com.audit.getterSetter.JourneyPlan;
import cpm.com.audit.utilities.AlertandMessages;
import cpm.com.audit.utilities.CommonString;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private WebView webView;
    private ImageView imageView;
    private String noticeboard, quiz_url;
    private RBGTDatabase db;
    private View headerView;
    //private ArrayList<CoverageBean> coverageList;
    private String error_msg;
    private Context context;
    private int downloadIndex;
    private SharedPreferences preferences;
    String user_name;
    android.support.v7.app.ActionBar actionBar;
    FloatingActionButton fab;
    Toolbar toolbar;
    String visit_date;
    private ArrayList<JourneyPlan> storelist = new ArrayList<>();
    private ArrayList<CoverageBean> coverageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declaration();
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
        TextView tv_username = (TextView) headerView.findViewById(R.id.nav_user_name);
        tv_username.setText(user_name);
        //tv_usertype.setText(user_type);
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/" + CommonString.FOLDER_NAME_IMAGE);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quiz_url != null) {
                    webView.loadUrl(quiz_url);
                    fab.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!noticeboard.equals("")) {
            webView.loadUrl(noticeboard);
        }
        if (quiz_url == null || quiz_url.equalsIgnoreCase("")) {
            fab.setVisibility(View.INVISIBLE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
        downloadIndex = preferences.getInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
        coverageList = db.getCoverageData(visit_date);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_route_plan) {
            if (downloadIndex == 0) {
                Intent startDownload = new Intent(context, CampaignListActivity.class);
                startActivity(startDownload);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            } else {
                AlertandMessages.showToastMsg(context, "Please download the data first");
            }
        } else if (id == R.id.nav_download) {
            if (checkNetIsAvailable()) {
                if (!db.isCoverageDataFilled(visit_date)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Parinaam");
                    builder.setMessage(getResources().getString(R.string.want_download_data)).setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        db.open();
                                        db.deletePreviousUploadedData(visit_date);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Intent in = new Intent(context, DownloadActivity.class);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Parinaam");
                    builder.setMessage(getResources().getString(R.string.previous_data_upload)).setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent in = new Intent(context, PreviousDataUploadActivity.class);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } else {
                Snackbar.make(webView, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        } else if (id == R.id.nav_upload) {
            db.open();
            if (checkNetIsAvailable()) {
                storelist = db.getStoreData(visit_date);
                if ((storelist.size() > 0) && downloadIndex == 0) {
                    if (coverageList.size() == 0) {
                        Snackbar.make(webView, R.string.no_data_for_upload, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    } else {
                        if (isStoreCheckedIn()) {
                            if (isValid()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Parinaam");
                                builder.setMessage(getResources().getString(R.string.want_upload_data)).setCancelable(false)
                                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i = new Intent(getBaseContext(), UploadWithoutWaitActivity.class);
                                                startActivity(i);
                                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                            }
                                        })
                                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                AlertandMessages.showSnackbarMsg(context, "No data for Upload");
                            }
                        } else {
                            Snackbar.make(webView, error_msg, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }
                    }
                } else {
                    Snackbar.make(webView, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            } else {
                Snackbar.make(webView, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        } else if (id == R.id.nav_exit) {
            ActivityCompat.finishAffinity(this);
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            finish();
        } else if (id == R.id.nav_services) {
            Intent startservice = new Intent(context, ServiceActivity.class);
            startActivity(startservice);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (checkNetIsAvailable()) {
                imageView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }
            super.onPageFinished(view, url);
            view.clearCache(true);
        }
    }

    private boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private boolean isStoreCheckedIn() {
        boolean result_flag = true;
        for (int i = 0; i < coverageList.size(); i++) {
            ArrayList<JourneyPlan> journeyPlanList = db.getSpecificStoreData(coverageList.get(i).getStoreId(),coverageList.get(i).getCampaignId());
            String status = null;
            if (journeyPlanList.size() > 0) {
                status = journeyPlanList.get(0).getUploadStatus();
            } else {
                result_flag = false;
                status = null;
            }
            if (status != null && (status.equals(CommonString.KEY_CHECK_IN) || status.equals(CommonString.KEY_VALID))) {
                result_flag = false;
                error_msg = getResources().getString(R.string.title_store_list_checkout_current);
                break;
            }
        }
        return result_flag;
    }

    private boolean isValid() {
        boolean flag = false;
        for (int i = 0; i < coverageList.size(); i++) {
            String storestatus;
            ArrayList<JourneyPlan> journeyPlans = db.getSpecificStoreData(coverageList.get(i).getStoreId(),coverageList.get(i).getCampaignId());
            if (journeyPlans.size() > 0) {
                storestatus = journeyPlans.get(0).getUploadStatus();
            } else {
                storestatus = null;
                flag = false;
            }

            if (storestatus != null && !storestatus.equalsIgnoreCase(CommonString.KEY_U)) {
                if ((storestatus.equalsIgnoreCase(CommonString.KEY_C) || storestatus.equalsIgnoreCase(CommonString.KEY_P) ||
                        (storestatus.equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE) && !coverageList.get(i).getReasonid().equalsIgnoreCase("11")) || storestatus.equalsIgnoreCase(CommonString.KEY_D))) {
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            error_msg = getResources().getString(R.string.no_data_for_upload);
        }
        return flag;
    }

    private boolean isPreviousDataValid() {

        boolean flag_isvalid = false;
/*

        JourneyPlan jcp;
        ArrayList<CoverageBean> coverage_list = db.getCoverageDataPrevious(date);
        for (int i = 0; i < coverage_list.size(); i++) {

            if (coverage_list.get(i).getReasonid().equalsIgnoreCase("11")) {
                db.deleteTableWithStoreID(coverage_list.get(i).getStoreId());
                continue;
            }

            if (coverage_list.get(i).getFrom().equals(CommonString.TAG_FROM_JCP)) {
                jcp = db.getSpecificStoreDataPrevious(date, coverage_list.get(i).getStoreId());
            } else {
                jcp = db.getSpecificStoreDataDeviationPrevious(date, coverage_list.get(i).getStoreId());
            }

            if (jcp.getUploadStatus() != null && jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_CHECK_IN)) {
                if (isValid(jcp.getStoreId()) || isValidForSelf(jcp)) {
                    flag_isvalid = true;
                    break;
                }*/
/* else {
                    db.deleteTableWithStoreID(String.valueOf(jcp.getStoreId()));
                }*//*

            } else if (jcp.getUploadStatus() != null && (jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_C)
                    || jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_P)
                    || jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)
                    || (jcp.getUploadStatus().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE) && !coverage_list.get(i).getReasonid().equalsIgnoreCase("11"))
            )) {
                flag_isvalid = true;
                break;
            }

        }
*/

        return flag_isvalid;
    }


    private boolean isValid(int store_id) {
        boolean result = false;
       /* if (db.isPrimaryDataFilledTick(date, store_id) && db.isSecondaryDataFilledTick(date, store_id) && db.isPosmDataFilledTick(date, store_id)) {
            result = true;
        }*/
        return result;
    }


    void declaration() {
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        user_name = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        noticeboard = preferences.getString(CommonString.KEY_NOTICE_BOARD, "");
        quiz_url = preferences.getString(CommonString.KEY_QUIZ_URL, "");
        imageView = (ImageView) findViewById(R.id.img_main);
        webView = (WebView) findViewById(R.id.webview);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar.setTitle(getString(R.string.main_menu_activity_name) + " - " + visit_date);
        //getSupportActionBar().setTitle(getString(R.string.main_menu_activity_name) + " \n- " + date);
        db = new RBGTDatabase(context);
        db.open();
    }


}
