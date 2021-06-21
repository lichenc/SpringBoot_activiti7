package com.example.tquan.entity;


import java.util.List;

/**
 * Created by chenjin on 2021/6/4 11:15
 */
public class TaskEntity {
    private String id;
    private String event;
    private String eventType;
    private String applyPerson;
    private String approvedPerson;
    private String taskType;
    private String applyReason;



    private List<TaskEntity> taskEntities;
    private int taskCount;

    public String getApplyReason() {
        return applyReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public List<TaskEntity> getTaskEntities() {
        return taskEntities;
    }

    public void setTaskEntities(List<TaskEntity> taskEntities) {
        this.taskEntities = taskEntities;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "event='" + event + '\'' +
                ", eventType='" + eventType + '\'' +
                ", applyPerson='" + applyPerson + '\'' +
                ", approvedPerson='" + approvedPerson + '\'' +
                ", taskType='" + taskType + '\'' +
                '}';
    }

}
