package com.example.tquan.entity;

import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 * Created by chenjin on 2021/5/20 14:56
 */
public class UserEntity {
    private String id;
    private String sn;
    private String sex;
    private String name;
    private String userTypeId;
    private String telephone;
    private String email;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private String optTime;
    private String companySn;
    private String optUser;
    private StringBuilder extended;
    private String value;


    //辅助字段：组织名称
    private String orgName;
    //辅助字段：账号个数
    private int accountCount;

    //辅助字段：岗位个数
    private int positionCount;
    //辅助字段：用户所属岗位集合
    private  List<PositionEntity> positionEntityList;
    //辅助字段：账号集合
    private List<AccountEntity> accountEntities;

    //辅助字段:用户组个数
    private int groupCount;
    //辅助字段：用户组集合
    private List<GroupEntity> groupEntities;

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public List<GroupEntity> getGroupEntities() {
        return groupEntities;
    }

    public void setGroupEntities(List<GroupEntity> groupEntities) {
        this.groupEntities = groupEntities;
    }

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

    public StringBuilder getExtended() {
        return extended;
    }

    public void setExtended(StringBuilder extended) {
        this.extended = extended;
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

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
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

    public int getAccountCount() {
        return accountCount;
    }

    public void setAccountCount(int accountCount) {
        this.accountCount = accountCount;
    }

    public List<AccountEntity> getAccountEntities() {
        return accountEntities;
    }

    public void setAccountEntities(List<AccountEntity> accountEntities) {
        this.accountEntities = accountEntities;
    }

    public List<PositionEntity> getPositionEntityList() {
        return positionEntityList;
    }

    public void setPositionEntityList(List<PositionEntity> positionEntityList) {
        this.positionEntityList = positionEntityList;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", sn='" + sn + '\'' +
                ", sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                ", userTypeId='" + userTypeId + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", optTime='" + optTime + '\'' +
                ", companySn='" + companySn + '\'' +
                ", optUser='" + optUser + '\'' +
                ", extended=" + extended +
                ", value='" + value + '\'' +
                ", orgName='" + orgName + '\'' +
                ", accountCount=" + accountCount +
                ", positionCount=" + positionCount +
                ", positionEntityList=" + positionEntityList +
                ", accountEntities=" + accountEntities +
                ", groupCount=" + groupCount +
                ", groupEntities=" + groupEntities +
                '}';
    }
}
