package com.example.tquan;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class TquanApplication {
  ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    public static void main(String[] args) {
        SpringApplication.run(TquanApplication.class, args);
    }
@Test
  public void testCreateProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        cfg.setJdbcDriver("com.mysql.cj.jdbc.Drivers");
        cfg.setJdbcUrl("jdbc:mysql://192.168.23.1:3306/iam");
        cfg.setJdbcUsername("root");
        cfg.setJdbcPassword("root");
        //配置建表策略
        cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine engine = cfg.buildProcessEngine();
    }

    public void CreateProcessEngineByCfgXml() {

        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine engine = cfg.buildProcessEngine();
    }

    @Test
    public void deployProcess() throws FileNotFoundException {
        String bpmnPath = "E:/idea/tongquan/src/main/resources/bpmn/apply.xml";
        //读取资源作为一个输入流
        FileInputStream bpmnfileInputStream = new FileInputStream(bpmnPath);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        String name = "apply.bpmn";
        builder.addInputStream(name,bpmnfileInputStream);
        //builder.addClasspathResource("apply.xml");
        builder.deploy();
    }
}
