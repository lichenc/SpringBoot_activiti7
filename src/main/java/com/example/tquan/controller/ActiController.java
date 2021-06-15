package com.example.tquan.controller;


import com.example.tquan.entity.*;
import com.example.tquan.service.PositionService;
import com.example.tquan.service.UserService;
import com.example.tquan.service.VariableService;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import cn.yunlingfy.springbootactiviti.infra.util.UploadFileMgr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


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
    private IdentityService identityService;*/
    // 历史表
/*
    @Resource
    private HistoryService historyService;
*/
    @Autowired
    private com.example.tquan.service.TaskService taskService;
    @Autowired
    private VariableService variableService;
    @Autowired
    private UserService userService;
    @Autowired
    private PositionService positionService;

    private Log log = LogFactory.getLog(getClass());

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
    @RequestMapping(value = "/activi")
    public String start(ActivitiEntity activiti,HttpServletRequest request,HttpSession session) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //可根据id、key、message启动流程
       /* Map<String, Object> map = new HashMap();
        map.put("name", activiti.getName());
        map.put("type", activiti.getType());
        map.put("app", activiti.getApp());
        map.put("role", activiti.getRole());
        map.put("description", activiti.getDescription());*/
        String sn = (String) session.getAttribute("userSn");
        Map<String,Object> map = new HashMap<String,Object>();
        System.out.println(sn);
        map.put("userId","001");
        map.put("user",activiti.getProposer());
        ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("myProcess_1",map);
        String name=activiti.getProposer();
        findTask("001",request);
        return "/apply";
    }

    /**
     * 查询任务
     */

    public HttpServletRequest findTask(String name,HttpServletRequest request) {
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
                CompleteTask(task.getId());
            }
        }
        return request;
    }

    /**
     * 申请人完成申请任务
     * @param taskId
     */
    @RequestMapping("/applicationTasks")
    public void CompleteTask(String taskId) {
        /*HashMap<String, Object> variables = new HashMap<>();
        variables.put("days", days);*///userKey在上文的流程变量中指定了
//        taskService.claim(taskid,"ZJ2");//指定办理人
//        taskService.setAssignee(taskid, null);//回退为组任务状态
        /*taskId="72507";*/
        processEngine.getTaskService()// 与正在执行任务相关的Service
                .complete(taskId);
        System.out.println("完成任务：任务ID：" + taskId);
    }





    /**
     * 审核人查询未拾取任务
     * @return
     */
    @RequestMapping("/audit")
    public HttpServletRequest audit(/*String name, */HttpServletRequest request,HttpSession session) {
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
       /* HttpSession session = sessionRequest.getSession();*/
        String name = (String) session.getAttribute("userSn");
   /*     name="001";*/
        TaskService taskService=processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee("001")//指定个人任务查询
                .list();
        request.setAttribute("task",list);
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println(task.getAssignee()+"任务拾取成功");
                System.out.println(task.getName()+"任务拾取成功");
                System.out.println(task.getCreateTime()+"任务拾取成功");
                System.out.println(task.getId()+"任务拾取成功");
                System.out.println(task.getProcessInstanceId()+"任务拾取成功");
                System.out.println(task+"任务拾取成功");

            }
        }
        return request;
    }

    //打回任务
    @RequestMapping("/repulse")
    public String setTaskAssignee(HttpSession session){
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        String name = (String) session.getAttribute("userSn");
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("001")
                .list();
        Task task = taskService.createTaskQuery().taskId(list.get(0).getId()).singleResult();
        taskService.setAssignee(list.get(0).getId(),null);//归还候选任务
       /* taskService.setAssignee(list.get(0).getId(),"wukong");//交办*/
        return "/audit";
    }

    //审核人拾取任务
    @RequestMapping("/pickup")
    public String claimTask(HttpServletRequest request,HttpSession session){
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        String name = (String) session.getAttribute("userSn");
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("001")
                .list();
        Task task = taskService.createTaskQuery().taskId(list.get(0).getId()).singleResult();
        taskService.claim(list.get(0).getId(),"001");
        System.out.println(list.get(0).getId()+"任务拾取成功");
        //返回所有任务
        request.setAttribute("pickupTask",list);
        for(Task tk : list){
            System.out.println("Id："+tk.getId());
            System.out.println("Name："+tk.getName());
            System.out.println("Assignee："+tk.getAssignee());
        }

        return "/audit";
    }



    /**
     * 审核人完成任务审核
     *
     * @return
     */
    @RequestMapping("/AuditTask")
    public String AuditTask(HttpSession session) {
       try {
           //1:得到ProcessEngine对象
           ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
           //2：得到TaskService对象
           TaskService taskService=processEngine.getTaskService();
           String name = (String) session.getAttribute("userSn");
           List<Task> list = taskService.createTaskQuery()
                   .taskAssignee("001")
                   .list();
           processEngine.getTaskService()// 与正在执行任务相关的Service
                   .complete(list.get(0).getId());

           //如果申请为岗位申请，审批通过之后需要授权申请的岗位
           VariableEntity variableEntity=variableService.getTaskDefByProcInstId(list.get(0).getProcessInstanceId());
           //判断是否是岗位申请任务
           if (variableEntity.getProcDefId().contains("positionApply")){
               String sn=null;
               String position=null;
               //设置查询申请人的参数
               VariableEntity variableEntity1=new VariableEntity();
               variableEntity1.setProcInstId(list.get(0).getProcessInstanceId());
               variableEntity1.setName("applyPerson");
               //查询申请人
               sn= variableService.getTextByName(variableEntity1);


               //设置查询岗位的参数
               VariableEntity variableEntity3=new VariableEntity();
               variableEntity3.setProcInstId(list.get(0).getProcessInstanceId());
               variableEntity3.setName("position");
               //查询申请的岗位
               position=variableService.getTextByName(variableEntity3);

               //设置查询用户的参数
               UserEntity userEntity=new UserEntity();
               userEntity.setSn(sn);
               //根据sn获取用户信息
               UserEntity userEntity1=userService.getUserByProperty(userEntity);
               //获取岗位id
               String positionId=positionService.getPositionByName(position);

               //设置为用户添加岗位的参数
               PositionEntity positionEntity=new PositionEntity();
               positionEntity.setUserId(userEntity1.getId());
               positionEntity.setPositionId(positionId);

               //执行用户关联岗位的add方法
               int iden=positionService.addUserPosition(positionEntity);
               //添加成功
               if (iden !=0){
                   log.info("=========================="+sn+"授权"+position+"成功!");
               }
           }
           System.out.println("完成任务：任务ID：" + list.get(0).getId());
       }catch (Exception e){
           e.printStackTrace();
       }
        return "/audit";
    }



    /**
     * 历史任务查询
     * @return
     */
    @RequestMapping("/completeRecordsTask")
    public String queryDoneTasks(String assignee, HttpServletRequest request,HttpSession session) {
        assignee="001";
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        HistoryService historyService=processEngine.getHistoryService();
        String name = (String) session.getAttribute("userSn");
        List<HistoricTaskInstance> taskList  = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("001")
                .finished()
                .list();
        for (HistoricTaskInstance task : taskList) {
           /* Task leaveTask = new LeaveTask();*/
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getProcessDefinitionId());
            request.setAttribute("completeRecords",taskList);
        }
        return "completeRecords";
    }


}
