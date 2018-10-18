package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jeevanp on 7/9/2018.
 */

public class MappingInitiativeGetterSetter {
    @SerializedName("Mapping_Visibility_Initiatives")
    @Expose
    private List<MappingVisibilityInitiative> mappingVisibilityInitiatives = null;

    public List<MappingVisibilityInitiative> getMappingVisibilityInitiatives() {
        return mappingVisibilityInitiatives;
    }

    public void setMappingVisibilityInitiatives(List<MappingVisibilityInitiative> mappingVisibilityInitiatives) {
        this.mappingVisibilityInitiatives = mappingVisibilityInitiatives;
    }
}
