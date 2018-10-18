package cpm.com.audit.utilities;

import android.os.Environment;

/**
 * Created by yadavendras on 19-12-2016.
 */

public class CommonString {
    // preferenec keys
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_RIGHTNAME = "rightname";
    public static final String KEY_DATE = "date";
    public static final String KEY_DDATE = "DATE";
    public static final String KEY_YYYYMMDD_DATE = "yyyymmddDate";
    public static final String KEY_STOREVISITED_STATUS = "STOREVISITED_STATUS";
    public static final String KEY_NOTICE_BOARD = "NOTICE_BOARD";
    public static final String KEY_QUIZ_URL = "QUIZ_URL";
    public static final String KEY_PATH = "path";
    public static final String KEY_VERSION = "APP_VERSION";
    public static final String KEY_LOGIN_DATA = "login_data";
    public static final String KEY_CHECKOUT_IMAGE = "CHECKOUT_IMAGE";
    public static final String KEY_NOTICE_BOARD_LINK = "NOTICE_BOARD_LINK";
    public static final String KEY_STORE_ID = "STORE_ID";
    public static final String KEY_STORE_CD = "STORE_CD";
    public static final String KEY_CATEGORY_CD = "CATEGORY_CD";
    public static final String KEY_CATEGORY_IMAGE = "CATEGORY_IMAGE";
    public static final String KEY_VISIT_DATE = "VISIT_DATE";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_STORE_IN_TIME = "STORE_IN_TIME";
    public static final String KEY_LATITUDE = "LATITUDE";
    public static final String KEY_LONGITUDE = "LONGITUDE";
    public static final String KEY_REASON_ID = "REASON_ID";
    public static final String KEY_REASON = "REASON";
    public static final String KEY_COVERAGE_REMARK = "REMARK";
    public static final String KEY_IMAGE = "IMAGE";
    public static final String KEY_ID = "Id";
    public static final String KEY_IID = "KEY_ID";
    public static final String TAG_OBJECT = "OBJECT";
    public static final String KEY_DOWNLOAD_INDEX = "download_Index";

    //KEYS RELATED TO T2P COMPLIANCE

    public static final String KEY_COMMON_ID = "COMMON_ID";
    public static final String KEY_BRAND_ID = "BRAND_ID";
    public static final String KEY_JOURNEY_PLAN = "JOURNEY_PLAN";
    public static final String TABLE_Journey_Plan = "Journey_Plan";
    public static final String TABLE_Journey_Plan_DBSR_Saved = "Journey_Plan_DBSR_Saved";
    public static final String TABLE_Deviation_Journey_Plan = "Deviation_Journey_Plan";

    public static final String KEY_P = "P";
    public static final String KEY_D = "D";
    public static final String KEY_U = "U";
    public static final String KEY_C = "C";
    public static final String KEY_Y = "Y";
    public static final String KEY_N = "N";
    public static final String STORE_STATUS_LEAVE = "L";
    public static final String KEY_VALID = "Valid";
    public static final String KEY_CHECK_IN = "I";
    // webservice constants

    public static final String KEY_SUCCESS = "Success";
    public static final String KEY_FAILURE = "Failure";

    public static final int LOGIN_SERVICE = 1;
    public static final int DOWNLOAD_ALL_SERVICE = 2;
    public static final int COVERAGE_DETAIL = 3;
    public static final int UPLOADJCPDetail = 4;
    public static final int UPLOADJsonDetail = 5;
    public static final int COVERAGEStatusDetail = 6;
    public static final int CHECKOUTDetail = 7;
    public static final int DELETE_COVERAGE = 8;
    public static final int COVERAGE_NONWORKING = 9;
    public static final int COVERAGE_DETAIL_CLIENT = 10;
    public static final int CHECKOUTDetail_CLIENT = 11;

    public static String URL2 = "http://audit.parinaam.in/webservice/CPMAuditservice.svc/";
    public static String URL3 = "http://audit.parinaam.in/webservice/Imageupload.asmx/";
    public static String URLGORIMAG = "http://audit.parinaam.in/webservice/Imageupload.asmx/";

    public static final String BACKUP_FILE_PATH = Environment.getExternalStorageDirectory() + "/AUDIT_Backup/";
    public static final String MESSAGE_SERVER_ERROR = "Server Error.Please Access After Some Time";
    public static final String MESSAGE_CHANGED = "Invalid UserId Or Password";

    public static final String MESSAGE_INTERNET_NOT_AVALABLE = "No Internet Connection.Please Check Your Network Connection";
    public static final String MESSAGE_EXCEPTION = "Problem Occured : Report The Problem To Parinaam ";
    public static final String MESSAGE_ERROR_IN_EXECUTING = " Error in executing :";
    public static final String MESSAGE_SOCKETEXCEPTION = "Network Communication Failure. Please Check Your Network Connection";
    public static final String MESSAGE_NO_RESPONSE_SERVER = "Server Not Responding.Please try again.";
    public static final String MESSAGE_XmlPull = "Problem Occured xml pull: Report The Problem To Parinaam";
    public static final String MESSAGE_INVALID_JSON = "Problem Occured while parsing Json : invalid json data";
    public static final String MESSAGE_NUMBER_FORMATE_EXEP = "Invailid Mid";

    public static final String TABLE_STORE_GEOTAGGING = "STORE_GEOTAGGING";
    public static final String TABLE_COVERAGE_DATA = "COVERAGE_DATA";
    public static final String TAG_FROM_JCP = "from_jcp";
    public static final int TAG_FROM_PREVIOUS = 0;
    public static final int TAG_FROM_CURRENT = 1;

    public static final String KEY_EXISTORNOT = "EXISTORNOT";
    public static final String KEY_CHECKLIST_CD = "CHECKLIST_CD";
    public static final String KEY_STATUS = "STATUS";
    public static final String KEY_CAMPAIGN_ID = "CAMPAIGN_ID";

    public static final String CREATE_TABLE_COVERAGE_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_COVERAGE_DATA
            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_STORE_ID + " INTEGER,"
            + KEY_CAMPAIGN_ID + " INTEGER,"
            + "USER_ID VARCHAR, "
            + KEY_VISIT_DATE + " VARCHAR,"
            + KEY_LATITUDE + " VARCHAR,"
            + KEY_LONGITUDE + " VARCHAR,"
            + KEY_IMAGE + " VARCHAR,"
            + KEY_CHECKOUT_IMAGE + " VARCHAR,"
            + KEY_REASON_ID + " INTEGER,"
            + KEY_COVERAGE_REMARK + " VARCHAR,"
            + KEY_REASON + " VARCHAR)";

    //File Path
    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/.Audit_Images/";
    public static final String FOLDER_NAME_WITH_PATH = Environment.getExternalStorageDirectory() + "/.Audit_Images";
    public static final String FOLDER_NAME_IMAGE = ".Audit_Images";
    public static final String ONBACK_ALERT_MESSAGE = "Unsaved data will be lost - Do you want to continue?";
    public static final String KEY_ANSWER_CD = "ANSWER_CD";
    public static final int CAPTURE_MEDIA = 131;

    public static final String CREATE_TABLE_STORE_GEOTAGGING = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STORE_GEOTAGGING
            + " ("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "STORE_ID"
            + " INTEGER,"
            + "LATITUDE"
            + " VARCHAR,"
            + "LONGITUDE"
            + " VARCHAR,"
            + "GEO_TAG"
            + " VARCHAR,"
            + "STATUS"
            + " VARCHAR,"
            + "FRONT_IMAGE" + " VARCHAR)";


    public static final String TABLE_CATEGORY_DBSR_DATA = "CATEGORY_DBSR_DATA";


    public static final String TABLE_AUDIT_DATA_SAVE = "Audit_Data_Save";
    public static final String CREATE_TABLE_AUDIT_DATA_SAVE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AUDIT_DATA_SAVE
            + " ("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_CD"
            + " INTEGER,"

            + "QUESTION_ID"
            + " INTEGER,"

            + "QUESTION"
            + " VARCHAR,"

            + "ANSWER"
            + " VARCHAR,"

            + "ANS_CAM_IMAGE"
            + " VARCHAR,"

            + "CAMERA_ALLOW"
            + " INTEGER,"

            + "ANSWER_ID"
            + " INTEGER,"

            + "CAMPAIGN_ID"
            + " INTEGER,"

            + "ANSWER_TYPE"
            + " VARCHAR"
            + ")";

}
