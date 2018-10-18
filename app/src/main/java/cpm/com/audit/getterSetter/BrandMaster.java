
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandMaster {

    @SerializedName("Brand_Id")
    @Expose
    private Integer brandId;
    @SerializedName("Brand")
    @Expose
    private String brand;
    @SerializedName("Sub_Category_Id")
    @Expose
    private Integer subcategoryId;
    @SerializedName("Brand_Sequence")
    @Expose
    private Integer brandSequence;

    public Integer getCompany_Id() {
        return company_Id;
    }

    public void setCompany_Id(Integer company_Id) {
        this.company_Id = company_Id;
    }

    @SerializedName("Company_Id")
    @Expose
    private Integer company_Id;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Integer subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public Integer getBrandSequence() {
        return brandSequence;
    }

    public void setBrandSequence(Integer brandSequence) {
        this.brandSequence = brandSequence;
    }

}
