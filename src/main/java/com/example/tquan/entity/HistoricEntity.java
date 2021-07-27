package com.example.tquan.entity;

import org.activiti.engine.task.Comment;

import java.util.Date;
import java.util.List;

public class HistoricEntity {
    private String id;
    private String actId;
    private String ProcessInstanceId;
    private String actName;
    private String createTime;
    private String endTime;
    private String account;
    private String applyPerson;
    private String approvedPerson;
    private String role;
    private String app;
    private String orgName;
    private String taskType;
    private String applyReason;
    private String names;
    private String remarks;
    private String defaultValues;
    private String isBasics;
    private String isRequrieds;
    private String isInserts;
    private String isEdits;
    private String inputTypes;
    private String compants;
    private String basic;
    private String actType;
    private String audit;
    private String accountOrg;
    private String status;
    private List<Comment> comment;
    private List<DefaultsEntity> textLists;
    private List<DefaultsEntity> passwordLists;
    private List<DefaultsEntity> selectLists;
    private List<DefaultsEntity> dateLists;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountOrg() {
        return accountOrg;
    }

    public void setAccountOrg(String accountOrg) {
        this.accountOrg = accountOrg;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public List<DefaultsEntity> getTextLists() {
        return textLists;
    }

    public void setTextLists(List<DefaultsEntity> textLists) {
        this.textLists = textLists;
    }

    public List<DefaultsEntity> getPasswordLists() {
        return passwordLists;
    }

    public void setPasswordLists(List<DefaultsEntity> passwordLists) {
        this.passwordLists = passwordLists;
    }

    public List<DefaultsEntity> getSelectLists() {
        return selectLists;
    }

    public void setSelectLists(List<DefaultsEntity> selectLists) {
        this.selectLists = selectLists;
    }

    public List<DefaultsEntity> getDateLists() {
        return dateLists;
    }

    public void setDateLists(List<DefaultsEntity> dateLists) {
        this.dateLists = dateLists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getProcessInstanceId() {
        return ProcessInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        ProcessInstanceId = processInstanceId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }

    public String getApprovedPerson() {
        return approvedPerson;
    }

    public void setApprovedPerson(String approvedPerson) {
        this.approvedPerson = approvedPerson;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(String defaultValues) {
        this.defaultValues = defaultValues;
    }

    public String getIsBasics() {
        return isBasics;
    }

    public void setIsBasics(String isBasics) {
        this.isBasics = isBasics;
    }

    public String getIsRequrieds() {
        return isRequrieds;
    }

    public void setIsRequrieds(String isRequrieds) {
        this.isRequrieds = isRequrieds;
    }

    public String getIsInserts() {
        return isInserts;
    }

    public void setIsInserts(String isInserts) {
        this.isInserts = isInserts;
    }

    public String getIsEdits() {
        return isEdits;
    }

    public void setIsEdits(String isEdits) {
        this.isEdits = isEdits;
    }

    public String getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(String inputTypes) {
        this.inputTypes = inputTypes;
    }

    public String getCompants() {
        return compants;
    }

    public void setCompants(String compants) {
        this.compants = compants;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    @Override
    public String toString() {
        return "HistoricEntity{" +
                "id='" + id + '\'' +
                ", actId='" + actId + '\'' +
                ", ProcessInstanceId='" + ProcessInstanceId + '\'' +
                ", actName='" + actName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", account='" + account + '\'' +
                ", applyPerson='" + applyPerson + '\'' +
                ", approvedPerson='" + approvedPerson + '\'' +
                ", role='" + role + '\'' +
                ", app='" + app + '\'' +
                ", orgName='" + orgName + '\'' +
                ", taskType='" + taskType + '\'' +
                ", applyReason='" + applyReason + '\'' +
                ", names='" + names + '\'' +
                ", remarks='" + remarks + '\'' +
                ", defaultValues='" + defaultValues + '\'' +
                ", isBasics='" + isBasics + '\'' +
                ", isRequrieds='" + isRequrieds + '\'' +
                ", isInserts='" + isInserts + '\'' +
                ", isEdits='" + isEdits + '\'' +
                ", inputTypes='" + inputTypes + '\'' +
                ", compants='" + compants + '\'' +
                ", basic='" + basic + '\'' +
                ", actType='" + actType + '\'' +
                ", audit='" + audit + '\'' +
                ", accountOrg='" + accountOrg + '\'' +
                ", comment=" + comment +
                ", textLists=" + textLists +
                ", passwordLists=" + passwordLists +
                ", selectLists=" + selectLists +
                ", dateLists=" + dateLists +
                '}';
    }
}
