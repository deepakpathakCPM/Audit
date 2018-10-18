
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NonWindowReason {

    @SerializedName("WReason_Id")
    @Expose
    private Integer wReasonId;
    @SerializedName("WReason")
    @Expose
    private String wReason;

    public Integer getWReasonId() {
        return wReasonId;
    }

    public void setWReasonId(Integer wReasonId) {
        this.wReasonId = wReasonId;
    }

    public String getWReason() {
        return wReason;
    }

    public void setWReason(String wReason) {
        this.wReason = wReason;
    }

}
