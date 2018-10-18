package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jeevanp on 7/12/2018.
 */

public class MappingPromotionGetterSetter {
    @SerializedName("Mapping_Promotion")
    @Expose
    private List<MappingPromotion> mappingPromotion = null;

    public List<MappingPromotion> getMappingPromotion() {
        return mappingPromotion;
    }

    public void setMappingPromotion(List<MappingPromotion> mappingPromotion) {
        this.mappingPromotion = mappingPromotion;
    }
}
