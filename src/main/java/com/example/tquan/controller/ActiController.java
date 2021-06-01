package com.example.tquan.controller;


import org.activiti.engine.*;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import cn.yunlingfy.springbootactiviti.infra.util.UploadFileMgr;
import com.example.tquan.entity.ActivitiEntity;
import com.example.tquan.service.AccountService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
//@RequestMapping("/audit")
public class ActiController{
    // 仓库服务类
   /* @Resource
    private RepositoryService repositoryService;*/
    // 运行服务类
  /*  @Resource
    private RuntimeService runtimeService;
    // 用户任务服务类
    @Resource
    private TaskService taskService;
    // 身份管理和认证(建议自建表)
    @Resource
    private IdentityService identityService;
    // 历史表
    @Resource
    private HistoryService historyService;*/
    // 用户任务服务类

    private TaskService taskService;

    private AccountService accountService;

    /**
     * 会默认按照Resources目录下的activiti.cfg.xml创建流程引擎
     */
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    /**
     * 根据配置文件activiti.cfg.xml创建ProcessEngine
     */

   /* public void CreateProcessEngineByCfgXml() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine engine = cfg.buildProcessEngine();
    }*/

    /**
     * 发布流程
     */
    //@RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.GET})
  /*  public String deployment() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        //String name = "apply.bpmn";
        //builder.addInputStream(name,bpmnfileInputStream);
        builder.addClasspathResource("bpmn/apply.bpmn");
        builder.deploy();
        return "";
    }*/


    /**
     * 启动流程实例,分配任务给个人
     */
   //@PostMapping("/audit")
    @RequestMapping(value = "/activi")
    public String start(ActivitiEntity activiti,HttpServletRequest request) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //可根据id、key、message启动流程
       /* Map<String, Object> map = new HashMap();
        map.put("name", activiti.getName());
        map.put("type", activiti.getType());
        map.put("app", activiti.getApp());
        map.put("role", activiti.getRole());
        map.put("description", activiti.getDescription());*/
        Map<String, Object> variables=new HashMap<String,Object>();
        variables.put("user", activiti.getProposer());
        runtimeService.startProcessInstanceByKey("myProcess_1",variables);
        String name=activiti.getProposer();
        findTask(name,request);
        return "/audit";
    }



   //查询当前人的个人任务
    //@RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.GET})
   //@RequestMapping("/audit")
    public HttpServletRequest findTask(String name,HttpServletRequest request) {
      // String assignee = "001";
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(name)//指定个人任务查询
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                request.setAttribute("task",task);
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());

            }
        }
        return request;
    }
    /**
     * 审核人完成任务
     */
    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.GET})
    public void aaaCompleteTask(String taskId, String days) {
        //任务ID
//        String taskId = "47506";
//        String days="4";

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("days", days);//userKey在上文的流程变量中指定了
//        taskService.claim(taskid,"ZJ2");//指定办理人
//        taskService.setAssignee(taskid, null);//回退为组任务状态

        taskService.complete(taskId, variables);
        System.out.println("完成任务：任务ID：" + taskId);
    }
}
