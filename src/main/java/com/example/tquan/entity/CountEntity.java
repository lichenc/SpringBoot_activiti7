package com.example.tquan.entity;

import java.util.List;

/**
 * Created by chenjin on 2021/7/2 16:23
 */
public class CountEntity {
    private int sumCount; //总流程数
    private int approvalCount;//待审批数
    private int waitTryAgainCount;//待重试数
    private int historyCount;//已审批数

    private int todaySumCount;//今日新增总流程
    private int todayApprovalCount;//今日新增待审批
    private int todayWaitTryAgainCount;//今日新增待重试
    private int todayHistoryCount;//今日新增已审批



    private List<LineChartEntity> lineChartEntityList;
    private List<LineChartEntity> pieChartEntityList;

    public int getTodaySumCount() {
        return todaySumCount;
    }

    public void setTodaySumCount(int todaySumCount) {
        this.todaySumCount = todaySumCount;
    }

    public int getTodayApprovalCount() {
        return todayApprovalCount;
    }

    public void setTodayApprovalCount(int todayApprovalCount) {
        this.todayApprovalCount = todayApprovalCount;
    }

    public int getTodayWaitTryAgainCount() {
        return todayWaitTryAgainCount;
    }

    public void setTodayWaitTryAgainCount(int todayWaitTryAgainCount) {
        this.todayWaitTryAgainCount = todayWaitTryAgainCount;
    }

    public int getTodayHistoryCount() {
        return todayHistoryCount;
    }

    public void setTodayHistoryCount(int todayHistoryCount) {
        this.todayHistoryCount = todayHistoryCount;
    }

    public List<LineChartEntity> getPieChartEntityList() {
        return pieChartEntityList;
    }

    public void setPieChartEntityList(List<LineChartEntity> pieChartEntityList) {
        this.pieChartEntityList = pieChartEntityList;
    }

    public List<LineChartEntity> getLineChartEntityList() {
        return lineChartEntityList;
    }

    public void setLineChartEntityList(List<LineChartEntity> lineChartEntityList) {
        this.lineChartEntityList = lineChartEntityList;
    }


    public int getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(int historyCount) {
        this.historyCount = historyCount;
    }

    public int getSumCount() {
        return sumCount;
    }

    public void setSumCount(int sumCount) {
        this.sumCount = sumCount;
    }

    public int getApprovalCount() {
        return approvalCount;
    }

    public void setApprovalCount(int approvalCount) {
        this.approvalCount = approvalCount;
    }

    public int getWaitTryAgainCount() {
        return waitTryAgainCount;
    }

    public void setWaitTryAgainCount(int waitTryAgainCount) {
        this.waitTryAgainCount = waitTryAgainCount;
    }
}
