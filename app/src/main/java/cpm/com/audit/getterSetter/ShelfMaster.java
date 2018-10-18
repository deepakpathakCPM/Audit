package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jeevanp on 7/9/2018.
 */

public class ShelfMaster {
    @SerializedName("Shelf_No")
    @Expose
    private String  shelfNo;

    public String getShelfNo() {
        return shelfNo;
    }

    public void setShelfNo(String shelfNo) {
        this.shelfNo = shelfNo;
    }
}
