package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jeevanp on 7/9/2018.
 */

public class MappingWindChecklistGetterSetter {

    @SerializedName("Mapping_Window_Checklist")
    @Expose
    private List<MappingWindowChecklist> mappingWindowChecklist = null;

    public List<MappingWindowChecklist> getMappingWindowChecklist() {
        return mappingWindowChecklist;
    }

    public void setMappingWindowChecklist(List<MappingWindowChecklist> mappingWindowChecklist) {
        this.mappingWindowChecklist = mappingWindowChecklist;
    }
}
