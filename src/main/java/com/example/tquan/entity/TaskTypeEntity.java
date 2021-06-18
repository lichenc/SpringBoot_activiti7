package com.example.tquan.entity;

public class TaskTypeEntity {
    private String id;
    private String name;
    private String method;
    private String event_type;

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    @Override
    public String toString() {
        return "TaskTypeEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", method='" + method + '\'' +
                ", event_type='" + event_type + '\'' +
                '}';
    }
}
