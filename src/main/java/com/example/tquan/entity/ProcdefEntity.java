package com.example.tquan.entity;

/**
 * Created by chenjin on 2021/7/8 17:40
 */
public class ProcdefEntity {
    private String procdefId;
    private String version;
    private String name;
    private String keyName;
    private String resourceName;
    private String deploymentId;
    private String status;


    public String getProcdefId() {
        return procdefId;
    }

    public void setProcdefId(String procdefId) {
        this.procdefId = procdefId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
