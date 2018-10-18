package cpm.com.audit.delegates;

public class CoverageBean {
    protected int MID;
    protected String storeId;
    protected String Remark;

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    protected String userId;
    protected String visitDate;
    private String latitude;
    private String longitude;
    private String reasonid = "";
    private String reason = "";
    private String image = "";

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    private String campaignId = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status = "";

    public String getCkeckout_image() {
        return ckeckout_image;
    }

    public void setCkeckout_image(String ckeckout_image) {
        this.ckeckout_image = ckeckout_image;
    }

    private String ckeckout_image = "";

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMID() {
        return MID;
    }

    public void setMID(int mID) {
        MID = mID;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getReasonid() {
        return reasonid;
    }

    public void setReasonid(String reasonid) {
        this.reasonid = reasonid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}
