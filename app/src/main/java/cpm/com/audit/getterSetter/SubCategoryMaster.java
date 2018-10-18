
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryMaster {

    @SerializedName("Sub_Category_Id")
    @Expose
    private Integer subCategoryId;
    @SerializedName("Sub_Category")
    @Expose
    private String subCategory;
    @SerializedName("Category_Id")
    @Expose
    private Integer categoryId;
    @SerializedName("Sub_Category_Sequence")
    @Expose
    private Integer subCategorySequence;

    String brandCD;

    public String getCompany_Id() {
        return company_Id;
    }

    public void setCompany_Id(String company_Id) {
        this.company_Id = company_Id;
    }

    String company_Id;



    public String getBrandCD() {
        return brandCD;
    }

    public void setBrandCD(String brandCD) {
        this.brandCD = brandCD;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    String brand;
    String sku_cd;
    String sku;
    String openingStock="";

    public String getMiddayStock() {
        return middayStock;
    }

    public void setMiddayStock(String middayStock) {
        this.middayStock = middayStock;
    }

    String middayStock="";

    public String getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(String closingStock) {
        this.closingStock = closingStock;
    }

    String closingStock="";

    public String getSku_cd() {
        return sku_cd;
    }

    public void setSku_cd(String sku_cd) {
        this.sku_cd = sku_cd;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(String openingStock) {
        this.openingStock = openingStock;
    }

    public String getFacingStock() {
        return facingStock;
    }

    public void setFacingStock(String facingStock) {
        this.facingStock = facingStock;
    }

    public String getExpiringStock() {
        return ExpiringStock;
    }

    public void setExpiringStock(String expiringStock) {
        ExpiringStock = expiringStock;
    }

    String facingStock="";
    String ExpiringStock="";

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSubCategorySequence() {
        return subCategorySequence;
    }

    public void setSubCategorySequence(Integer subCategorySequence) {
        this.subCategorySequence = subCategorySequence;
    }
String SpinValue="0";
    String stockIMGONE="";

    public String getSpinValue() {
        return SpinValue;
    }

    public void setSpinValue(String spinValue) {
        SpinValue = spinValue;
    }

    public String getStockIMGONE() {
        return stockIMGONE;
    }

    public void setStockIMGONE(String stockIMGONE) {
        this.stockIMGONE = stockIMGONE;
    }

    public String getStockIMGTWO() {
        return stockIMGTWO;
    }

    public void setStockIMGTWO(String stockIMGTWO) {
        this.stockIMGTWO = stockIMGTWO;
    }

    public String getStockIMGTHREE() {
        return stockIMGTHREE;
    }

    public void setStockIMGTHREE(String stockIMGTHREE) {
        this.stockIMGTHREE = stockIMGTHREE;
    }

    String stockIMGTWO="";
    String stockIMGTHREE="";

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    String keyId;
    String MSL;

    public String getMSL() {
        return MSL;
    }

    public void setMSL(String MSL) {
        this.MSL = MSL;
    }

    public String getMBQ() {
        return MBQ;
    }

    public void setMBQ(String MBQ) {
        this.MBQ = MBQ;
    }

    String MBQ;


}
