package cpm.com.audit.dailyEntry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cpm.com.audit.R;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.delegates.CoverageBean;
import cpm.com.audit.download.DownloadActivity;
import cpm.com.audit.getterSetter.JourneyPlan;
import cpm.com.audit.utilities.AlertandMessages;
import cpm.com.audit.utilities.CommonString;

public class CampaignListActivity extends AppCompatActivity {

    private Context context;
    private String userId, rightname;
    private ArrayList<CoverageBean> coverage = new ArrayList<>();
    private String date;
    private RBGTDatabase db;
    private ValueAdapter adapter;
    private RecyclerView recyclerView;
    private boolean result_flag = false;
    private FloatingActionButton fab;
    SharedPreferences preferences;
    LinearLayout linearlay, campaignlist_ll;
    TextView txt_label;
    ArrayList<JourneyPlan> campaignList;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_list);
        declaration();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Parinaam");
                builder.setMessage(getResources().getString(R.string.want_download_data)).setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
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
            }
        });

    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setLitData();
    }

    private void setLitData() {

        campaignList = db.getCampaignList(date);
        coverage = db.getCoverageData(date);
        if (campaignList.size() > 0) {
            adapter = new ValueAdapter(getApplicationContext(), campaignList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setVisibility(View.VISIBLE);
            campaignlist_ll.setVisibility(View.VISIBLE);
            linearlay.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);

        } else {
            recyclerView.setVisibility(View.GONE);
            campaignlist_ll.setVisibility(View.GONE);
            linearlay.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
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

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {

        private LayoutInflater inflator;
        List<JourneyPlan> data = Collections.emptyList();

        public ValueAdapter(Context context, List<JourneyPlan> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.storeviewlist, parent, false);
            return new MyViewHolder(view);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder viewHolder, final int position) {

            final JourneyPlan current = data.get(position);
            viewHolder.txt.setText(current.getCampaign() + " - (" + current.getCampaignId() + ")");
            viewHolder.txt_add.setVisibility(View.INVISIBLE);

            if (isStoreCheckinByCampaignId(current)) {
                viewHolder.imageview.setVisibility(View.INVISIBLE);
                viewHolder.imageview.setVisibility(View.INVISIBLE);
                viewHolder.Cardbtn.setCardBackgroundColor(getResources().getColor(R.color.green));
            } else {
                viewHolder.Cardbtn.setCardBackgroundColor(getResources().getColor(current.getColourCode()));
                viewHolder.imageview.setVisibility(View.INVISIBLE);
            }

            viewHolder.relativelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean entry_flag = false;
                    String msg = "";

                    //region Check for Checkin in JCP stores
                    boolean entry_flag_from_jcp = true;

                    if (viewHolder.Cardbtn.getCardBackgroundColor() != getResources().getColorStateList(R.color.green)) {
                        for (int j = 0; j < campaignList.size(); j++) {
                            if (current.getCampaignId() != campaignList.get(j).getCampaignId()) {
                                if (isStoreCheckinByCampaignId(campaignList.get(j))) {
                                    entry_flag_from_jcp = false;
                                    msg = getResources().getString(R.string.title_store_list_checkout_current);
                                    break;
                                }
                            }
                        }
                    }

                    //endregion
                    if (entry_flag_from_jcp) {
                        entry_flag = true;
                    }
                    if (entry_flag) {
                        startActivity(intent.putExtra(CommonString.TAG_OBJECT, current));
                    } else {
                        AlertandMessages.showSnackbarMsg(v, msg);
                    }


                }
            });

        }

        @SuppressWarnings("deprecation")
        public boolean CheckNetAvailability() {
            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState() == NetworkInfo.State.CONNECTED
                    || connectivityManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                // we are connected to a network
                connected = true;
            }
            return connected;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt, txt_add;
            RelativeLayout relativelayout;
            ImageView imageview;
            CardView Cardbtn;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.storelistviewxml_storename);
                txt_add = (TextView) itemView.findViewById(R.id.storelistviewxml_storeaddress);
                relativelayout = (RelativeLayout) itemView.findViewById(R.id.storenamelistview_layout);
                //imageview = (ImageView) itemView.findViewById(R.id.imageView2);
                imageview = (ImageView) itemView.findViewById(R.id.storelistviewxml_storeico);
                Cardbtn = (CardView) itemView.findViewById(R.id.card_view);
            }
        }

    }

    public boolean isStoreCheckinByCampaignId(JourneyPlan current) {
        boolean isValid = false;
        db.open();
        ArrayList<JourneyPlan> storeList = db.getStoreListByCampaignId(current);
        for (int i = 0; i < storeList.size(); i++) {
            if (storeList.get(i).getUploadStatus().equalsIgnoreCase(CommonString.KEY_CHECK_IN)) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    private void declaration() {
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        date = preferences.getString(CommonString.KEY_DATE, null);
        linearlay = (LinearLayout) findViewById(R.id.no_data_lay_ll);
        campaignlist_ll = (LinearLayout) findViewById(R.id.storelist_ll);
        recyclerView = (RecyclerView) findViewById(R.id.drawer_layout_recycle);
        userId = preferences.getString(CommonString.KEY_USERNAME, "");
        rightname = preferences.getString(CommonString.KEY_RIGHTNAME, "");
        txt_label = (TextView) findViewById(R.id.txt_label);
        context = this;
        db = new RBGTDatabase(context);
        db.open();
        getSupportActionBar().setTitle("");
        txt_label.setText("Campaign List - " + date);
        intent = new Intent(context, StoreListActivity.class);

    }
}
