
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NonCategoryReasonGetterSetter {

    @SerializedName("Non_Category_Reason")
    @Expose
    private List<NonCategoryReason> nonCategoryReason = null;

    public List<NonCategoryReason> getNonCategoryReason() {
        return nonCategoryReason;
    }

    public void setNonCategoryReason(List<NonCategoryReason> nonCategoryReason) {
        this.nonCategoryReason = nonCategoryReason;
    }

}
