package cpm.com.audit.getterSetter;

import java.io.Serializable;

/**
 * Created by deepakp on 05-01-2018.
 */

public class SearchListGetterSetter implements Serializable {

    public String getSTORE_CD() {
        return STORE_CD;
    }

    public void setSTORE_CD(String STORE_CD) {
        this.STORE_CD = STORE_CD;
    }

    public String getKEYACCOUNT() {
        return KEYACCOUNT;
    }

    public void setKEYACCOUNT(String KEYACCOUNT) {
        this.KEYACCOUNT = KEYACCOUNT;
    }

    public String getSTORENAME() {
        return STORENAME;
    }

    public void setSTORENAME(String STORENAME) {
        this.STORENAME = STORENAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getSTORETYPE() {
        return STORETYPE;
    }

    public void setSTORETYPE(String STORETYPE) {
        this.STORETYPE = STORETYPE;
    }

    public String getROUTE_NAME() {
        return ROUTE_NAME;
    }

    public void setROUTE_NAME(String ROUTE_NAME) {
        this.ROUTE_NAME = ROUTE_NAME;
    }

    public String getVISIT_DATE() {
        return VISIT_DATE;
    }

    public void setVISIT_DATE(String VISIT_DATE) {
        this.VISIT_DATE = VISIT_DATE;
    }

    String VISIT_DATE;
    String STORE_CD;
    String KEYACCOUNT;
    String STORENAME;
    String ADDRESS;
    String CITY;
    String STORETYPE;
    String ROUTE_NAME;
}
