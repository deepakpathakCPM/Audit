
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JourneyPlan implements Serializable {

    @SerializedName("Campaign_Id")
    @Expose
    private Integer campaignId;
    @SerializedName("Campaign")
    @Expose
    private String campaign;
    @SerializedName("Store_Id")
    @Expose
    private Integer storeId;
    @SerializedName("Visit_Date")
    @Expose
    private String visitDate;
    @SerializedName("Distributor")
    @Expose
    private String distributor;
    @SerializedName("Store_Name")
    @Expose
    private String storeName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Store_Type")
    @Expose
    private String storeType;
    @SerializedName("Store_Category")
    @Expose
    private String storeCategory;
    @SerializedName("Classification")
    @Expose
    private String classification;
    @SerializedName("Reason_Id")
    @Expose
    private Integer reasonId;
    @SerializedName("Upload_Status")
    @Expose
    private String uploadStatus;
    @SerializedName("Geo_Tag")
    @Expose
    private String geoTag;

    public int getColourCode() {
        return colourCode;
    }

    public void setColourCode(int colourCode) {
        this.colourCode = colourCode;
    }

    private int colourCode;

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getGeoTag() {
        return geoTag;
    }

    public void setGeoTag(String geoTag) {
        this.geoTag = geoTag;
    }

}
