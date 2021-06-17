package com.example.tquan.entity;

public class Approver {

        private String audit;
        private String pAudit;
        private String login_name;

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getpAudit() {
        return pAudit;
    }

    public void setpAudit(String pAudit) {
        this.pAudit = pAudit;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    @Override
    public String toString() {
        return "Approver{" +
                "audit='" + audit + '\'' +
                ", pAudit='" + pAudit + '\'' +
                ", login_name='" + login_name + '\'' +
                '}';
    }
}
