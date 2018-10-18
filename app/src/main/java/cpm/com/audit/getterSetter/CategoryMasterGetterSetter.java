
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryMasterGetterSetter {

    @SerializedName("Category_Master")
    @Expose
    private List<CategoryMaster> categoryMaster = null;

    public List<CategoryMaster> getCategoryMaster() {
        return categoryMaster;
    }

    public void setCategoryMaster(List<CategoryMaster> categoryMaster) {
        this.categoryMaster = categoryMaster;
    }

}
