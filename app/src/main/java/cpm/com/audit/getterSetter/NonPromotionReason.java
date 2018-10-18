package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jeevanp on 7/16/2018.
 */

public class NonPromotionReason {
    @SerializedName("PReason_Id")
    @Expose
    private Integer pReasonId;
    @SerializedName("PReason")
    @Expose
    private String pReason;

    public Integer getPReasonId() {
        return pReasonId;
    }

    public void setPReasonId(Integer pReasonId) {
        this.pReasonId = pReasonId;
    }

    public String getPReason() {
        return pReason;
    }

    public void setPReason(String pReason) {
        this.pReason = pReason;
    }
}
