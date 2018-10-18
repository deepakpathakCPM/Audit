package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jeevanp on 7/9/2018.
 */

public class MappingWindowChecklist {
    @SerializedName("Window_Id")
    @Expose
    private Integer windowId;
    @SerializedName("Checklist_id")
    @Expose
    private Integer checklistId;

    public Integer getWindowId() {
        return windowId;
    }

    public void setWindowId(Integer windowId) {
        this.windowId = windowId;
    }

    public Integer getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Integer checklistId) {
        this.checklistId = checklistId;
    }
}
