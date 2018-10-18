package cpm.com.audit.autoupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import cpm.com.audit.R;
import cpm.com.audit.utilities.AlertandMessages;
import cpm.com.audit.utilities.CommonString;

public class AutoUpdateActivity extends AppCompatActivity {

    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_update);
        Intent intent = getIntent();
        path = intent.getStringExtra(CommonString.KEY_PATH);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Parinaam");
        builder.setMessage(getString(R.string.new_update_available))
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AutoUpdateActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

						/*new File(
                                "/data/data/com.cpm.gsk_mt/databases/GTMT_DATABASE")
								.delete();*/

                        new DownloadTask(AutoUpdateActivity.this).execute();

                    }
                });

        AlertDialog alert = builder.create();

        alert.show();
    }

    private class DownloadTask extends AsyncTask<Void, Data, String> {

        private final Context context;

        DownloadTask(Context context) {
            this.context = context;
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.custom_dialog_progress);
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                Data data = new Data();
                data.name = "Downloading Application";
                publishProgress(data);
                String versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                data.name = "Upgrading Version : " + versionCode;
                publishProgress(data);
                // download application
                URL url = new URL(path);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);
                c.getResponseCode();
                c.connect();
                int length = c.getContentLength();
                String size = new DecimalFormat("##.##").format((double) length / 1024 / 1024) + " MB";
                String PATH = Environment.getExternalStorageDirectory() + "/download/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, "app.apk");
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                int bytes = 0;
                byte[] buffer = new byte[1024];
                int len1;
                while ((len1 = is.read(buffer)) != -1) {

                    bytes = (bytes + len1);
                    String s = new DecimalFormat("##.##").format((double) (bytes / 1024) / 1024);
                    String p = s.length() == 3 ? s + "0" : s;
                    p = p + " MB";
                    data.value = (int) (((double) bytes) / length * 100);
                    data.name = "Download " + p + "/" + size;
                    publishProgress(data);
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
                return CommonString.KEY_SUCCESS;

            } catch (PackageManager.NameNotFoundException | MalformedURLException e) {
                // TODO Auto-generated catch block
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_EXCEPTION, true);
                    }
                });
            } catch (IOException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_EXCEPTION, true);
                    }
                });
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(Data... values) {
            // TODO Auto-generated method stub
            pb.setProgress(values[0].value);
            percentage.setText(values[0].value + "%");
            message.setText(values[0].name);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result.equals(CommonString.KEY_SUCCESS)) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "app.apk")), "application/vnd.android.package-archive");
                startActivity(i);
                AutoUpdateActivity.this.finish();
            }
        }
    }

    class Data {
        int value;
        String name;
    }
}
