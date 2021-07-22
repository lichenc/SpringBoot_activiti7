package com.example.tquan.entity;

public class DefaultEntity {
    private String appId;
    private String remark;
    private String name;
    private String defaultValue;
    private String appName;
    private String loginName;
    private String loName;
    //所有属性
    private String accountName;
    private String applyName;
    private String sta;

    private String allField;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefault_value() {
        return defaultValue;
    }

    public void setDefault_value(String default_value) {
        this.defaultValue = default_value;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public String getAllField() {
        return allField;
    }

    public void setAllField(String allField) {
        this.allField = allField;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoName() {
        return loName;
    }

    public void setLoName(String loName) {
        this.loName = loName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    @Override
    public String toString() {
        return "DefaultEntity{" +
                "appId='" + appId + '\'' +
                ", remark='" + remark + '\'' +
                ", name='" + name + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", appName='" + appName + '\'' +
                ", loginName='" + loginName + '\'' +
                ", loName='" + loName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", applyName='" + applyName + '\'' +
                ", sta='" + sta + '\'' +
                ", allField='" + allField + '\'' +
                '}';
    }
}
