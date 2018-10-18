package cpm.com.audit.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cpm.com.audit.R;
import cpm.com.audit.Retrofit_method.PostApiForFile;
import cpm.com.audit.Retrofit_method.StringConverterFactory;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.utilities.AlertandMessages;
import cpm.com.audit.utilities.CommonString;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A placeholder fragment containing a simple view.
 */
public class ServiceActivityFragment extends Fragment {

    public ServiceActivityFragment() {
    }

    ArrayList<ServiceGetterSetter> serviceList;
    boolean ResultFlag = false;
    String strflag = "";
    Context context;
    SharedPreferences preferences;
    String user_name;
    RecyclerView rec;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        rec = (RecyclerView) view.findViewById(R.id.rec_daily_entry_menu);
        context = view.getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        user_name = preferences.getString(CommonString.KEY_USERNAME, null);
        ServiceGetterSetter service = new ServiceGetterSetter();
        service.setName(getString(R.string.export_database));
        service.setIcon(R.mipmap.entry_grey);
        //SelectLanguageFragment selectLanguageFragment = new SelectLanguageFragment();
        service.setFragment(null);

        serviceList = new ArrayList<>();
        serviceList.add(service);

        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        ServiceAdapter serviceAdapter = new ServiceAdapter();
        rec.setAdapter(serviceAdapter);

        return view;
    }

    class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final ServiceGetterSetter mItem = serviceList.get(position);
            holder.tv_settings.setText(mItem.getName());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment nextFrag = mItem.getFragment();

                    if (nextFrag != null) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment, nextFrag, "Settings")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        showExportDialog();
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return serviceList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public CardView cardView;
            public TextView tv_settings;

            public ViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.card_layout);
                tv_settings = (TextView) itemView.findViewById(R.id.tv_settings);
            }
        }
    }

    class ServiceGetterSetter {

        String name;
        int icon;
        Fragment fragment;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }


        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }
    }

    public void showExportDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(R.string.Areyou_sure_take_backup)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            File file = new File(Environment.getExternalStorageDirectory(), "AUDIT_Backup");
                            if (!file.isDirectory()) {
                                file.mkdir();
                            }

                            File sd = Environment.getExternalStorageDirectory();
                            File data = Environment.getDataDirectory();

                            if (sd.canWrite()) {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yy");
                                String dateString = sdf.format(date);
                                String currentDBPath = "//data//cpm.com.audit//databases//" + RBGTDatabase.DATABASE_NAME;
                                String backupDBPath = user_name + "_AUDIT_BACKUP_" + dateString.replace('/', '_') + getCurrentTime().replace(":", "") + ".db";
                                String path = Environment.getExternalStorageDirectory().getPath() + "/AUDIT_Backup";
                                File currentDB = new File(data, currentDBPath);
                                File backupDB = new File(path, backupDBPath);
                                if (currentDB.exists()) {
                                    @SuppressWarnings("resource")
                                    FileChannel src = new FileInputStream(currentDB).getChannel();
                                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();
                                }

                                File dir = new File(CommonString.BACKUP_FILE_PATH);
                                ArrayList<String> list = new ArrayList();
                                list = getFileNames(dir.listFiles());
                                if (list.size() > 0) {
                                    for (int i1 = 0; i1 < list.size(); i1++) {
                                        if (list.get(i1).contains("_AUDIT_BACKUP_")) {
                                            File originalFile = new File(CommonString.BACKUP_FILE_PATH + list.get(i1));
                                            uploadBackup(getActivity(), originalFile.getName(), "DBBackup");

                                        }
                                    }
                                }

                                Snackbar.make(rec, "Database Exported And Uploaded Successfully", Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AlertandMessages.showAlert((Activity) context, context.getString(R.string.errordatabase_not_exporting), true);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public ArrayList<String> getFileNames(File[] file) {
        ArrayList<String> arrayFiles = new ArrayList<String>();
        if (file.length > 0) {
            for (int i = 0; i < file.length; i++)
                arrayFiles.add(file[i].getName());
        }
        return arrayFiles;
    }

    private void uploadBackup(final Context context, String file_name, String folder_name) {
        RequestBody body1;
        final File originalFile = new File(CommonString.BACKUP_FILE_PATH + file_name);
        RequestBody photo = RequestBody.create(MediaType.parse("application/octet-stream"), originalFile);
        body1 = new MultipartBuilder().type(MultipartBuilder.FORM)
                .addFormDataPart("file", originalFile.getName(), photo)
                .addFormDataPart("Foldername", folder_name)
                .build();
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(CommonString.URLGORIMAG)
                .addConverterFactory(new StringConverterFactory())
                .build();
        PostApiForFile api = adapter.create(PostApiForFile.class);
        Call<String> call = api.getUploadImage(body1);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response) {
                String responseBody = response.body();
                if (responseBody != null && response.isSuccess()) {
                    if (response.body().contains(CommonString.KEY_SUCCESS)) {
                        originalFile.delete();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, originalFile.getName() + " not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;

    }

}
