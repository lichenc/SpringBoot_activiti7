package com.example.tquan.entity;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

/**
 * 用户实体类
 * Created by tangmiaomiao on 2021/5/20 11:45
 */
public class ImApp {
    private String id;
    private String sn;
    private String name;
    private String appName;
    public ImApp(){

    }
    public ImApp(String id,String sn,String name,String appName){
        this.id = id;
        this.sn = sn;
        this.name = name;
        this.appName = appName;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getSn() {
        return sn;
    }

    public String getName() {
        return name;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "ImApp{" +
                "id='" + id + '\'' +
                ", sn='" + sn + '\'' +
                ", name='" + name + '\'' +
                ", appName='" + appName + '\'' +
                '}';
    }
}
