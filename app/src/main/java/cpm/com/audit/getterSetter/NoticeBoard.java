
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticeBoard {

    @SerializedName("USER_ID")
    @Expose
    private String uSERID;
    @SerializedName("PROJECT_CODE")
    @Expose
    private String pROJECTCODE;
    @SerializedName("Notice_Board_Url")
    @Expose
    private String nOTICEBOARD;
    @SerializedName("QUIZ_URL")
    @Expose
    private String qUIZURL;

    public String getUSERID() {
        return uSERID;
    }

    public void setUSERID(String uSERID) {
        this.uSERID = uSERID;
    }

    public String getPROJECTCODE() {
        return pROJECTCODE;
    }

    public void setPROJECTCODE(String pROJECTCODE) {
        this.pROJECTCODE = pROJECTCODE;
    }

    public String getNOTICEBOARD() {
        return nOTICEBOARD;
    }

    public void setNOTICEBOARD(String nOTICEBOARD) {
        this.nOTICEBOARD = nOTICEBOARD;
    }

    public String getQUIZURL() {
        return qUIZURL;
    }

    public void setQUIZURL(String qUIZURL) {
        this.qUIZURL = qUIZURL;
    }

}
