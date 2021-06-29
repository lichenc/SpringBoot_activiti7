package com.example.tquan.entity;

/**
 * Created by chenjin on 2021/6/24 17:17
 */
public class VariableAddEntity {
    private String id;
    private int rev;
    private String type;
    private String name;
    private String executionId;
    private String procInstId;
    private String text;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
