package cpm.com.audit.getterSetter;
/**
 * Created by yadavendras on 3/22/2018.
 */

public class AuditDataGetterSetter {

    String QUESTION_ID;
    String CULTURE_ID;
    String CATEGORY_ID;
    String QUESTION;
    String ANSWER_TYPE;
    String ANSWER_ID;
    String ANSWER;
    String CAM_IMAGE="";
    String CAMERA_ALLOW="0";
    String KEYACCOUNT_ID="0";
    String NO_OF_CAMERA ="1";
    String CAM_IMAGE2="";

    public String getQUESTION_CATEGORY_ID() {
        return QUESTION_CATEGORY_ID;
    }

    public void setQUESTION_CATEGORY_ID(String QUESTION_CATEGORY_ID) {
        this.QUESTION_CATEGORY_ID = QUESTION_CATEGORY_ID;
    }

    public String getQUESTION_CATEGORY() {
        return QUESTION_CATEGORY;
    }

    public void setQUESTION_CATEGORY(String QUESTION_CATEGORY) {
        this.QUESTION_CATEGORY = QUESTION_CATEGORY;
    }

    String QUESTION_CATEGORY_ID;
    String QUESTION_CATEGORY;

    public String getRIGHT_ANSWER() {
        return RIGHT_ANSWER;
    }

    public void setRIGHT_ANSWER(String RIGHT_ANSWER) {
        this.RIGHT_ANSWER = RIGHT_ANSWER;
    }

    String RIGHT_ANSWER = "";


    public String getQUESTION_ID() {
        return QUESTION_ID;
    }

    public void setQUESTION_ID(String QUESTION_ID) {
        this.QUESTION_ID = QUESTION_ID;
    }

    public String getCULTURE_ID() {
        return CULTURE_ID;
    }

    public void setCULTURE_ID(String CULTURE_ID) {
        this.CULTURE_ID = CULTURE_ID;
    }

    public String getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID = CATEGORY_ID;
    }

    public String getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }

    public String getANSWER_TYPE() {
        return ANSWER_TYPE;
    }

    public void setANSWER_TYPE(String ANSWER_TYPE) {
        this.ANSWER_TYPE = ANSWER_TYPE;
    }

    public String getANSWER_ID() {
        return ANSWER_ID;
    }

    public void setANSWER_ID(String ANSWER_ID) {
        this.ANSWER_ID = ANSWER_ID;
    }

    public String getANSWER() {
        return ANSWER;
    }

    public void setANSWER(String ANSWER) {
        this.ANSWER = ANSWER;
    }

    public String getCAM_IMAGE() {
        return CAM_IMAGE;
    }

    public void setCAM_IMAGE(String CAM_IMAGE) {
        this.CAM_IMAGE = CAM_IMAGE;
    }

    public String getCAMERA_ALLOW() {
        return CAMERA_ALLOW;
    }

    public void setCAMERA_ALLOW(String CAMERA_ALLOW) {
        this.CAMERA_ALLOW = CAMERA_ALLOW;
    }

    public String getKEYACCOUNT_ID() {
        return KEYACCOUNT_ID;
    }

    public void setKEYACCOUNT_ID(String KEYACCOUNT_ID) {
        this.KEYACCOUNT_ID = KEYACCOUNT_ID;
    }

    public String getNO_OF_CAMERA() {
        return NO_OF_CAMERA;
    }

    public void setNO_OF_CAMERA(String NO_OF_CAMERA) {
        this.NO_OF_CAMERA = NO_OF_CAMERA;
    }

    public String getCAM_IMAGE2() {
        return CAM_IMAGE2;
    }

    public void setCAM_IMAGE2(String CAM_IMAGE2) {
        this.CAM_IMAGE2 = CAM_IMAGE2;
    }
}
