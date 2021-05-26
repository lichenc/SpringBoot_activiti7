package com.example.tquan.entity;

import java.util.Date;

/**
 * 用户实体类
 * Created by chenjin on 2021/5/20 14:56
 */
public class UserEntity {
    private String id;
    private String sn;
    private String sex;
    private String name;
    private Integer userTypeId;
    private String telephone;
    private String email;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private String optTime;
    private String companySn;

    //辅助字段：组织名称
    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOptTime() {
        return optTime;
    }

    public void setOptTime(String optTime) {
        this.optTime = optTime;
    }

    public String getCompanySn() {
        return companySn;
    }

    public void setCompanySn(String companySn) {
        this.companySn = companySn;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", sn='" + sn + '\'' +
                ", sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                ", userTypeId=" + userTypeId +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", optTime='" + optTime + '\'' +
                ", companySn='" + companySn + '\'' +
                '}';
    }
}
