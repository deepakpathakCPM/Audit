package cpm.com.audit.download;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import cpm.com.audit.R;
import cpm.com.audit.Retrofit_method.UploadImageWithRetrofit;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.utilities.CommonString;

public class DownloadActivity extends AppCompatActivity {
    RBGTDatabase db;
    String userId, date, rightname;
    private SharedPreferences preferences = null;
    Context context;
    int downloadindex = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        context = this;
        db = new RBGTDatabase(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        date = preferences.getString(CommonString.KEY_DATE, "");
        rightname = preferences.getString(CommonString.KEY_RIGHTNAME, "");
        downloadindex = preferences.getInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
        getSupportActionBar().setTitle("Download - " + date);
        UploadDataTask();
    }

    public void UploadDataTask() {
        try {
            ArrayList<String> keysList = new ArrayList<>();
            ArrayList<String> jsonList = new ArrayList<>();
            ArrayList<String> KeyNames = new ArrayList<>();
            KeyNames.clear();
            keysList.clear();
            keysList.add("Table_Structure");
            keysList.add("Journey_Plan");
            keysList.add("Non_Working_Reason");
            keysList.add("Audit_Question_Master");
            keysList.add("Audit_Answer_Master");

            if (keysList.size() > 0) {
                for (int i = 0; i < keysList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Downloadtype", keysList.get(i));
                    jsonObject.put("Username", userId);
                    jsonList.add(jsonObject.toString());
                    KeyNames.add(keysList.get(i));
                }

                if (jsonList.size() > 0) {
                    ProgressDialog pd = new ProgressDialog(context);
                    pd.setCancelable(false);
                    pd.setMessage("Downloading Data" + "(" + "/" + ")");
                    pd.show();
                    UploadImageWithRetrofit downloadData = new UploadImageWithRetrofit(context, db, pd, CommonString.TAG_FROM_CURRENT);
                    downloadData.listSize = jsonList.size();
                    downloadData.downloadDataUniversalWithoutWait(jsonList, KeyNames, downloadindex, CommonString.DOWNLOAD_ALL_SERVICE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
