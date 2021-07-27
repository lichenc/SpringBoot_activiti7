package com.example.tquan.entity;

import org.activiti.engine.task.Comment;

import java.util.Date;
import java.util.List;

public class AuditEntity {
    private String id;
    private String name;
    private String time;
    private String reason;
    private String role;
    private String assignee;
    private String orgName;
    private String processInstanceId;
    private String executionId;
    private String processDefinitionId;
    private String app;
    private String actType;
    private String account;
    private String types;
    private String applyPerson;
    private String approvedPerson;
    private String applyReason;
    private List<Comment> comment;

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
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

    @Override
    public String toString() {
        return "AuditEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", reason='" + reason + '\'' +
                ", role='" + role + '\'' +
                ", assignee='" + assignee + '\'' +
                ", orgName='" + orgName + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", executionId='" + executionId + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", app='" + app + '\'' +
                ", actType='" + actType + '\'' +
                ", account='" + account + '\'' +
                ", types='" + types + '\'' +
                ", applyPerson='" + applyPerson + '\'' +
                ", approvedPerson='" + approvedPerson + '\'' +
                ", applyReason='" + applyReason + '\'' +
                ", comment=" + comment +
                '}';
    }
}
