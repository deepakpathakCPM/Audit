package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jeevanp on 7/9/2018.
 */

public class SelfMasterGetterSetter {
    @SerializedName("Shelf_Master")
    @Expose
    private List<ShelfMaster> shelfMaster = null;

    public List<ShelfMaster> getShelfMaster() {
        return shelfMaster;
    }

    public void setShelfMaster(List<ShelfMaster> shelfMaster) {
        this.shelfMaster = shelfMaster;
    }

}
