package com.example.tquan.entity;

import java.util.Date;

/**
 * Created by chenjin on 2021/6/1 21:16
 */
public class PositionEntity {
    private String id;
    private String sn;
    private String name;
    private String remark;
    private Date createTime;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCompanySn() {
        return companySn;
    }

    public void setCompanySn(String companySn) {
        this.companySn = companySn;
    }

    @Override
    public String toString() {
        return "PositionEntity{" +
                "id='" + id + '\'' +
                ", sn='" + sn + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", companySn='" + companySn + '\'' +
                '}';
    }
}
