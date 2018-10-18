
package cpm.com.audit.getterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditAnswerMasterGetterSetter {

    @SerializedName("Audit_Answer_Master")
    @Expose
    private List<AuditAnswerMaster> auditAnswerMaster = null;

    public List<AuditAnswerMaster> getAuditAnswerMaster() {
        return auditAnswerMaster;
    }

    public void setAuditAnswerMaster(List<AuditAnswerMaster> auditAnswerMaster) {
        this.auditAnswerMaster = auditAnswerMaster;
    }

}
