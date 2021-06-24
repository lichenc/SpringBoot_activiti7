package com.example.tquan.entity;

import java.util.Date;
import java.util.List;

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

    //辅助字段：岗位id，用户id   用来添加用户与岗位的关联
    private String userId;
    private String positionId;
    private List<Approver> approverList;    //审批人集合


    private String applyReason;
    private String approvedPerson;
    private String position;

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

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public List<Approver> getApproverList() {
        return approverList;
    }

    public void setApproverList(List<Approver> approverList) {
        this.approverList = approverList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    private List<PositionEntity> positionEntityList;
    private String userSn;

    public List<PositionEntity> getPositionEntityList() {
        return positionEntityList;
    }

    public void setPositionEntityList(List<PositionEntity> positionEntityList) {
        this.positionEntityList = positionEntityList;
    }

    public String getUserSn() {
        return userSn;
    }

    public void setUserSn(String userSn) {
        this.userSn = userSn;
    }

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
