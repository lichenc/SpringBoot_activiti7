package com.example.tquan.entity;

import org.activiti.engine.task.Comment;

import java.util.List;
import java.util.Map;

public class DefaultsEntity {
    private String id;
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
    private List<ActivitiEntity> taskTypeAll;
    private List<ImApp> imApp;
    private List<Comment> comment;
    private List<ActEntity> actAll;
    private String accountOrg;

    public String getAccountOrg() {
        return accountOrg;
    }

    public void setAccountOrg(String accountOrg) {
        this.accountOrg = accountOrg;
    }

    public List<ActEntity> getActAll() {
        return actAll;
    }

    public void setActAll(List<ActEntity> actAll) {
        this.actAll = actAll;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public List<ImApp> getImApp() {
        return imApp;
    }

    public void setImApp(List<ImApp> imApp) {
        this.imApp = imApp;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public List<ActivitiEntity> getTaskTypeAll() {
        return taskTypeAll;
    }

    public void setTaskTypeAll(List<ActivitiEntity> taskTypeAll) {
        this.taskTypeAll = taskTypeAll;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    private List<Map<String, Object>> listCompants;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
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

    public List<Map<String, Object>> getListCompants() {
        return listCompants;
    }

    public void setListCompants(List<Map<String, Object>> listCompants) {
        this.listCompants = listCompants;
    }

    @Override
    public String toString() {
        return "DefaultsEntity{" +
                "id='" + id + '\'' +
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
                ", taskTypeAll=" + taskTypeAll +
                ", imApp=" + imApp +
                ", comment=" + comment +
                ", actAll=" + actAll +
                ", accountOrg='" + accountOrg + '\'' +
                ", listCompants=" + listCompants +
                '}';
    }


}
