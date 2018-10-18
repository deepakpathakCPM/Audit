
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryMaster {

    @SerializedName("Category_Id")
    @Expose
    private Integer categoryId;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Category_Sequence")
    @Expose
    private Integer categorySequence;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCategorySequence() {
        return categorySequence;
    }

    public void setCategorySequence(Integer categorySequence) {
        this.categorySequence = categorySequence;
    }

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey_Id() {
        return key_Id;
    }

    public void setKey_Id(String key_Id) {
        this.key_Id = key_Id;
    }

    String key_Id;
    String image;
    boolean exist;
    int reasonId;
    String reason;

}
