
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreCategoryMaster implements Serializable {
    @SerializedName("Rsp_Id")
    @Expose
    private Integer rspId;
    @SerializedName("Rsp_Name")
    @Expose
    private String rspName;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Store_Id")
    @Expose
    private Integer storeId;
    @SerializedName("Brand_Id")
    @Expose
    private Integer brandId;
    @SerializedName("IREP_Status")
    @Expose
    private Boolean iREPStatus;
    private long id;

    public Integer getRspId() {
        return rspId;
    }

    public void setRspId(Integer rspId) {
        this.rspId = rspId;
    }

    public String getRspName() {
        return rspName;
    }

    public void setRspName(String rspName) {
        this.rspName = rspName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Boolean getIREPStatus() {
        return iREPStatus;
    }

    public void setIREPStatus(Boolean iREPStatus) {
        this.iREPStatus = iREPStatus;
    }

    private String key_id="0";

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    private String flag="";

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRsp_uniqueID() {
        return rsp_uniqueID;
    }

    public void setRsp_uniqueID(String rsp_uniqueID) {
        this.rsp_uniqueID = rsp_uniqueID;
    }

    private String rsp_uniqueID="";
    boolean isSelected=false;
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * @param isSelected the isSelected to set
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


}
