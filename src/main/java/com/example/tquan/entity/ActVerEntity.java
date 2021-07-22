package com.example.tquan.entity;

public class ActVerEntity {
    private String id;
    private String appAccount;
    private String app_id;
    private String appName;
    private String userId;
    private String status;
    private String acct_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppAccount() {
        return appAccount;
    }

    public void setAppAccount(String appAccount) {
        this.appAccount = appAccount;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcct_type() {
        return acct_type;
    }

    public void setAcct_type(String acct_type) {
        this.acct_type = acct_type;
    }

    @Override
    public String toString() {
        return "ActVerEntity{" +
                "id='" + id + '\'' +
                ", appAccount='" + appAccount + '\'' +
                ", app_id='" + app_id + '\'' +
                ", appName='" + appName + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", acct_type='" + acct_type + '\'' +
                '}';
    }
}
