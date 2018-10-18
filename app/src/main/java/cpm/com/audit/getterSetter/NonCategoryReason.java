
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NonCategoryReason {

    @SerializedName("CReason_Id")
    @Expose
    private Integer cReasonId;
    @SerializedName("CReason")
    @Expose
    private String cReason;

    public Integer getCReasonId() {
        return cReasonId;
    }

    public void setCReasonId(Integer cReasonId) {
        this.cReasonId = cReasonId;
    }

    public String getCReason() {
        return cReason;
    }

    public void setCReason(String cReason) {
        this.cReason = cReason;
    }

}
