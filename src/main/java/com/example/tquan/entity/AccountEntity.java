package com.example.tquan.entity;

import java.util.Date;

/**
 * 账号实体类
 * Created by chenjin on 2021/5/17 10:45
 */
public class AccountEntity {
    private Integer id;
    private String loginName;
    private String loginPwd;
    private Integer appId;
    private Integer userId;
    private Integer status;
    private Date createTime;
    private String optUser;
    private Integer openType;
    private Integer acctType;
    private Integer companySn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public Integer getAcctType() {
        return acctType;
    }

    public void setAcctType(Integer acctType) {
        this.acctType = acctType;
    }

    public Integer getCompanySn() {
        return companySn;
    }

    public void setCompanySn(Integer companySn) {
        this.companySn = companySn;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", loginPwd='" + loginPwd + '\'' +
                ", appId=" + appId +
                ", userId=" + userId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", optUser='" + optUser + '\'' +
                ", openType=" + openType +
                ", acctType=" + acctType +
                ", companySn=" + companySn +
                '}';
    }
}
