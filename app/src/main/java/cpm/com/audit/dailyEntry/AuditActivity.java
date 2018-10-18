package cpm.com.audit.dailyEntry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.audit.R;
import cpm.com.audit.database.RBGTDatabase;
import cpm.com.audit.getterSetter.AuditDataGetterSetter;
import cpm.com.audit.getterSetter.AuditQuestionMaster;
import cpm.com.audit.getterSetter.JourneyPlan;
import cpm.com.audit.utilities.AlertandMessages;
import cpm.com.audit.utilities.CommonFunctions;
import cpm.com.audit.utilities.CommonString;

public class AuditActivity extends AppCompatActivity {

    List<Integer> checkHeaderArray = new ArrayList<>();
    List<Integer> checkGroupArray = new ArrayList<>();
    boolean checkflag = true;
    private SharedPreferences preferences;
    String pathforcheck = "", _path = "", str, img_str = ""; //img_str2 = "";
    String visit_date, username, intime, date, keyAccount_id;
    RBGTDatabase db;
    ArrayList<AuditDataGetterSetter> question_list, childListData;
    HashMap<AuditDataGetterSetter, ArrayList<AuditDataGetterSetter>> hashMapAnsListChildData;
    int child_position = -1;
    int header_position = -1;
    String error_msg = "";
    Toolbar toolbar;
    Context context;
    JourneyPlan jcpGetset;
    FloatingActionButton fab;
    ExpandableListView expandableListView;
    ExpandableListAdapter adapter;
    ArrayList<AuditQuestionMaster> listDataHeader;
    HashMap<AuditQuestionMaster, ArrayList<AuditDataGetterSetter>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");
        toolbar.setTitle("Audit - " + visit_date);
        hashMapAnsListChildData = new HashMap<>();
        childListData = new ArrayList<>();
        str = CommonString.FILE_PATH;
        expandableListView = (ExpandableListView) findViewById(R.id.list);

        db = new RBGTDatabase(context);
        db.open();

        //Intent data
        if (getIntent().getSerializableExtra(CommonString.TAG_OBJECT) != null) {
            jcpGetset = (JourneyPlan) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
        }
        //Header
        listDataHeader = db.getAuditQuestionCategoryData(jcpGetset);
        question_list = new ArrayList<>();
        listDataChild = new HashMap<>();

        for (int i = 0; i < listDataHeader.size(); i++) {
            //question_list = db.getAfterSaveAuditQuestionAnswerData(jcpGetset.getStoreId().toString(), jcpGetset.getCampaignId().toString());
            if (question_list.size() == 0) {
                question_list = db.getAuditCategoryWise(jcpGetset, listDataHeader.get(i).getQuestionCategoryId());
                if (question_list.size() > 0) {
                    String select = getString(R.string.title_activity_select_dropdown);
                    // Adding child data
                    for (int j = 0; j < question_list.size(); j++) {
                        childListData = db.getAuditAnswerData(question_list.get(j), select);
                        ArrayList<AuditDataGetterSetter> answerList = new ArrayList<>();
                        for (int k = 0; k < childListData.size(); k++) {
                            answerList.add(childListData.get(k));
                        }
                        hashMapAnsListChildData.put(question_list.get(j), answerList); // Header, Child data
                    }
                }
            }
            listDataChild.put(listDataHeader.get(i), question_list);
        }

        adapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
        expandableListView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableListView.clearFocus();
                // questionAdapter.notifyDataSetChanged();
                if (validateData(listDataHeader, listDataChild)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.title_activity_Want_save)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (db.saveAuditQuestionAnswerData(question_list, jcpGetset) > 0) {
                                        finish();
                                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    } else {
                                        AlertandMessages.showToastMsg(context, "Error in data saving");
                                    }
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Snackbar.make(expandableListView, error_msg, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        expandableListView.clearFocus();

/*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            // hidding fab button when scrolling layout
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy != 0) {
                    if (dy < 0) {
                        fab.show();
                    } else if (dy > 0) {
                        fab.hide();
                    }
                } else {
                    fab.show();
                }
            }
        });
*/


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Parinaam");
            builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            finish();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            android.app.AlertDialog alert = builder.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Parinaam");
        builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private ArrayList<AuditQuestionMaster> listDataHeader; // header titles
        private HashMap<AuditQuestionMaster, ArrayList<AuditDataGetterSetter>> listDataChild;

        public ExpandableListAdapter(Context context, ArrayList<AuditQuestionMaster> listDataHeader, HashMap<AuditQuestionMaster, ArrayList<AuditDataGetterSetter>> listDataChild) {
            this._context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listDataChild;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @SuppressLint("NewApi")
        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            AuditDataGetterSetter checkList = (AuditDataGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.audit_question_list_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.data = checkList;
            holder.txt_question.setText(holder.data.getQUESTION());

            final ArrayList<AuditDataGetterSetter> ans_list = hashMapAnsListChildData.get(holder.data);

            if (holder.data.getANSWER_TYPE().equalsIgnoreCase("List_Single_Choice") || holder.data.getANSWER_TYPE().equalsIgnoreCase("List_Multi_Choice")) {
                holder.sp_auditAnswer.setVisibility(View.VISIBLE);
                holder.edt_answer.setVisibility(View.GONE);
            } else {
                holder.edt_answer.setVisibility(View.VISIBLE);
                holder.sp_auditAnswer.setVisibility(View.GONE);
            }

            if (ans_list.size() > 0) {
                holder.sp_auditAnswer.setAdapter(new AnswerSpinnerAdapter(context, R.layout.custom_spinner_item, ans_list));
            }

            final ArrayList<AuditDataGetterSetter> finalAns_list = ans_list;
            final ViewHolder finalHolder = holder;
            holder.sp_auditAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    AuditDataGetterSetter ans = finalAns_list.get(position);
                    finalHolder.data.setANSWER_ID(ans.getANSWER_ID());
                    finalHolder.data.setANSWER(ans.getANSWER());
                    finalHolder.data.setCAMERA_ALLOW(ans.getCAMERA_ALLOW());
                    if (finalHolder.data.getCAMERA_ALLOW().equals("1")) {
                        finalHolder.parent_cam_layout.setVisibility(View.VISIBLE);
                    } else {
                        finalHolder.parent_cam_layout.setVisibility(View.GONE);
                        if (finalHolder.data.getCAM_IMAGE() != null && finalHolder.data.getCAM_IMAGE().equals("")) {
                            if (new File(str + finalHolder.data.getCAM_IMAGE()).exists()) {
                                new File(str + finalHolder.data.getCAM_IMAGE()).delete();
                            }
                        }
                        finalHolder.data.setCAM_IMAGE("");
                        finalHolder.img_cam.setBackgroundResource(R.mipmap.camera_pink);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder.img_cam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathforcheck = jcpGetset.getStoreId().toString() + "_" + jcpGetset.getCampaignId().toString() + "_AuditAnsPic-" + date.replace("/", "") + CommonFunctions.getCurrentTimeHHMMSS().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + pathforcheck;
                    CommonFunctions.startAnncaCameraActivity(context, _path, null, false);
                    child_position = childPosition;
                    //startCameraActivity(0);
                }
            });

            final ViewHolder finalHolder1 = holder;
            holder.edt_answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        finalHolder1.data.setANSWER_ID("0");
                        finalHolder1.data.setANSWER(((EditText) v).getText().toString());
                        ans_list.get(0).setANSWER(((EditText) v).getText().toString());
                    }
                }
            });

            if (ans_list.size() > 0) {
                for (int i = 0; i < ans_list.size(); i++) {
                    if ((holder.data.getANSWER_TYPE().equalsIgnoreCase("List_Single_Choice") || holder.data.getANSWER_TYPE().equalsIgnoreCase("List_Multi_Choice")) && ans_list.get(i).getANSWER_ID().equals(holder.data.getANSWER_ID())) {
                        holder.sp_auditAnswer.setSelection(i);
                        break;
                    } else {
                        //holder.edt_answer.setText(ans_list.get(i).getANSWER());
                        holder.edt_answer.setText(holder.data.getANSWER());
                    }
                }
            } else {
                holder.edt_answer.setText("");
            }


            if (!img_str.equals("")) {
                if (child_position == childPosition) {
                    holder.data.setCAM_IMAGE(img_str);
                    img_str = "";
                    child_position = -1;
                }
            }

            if (holder.data.getCAM_IMAGE().equals("")) {
                holder.img_cam.setBackgroundResource(R.mipmap.camera_pink);
            } else {
                holder.img_cam.setBackgroundResource(R.mipmap.camera_green);
            }

            if (!checkflag) {
                if (checkGroupArray.contains(groupPosition) && checkHeaderArray.contains(childPosition)) {
                    holder.card_view.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.card_view.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_header_expandable_window, parent, false);
            }
            final AuditQuestionMaster headerTitle = (AuditQuestionMaster) getGroup(groupPosition);

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.txt_header);
            CardView cardView = (CardView) convertView.findViewById(R.id.cardview_exists);
            lblListHeader.setText(headerTitle.getQuestionCategory());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableListView.isGroupExpanded(groupPosition)) {
                        expandableListView.collapseGroup(groupPosition);
                    } else {
                        expandableListView.expandGroup(groupPosition);
                    }
                }
            });

            if (!checkflag) {
                if (checkGroupArray.contains(groupPosition)) {
                    cardView.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    cardView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView txt_question;
            public final Spinner sp_auditAnswer;
            public final EditText edt_answer;
            public final ImageView img_cam;
            public final RelativeLayout parent_cam_layout;
            CardView card_view;
            AuditDataGetterSetter data;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                txt_question = (TextView) view.findViewById(R.id.txt_question);
                sp_auditAnswer = (Spinner) view.findViewById(R.id.sp_auditAnswer);
                card_view = (CardView) view.findViewById(R.id.card_view);
                edt_answer = (EditText) view.findViewById(R.id.edt_answer);
                img_cam = (ImageView) view.findViewById(R.id.img_cam);
                parent_cam_layout = (RelativeLayout) view.findViewById(R.id.parent_cam_layout);
            }
        }

    }


    public class AnswerSpinnerAdapter extends ArrayAdapter<AuditDataGetterSetter> {
        List<AuditDataGetterSetter> list;
        Context context;
        int resourceId;

        public AnswerSpinnerAdapter(Context context, int resourceId, ArrayList<AuditDataGetterSetter> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            AuditDataGetterSetter cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.tv_text);
            txt_spinner.setText(list.get(position).getANSWER());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            AuditDataGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.tv_text);
            txt_spinner.setText(cm.getANSWER());

            return view;
        }
    }

    boolean validateData(ArrayList<AuditQuestionMaster> listDataHeader, HashMap<AuditQuestionMaster, ArrayList<AuditDataGetterSetter>> listDataChild) {
        //boolean flag = true;
        checkHeaderArray.clear();
        checkGroupArray.clear();
        loop:
        for (int j = 0; j < listDataHeader.size(); j++) {
            ArrayList<AuditDataGetterSetter> data = listDataChild.get(listDataHeader.get(j));
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getANSWER_TYPE().equalsIgnoreCase("Text")) {
                    if (data.get(i).getANSWER() == null || data.get(i).getANSWER().equalsIgnoreCase("")) {
                        error_msg = getString(R.string.pls_answer_all_qns);
                        checkflag = false;
                    } else {
                        checkflag = true;
                    }
                } else {
                    if (data.get(i).getANSWER_ID().equalsIgnoreCase("0")) {
                        error_msg = getString(R.string.pls_answer_all_qns);
                        checkflag = false;
                    } else if (data.get(i).getCAMERA_ALLOW().equals("1") && data.get(i).getCAM_IMAGE().equals("")) {
                        error_msg = getString(R.string.click_image);
                        checkflag = false;
                    } else {
                        checkflag = true;
                    }
                }
                if (checkflag == false) {
                    if (!checkHeaderArray.contains(i)) {
                        checkHeaderArray.add(i);
                        checkGroupArray.add(j);
                    }
                    break loop;
                }
            }
        }


        return checkflag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;
            case -1:
                if (pathforcheck != null && !pathforcheck.equals("")) {
                    if (new File(str + pathforcheck).exists()) {
                        //if (requestCode == 0) {
                        img_str = pathforcheck;
                        // }
                         /*else {
                            img_str2 = pathforcheck;
                        }*/
                        pathforcheck = "";
                        adapter.notifyDataSetChanged();
                    }
                }

        }
    }
}
