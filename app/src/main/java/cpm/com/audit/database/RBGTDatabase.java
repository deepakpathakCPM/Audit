package cpm.com.audit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cpm.com.audit.R;
import cpm.com.audit.delegates.CoverageBean;
import cpm.com.audit.getterSetter.AuditAnswerMaster;
import cpm.com.audit.getterSetter.AuditAnswerMasterGetterSetter;
import cpm.com.audit.getterSetter.AuditDataGetterSetter;
import cpm.com.audit.getterSetter.AuditQuestionMaster;
import cpm.com.audit.getterSetter.AuditQuestionMasterGetterSetter;
import cpm.com.audit.getterSetter.GeotaggingBeans;
import cpm.com.audit.getterSetter.JCPGetterSetter;
import cpm.com.audit.getterSetter.JourneyPlan;
import cpm.com.audit.getterSetter.NonWorkingReason;
import cpm.com.audit.getterSetter.NonWorkingReasonGetterSetter;
import cpm.com.audit.utilities.CommonString;

public class RBGTDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RB_GT_DB";
    public static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    Context context;

    public RBGTDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void open() {
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //jeevan
            db.execSQL(CommonString.CREATE_TABLE_COVERAGE_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STORE_GEOTAGGING);
            db.execSQL(CommonString.CREATE_TABLE_AUDIT_DATA_SAVE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int createtable(String sqltext) {
        try {
            db.execSQL(sqltext);
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }


    public void deleteTableWithStoreID(String storeid, String campaignId) {
        db.delete(CommonString.TABLE_COVERAGE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "' and " + CommonString.KEY_CAMPAIGN_ID + " = '" + campaignId + "'", null);
        db.delete(CommonString.TABLE_AUDIT_DATA_SAVE, CommonString.KEY_STORE_CD + "='" + storeid + "' and " + CommonString.KEY_CAMPAIGN_ID + " = '" + campaignId + "'", null);
    }

    public void updateStatus(String id, String campaignID, String status) {
        ContentValues values = new ContentValues();
        try {
            values.put("GEO_TAG", status);
            db.update(CommonString.KEY_JOURNEY_PLAN, values, CommonString.KEY_STORE_ID + "='" + id + "' and " + CommonString.KEY_CAMPAIGN_ID + "='" + campaignID + "'", null);
        } catch (Exception ex) {
        }
    }

    public long updateInsertedGeoTagStatus(String id, String status) {
        ContentValues values = new ContentValues();
        try {
            values.put("GEO_TAG", status);
            values.put("STATUS", status);
            return db.update(CommonString.TABLE_STORE_GEOTAGGING, values, CommonString.KEY_STORE_ID + "='" + id + "'", null);
        } catch (Exception ex) {
            return 0;
        }
    }

    public ArrayList<CoverageBean> getCoverageDataPrevious(String visitdate) {

        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where "
                            + CommonString.KEY_VISIT_DATE + " <> '" + visitdate + "'",
                    null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();

                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setCampaignId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CAMPAIGN_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    //sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));

                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<CoverageBean> getCoverageWithStoreIDAndVisitDate_Data(String store_id, String visitdate) {

        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_STORE_ID + "='" + store_id + "' AND " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();

                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setCampaignId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CAMPAIGN_ID)));
                    sb.setVisitDate((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE))))));
                    sb.setLatitude(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)))));
                    sb.setLongitude(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)))));
                    sb.setImage((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE))))));
                    sb.setReasonid((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID))))));
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)) == null) {
                        sb.setRemark("");
                    } else {
                        sb.setRemark((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK))))));
                    }
                    sb.setReason((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON))))));

                   /* sb.setInTime(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_IN_TIME)))));
                    sb.setOutTime(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_OUT_TIME)))));*/
                    sb.setMID(Integer.parseInt(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))))));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }
        return list;
    }

    public long updateCheckoutStatus(String id, String campaignId, String status, String table) {
        ContentValues values = new ContentValues();
        try {
            values.put("Upload_Status", status);
            return db.update(table, values, "Store_Id " + " = '" + id + "' and Campaign_Id = '" + campaignId + "'", null);
        } catch (Exception ex) {
            Log.e("Exception", " Journey_Plan" + ex.toString());
            return 0;
        }
    }

    public void updateStoreStatus(String storeid, String campaignId, String visitdate, String status) {
        try {
            ContentValues values = new ContentValues();
            values.put("Upload_Status", status);
            db.update("Journey_Plan", values, "Store_Id ='" + storeid + "' AND Visit_Date ='" + visitdate + "' and Campaign_Id = '" + campaignId + "'", null);
        } catch (Exception e) {

        }
    }

    public long InsertCoverageData(CoverageBean data) {
        db.delete(CommonString.TABLE_COVERAGE_DATA, "STORE_ID" + "='" + data.getStoreId() + "' AND CAMPAIGN_ID = '" + data.getCampaignId() + "' AND VISIT_DATE='" + data.getVisitDate() + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            values.put(CommonString.KEY_STORE_ID, data.getStoreId());
            values.put(CommonString.KEY_CAMPAIGN_ID, data.getCampaignId());
            values.put(CommonString.KEY_USER_ID, data.getUserId());
            values.put(CommonString.KEY_VISIT_DATE, data.getVisitDate());
            values.put(CommonString.KEY_LATITUDE, data.getLatitude());
            values.put(CommonString.KEY_LONGITUDE, data.getLongitude());
            values.put(CommonString.KEY_IMAGE, data.getImage());
            values.put(CommonString.KEY_COVERAGE_REMARK, data.getRemark());
            values.put(CommonString.KEY_REASON_ID, data.getReasonid());
            values.put(CommonString.KEY_REASON, data.getReason());
            values.put(CommonString.KEY_CHECKOUT_IMAGE, data.getCkeckout_image());
            l = db.insert(CommonString.TABLE_COVERAGE_DATA, null, values);
        } catch (Exception ex) {
            Log.d("Database Exception while Insert Closes Data ", ex.toString());
        }
        return l;
    }

    public long updateStoreStatusOnLeave(String storeid, String campaignId, String visitdate, String status) {
        long id = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("UPLOAD_STATUS", status);
            id = db.update(CommonString.TABLE_Journey_Plan, values, CommonString.KEY_STORE_ID + "='" + storeid + "' AND "
                    + CommonString.KEY_VISIT_DATE + "='" + visitdate
                    + "' AND " + CommonString.KEY_CAMPAIGN_ID + " = '" + campaignId + "'", null);
            return id;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean insertJCPData(JCPGetterSetter data) {
        db.delete("Journey_Plan", null, null);
        List<JourneyPlan> jcpList = data.getJourneyPlan();
        ContentValues values = new ContentValues();
        try {
            if (jcpList.size() == 0) {
                return false;
            }
            for (int i = 0; i < jcpList.size(); i++) {

                values.put("Campaign_Id", jcpList.get(i).getCampaignId());
                values.put("Campaign", jcpList.get(i).getCampaign());
                values.put("Store_Id", jcpList.get(i).getStoreId());
                values.put("Visit_Date", jcpList.get(i).getVisitDate());
                values.put("Distributor", jcpList.get(i).getDistributor());
                values.put("Store_Name", jcpList.get(i).getStoreName());
                values.put("Address", jcpList.get(i).getAddress());
                values.put("Pincode", jcpList.get(i).getPincode());
                values.put("City", jcpList.get(i).getCity());
                values.put("Store_Type", jcpList.get(i).getStoreType());
                values.put("Store_Category", jcpList.get(i).getStoreCategory());
                values.put("Classification", jcpList.get(i).getClassification());
                values.put("Reason_Id", jcpList.get(i).getReasonId());
                values.put("Upload_Status", jcpList.get(i).getUploadStatus());
                values.put("Geo_Tag", jcpList.get(i).getGeoTag());

                long id = db.insert("Journey_Plan", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Exception in Jcp", ex.toString());
            return false;
        }
    }

    public boolean insertNonWorkingData(NonWorkingReasonGetterSetter nonWorkingdata) {
        db.delete("Non_Working_Reason", null, null);
        ContentValues values = new ContentValues();
        List<NonWorkingReason> data = nonWorkingdata.getNonWorkingReason();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Reason_Id", data.get(i).getReasonId());
                values.put("Reason", data.get(i).getReason());
                values.put("Entry_Allow", data.get(i).getEntryAllow());
                values.put("Image_Allow", data.get(i).getImageAllow());
                values.put("GPS_Mandatory", data.get(i).getGPSMandatory());

                long id = db.insert("Non_Working_Reason", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertAuditQuestionMasterData(AuditQuestionMasterGetterSetter auditQuestionMasterGetSet) {
        db.delete("Audit_Question_Master", null, null);
        ContentValues values = new ContentValues();
        List<AuditQuestionMaster> data = auditQuestionMasterGetSet.getAuditQuestionMaster();
        try {
            if (data.size() == 0) {
                return false;
            }
            for (int i = 0; i < data.size(); i++) {

                values.put("Question_Id", data.get(i).getQuestionId());
                values.put("Question", data.get(i).getQuestion());
                values.put("Question_Category_Id", data.get(i).getQuestionCategoryId());
                values.put("Question_Category", data.get(i).getQuestionCategory());
                values.put("Question_Type", data.get(i).getQuestionType());
                values.put("Campaign_Id", data.get(i).getCampaignId());

                long id = db.insert("Audit_Question_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertAuditAnswerMasterData(AuditAnswerMasterGetterSetter auditQuestionMasterGetSet) {
        db.delete("Audit_Answer_Master", null, null);
        ContentValues values = new ContentValues();
        List<AuditAnswerMaster> data = auditQuestionMasterGetSet.getAuditAnswerMaster();
        try {
            if (data.size() == 0) {
                return false;
            }
            for (int i = 0; i < data.size(); i++) {

                values.put("Answer_Id", data.get(i).getAnswerId());
                values.put("Answer", data.get(i).getAnswer());
                values.put("Question_Id", data.get(i).getQuestionId());
                values.put("Right_Answer", data.get(i).getRightAnswer());
                values.put("Image_Allow", data.get(i).getImageAllow());

                long id = db.insert("Audit_Answer_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean isCoverageDataFilled(String visit_date) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM COVERAGE_DATA " + "where " + CommonString.KEY_VISIT_DATE + "<>'" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }

    public void deletePreviousUploadedData(String visit_date) {
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from COVERAGE_DATA where VISIT_DATE < '" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
                    db.delete(CommonString.TABLE_STORE_GEOTAGGING, null, null);
                    db.delete(CommonString.TABLE_AUDIT_DATA_SAVE, null, null);
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!", e.toString());
        }
    }

    public JourneyPlan getSpecificStoreDataPrevious(String date, String store_id, String campaignId) {
        JourneyPlan sb = new JourneyPlan();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from Journey_Plan  " +
                    "where Visit_Date <> '" + date + "' AND Store_Id='" + store_id + "' AND Campaign_Id = '" + campaignId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    sb.setCampaignId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign_Id"))));
                    sb.setCampaign((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign"))));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setDistributor((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Name")));
                    sb.setAddress(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));
                    sb.setCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City")));
                    sb.setStoreType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type")));
                    sb.setStoreCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category")));
                    sb.setClassification(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification")));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }

        return sb;
    }

    public ArrayList<CoverageBean> getSpecificCoverageData(String visitdate, String store_cd, String campaignId) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "' AND " +
                    CommonString.KEY_STORE_ID + "='" + store_cd + "' and " + CommonString.KEY_CAMPAIGN_ID + " = '" + campaignId + "'", null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setCampaignId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CAMPAIGN_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));

                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }


    public ArrayList<JourneyPlan> getSpecificStoreData(String store_cd, String campaignId) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from Journey_Plan  " + "where Store_Id = '" + store_cd + "' and Campaign_Id = '" + campaignId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();

                    sb.setCampaignId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign_Id"))));
                    sb.setCampaign((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign"))));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setDistributor((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Name")));
                    sb.setAddress(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));
                    sb.setCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City")));
                    sb.setStoreType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type")));
                    sb.setStoreCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category")));
                    sb.setClassification(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification")));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }

    public ArrayList<NonWorkingReason> getNonWorkingEntryAllowData() {

        ArrayList<NonWorkingReason> list = new ArrayList<NonWorkingReason>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Non_Working_Reason WHERE Entry_Allow=1", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NonWorkingReason sb = new NonWorkingReason();

                    sb.setReasonId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));
                    sb.setEntryAllow("1".equalsIgnoreCase(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"))));
                    sb.setImageAllow("1".equalsIgnoreCase(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow"))));
                    sb.setGPSMandatory("1".equalsIgnoreCase(dbcursor.getString(dbcursor.getColumnIndexOrThrow("GPS_Mandatory"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            return list;
        }
        return list;
    }

    public ArrayList<NonWorkingReason> getNonWorkingData() {
        ArrayList<NonWorkingReason> list = new ArrayList<NonWorkingReason>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Non_Working_Reason", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NonWorkingReason sb = new NonWorkingReason();

                    sb.setReasonId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));
                    sb.setEntryAllow("1".equalsIgnoreCase(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"))));
                    sb.setImageAllow("1".equalsIgnoreCase(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow"))));
                    sb.setGPSMandatory("1".equalsIgnoreCase(dbcursor.getString(dbcursor.getColumnIndexOrThrow("GPS_Mandatory"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    public ArrayList<GeotaggingBeans> getinsertGeotaggingData(String storeid, String status) {
        ArrayList<GeotaggingBeans> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from " + CommonString.TABLE_STORE_GEOTAGGING + "" +
                    " where " + CommonString.KEY_STORE_ID + " ='" + storeid + "' and " + CommonString.KEY_STATUS + " = '" + status + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    GeotaggingBeans geoTag = new GeotaggingBeans();
                    geoTag.setStoreid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    geoTag.setLatitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE))));
                    geoTag.setLongitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE))));
                    geoTag.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FRONT_IMAGE")));
                    list.add(geoTag);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;

    }

    public void deleteSpecificStoreData(String storeid) {
        db.delete(CommonString.TABLE_COVERAGE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STORE_GEOTAGGING, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_AUDIT_DATA_SAVE, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);

    }

    public ArrayList<JourneyPlan> getStoreData(String date) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * FROM Journey_Plan  " + "WHERE Visit_Date ='" + date + "' ORDER BY Store_Name", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();

                    sb.setCampaignId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign_Id"))));
                    sb.setCampaign((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign"))));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setDistributor((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Name")));
                    sb.setAddress(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));
                    sb.setCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City")));
                    sb.setStoreType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type")));
                    sb.setStoreCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category")));
                    sb.setClassification(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification")));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setColourCode(R.color.lightskyblue);

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }

    public ArrayList<JourneyPlan> getCampaignList(String date) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT distinct Campaign_Id,Campaign,Visit_Date FROM Journey_Plan " + "WHERE Visit_Date ='" + date + "' ORDER BY Campaign_Id", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();

                    sb.setCampaignId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign_Id"))));
                    sb.setCampaign((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setColourCode(R.color.lightskyblue);

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }

    public ArrayList<CoverageBean> getCoverageData(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_COVERAGE_DATA + " WHERE " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setCampaignId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CAMPAIGN_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }

    public ArrayList<CoverageBean> getCoverageDataByCampaignIDAndStoreId(JourneyPlan jcpgetset) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_COVERAGE_DATA + " WHERE " + CommonString.KEY_VISIT_DATE + " = '" + jcpgetset.getVisitDate() + "' and " + CommonString.KEY_CAMPAIGN_ID + " = " + jcpgetset.getCampaignId() + " and " + CommonString.KEY_STORE_ID + " = " + jcpgetset.getStoreId() + "", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setCampaignId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CAMPAIGN_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }


    public ArrayList<CoverageBean> getCoverageDataByCampaignID(JourneyPlan jcpgetset) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_COVERAGE_DATA + " WHERE " + CommonString.KEY_VISIT_DATE + " = '" + jcpgetset.getVisitDate() + "' and " + CommonString.KEY_CAMPAIGN_ID + " = " + jcpgetset.getCampaignId() + "", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setCampaignId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CAMPAIGN_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }


    //get inserted Audit data
    public ArrayList<AuditDataGetterSetter> getAfterSaveAuditQuestionAnswerData(String storeid, String campaignId) {
        ArrayList<AuditDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * " + "From " + CommonString.TABLE_AUDIT_DATA_SAVE
                    + " where STORE_CD= " + storeid + " AND CAMPAIGN_ID =" + campaignId + "", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditDataGetterSetter sb = new AuditDataGetterSetter();

                    sb.setQUESTION_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_ID")));
                    sb.setQUESTION(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION")));
                    sb.setANSWER(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER")));
                    sb.setANSWER_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_ID")));
                    sb.setANSWER_TYPE(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_TYPE")));
                    sb.setCATEGORY_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAMPAIGN_ID")));
                    sb.setCAM_IMAGE(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANS_CAM_IMAGE")));
                    sb.setCAMERA_ALLOW(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CAMERA_ALLOW")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }

        return list;
    }

    //Category wise Audit
    public ArrayList<AuditDataGetterSetter> getAuditCategoryWise(JourneyPlan jcpGetset, int categoryID) {
        Cursor cursordata = null;

        ArrayList<AuditDataGetterSetter> auditData = new ArrayList<>();
        try {

            //cursordata = db.rawQuery("Select DISTINCT QUESTION ,QUESTION_ID, ANSWER_TYPE, KEYACCOUNT_ID from ADDITIONAL_QUESTION  " + "where CATEGORY_ID ='" + categoryId + "' AND STORETYPE_ID='"+ store_type_id +"'", null);

          /*  cursordata = db.rawQuery("Select 2 as QUESTION_ID,1 as CULTURE_ID,6 as STORETYPE_ID," +
                    "3 as CATEGORY_ID,'Is Panadol SKU’s visible in front of the counter or back of the counter?' as QUESTION," +
                    " 'DROPDOWN' as ANSWER_TYPE,3 as ANSWER_ID,'Front of the Counter' as ANSWER,1 as CAMERA_ALLOW" +
                    " union " +
                    "Select 2 as QUESTION_ID,1 as CULTURE_ID,6 as STORETYPE_ID," +
                    "3 as CATEGORY_ID,'Is Panadol SKU’s visible in front of the counter or back of the counter?' as QUESTION," +
                    "'EDITTEXT' as ANSWER_TYPE,4 as ANSWER_ID,'Back of the Counter' as ANSWER,0 as CAMERA_ALLOW" +
                    " union " +
                    "Select 2 as QUESTION_ID,1 as CULTURE_ID,6 as STORETYPE_ID," +
                    "3 as CATEGORY_ID,'Is Panadol SKU’s visible in front of the counter or back of the counter?' as QUESTION," +
                    "'DROPDOWN' as ANSWER_TYPE,5 as ANSWER_ID,'Not Available' as ANSWER,0 as CAMERA_ALLOW", null);*/


            cursordata = db.rawQuery("Select * from Audit_Question_Master where Campaign_Id = " + jcpGetset.getCampaignId() + " and Question_Category_Id = " + categoryID + "", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    AuditDataGetterSetter sb = new AuditDataGetterSetter();

                    sb.setQUESTION(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("Question")));
                    sb.setQUESTION_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("Question_Id")));
                    sb.setANSWER_TYPE(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("Question_Type")));
                    sb.setKEYACCOUNT_ID("0");

                    auditData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();
            }

        } catch (Exception ex) {
            Log.d("Exception ", " in ADDITIONAL_QUESTION " + ex.toString());
        }
        return auditData;
    }

    public ArrayList<AuditQuestionMaster> getAuditQuestionCategoryData(JourneyPlan jcpGetset) {
        Cursor cursordata = null;
        ArrayList<AuditQuestionMaster> auditData = new ArrayList<>();
        try {
            cursordata = db.rawQuery("Select Distinct Question_Category_Id,Question_Category from Audit_Question_Master where Campaign_Id = " + jcpGetset.getCampaignId() + "", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    AuditQuestionMaster sb = new AuditQuestionMaster();

                    sb.setQuestionCategoryId(cursordata.getInt(cursordata.getColumnIndexOrThrow("Question_Category_Id")));
                    sb.setQuestionCategory(cursordata.getString(cursordata.getColumnIndexOrThrow("Question_Category")));

                    auditData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();
            }

        } catch (Exception ex) {
            Log.d("Exception ", " in ADDITIONAL_QUESTION " + ex.toString());
        }
        return auditData;

    }


    public ArrayList<AuditDataGetterSetter> getAuditAnswerData(AuditDataGetterSetter auditGetSet, String select) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<AuditDataGetterSetter> list = new ArrayList<>();
        if (auditGetSet.getANSWER_TYPE().equalsIgnoreCase("List_Single_Choice")
                || auditGetSet.getANSWER_TYPE().equalsIgnoreCase("List_Multi_Choice")) {
            AuditDataGetterSetter sb1 = new AuditDataGetterSetter();
            sb1.setANSWER_ID("0");
            sb1.setANSWER(select);
            list.add(0, sb1);
        } else if (auditGetSet.getANSWER_TYPE().equalsIgnoreCase("Text")) {
            AuditDataGetterSetter sb1 = new AuditDataGetterSetter();
            sb1.setANSWER_ID("0");
            sb1.setANSWER("");
            list.add(0, sb1);
        }

        Cursor dbcursor = null;

        try {
            /*dbcursor = db.rawQuery("Select * from ADDITIONAL_QUESTION " +
                    "where QUESTION_ID='" + question_id + "' AND CATEGORY_ID ='" + categoryId + "' AND STORETYPE_ID='" + store_type_id + "'", null);*/

            /*dbcursor = db.rawQuery("Select 2 as QUESTION_ID,1 as CULTURE_ID,6 as STORETYPE_ID," +
                    "3 as CATEGORY_ID,'Is Panadol SKU’s visible in front of the counter or back of the counter?' as QUESTION," +
                    " 'DROPDOWN' as ANSWER_TYPE,3 as ANSWER_ID,'Front of the Counter' as ANSWER,1 as CAMERA_ALLOW,1 as NO_OF_CAMERA" +
                    " union " +
                    "Select 2 as QUESTION_ID,1 as CULTURE_ID,6 as STORETYPE_ID," +
                    "3 as CATEGORY_ID,'Is Panadol SKU’s visible in front of the counter or back of the counter?' as QUESTION," +
                    "'EDITTEXT' as ANSWER_TYPE,4 as ANSWER_ID,'' as ANSWER,0 as CAMERA_ALLOW,0 as NO_OF_CAMERA" +
                    " union " +
                    "Select 2 as QUESTION_ID,1 as CULTURE_ID,6 as STORETYPE_ID," +
                    "3 as CATEGORY_ID,'Is Panadol SKU’s visible in front of the counter or back of the counter?' as QUESTION," +
                    "'DROPDOWN' as ANSWER_TYPE,5 as ANSWER_ID,'Not Available' as ANSWER,0 as CAMERA_ALLOW,1 as NO_OF_CAMERA", null);*/

            dbcursor = db.rawQuery("Select * from Audit_Answer_Master where Question_Id = " + auditGetSet.getQUESTION_ID() + "", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditDataGetterSetter sb = new AuditDataGetterSetter();

                    sb.setANSWER_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer_Id")));
                    sb.setANSWER(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer")));
                    sb.setCAMERA_ALLOW(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow")));
                    sb.setRIGHT_ANSWER(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Right_Answer")));
                    //sb.setNO_OF_CAMERA(dbcursor.getString(dbcursor.getColumnIndexOrThrow("NO_OF_CAMERA")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " answer " + e.toString());
            return list;
        }
        Log.d("Fetching", " audit answer-->Stop<-");
        return list;
    }

    public ArrayList<JourneyPlan> getStoreListByCampaignId(JourneyPlan journeyPlan) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * FROM Journey_Plan  " + "WHERE Visit_Date ='" + journeyPlan.getVisitDate() + "' and Campaign_Id = " + journeyPlan.getCampaignId() + " ORDER BY Store_Name", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();

                    sb.setCampaignId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign_Id"))));
                    sb.setCampaign((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Campaign"))));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setDistributor((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Name")));
                    sb.setAddress(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));
                    sb.setCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City")));
                    sb.setStoreType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type")));
                    sb.setStoreCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category")));
                    sb.setClassification(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification")));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setColourCode(R.color.lightskyblue);

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }

    public long saveAuditQuestionAnswerData(ArrayList<AuditDataGetterSetter> questionAnswerList, JourneyPlan jcpGetset) {
        db.delete(CommonString.TABLE_AUDIT_DATA_SAVE, "STORE_CD" + " = " + jcpGetset.getStoreId() + " AND CAMPAIGN_ID = " + jcpGetset.getCampaignId() + "", null);
        long id = 0;
        ContentValues values = new ContentValues();
        try {

            for (int i = 0; i < questionAnswerList.size(); i++) {
                AuditDataGetterSetter data = questionAnswerList.get(i);

                values.put("STORE_CD", jcpGetset.getStoreId());
                values.put("QUESTION_ID", data.getQUESTION_ID());
                values.put("QUESTION", data.getQUESTION());
                values.put("ANSWER", data.getANSWER());
                values.put("ANSWER_ID", data.getANSWER_ID());
                values.put("ANSWER_TYPE", data.getANSWER_TYPE());
                values.put("CAMPAIGN_ID", jcpGetset.getCampaignId());
                values.put("ANS_CAM_IMAGE", data.getCAM_IMAGE());
                values.put("CAMERA_ALLOW", data.getCAMERA_ALLOW());

                id = db.insert(CommonString.TABLE_AUDIT_DATA_SAVE, null, values);
            }

            return id;
        } catch (Exception ex) {
            Log.d("Database ", "Exception while Insert Audit Data " + ex.toString());
            return 0;
        }
    }

}
