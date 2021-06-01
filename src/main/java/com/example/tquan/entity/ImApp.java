package com.example.tquan.entity;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

/**
 * 用户实体类
 * Created by tangmiaomiao on 2021/5/20 11:45
 */
public class ImApp {
    private Integer id;
    private String sn;
    private String name;
    public ImApp(){

    }
    public ImApp(Integer id,String sn,String name){
        this.id = id;
        this.sn = sn;
        this.name = name;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getSn() {
        return sn;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "ImApp{" +
                "id=" + id +
                ", sn='" + sn + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
