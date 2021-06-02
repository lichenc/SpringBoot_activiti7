package com.example.tquan.entity;

import java.util.Date;

/**
 * Created by chenjin on 2021/6/1 22:23
 */
public class GroupEntity {
    private String id;
    private String sn;
    private String name;
    private String remark;
    private  String companySn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompanySn() {
        return companySn;
    }

    public void setCompanySn(String companySn) {
        this.companySn = companySn;
    }
}
