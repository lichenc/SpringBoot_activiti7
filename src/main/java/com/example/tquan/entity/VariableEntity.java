package com.example.tquan.entity;

/**
 * Created by chenjin on 2021/6/9 22:08
 */
public class VariableEntity {
    private String id;
    private String procInstId;
    private String applyPerson;
    private String applyCreateTime;
    private String position;
    private String approvedPerson;
    private String taskType;
    private String applyReason;

    private String text;
    private String name;
    private String procDefId;
    private String processName;  //结点名称


    //辅助参数：按条件查找
    private String startTime;
    private String endTime;

    private String repulseReason; //打回原因

    public String getRepulseReason() {
        return repulseReason;
    }

    public void setRepulseReason(String repulseReason) {
        this.repulseReason = repulseReason;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }

    public String getApplyCreateTime() {
        return applyCreateTime;
    }

    public void setApplyCreateTime(String applyCreateTime) {
        this.applyCreateTime = applyCreateTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getApprovedPerson() {
        return approvedPerson;
    }

    public void setApprovedPerson(String approvedPerson) {
        this.approvedPerson = approvedPerson;
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

    public VariableEntity(String procInstId, String name) {
        this.procInstId = procInstId;
        this.name = name;
    }
    public VariableEntity() {

    }
}
