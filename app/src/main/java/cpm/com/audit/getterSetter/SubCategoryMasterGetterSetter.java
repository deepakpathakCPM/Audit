
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryMasterGetterSetter {
    @SerializedName("Sub_Category_Master")
    @Expose
    private List<SubCategoryMaster> subCategoryMaster = null;

    public List<SubCategoryMaster> getSubCategoryMaster() {
        return subCategoryMaster;
    }

    public void setSubCategoryMaster(List<SubCategoryMaster> subCategoryMaster) {
        this.subCategoryMaster = subCategoryMaster;
    }

}
