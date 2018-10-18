
package cpm.com.audit.getterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditQuestionMasterGetterSetter {

    @SerializedName("Audit_Question_Master")
    @Expose
    private List<AuditQuestionMaster> auditQuestionMaster = null;

    public List<AuditQuestionMaster> getAuditQuestionMaster() {
        return auditQuestionMaster;
    }

    public void setAuditQuestionMaster(List<AuditQuestionMaster> auditQuestionMaster) {
        this.auditQuestionMaster = auditQuestionMaster;
    }

}
