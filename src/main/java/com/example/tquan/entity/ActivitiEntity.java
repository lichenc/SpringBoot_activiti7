package com.example.tquan.entity;

import org.activiti.engine.task.Comment;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActivitiEntity {

        //private String id;
        private String name;
        private String type;
        private String app;
        private String audit;
        private String role;
        private String appId;
        private String account;
        private String actPwd;
        private String status;
        private String userId;
        private String description;
        private String taskId;
        private String taskName;
        private String taskCreateTime;
        private String taskApplyReason;
        private String taskRole;
        private String taskAssignee;
        private String taskOrgName;
        private String taskProcessInstanceId;
        private String taskExecutionId;
        private String taskProcessDefinitionId;
        private String taskApp;
        private String taskActType;
        private String taskAccount;
        private String taskTypes;
        private String taskApplyPerson;
        private String taskApprovedPerson;
        private List<Comment> comment;
        private List<DefaultsEntity> textLists;
        private List<DefaultsEntity> passwordLists;
        private List<DefaultsEntity> selectLists;
        private List<DefaultsEntity> dateLists;

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

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }
    public String getTaskOrgName() {
        return taskOrgName;
    }

    public void setTaskOrgName(String taskOrgName) {
        this.taskOrgName = taskOrgName;
    }

    public String getTaskApplyReason() {
        return taskApplyReason;
    }

    public void setTaskApplyReason(String taskApplyReason) {
        this.taskApplyReason = taskApplyReason;
    }

    public String getTaskRole() {
        return taskRole;
    }

    public void setTaskRole(String taskRole) {
        this.taskRole = taskRole;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getActPwd() {
        return actPwd;
    }

    public void setActPwd(String actPwd) {
        this.actPwd = actPwd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskCreateTime() {
        return taskCreateTime;
    }

    public void setTaskCreateTime(String taskCreateTime) {
        this.taskCreateTime = taskCreateTime;
    }

    public String getTaskAssignee() {
        return taskAssignee;
    }

    public void setTaskAssignee(String taskAssignee) {
        this.taskAssignee = taskAssignee;
    }

    public String getTaskProcessInstanceId() {
        return taskProcessInstanceId;
    }

    public void setTaskProcessInstanceId(String taskProcessInstanceId) {
        this.taskProcessInstanceId = taskProcessInstanceId;
    }

    public String getTaskExecutionId() {
        return taskExecutionId;
    }

    public void setTaskExecutionId(String taskExecutionId) {
        this.taskExecutionId = taskExecutionId;
    }

    public String getTaskProcessDefinitionId() {
        return taskProcessDefinitionId;
    }

    public void setTaskProcessDefinitionId(String taskProcessDefinitionId) {
        this.taskProcessDefinitionId = taskProcessDefinitionId;
    }

    public String getTaskApp() {
        return taskApp;
    }

    public void setTaskApp(String taskApp) {
        this.taskApp = taskApp;
    }

    public String getTaskActType() {
        return taskActType;
    }

    public void setTaskActType(String taskActType) {
        this.taskActType = taskActType;
    }

    public String getTaskAccount() {
        return taskAccount;
    }

    public void setTaskAccount(String taskAccount) {
        this.taskAccount = taskAccount;
    }

    public String getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(String taskTypes) {
        this.taskTypes = taskTypes;
    }

    public String getTaskApplyPerson() {
        return taskApplyPerson;
    }

    public void setTaskApplyPerson(String taskApplyPerson) {
        this.taskApplyPerson = taskApplyPerson;
    }

    public String getTaskApprovedPerson() {
        return taskApprovedPerson;
    }

    public void setTaskApprovedPerson(String taskApprovedPerson) {
        this.taskApprovedPerson = taskApprovedPerson;
    }

    @Override
    public String toString() {
        return "ActivitiEntity{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", app='" + app + '\'' +
                ", audit='" + audit + '\'' +
                ", role='" + role + '\'' +
                ", appId='" + appId + '\'' +
                ", account='" + account + '\'' +
                ", actPwd='" + actPwd + '\'' +
                ", status='" + status + '\'' +
                ", userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskCreateTime=" + taskCreateTime +
                ", taskApplyReason='" + taskApplyReason + '\'' +
                ", taskRole='" + taskRole + '\'' +
                ", taskAssignee='" + taskAssignee + '\'' +
                ", taskOrgName='" + taskOrgName + '\'' +
                ", taskProcessInstanceId='" + taskProcessInstanceId + '\'' +
                ", taskExecutionId='" + taskExecutionId + '\'' +
                ", taskProcessDefinitionId='" + taskProcessDefinitionId + '\'' +
                ", taskApp='" + taskApp + '\'' +
                ", taskActType='" + taskActType + '\'' +
                ", taskAccount='" + taskAccount + '\'' +
                ", taskTypes='" + taskTypes + '\'' +
                ", taskApplyPerson='" + taskApplyPerson + '\'' +
                ", taskApprovedPerson='" + taskApprovedPerson + '\'' +
                ", comment=" + comment +
                ", textLists=" + textLists +
                ", passwordLists=" + passwordLists +
                ", selectLists=" + selectLists +
                ", dateLists=" + dateLists +
                '}';
    }
}



