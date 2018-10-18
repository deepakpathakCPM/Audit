package cpm.com.audit.Retrofit_method.upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import cpm.com.audit.R;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.delegates.CoverageBean;
import cpm.com.audit.getterSetter.JourneyPlan;
import cpm.com.audit.Retrofit_method.UploadImageWithRetrofit;
import cpm.com.audit.utilities.CommonString;

import java.util.ArrayList;

/**
 * Created by deepakp on 11/7/2017.
 */

public class UploadWithoutWaitActivity extends AppCompatActivity {

    RBGTDatabase db;
    ArrayList<CoverageBean> coverageList;
    String date, userId, app_version;
    String Path, app_ver;
    private ProgressDialog pb;
    private SharedPreferences preferences;
    Object result = "";
    Toolbar toolbar;
    JourneyPlan jcpGetset;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        if (getIntent().getSerializableExtra(CommonString.TAG_OBJECT) != null) {
            jcpGetset = (JourneyPlan) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            app_ver = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        app_version = preferences.getString(CommonString.KEY_VERSION, null);
        toolbar.setTitle("Upload - " + date);
        Path = CommonString.FILE_PATH;
        db = new RBGTDatabase(context);
        db.open();
        coverageList = db.getCoverageData(date);
        pb = new ProgressDialog(context);
        pb.setCancelable(false);
        pb.setMessage("Uploading Data");
        if (pb != null && (!pb.isShowing())) {
            pb.show();
        }
        UploadImageWithRetrofit upload = new UploadImageWithRetrofit(context, db, pb, CommonString.TAG_FROM_CURRENT);
        upload.uploadDataUsingCoverageRecursive(coverageList, 0);
    }


}
