package com.example.tquan.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * 初始化Activit数据库表
 * Created by chenjin on 2021/5/17 16:16
 */
public class InItTables {
    public static void main(String[] args) {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://192.168.23.1:3306/iam?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone = GMT");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("root");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println(processEngine);
    }

}
