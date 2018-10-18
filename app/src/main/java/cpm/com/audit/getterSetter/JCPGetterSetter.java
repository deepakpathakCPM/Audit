
package cpm.com.audit.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JCPGetterSetter {

    @SerializedName("Journey_Plan")
    @Expose
    private List<JourneyPlan> journeyPlan = null;

    public List<JourneyPlan> getJourneyPlan() {
        return journeyPlan;
    }

    public void setJourneyPlan(List<JourneyPlan> journeyPlan) {
        this.journeyPlan = journeyPlan;
    }

    @SerializedName("Journey_Plan_DBSR")
    @Expose
    private List<JourneyPlan> journeyPlan_dbsr = null;

    public List<JourneyPlan> getJourneyPlan_dbsr() {
        return journeyPlan_dbsr;
    }

    public void setJourneyPlan_dbsr(List<JourneyPlan> journeyPlan_dbsr) {
        this.journeyPlan_dbsr = journeyPlan_dbsr;
    }

}
