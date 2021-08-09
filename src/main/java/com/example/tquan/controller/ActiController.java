package com.example.tquan.controller;


import com.beust.jcommander.internal.Lists;
import com.example.tquan.entity.*;
import com.example.tquan.service.*;
import com.example.tquan.util.IamInterface;
import com.example.tquan.util.PwdNum;
import com.example.tquan.util.RsaUtil;
/*import net.sf.json.JSONObject;*/
import com.ninghang.core.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Comment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
/*import net.sf.json.JSONObject;*/


import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;

import java.io.*;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.util.*;
import java.text.SimpleDateFormat;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;








@Controller

//@RequestMapping("/audit")
public class ActiController{
    @Autowired
    private IamOauthEntity iam;
    @Autowired
    private IamAccountEntity account;
    @Autowired
    private  IamUserEntity user;
    @Autowired
    private DefaultService defaultService;
    @Autowired
    private ImAppService imAppService;
    @Autowired
    private ActVerService actVerService;
    @Autowired
    private ApproverService approverService ;
    @Autowired
    private TaskTypeService taskTypeService;
    /**
     * 会默认按照Resources目录下的activiti.cfg.xml创建流程引擎
     */
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @Autowired
    private VariableService variableService;
    @Autowired
    private UserService userService;
    @Autowired
    private PositionService positionService;

    private Log log = LogFactory.getLog(getClass());
    IamInterface iamInterface=new IamInterface();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.US);

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
     *
     */
    @RequestMapping("/verifyActTask")
    @ResponseBody
    public List<ActivitiEntity> start(@Param("id")String id,@Param("taskType")String taskType, @Param("applyName")String applyName,
                                      @Param("appName")String appName,@Param("accountName")String accountName,
                                      @Param("orgId")String orgId,@Param("audit")String audit,
                                      @Param("role")String role,@Param("applyReason")String applyReason,
                                      @Param("accountOrg")String accountOrg,@Param("accountOrgId")String accountOrgId,@Param("actType")String actType,
                                      @Param("textList")String textList,@Param("passwordList")String passwordList,
                                      @Param("dateList")String dateList,@Param("selectList")String selectList,
                                      HttpSession session) {
        List<ActivitiEntity> listTask=new ArrayList<>();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //可根据id、key、message启动流程
        String sn = (String) session.getAttribute("userSn");
        String name = (String) session.getAttribute("userName");
        String userId = (String) session.getAttribute("UserId");
        //查询申请的账号
        List<ImApp> list=imAppService.findApply(appName);
        Map<String,Object> map = new HashMap<String,Object>();
        System.out.println(name+":当前申请人账号："+sn+":id:"+id);
        TaskService taskSer=processEngine.getTaskService();
        List<Task> listAlles= taskSer.createTaskQuery()//创建任务查询对象
                .taskAssignee(sn)//指定个人任务查询
                .list();
        if(id==null||id==""){
            if(taskType.equals("帐号新增")==true){
                List<DefaultEntity> actNums = defaultService.actNum(accountName, applyName);
                if(actNums.size()>0){
                    System.out.println(actNums.get(0).getSta());
                    if("1".equals(actNums.get(0).getSta())){
                        ActivitiEntity task=new ActivitiEntity();
                        task.setTaskId("1");
                        listTask.add(task);
                        return listTask;
                    }else if("2".equals(actNums.get(0).getSta())){
                        ActivitiEntity task=new ActivitiEntity();
                        task.setTaskId("2");
                        listTask.add(task);
                        return listTask;
                    }
                }else {
                    map.put("applyPerson",sn);
                    String audits=audit.substring(audit.indexOf("(")+1,audit.indexOf(")"));
                    map.put("approvedPerson",audits);
                    map.put("name",audits);
                    map.put("taskType",taskType);
                    map.put("app",appName);
                    map.put("appId",list.get(0).getId());
                    map.put("role",role);
                    map.put("account",accountName);
                    map.put("status","1");
                    map.put("usersId",userId);
                    /*map.put("orgName",orgName);*/
                    map.put("orgId",orgId);
                    map.put("accountOrgId",accountOrgId);
                    map.put("actType",actType);
                    map.put("accountOrg",accountOrg);
                    map.put("applyReason",applyReason);
                    map.put("textList",textList);
                    map.put("passwordList",passwordList);
                    map.put("dateList",dateList);
                    map.put("selectList",selectList);
                    ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("myProcess_1",map);
                    TaskService taskService=processEngine.getTaskService();
                    List<Task> lists = taskService.createTaskQuery()//创建任务查询对象
                            .taskAssignee(sn)//指定个人任务查询
                            .orderByTaskCreateTime()//根据时间倒叙
                            .desc()
                            .list();
                    System.out.println("时间倒叙的id："+lists.get(0).getId());
                    processEngine.getTaskService()// 与正在执行任务相关的Service
                            .complete(lists.get(0).getId());
                    findTasks(session);
                }
            }if(taskType.equals("帐号修改")==true){
                map.put("applyPerson",sn);
                String audits=audit.substring(audit.indexOf("(")+1,audit.indexOf(")"));
                map.put("approvedPerson",audits);
                map.put("name",audits);
                map.put("taskType",taskType);
                map.put("app",appName);
                map.put("appId",list.get(0).getId());
                map.put("role",role);
                map.put("account",accountName);
                map.put("status","1");
                map.put("usersId",userId);
                /*map.put("orgName",orgName);*/
                map.put("orgId",orgId);
                map.put("accountOrgId",accountOrgId);
                map.put("actType",actType);
                map.put("accountOrg",accountOrg);
                map.put("applyReason",applyReason);
                map.put("textList",textList);
                map.put("passwordList",passwordList);
                map.put("dateList",dateList);
                map.put("selectList",selectList);
                ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("myProcess_1",map);
                TaskService taskService=processEngine.getTaskService();
                List<Task> lists = taskService.createTaskQuery()//创建任务查询对象
                        .taskAssignee(sn)//指定个人任务查询
                        .orderByTaskCreateTime()//根据时间倒叙
                        .desc()
                        .list();
                processEngine.getTaskService()// 与正在执行任务相关的Service
                        .complete(lists.get(0).getId());
                findTasks(session);
            } else if(taskType.equals("帐号启用")==true){
                map.put("applyPerson",sn);
                String audits=audit.substring(audit.indexOf("(")+1,audit.indexOf(")"));
                map.put("approvedPerson",audits);
                map.put("name",audits);
                map.put("taskType",taskType);
                map.put("app",appName);
                map.put("appId",list.get(0).getId());
                map.put("role",role);
                map.put("account",accountName);
                map.put("status","1");
                map.put("usersId",userId);
                /*map.put("orgName",orgName);*/
                map.put("accountOrg",accountOrg);
                map.put("applyReason",applyReason);
                ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("myProcess_1",map);
                TaskService taskService=processEngine.getTaskService();
                List<Task> lists = taskService.createTaskQuery()//创建任务查询对象
                        .taskAssignee(sn)//指定个人任务查询
                        .orderByTaskCreateTime()//根据时间倒叙
                        .desc()
                        .list();
                processEngine.getTaskService()// 与正在执行任务相关的Service
                        .complete(lists.get(0).getId());
                findTasks(session);
            }
        }else {
            for (int p=0;p<listAlles.size();p++) {
                System.out.println(id+"*****"+listAlles.get(p).getProcessInstanceId());
                if(id.equals(listAlles.get(p).getProcessInstanceId())){
                    if(taskType.equals("帐号新增")==true){
                        List actNums = defaultService.actNum(accountName, applyName);
                        System.out.println(actNums.size());
                        if(actNums.size()>0){
                            System.out.println(actNums.get(0).toString());
                            if("1".equals(actNums.get(0).toString())){
                                ActivitiEntity task=new ActivitiEntity();
                                task.setTaskId("1");
                                listTask.add(task);
                                return listTask;
                            }else if("2".equals(actNums.get(0).toString())){
                                ActivitiEntity task=new ActivitiEntity();
                                task.setTaskId("2");
                                listTask.add(task);
                                return listTask;
                            }
                        }else {
                            map.put("applyPerson",sn);
                            String audits=audit.substring(audit.indexOf("(")+1,audit.indexOf(")"));
                            map.put("approvedPerson",audits);
                            map.put("name",audits);
                            map.put("taskType",taskType);
                            map.put("app",appName);
                            map.put("appId",list.get(0).getId());
                            map.put("role",role);
                            map.put("account",accountName);
                            map.put("status","1");
                            map.put("usersId",userId);
                            /*map.put("orgName",orgName);*/
                            map.put("orgId",orgId);
                            map.put("accountOrgId",accountOrgId);
                            map.put("actType",actType);
                            map.put("accountOrg",accountOrg);
                            map.put("applyReason",applyReason);
                            map.put("textList",textList);
                            map.put("passwordList",passwordList);
                            map.put("dateList",dateList);
                            map.put("selectList",selectList);
                            runtimeService.setVariablesLocal(id,map);
                            TaskService taskService=processEngine.getTaskService();
                            Task lists = taskService.createTaskQuery()//创建任务查询对象
                                    .processInstanceId(id)
                                    .singleResult();
                            processEngine.getTaskService()// 与正在执行任务相关的Service
                                    .complete(lists.getId());
                            findTasks(session);
                        }
                    }if(taskType.equals("帐号修改")==true){
                        map.put("applyPerson",sn);
                        String audits=audit.substring(audit.indexOf("(")+1,audit.indexOf(")"));
                        map.put("approvedPerson",audits);
                        map.put("name",audits);
                        map.put("taskType",taskType);
                        map.put("app",appName);
                        map.put("appId",list.get(0).getId());
                        map.put("role",role);
                        map.put("account",accountName);
                        map.put("status","1");
                        map.put("usersId",userId);
                        /*map.put("orgName",orgName);*/
                        map.put("orgId",orgId);
                        map.put("accountOrgId",accountOrgId);
                        map.put("actType",actType);
                        map.put("accountOrg",accountOrg);
                        map.put("applyReason",applyReason);
                        map.put("textList",textList);
                        map.put("passwordList",passwordList);
                        map.put("dateList",dateList);
                        map.put("selectList",selectList);
                        runtimeService.setVariables(id,map);
                        TaskService taskService=processEngine.getTaskService();
                        Task lists = taskService.createTaskQuery()//创建任务查询对象
                                .processInstanceId(id)
                                .singleResult();
                        processEngine.getTaskService()// 与正在执行任务相关的Service
                                .complete(lists.getId());
                        findTasks(session);
                    } else if(taskType.equals("帐号启用")==true){
                        map.put("applyPerson",sn);
                        String audits=audit.substring(audit.indexOf("(")+1,audit.indexOf(")"));
                        map.put("approvedPerson",audits);
                        map.put("name",audits);
                        map.put("taskType",taskType);
                        map.put("app",appName);
                        map.put("appId",list.get(0).getId());
                        map.put("role",role);
                        map.put("account",accountName);
                        map.put("status","1");
                        map.put("usersId",userId);
                        /*map.put("orgName",orgName);*/
                        map.put("accountOrg",accountOrg);
                        map.put("applyReason",applyReason);
                        runtimeService.setVariables(id,map);
                        TaskService taskService=processEngine.getTaskService();
                        Task lists = taskService.createTaskQuery()//创建任务查询对象
                                .processInstanceId(id)
                                .singleResult();
                        processEngine.getTaskService()// 与正在执行任务相关的Service
                                .complete(lists.getId());
                        findTasks(session);
                    }
                }
            }
        }
        return findTasks(session);
    }

    /**
     * 查询全部任务
     * @return
     */
    @RequestMapping("/selectApplyTast")
    @ResponseBody
    public  List<ActivitiEntity> findTasks(HttpSession session){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.US);
        String name = (String) session.getAttribute("userName");
        String login_name = (String) session.getAttribute("userSn");
        List<ActivitiEntity> listTask=new ArrayList<>();
        List<Approver> audits=approverService.audit(login_name);
        String audit=audits.get(0).getAudit().substring(audits.get(0).getAudit().indexOf("(")+1,audits.get(0).getAudit().indexOf(")"));
        TaskService taskService=processEngine.getTaskService();
        List<Task> listsl = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(audit)//指定个人任务查询
                .list();
        List<Task> lists2 = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(login_name)//指定个人任务查询
                .list();
        if(audit.equals(login_name)){
            for (int p=0;p<listsl.size();p++){
                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(listsl.get(p).getProcessInstanceId().toString());
                if(!variables.get("taskType").toString().equals("岗位新增")){
                    ActivitiEntity task=new ActivitiEntity();
                    task.setTaskId(listsl.get(p).getId());
                    task.setTaskName(listsl.get(p).getName());
                    task.setTaskAssignee(listsl.get(p).getAssignee());
                    task.setTaskCreateTime(df.format(listsl.get(p).getCreateTime()));
                    task.setTaskExecutionId(listsl.get(p).getExecutionId());
                    task.setTaskProcessInstanceId(listsl.get(p).getProcessInstanceId());
                    task.setTaskApp(variables.get("app").toString());
                    task.setTaskTypes(variables.get("taskType").toString());
                    task.setTaskAccount(variables.get("account").toString());
                    task.setTaskApplyPerson(name);
                    task.setTaskApprovedPerson(audits.get(0).getAudit());
                    for (Map.Entry<String, Object> entry : variables.entrySet()) {
                        if ("applyPerson".equals(entry.getKey())&&login_name.equals(entry.getValue())) {
                            listTask.add(task);
                        }else {

                        }
                }
                }
            }
        }else {
            for (int p=0;p<listsl.size();p++){
                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(listsl.get(p).getProcessInstanceId().toString());
                if(!variables.get("taskType").toString().equals("岗位新增")) {
                    ActivitiEntity task = new ActivitiEntity();
                    task.setTaskId(listsl.get(p).getId());
                    task.setTaskName(listsl.get(p).getName());
                    task.setTaskAssignee(listsl.get(p).getAssignee());
                    task.setTaskCreateTime(df.format(listsl.get(p).getCreateTime()));
                    task.setTaskExecutionId(listsl.get(p).getExecutionId());
                    task.setTaskProcessInstanceId(listsl.get(p).getProcessInstanceId());
                    task.setTaskApp(variables.get("app").toString());
                    task.setTaskTypes(variables.get("taskType").toString());
                    task.setTaskAccount(variables.get("account").toString());
                    task.setTaskApplyPerson(name);
                    task.setTaskApprovedPerson(audits.get(0).getAudit());
                    for (Map.Entry<String, Object> entry : variables.entrySet()) {
                        if ("applyPerson".equals(entry.getKey()) && login_name.equals(entry.getValue())) {
                            listTask.add(task);
                        } else {

                        }
                    }
                }
            }
            for (int p=0;p<lists2.size();p++) {
                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(lists2.get(p).getProcessInstanceId().toString());
                if(!variables.get("taskType").toString().equals("岗位新增")) {
                    ActivitiEntity task = new ActivitiEntity();
                    task.setTaskId(lists2.get(p).getId());
                    task.setTaskName(lists2.get(p).getName());
                    task.setTaskAssignee(lists2.get(p).getAssignee());
                    task.setTaskCreateTime(df.format(lists2.get(p).getCreateTime()));
                    task.setTaskExecutionId(lists2.get(p).getExecutionId());
                    task.setTaskProcessInstanceId(lists2.get(p).getProcessInstanceId());
                    task.setTaskApp(variables.get("app").toString());
                    task.setTaskTypes(variables.get("taskType").toString());
                    task.setTaskAccount(variables.get("account").toString());
                    task.setTaskApplyPerson(name);
                    task.setTaskApprovedPerson(audits.get(0).getAudit());
                    listTask.add(task);
                }
            }
        }

        return listTask;
    }


    /**
     * 审核人查询全部任务
     */
    @RequestMapping("/able")
    @ResponseBody
    public List<AuditEntity> able(HttpSession session) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.US);
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        /* HttpSession session = sessionRequest.getSession();*/
        List<AuditEntity> lists=new ArrayList<>();
        String name = (String) session.getAttribute("userSn");
        List<Approver> audits=approverService.audit(name);
        TaskService taskServices=processEngine.getTaskService();
        List<Task> list = taskServices.createTaskQuery()//创建任务查询对象
                .taskAssignee(name)//指定个人任务查询
                .list();
        for(Task tk : list){
            if(!"_3".equals(tk.getTaskDefinitionKey())){
                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(tk.getProcessInstanceId());
                AuditEntity task=new AuditEntity();
                TaskService taskService=processEngine.getTaskService();
                Task li = taskService.createTaskQuery()//创建任务查询对象
                        .processInstanceId(tk.getProcessInstanceId())
                        .singleResult();
                HistoryService historyService = processEngine.getHistoryService();
                List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(li.getProcessInstanceId()).list();
                for (HistoricActivityInstance hai : hais) {
                    String historytaskId = hai.getTaskId();
                    List<Comment> comments = taskService.getTaskComments(historytaskId);
                    // 如果当前任务有批注信息，添加到集合中
                    if(comments!=null && comments.size()>0){
                        task.setComment(comments);
                    }
                }
                if (!variables.get("taskType").equals("岗位新增")){
                   /* task.setOrgName(variables.get("orgName").toString());*/
                    task.setRole(variables.get("role").toString());
                    task.setAccount(variables.get("account").toString());
                    task.setActType(variables.get("actType").toString());
                    task.setApp(variables.get("app").toString());
                }
                task.setId(li.getId());
                task.setName(li.getName());
                task.setAssignee(li.getAssignee());
                task.setTime(df.format(li.getCreateTime()));
                task.setExecutionId(li.getExecutionId());
                task.setProcessInstanceId(li.getProcessInstanceId());
                task.setApprovedPerson(audits.get(0).getAudit());
                task.setTypes(variables.get("taskType").toString());
                task.setApplyPerson(variables.get("applyPerson").toString());
                lists.add(task);
                System.out.println(lists);
            }
        }
        return lists;
    }


    //驳回任务
    @RequestMapping("/repulse")
    @ResponseBody
    public List setTaskAssignee(@RequestParam("userName") String userName,@RequestParam("id") String pid,@RequestParam("approvalOpinion")String approvalOpinion,HttpSession session,String id,String repulseReason){
        List list1=new ArrayList();
        String name = (String) session.getAttribute("userSn");
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        Task list2 = taskService.createTaskQuery()//创建任务查询对象
                .processInstanceId(pid)
                .singleResult();
        System.out.println(list2.getId()+"***"+pid+"***"+approvalOpinion+"***"+userName);
        taskService.addComment(list2.getId(),pid,approvalOpinion);//增加批注
        //taskService.setAssignee(list2.getId(),null);//归还候选任务
        Map<String, Object> variables = new HashMap<>();
        //msg来决定流程走向。具体看bpmn图
        variables.put("msg", false);
        list2.setAssignee(userName);
        taskService.setVariableLocal(list2.getId(),"msg",false);
        taskService.complete(list2.getId(), variables);

        //查询是否是第一次打回
        VariableEntity variableEntity = new VariableEntity();
        variableEntity.setName("repulseReason");
        variableEntity.setProcInstId(id);
        String text = variableService.getTextByName(variableEntity);
        //多次打回
        if (text != null) {
            VariableEntity variableEntity1 = new VariableEntity();
            variableEntity1.setProcInstId(id);
            variableEntity1.setName("repulseReason");
            variableEntity1.setText(approvalOpinion);
            variableService.updateTaskParam(variableEntity1);
            //第一次打回
        } else {
            //新增打回原因
            //设置新增打回原因的参数
            VariableAddEntity variableAddEntity = new VariableAddEntity();
            variableAddEntity.setRev(1);
            int ids = (int) (Math.random() * 9000) + 1000;
            variableAddEntity.setId(String.valueOf(ids));
            variableAddEntity.setType("string");
            variableAddEntity.setName("repulseReason");
            variableAddEntity.setExecutionId(id);
            variableAddEntity.setProcInstId(id);
            variableAddEntity.setText(approvalOpinion);
            variableService.addRepulseReason(variableAddEntity);
        }
        list1.add(0);
        return list1;
    }

    //审核人查询任务详细信息
    @RequestMapping("/SelAuditTask")
    @ResponseBody
    public List audit(@Param("id")String id, HttpServletRequest request,HttpSession session) {
        List<ActivitiEntity> list=new ArrayList<>();
        String login_name = (String) session.getAttribute("userSn");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService=processEngine.getTaskService();
        //默认当前人拾取任务
        Task listClaim = taskService.createTaskQuery()//创建任务查询对象
                .processInstanceId(id)
                .singleResult();
        taskService.claim(listClaim.getId(),login_name);
        List<Approver> audits=approverService.audit(login_name);
        Map<String, Object> variables = processEngine.getRuntimeService().getVariables(id);
        ActivitiEntity task=new ActivitiEntity();
        Task listsl = taskService.createTaskQuery()//创建任务查询对象
                .processInstanceId(id)
                .singleResult();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(listsl.getProcessInstanceId()).list();
        for (HistoricActivityInstance hai : hais) {
            String historytaskId = hai.getTaskId();
            List<Comment> comments = taskService.getTaskComments(historytaskId);
            // 4）如果当前任务有批注信息，添加到集合中
            if(comments!=null && comments.size()>0){
                task.setComment(comments);
            }else {
                task.setComment(comments);
            }
        }
        if (!variables.get("taskType").toString().equals("岗位新增")){
            task.setTaskId(listsl.getId());
            task.setTaskName(listsl.getName());
            task.setTaskAssignee(listsl.getAssignee());
            task.setTaskCreateTime(df.format(listsl.getCreateTime()));
            task.setTaskExecutionId(listsl.getExecutionId());
            task.setTaskProcessInstanceId(listsl.getProcessInstanceId());
            task.setTaskApprovedPerson(audits.get(0).getAudit());
           /* task.setTaskOrgName(variables.get("orgName").toString());*/
            task.setTaskApplyReason(variables.get("applyReason").toString());
            task.setTaskRole(variables.get("role").toString());
            task.setTaskTypes(variables.get("taskType").toString());
            task.setTaskAccount(variables.get("account").toString());
            task.setTaskActType(variables.get("actType").toString());
            task.setTaskApplyPerson(variables.get("applyPerson").toString());
            task.setTaskApp(variables.get("app").toString());
            List<ActEntity> accountField=defaultService.act(variables.get("app").toString());
            JSONObject textList = JSONObject.fromObject(variables.get("textList").toString());
            JSONArray textListKey = JSONArray.fromObject(textList.keySet());
            JSONArray textListValue = JSONArray.fromObject(textList.values());
            List<DefaultsEntity> textList2=new ArrayList<>();
            for (int text=0;text<textList.size();text++) {
                for (int i=0;i<accountField.size();i++) {
                    if(accountField.get(i).getName().equals(textListKey.get(text).toString())){
                        DefaultsEntity defaultsEntity = new DefaultsEntity();
                        defaultsEntity.setRemarks(accountField.get(i).getRemark());
                        defaultsEntity.setNames(textListKey.get(text).toString());
                        defaultsEntity.setDefaultValues(textListValue.get(text).toString());
                        textList2.add(defaultsEntity);
                    }
                }
            }
            task.setTextLists(textList2);
            JSONObject passwordList = JSONObject.fromObject(variables.get("passwordList").toString());
            JSONArray passwordListKey = JSONArray.fromObject(passwordList.keySet());
            JSONArray passwordListValue = JSONArray.fromObject(passwordList.values());
            List<DefaultsEntity> passwordList2=new ArrayList<>();
            for (int text=0;text<passwordList.size();text++) {
                for (int i=0;i<accountField.size();i++) {
                    if(accountField.get(i).getName().equals(passwordListKey.get(text).toString())) {
                        DefaultsEntity defaultsEntity = new DefaultsEntity();
                        defaultsEntity.setRemarks(accountField.get(i).getRemark());
                        defaultsEntity.setNames(passwordListKey.get(text).toString());
                        defaultsEntity.setDefaultValues(passwordListValue.get(text).toString());
                        passwordList2.add(defaultsEntity);
                    }
                }
            }
            task.setPasswordLists(passwordList2);
            JSONObject selectList = JSONObject.fromObject(variables.get("selectList").toString());
            JSONArray selectListKey = JSONArray.fromObject(selectList.keySet());
            JSONArray selectListValue = JSONArray.fromObject(selectList.values());
            List<DefaultsEntity> selectList2=new ArrayList<>();
            //获取下拉框的value值
            for (int text=0;text<selectList.size();text++) {
                for (int i=0;i<accountField.size();i++) {
                    if(accountField.get(i).getName().equals(selectListKey.get(text).toString())) {
                        DefaultsEntity defaultsEntity = new DefaultsEntity();
                        if(StringUtils.isEmpty(accountField.get(i).getCompant())){

                        }else {
                            String compantStr = accountField.get(i).getCompant();
                            List<String> result = Arrays.asList(compantStr.split(","));
                            for(int c=0;c<result.size();c++){
                                String [] com= result.get(c).split("[=]");
                                String keys= com[0];
                                String values= com[1];
                                if(keys.equals(selectListValue.get(text).toString())){
                                    defaultsEntity.setDefaultValues(values);
                                }
                            }
                        }
                        defaultsEntity.setRemarks(accountField.get(i).getRemark());
                        defaultsEntity.setNames(selectListKey.get(text).toString());
                        selectList2.add(defaultsEntity);
                    }
                }
            }
            task.setSelectLists(selectList2);
            JSONObject dateList = JSONObject.fromObject(variables.get("dateList").toString());
            JSONArray dateListKey = JSONArray.fromObject(dateList.keySet());
            JSONArray dateListValue = JSONArray.fromObject(dateList.values());
            List<DefaultsEntity> dateList2=new ArrayList<>();
            for (int text=0;text<dateList.size();text++) {
                for (int i=0;i<accountField.size();i++) {
                    if(accountField.get(i).getName().equals(dateListKey.get(text).toString())) {
                        DefaultsEntity defaultsEntity = new DefaultsEntity();
                        defaultsEntity.setRemarks(accountField.get(i).getRemark());
                        defaultsEntity.setNames(dateListKey.get(text).toString());
                        defaultsEntity.setDefaultValues(dateListValue.get(text).toString());
                        dateList2.add(defaultsEntity);
                    }
                }
            }
            task.setDateLists(dateList2);
        }
        task.setTaskId(listsl.getId());
        task.setTaskName(listsl.getName());
        task.setTaskAssignee(listsl.getAssignee());
        task.setTaskCreateTime(df.format(listsl.getCreateTime()));
        task.setTaskExecutionId(listsl.getExecutionId());
        task.setTaskProcessInstanceId(listsl.getProcessInstanceId());
        task.setTaskApprovedPerson(audits.get(0).getAudit());

        task.setTaskApplyReason(variables.get("applyReason").toString());

        task.setTaskTypes(variables.get("taskType").toString());

        task.setTaskApplyPerson(variables.get("applyPerson").toString());
        list.add(task);
        System.out.println(list.toString());
        return list;
    }


    /**
     * 审核人完成任务审核
     *
     * @return
     */
    @RequestMapping("/AuditTask")
    @ResponseBody
    public List AuditTask(@RequestParam("id") String pid,@RequestParam("approvalOpinion") String approvalOpinion, HttpSession session, String uuid) throws Exception {
        String userId = (String) session.getAttribute("UserId");
        List<ActivitiEntity> li=new ArrayList<>();
        //1:得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processInstanceId(pid)
                .singleResult();
        String processInstanceId = task.getProcessInstanceId();
        Map<String, Object> variables = processEngine.getRuntimeService().getVariables(processInstanceId);
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            System.out.println(entry + "Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        Map<String, Object> variable = new HashMap<>();
        //msg来决定流程是否完成。具体看bpmn图
        variable.put("msg", true);
        processEngine.getTaskService()// 与正在执行任务相关的Service
                .complete(task.getId(),variable);
        //如果申请为岗位申请，审批通过之后需要授权申请的岗位
        VariableEntity variableEntity = variableService.getTaskDefByProcInstId(task.getId());
        if (variableEntity != null) {
            //判断是否是岗位申请任务
            if (variableEntity.getProcDefId().contains("positionApply")) {
                String sn = null;
                String position = null;
                //设置查询申请人的参数
                VariableEntity variableEntity1 = new VariableEntity();
                variableEntity1.setProcInstId(processInstanceId);
                variableEntity1.setName("applyPerson");
                //查询申请人
                sn = variableService.getTextByName(variableEntity1);


                //设置查询岗位的参数
                VariableEntity variableEntity3 = new VariableEntity();
                variableEntity3.setProcInstId(processInstanceId);
                variableEntity3.setName("position");
                //查询申请的岗位
                position = variableService.getTextByName(variableEntity3);

                //设置查询用户的参数
                UserEntity userEntity = new UserEntity();
                userEntity.setSn(sn);
                //根据sn获取用户信息
                UserEntity userEntity1 = userService.getUserByProperty(userEntity);
                //获取岗位id
                String positionId = positionService.getPositionByName(position);

                //设置为用户添加岗位的参数
                PositionEntity positionEntity = new PositionEntity();
                positionEntity.setUserId(userEntity1.getId());
                positionEntity.setPositionId(positionId);
                //用户移动
                //账号
                String ifo=iamInterface.oauth(uuid,iam.getKey(),iam.getPassword(),iam.getAddr(),iam.getUsername(),iam.getType(),iam.getCharset());
                if (StringUtils.isEmpty(ifo.toString())) {
                    System.out.println("uuid为空，认证失败，无法移动用户");
                } else {
                    /*ids[0]: 1628394176024783_1626841411357127
                     * newOrgId: 1626841471677700
                    * */
                    List<NameValuePair> params = Lists.newArrayList();
                    params.add(new BasicNameValuePair("uim-login-user-id",ifo.toString()));
                    params.add(new BasicNameValuePair("ids[0]", userId+"_"+defaultService.org(userId)));
                    params.add(new BasicNameValuePair("newOrgId", variable.get("orgId").toString()));
                    //转换为键值对
                    String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                    String ifa=iamInterface.movenUser(str,user.getAddr(),user.getType());
                    System.out.println("======= 用户移动结果：" + ifa);
                }
                //执行用户关联岗位的add方法
                int iden = positionService.addUserPosition(positionEntity);
                //添加成功
                if (iden != 0) {
                    log.info("==========================" + sn + "授权" + position + "成功!");
                }
            } else {
                //账号
                String ifo=iamInterface.oauth(uuid,iam.getKey(),iam.getPassword(),iam.getAddr(),iam.getUsername(),iam.getType(),iam.getCharset());
                if (StringUtils.isEmpty(ifo.toString())) {
                    System.out.println("uuid为空，认证失败，无法操作账号");
                } else {
                    if("帐号新增".equals(variables.get("taskType").toString())){
                        String pwds = "";
                        //默认密码
                        List<ActEntity> accountField=defaultService.act(variables.get("app").toString());
                        for (int i=0;i<accountField.size();i++) {
                            if (StringUtils.isEmpty(accountField.get(i).getDefaultValue())) {
                                if ("LOGIN_PWD".equals(accountField.get(i).getName()) ){
                                    pwds = accountField.get(i).getDefaultValue();
                                }
                            } else {
                                if (account.getRandomSwitch().equals("true") && !account.getImmobilizationSwitch().equals("true")) {
                                    //随机数
                                    PwdNum pwd = new PwdNum();
                                    pwds = pwd.Pwd(account.getPwdRank(), account.getStrLength(), pwds);

                                } else if (account.getImmobilizationSwitch().equals("true") && !account.getRandomSwitch().equals("true")) {
                                    pwds = account.getPwdDefault();
                                } else {
                                    pwds = "000000";
                                }
                            }
                        }
                        System.out.println("账号密码："+pwds);
                        List<NameValuePair> params = Lists.newArrayList();
                        params.add(new BasicNameValuePair("uim-login-user-id",ifo.toString()));
                        params.add(new BasicNameValuePair("loginName", variables.get("account").toString()));
                        params.add(new BasicNameValuePair("loginPwd", pwds));
                        params.add(new BasicNameValuePair("appId", variables.get("appId").toString()));
                        params.add(new BasicNameValuePair("userId", variables.get("usersId").toString()));
                        params.add(new BasicNameValuePair("status", variables.get("status").toString()));
                        //转换为键值对
                        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                        String ifa=iamInterface.accountSave(str,account.getAddr(),account.getType());
                        System.out.println("======= 新增账号结果：" + ifa);

                    }else if("帐号启用".equals(variables.get("taskType").toString())){
                        List list1=defaultService.actField(variables.get("app").toString(),variables.get("account").toString());
                        List<NameValuePair> params = Lists.newArrayList();
                        params.add(new BasicNameValuePair("uim-login-user-id",ifo.toString()));
                        params.add(new BasicNameValuePair("ids", list1.toString()));

                        //转换为键值对
                        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                        String ifa=iamInterface.accountStart(str,account.getAddr(),account.getType());
                        System.out.println("======= 启用账号结果：" + ifa);
                    }else if("帐号修改".equals(variables.get("taskType").toString())){
                        List list1=defaultService.actField(variables.get("app").toString(),variables.get("account").toString());
                        List<NameValuePair> params = Lists.newArrayList();
                        params.add(new BasicNameValuePair("uim-login-user-id",ifo.toString()));
                        params.add(new BasicNameValuePair("loginName", variables.get("account").toString()));
                        params.add(new BasicNameValuePair("appId", variables.get("appId").toString()));
                        params.add(new BasicNameValuePair("userId", variables.get("usersId").toString()));
                        params.add(new BasicNameValuePair("status", variables.get("status").toString()));
                        params.add(new BasicNameValuePair("id", list1.toString()));
                        //转换为键值对
                        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                        String ifa=iamInterface.accountUpdate(str,account.getAddr(),account.getType());
                        System.out.println("======= 修改账号结果：" + ifa);
                    }

                }
            }
        }

        return li;
    }



    /**
     * 申请人删除任务
     *
     * @return
     */
    @RequestMapping("/deleteTask")
    @ResponseBody
    public int deleteTask (@RequestParam("id") String pid){
        int withdraw=0;
        //1:得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2：得到HistoryService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.deleteProcessInstance(pid, "是的");
        withdraw=1;
        return withdraw;
    }

    /**
     * 申请人撤回未审批的任务
     *
     * @return
     */
    @RequestMapping("/withdraw")
    @ResponseBody
    public int withdraw (@RequestParam("id") String id, HttpSession session, HttpServletRequest request) throws
            Exception {
        int withdraw=0;
        //1:得到ProcessEngine对象
        /*        id="10049";*/
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService = processEngine.getTaskService();
        //2：得到HistoryService对象
        HistoryService historyService = processEngine.getHistoryService();
        //2：得到RuntimeService对象
        String name = (String) session.getAttribute("userSn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        Task task = taskService.createTaskQuery()
                /* .taskAssignee(name)*/
                .processInstanceId(id)
                .singleResult();//审核人
        System.out.println(task.getId());
        if (task == null) {
            /// throw new ServiceException("流程未启动或已执行完成，无法撤回");
            System.out.println("流程未启动或已执行完成，无法撤回");
            withdraw=1;
        } else {
            System.out.println("可以撤回");

            List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                    /* .taskAssignee(name)//撤回人*/
                    .processInstanceId(id)
                    .orderByTaskCreateTime()
                    .asc()
                    .list();
            String myTaskId = null;
            HistoricTaskInstance myTask = null;
            for (HistoricTaskInstance hti : htiList) {
                /* if(loginUser.getUsername().equals(hti.getAssignee())) {*/
                myTaskId = hti.getId();
                myTask = hti;
                break;
                /* }*/
            }
            if (null == myTaskId) {
                System.out.println("该任务非当前用户提交，无法撤回");
                withdraw=2;
            } else {
                System.out.println("可以撤回");
                RepositoryService repositoryService = processEngine.getRepositoryService();
                String processDefinitionId = myTask.getProcessDefinitionId();
                ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

                //变量
                Map<String, VariableInstance> variables = runtimeService.getVariableInstances(task.getExecutionId());
                String myActivityId = null;
                List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                        .executionId(myTask.getExecutionId()).finished().list();
                for (HistoricActivityInstance hai : haiList) {
                    if (myTaskId.equals(hai.getTaskId())) {
                        myActivityId = hai.getActivityId();
                        break;
                    }
                }

                FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);
                Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
                String activityId = execution.getActivityId();
                System.out.println(activityId);
                //logger.warn("------->> activityId:" + activityId);
                FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

                //记录原活动方向
                List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
                oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

                //清理活动方向
                flowNode.getOutgoingFlows().clear();
                //建立新方向
                List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
                SequenceFlow newSequenceFlow = new SequenceFlow();
                newSequenceFlow.setId("newSequenceFlowId");
                newSequenceFlow.setSourceFlowElement(flowNode);
                newSequenceFlow.setTargetFlowElement(myFlowNode);
                newSequenceFlowList.add(newSequenceFlow);
                flowNode.setOutgoingFlows(newSequenceFlowList);
                Authentication.setAuthenticatedUserId(name);
                taskService.addComment(task.getId(), task.getProcessInstanceId(), "");
                Map<String, Object> currentVariables = new HashMap<String, Object>();
                currentVariables.put("applier", name);
                //完成任务
                taskService.complete(task.getId(), currentVariables);
                //恢复原方向
                flowNode.setOutgoingFlows(oriSequenceFlows);
                withdraw=3;
            }
        }
        return withdraw;
    }

    /*
     * 历史任务查询
     * @return
     */

    @RequestMapping("/hisTask")
    @ResponseBody
    public List<HistoricEntity> queryDoneTasks (@RequestParam("id") String id, HttpSession session){
        List<HistoricEntity> listTask=new ArrayList<>();
        //1:得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2：得到HistoryService对象
        HistoryService historyService = processEngine.getHistoryService();
        TaskService taskService=processEngine.getTaskService();
        String name = (String) session.getAttribute("userSn");
        List<HistoricActivityInstance> taskList = historyService.createHistoricActivityInstanceQuery()
                .taskAssignee(name)
                .finished()
                .list();
        String startName="";
        HashSet<String> hashSet = new HashSet<>();
        for (HistoricActivityInstance task : taskList) {
            hashSet.add(task.getProcessInstanceId());
        }
        for (String hset : hashSet) {
            if(id==null||id=="") {

            }else {
                hset=id;
            }
            System.out.println("id:"+hset);
            List<HistoricActivityInstance> pIdTasks = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(hset)
                    .list();
            for (HistoricActivityInstance tasks:pIdTasks){
                HistoricEntity hisTask=new HistoricEntity();
                //已完成结束的任务
                if("endEvent".equals(tasks.getActivityType())){
                    System.out.println("结束的任务："+tasks.getProcessInstanceId()+"###"+tasks.getActivityName());
                    List<HistoricVariableInstance> list = processEngine.getHistoryService()
                            .createHistoricVariableInstanceQuery() //创建一个历史的流程变量查询对象
                            .processInstanceId(tasks.getProcessInstanceId())
                            .list();
                    if(list !=null && list.size()>0){
                        hisTask.setId(tasks.getId());
                        hisTask.setProcessInstanceId(tasks.getProcessInstanceId());
                        hisTask.setCreateTime(df.format(tasks.getStartTime()));
                        hisTask.setEndTime(df.format(tasks.getEndTime()));
                        hisTask.setStatus("已完成");
                        List<ActEntity> accountField = null;
                        //任务结束的变量值，字段不同可根据任务类型判断

                        for(HistoricVariableInstance hvi:list){
                            List<Comment> comments = taskService.getTaskComments(hvi.getTaskId());
                            //如果当前任务有批注信息，添加到集合中
                            System.out.println(comments.size());
                            if(comments!=null && comments.size()>0){
                                hisTask.setComment(comments);
                            }else {
                                hisTask.setComment(comments);
                            }

                            if("account".equals(hvi.getVariableName())){
                                hisTask.setAccount(hvi.getValue().toString());
                            }
                            if("role".equals(hvi.getVariableName())){
                                hisTask.setRole(hvi.getValue().toString());
                            }
                            /*if("orgName".equals(hvi.getVariableName())){
                                hisTask.setOrgName(hvi.getValue().toString());
                            }*/
                            if("applyReason".equals(hvi.getVariableName())){
                                hisTask.setApplyReason(hvi.getValue().toString());
                            }
                            if("accountOrg".equals(hvi.getVariableName())){
                                hisTask.setAccountOrg(hvi.getValue().toString());
                            }
                            if("applyPerson".equals(hvi.getVariableName())){
                                hisTask.setApplyPerson(hvi.getValue().toString());
                            }
                            if("approvedPerson".equals(hvi.getVariableName())){
                                hisTask.setApprovedPerson(hvi.getValue().toString());
                            }
                            if("taskType".equals(hvi.getVariableName())){
                                hisTask.setTaskType(hvi.getValue().toString());
                            }
                            if("name".equals(hvi.getVariableName())){
                                hisTask.setNames(hvi.getValue().toString());
                            }
                            if("actType".equals(hvi.getVariableName())){
                                hisTask.setActType(hvi.getValue().toString());
                            }
                            if("app".equals(hvi.getVariableName())){
                                hisTask.setApp(hvi.getValue().toString());
                                accountField=defaultService.act(hvi.getValue().toString());
                            }
                            if("textList".equals(hvi.getVariableName())){
                                JSONObject textLists = JSONObject.fromObject(hvi.getValue().toString());
                                JSONArray textListKey = JSONArray.fromObject(textLists.keySet());
                                JSONArray textListValue = JSONArray.fromObject(textLists.values());
                                List<DefaultsEntity> textList2=new ArrayList<>();
                                for (int text=0;text<textLists.size();text++) {
                                    for (int i=0;i<accountField.size();i++) {
                                        if(accountField.get(i).getName().equals(textListKey.get(text).toString())){
                                            DefaultsEntity defaultsEntity = new DefaultsEntity();
                                            defaultsEntity.setRemarks(accountField.get(i).getRemark());
                                            defaultsEntity.setNames(textListKey.get(text).toString());
                                            defaultsEntity.setDefaultValues(textListValue.get(text).toString());
                                            textList2.add(defaultsEntity);
                                        }
                                    }
                                }
                                hisTask.setTextLists(textList2);
                            }
                            if("passwordList".equals(hvi.getVariableName())){

                                JSONObject passwordList = JSONObject.fromObject(hvi.getValue().toString());
                                JSONArray passwordListKey = JSONArray.fromObject(passwordList.keySet());
                                JSONArray passwordListValue = JSONArray.fromObject(passwordList.values());
                                List<DefaultsEntity> passwordList2=new ArrayList<>();
                                for (int text=0;text<passwordList.size();text++) {
                                    for (int i=0;i<accountField.size();i++) {
                                        if(accountField.get(i).getName().equals(passwordListKey.get(text).toString())) {
                                            DefaultsEntity defaultsEntity = new DefaultsEntity();
                                            defaultsEntity.setRemarks(accountField.get(i).getRemark());
                                            defaultsEntity.setNames(passwordListKey.get(text).toString());
                                            defaultsEntity.setDefaultValues(passwordListValue.get(text).toString());
                                            passwordList2.add(defaultsEntity);
                                        }
                                    }
                                }
                                hisTask.setPasswordLists(passwordList2);
                            }
                            if("selectList".equals(hvi.getVariableName())){
                                JSONObject selectList = JSONObject.fromObject(hvi.getValue().toString());
                                JSONArray selectListKey = JSONArray.fromObject(selectList.keySet());
                                JSONArray selectListValue = JSONArray.fromObject(selectList.values());
                                List<DefaultsEntity> selectList2=new ArrayList<>();
                                //获取下拉框的value值
                                for (int text=0;text<selectList.size();text++) {
                                    for (int i=0;i<accountField.size();i++) {
                                        if(accountField.get(i).getName().equals(selectListKey.get(text).toString())) {
                                            DefaultsEntity defaultsEntity = new DefaultsEntity();
                                            if(StringUtils.isEmpty(accountField.get(i).getCompant())){

                                            }else {
                                                String compantStr = accountField.get(i).getCompant();
                                                List<String> result = Arrays.asList(compantStr.split(","));
                                                for(int c=0;c<result.size();c++){
                                                    String [] com= result.get(c).split("[=]");
                                                    String keys= com[0];
                                                    String values= com[1];
                                                    if(keys.equals(selectListValue.get(text).toString())){
                                                        defaultsEntity.setDefaultValues(values);
                                                    }
                                                }
                                            }
                                            defaultsEntity.setRemarks(accountField.get(i).getRemark());
                                            defaultsEntity.setNames(selectListKey.get(text).toString());
                                            selectList2.add(defaultsEntity);
                                        }
                                    }
                                }
                                hisTask.setSelectLists(selectList2);
                            }
                            if("dateList".equals(hvi.getVariableName())){
                                JSONObject dateList = JSONObject.fromObject(hvi.getValue().toString());
                                JSONArray dateListKey = JSONArray.fromObject(dateList.keySet());
                                JSONArray dateListValue = JSONArray.fromObject(dateList.values());
                                List<DefaultsEntity> dateList2=new ArrayList<>();
                                for (int text=0;text<dateList.size();text++) {
                                    for (int i=0;i<accountField.size();i++) {
                                        if(accountField.get(i).getName().equals(dateListKey.get(text).toString())) {
                                            DefaultsEntity defaultsEntity = new DefaultsEntity();
                                            defaultsEntity.setRemarks(accountField.get(i).getRemark());
                                            defaultsEntity.setNames(dateListKey.get(text).toString());
                                            defaultsEntity.setDefaultValues(dateListValue.get(text).toString());
                                            dateList2.add(defaultsEntity);
                                        }
                                    }
                                }
                                hisTask.setDateLists(dateList2);

                            }
                        }
                    }
                    listTask.add(hisTask);
                }else if("startEvent".equals(tasks.getActivityType())){
                    HistoricActivityInstance unTasks =historyService.createHistoricActivityInstanceQuery()
                            .processInstanceId(hset)
                            .unfinished()//未完成的活动(任务)
                            .singleResult();
                    //未结束的任务
                    if(unTasks!=null){
                        System.out.println("未结束的任务："+unTasks.getProcessInstanceId()+"*****"+unTasks.getActivityName());
                        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                                .createHistoricVariableInstanceQuery() //创建一个历史的流程变量查询对象
                                .processInstanceId(tasks.getProcessInstanceId())
                                .list();
                        if(list !=null && list.size()>0){
                            hisTask.setId(tasks.getId());
                            hisTask.setProcessInstanceId(tasks.getProcessInstanceId());
                            hisTask.setCreateTime(df.format(tasks.getStartTime()));
                            hisTask.setEndTime(df.format(tasks.getEndTime()));
                            hisTask.setStatus("未完成");

                            List<ActEntity> accountField = null;
                            //任务未结束的变量值，字段不同可根据任务类型判断

                            for(HistoricVariableInstance hvi:list){
                                List<Comment> comments = taskService.getTaskComments(hvi.getTaskId());
                                //如果当前任务有批注信息，添加到集合中
                                System.out.println(comments.size());
                                if(comments!=null && comments.size()>0){
                                    hisTask.setComment(comments);
                                }else {
                                    hisTask.setComment(comments);
                                }
                                if("account".equals(hvi.getVariableName())){
                                    hisTask.setAccount(hvi.getValue().toString());
                                }
                                if("role".equals(hvi.getVariableName())){
                                    hisTask.setRole(hvi.getValue().toString());
                                }
                                /*if("orgName".equals(hvi.getVariableName())){
                                    hisTask.setOrgName(hvi.getValue().toString());
                                }*/
                                if("applyReason".equals(hvi.getVariableName())){
                                    hisTask.setApplyReason(hvi.getValue().toString());
                                }
                                if("accountOrg".equals(hvi.getVariableName())){
                                    hisTask.setAccountOrg(hvi.getValue().toString());
                                }
                                if("applyPerson".equals(hvi.getVariableName())){
                                    hisTask.setApplyPerson(hvi.getValue().toString());
                                }
                                if("approvedPerson".equals(hvi.getVariableName())){
                                    hisTask.setApprovedPerson(hvi.getValue().toString());
                                }
                                if("taskType".equals(hvi.getVariableName())){
                                    hisTask.setTaskType(hvi.getValue().toString());
                                }
                                if("name".equals(hvi.getVariableName())){
                                    hisTask.setNames(hvi.getValue().toString());
                                }
                                if("actType".equals(hvi.getVariableName())){
                                    hisTask.setActType(hvi.getValue().toString());
                                }
                                if("app".equals(hvi.getVariableName())){
                                    hisTask.setApp(hvi.getValue().toString());
                                    accountField=defaultService.act(hvi.getValue().toString());
                                }
                                if("textList".equals(hvi.getVariableName())){
                                    JSONObject textLists = JSONObject.fromObject(hvi.getValue().toString());
                                    JSONArray textListKey = JSONArray.fromObject(textLists.keySet());
                                    JSONArray textListValue = JSONArray.fromObject(textLists.values());
                                    List<DefaultsEntity> textList2=new ArrayList<>();
                                    for (int text=0;text<textLists.size();text++) {
                                        for (int i=0;i<accountField.size();i++) {
                                            if(accountField.get(i).getName().equals(textListKey.get(text).toString())){
                                                DefaultsEntity defaultsEntity = new DefaultsEntity();
                                                defaultsEntity.setRemarks(accountField.get(i).getRemark());
                                                defaultsEntity.setNames(textListKey.get(text).toString());
                                                defaultsEntity.setDefaultValues(textListValue.get(text).toString());
                                                textList2.add(defaultsEntity);
                                            }
                                        }
                                    }
                                    hisTask.setTextLists(textList2);
                                }
                                if("passwordList".equals(hvi.getVariableName())){

                                    JSONObject passwordList = JSONObject.fromObject(hvi.getValue().toString());
                                    JSONArray passwordListKey = JSONArray.fromObject(passwordList.keySet());
                                    JSONArray passwordListValue = JSONArray.fromObject(passwordList.values());
                                    List<DefaultsEntity> passwordList2=new ArrayList<>();
                                    for (int text=0;text<passwordList.size();text++) {
                                        for (int i=0;i<accountField.size();i++) {
                                            if(accountField.get(i).getName().equals(passwordListKey.get(text).toString())) {
                                                DefaultsEntity defaultsEntity = new DefaultsEntity();
                                                defaultsEntity.setRemarks(accountField.get(i).getRemark());
                                                defaultsEntity.setNames(passwordListKey.get(text).toString());
                                                defaultsEntity.setDefaultValues(passwordListValue.get(text).toString());
                                                passwordList2.add(defaultsEntity);
                                            }
                                        }
                                    }
                                    hisTask.setPasswordLists(passwordList2);
                                }
                                if("selectList".equals(hvi.getVariableName())){
                                    JSONObject selectList = JSONObject.fromObject(hvi.getValue().toString());
                                    JSONArray selectListKey = JSONArray.fromObject(selectList.keySet());
                                    JSONArray selectListValue = JSONArray.fromObject(selectList.values());
                                    List<DefaultsEntity> selectList2=new ArrayList<>();
                                    //获取下拉框的value值
                                    for (int text=0;text<selectList.size();text++) {
                                        for (int i=0;i<accountField.size();i++) {
                                            if(accountField.get(i).getName().equals(selectListKey.get(text).toString())) {
                                                DefaultsEntity defaultsEntity = new DefaultsEntity();
                                                if(StringUtils.isEmpty(accountField.get(i).getCompant())){

                                                }else {
                                                    String compantStr = accountField.get(i).getCompant();
                                                    List<String> result = Arrays.asList(compantStr.split(","));
                                                    for(int c=0;c<result.size();c++){
                                                        String [] com= result.get(c).split("[=]");
                                                        String keys= com[0];
                                                        String values= com[1];
                                                        if(keys.equals(selectListValue.get(text).toString())){
                                                            defaultsEntity.setDefaultValues(values);
                                                        }
                                                    }
                                                }
                                                defaultsEntity.setRemarks(accountField.get(i).getRemark());
                                                defaultsEntity.setNames(selectListKey.get(text).toString());
                                                selectList2.add(defaultsEntity);
                                            }
                                        }
                                    }
                                    hisTask.setSelectLists(selectList2);
                                }
                                System.out.println("=====:  "+hvi.getVariableName());
                                if("dateList".equals(hvi.getVariableName())){
                                    JSONObject dateList = JSONObject.fromObject(hvi.getValue().toString());
                                    JSONArray dateListKey = JSONArray.fromObject(dateList.keySet());
                                    JSONArray dateListValue = JSONArray.fromObject(dateList.values());
                                    List<DefaultsEntity> dateList2=new ArrayList<>();
                                    for (int text=0;text<dateList.size();text++) {
                                        for (int i=0;i<accountField.size();i++) {
                                            if(accountField.get(i).getName().equals(dateListKey.get(text).toString())) {
                                                DefaultsEntity defaultsEntity = new DefaultsEntity();
                                                defaultsEntity.setRemarks(accountField.get(i).getRemark());
                                                defaultsEntity.setNames(dateListKey.get(text).toString());
                                                defaultsEntity.setDefaultValues(dateListValue.get(text).toString());
                                                dateList2.add(defaultsEntity);
                                            }
                                        }
                                    }
                                    hisTask.setDateLists(dateList2);
                                }
                            }
                            listTask.add(hisTask);
                        }
                    }
                }
            }
        }
        return listTask;
    }
}
