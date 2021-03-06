package com.example.tquan.controller;


import com.beust.jcommander.internal.Lists;
import com.example.tquan.entity.*;
import com.example.tquan.service.*;
import com.example.tquan.util.IamInterface;
import com.example.tquan.util.PwdNum;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


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

import java.text.ParseException;
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


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private PositionController positionController;
    @Autowired
    private ImAppService imAppService;
    @Autowired
    private ActVerService actVerService;
    @Autowired
    private ApproverService approverService ;
    @Autowired
    private TaskTypeService taskTypeService;
    /**
     * ???????????????Resources????????????activiti.cfg.xml??????????????????
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
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);

    /**
     * ??????????????????activiti.cfg.xml??????ProcessEngine
     */

   /* public void CreateProcessEngineByCfgXml() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine engine = cfg.buildProcessEngine();
    }*/

    /**
     * ????????????
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
     * ??????????????????,?????????????????????
     *
     */
    @RequestMapping("/verifyActTask")
    @ResponseBody
    public List<ActivitiEntity> start(@Param("id")String id,@Param("taskType")String taskType, @Param("applyName")String applyName,
                                      @Param("appName")String appName,@Param("accountName")String accountName,
                                      @Param("audit")String audit,
                                      @Param("role")String role,@Param("applyReason")String applyReason,
                                      @Param("accountOrg")String accountOrg,@Param("accountOrgId")String accountOrgId,@Param("actType")String actType,
                                      @Param("textList")String textList,@Param("passwordList")String passwordList,
                                      @Param("dateList")String dateList,@Param("selectList")String selectList,
                                      HttpSession session,String uuid,String times,String taskTy) throws Exception {
        List<ActivitiEntity> listTask=new ArrayList<>();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //?????????id???key???message????????????
        String sn = (String) session.getAttribute("userSn");
        String name = (String) session.getAttribute("userName");
        String userId = (String) session.getAttribute("UserId");
        //?????????????????????
        List<ImApp> list=imAppService.findApply(appName);
        Map<String,Object> map = new HashMap<String,Object>();
        TaskService taskSer=processEngine.getTaskService();
        List<Task> listAlles= taskSer.createTaskQuery()//????????????????????????
                .taskAssignee(sn)//????????????????????????
                .list();
        if(id==null||id==""){
            if(taskType.equals("????????????")==true){
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
                    map.put("applyPersonName",name);
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
                    if (accountOrgId==null||accountOrgId==""){
                        System.out.println("??????id???"+positionController.userOrg(uuid,session));
                        map.put("accountOrgId",positionController.userOrg(uuid,session));
                    }else {
                        map.put("accountOrgId",accountOrgId);
                    }
                    map.put("actType",actType);
                    map.put("accountOrg",accountOrg);
                    map.put("applyReason",applyReason);
                    map.put("textList",textList);
                    map.put("passwordList",passwordList);
                    map.put("dateList",dateList);
                    map.put("selectList",selectList);
                    ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("myProcess_1",map);
                    TaskService taskService=processEngine.getTaskService();
                    List<Task> lists = taskService.createTaskQuery()//????????????????????????
                            .taskAssignee(sn)//????????????????????????
                            .orderByTaskCreateTime()//??????????????????
                            .desc()
                            .list();
                    processEngine.getTaskService()// ??????????????????????????????Service
                            .complete(lists.get(0).getId());
                }
            }if(taskType.equals("????????????")==true){
                map.put("applyPerson",sn);
                map.put("applyPersonName",name);
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
                List<Task> lists = taskService.createTaskQuery()//????????????????????????
                        .taskAssignee(sn)//????????????????????????
                        .orderByTaskCreateTime()//??????????????????
                        .desc()
                        .list();
                processEngine.getTaskService()// ??????????????????????????????Service
                        .complete(lists.get(0).getId());
            } else if(taskType.equals("????????????")==true){
                map.put("applyPerson",sn);
                map.put("applyPersonName",name);
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
                map.put("applyReason",applyReason);
                ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("myProcess_1",map);
                TaskService taskService=processEngine.getTaskService();
                List<Task> lists = taskService.createTaskQuery()//????????????????????????
                        .taskAssignee(sn)//????????????????????????
                        .orderByTaskCreateTime()//??????????????????
                        .desc()
                        .list();
                processEngine.getTaskService()// ??????????????????????????????Service
                        .complete(lists.get(0).getId());
            }
        }else {
            for (int p=0;p<listAlles.size();p++) {
                System.out.println(id+"*****"+listAlles.get(p).getProcessInstanceId());
                if(id.equals(listAlles.get(p).getProcessInstanceId())){
                    if(taskType.equals("????????????")==true){
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
                            map.put("applyPersonName",name);
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
                            if (accountOrgId==null||accountOrgId==""){
                                System.out.println("??????id???"+positionController.userOrg(uuid,session));
                                map.put("accountOrgId",positionController.userOrg(uuid,session));
                            }else {
                                map.put("accountOrgId",accountOrgId);
                            }
                            map.put("actType",actType);
                            map.put("accountOrg",accountOrg);
                            map.put("applyReason",applyReason);
                            map.put("textList",textList);
                            map.put("passwordList",passwordList);
                            map.put("dateList",dateList);
                            map.put("selectList",selectList);
                            runtimeService.setVariablesLocal(id,map);
                            TaskService taskService=processEngine.getTaskService();
                            Task lists = taskService.createTaskQuery()//????????????????????????
                                    .processInstanceId(id)
                                    .singleResult();
                            processEngine.getTaskService()// ??????????????????????????????Service
                                    .complete(lists.getId());
                        }
                    }if(taskType.equals("????????????")==true){
                        map.put("applyPerson",sn);
                        map.put("applyPersonName",name);
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
                        Task lists = taskService.createTaskQuery()//????????????????????????
                                .processInstanceId(id)
                                .singleResult();
                        processEngine.getTaskService()// ??????????????????????????????Service
                                .complete(lists.getId());
                    } else if(taskType.equals("????????????")==true){
                        map.put("applyPerson",sn);
                        map.put("applyPersonName",name);
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
                        map.put("applyReason",applyReason);
                        runtimeService.setVariables(id,map);
                        TaskService taskService=processEngine.getTaskService();
                        Task lists = taskService.createTaskQuery()//????????????????????????
                                .processInstanceId(id)
                                .singleResult();
                        processEngine.getTaskService()// ??????????????????????????????Service
                                .complete(lists.getId());

                    }
                }
            }
        }
        findTasks(session,times,taskTy);
        return findTasks(session,times,taskTy);
    }

    /**
     * ??????????????????
     * @return
     */
    @RequestMapping("/selectApplyTast")
    @ResponseBody
    public  List<ActivitiEntity> findTasks(HttpSession session,String times,String taskTy) throws ParseException {
        String name = (String) session.getAttribute("userName");
        String login_name = (String) session.getAttribute("userSn");
        List<ActivitiEntity> listTask=new ArrayList<>();
        List<Approver> audits=approverService.audit(login_name);
        String audit=audits.get(0).getAudit().substring(audits.get(0).getAudit().indexOf("(")+1,audits.get(0).getAudit().indexOf(")"));
        TaskService taskService=processEngine.getTaskService();
        List<Task> listsl = taskService.createTaskQuery()//????????????????????????
                .taskAssignee(audit)//????????????????????????
                .list();
        List<Task> lists2 = taskService.createTaskQuery()//????????????????????????
                .taskAssignee(login_name)//????????????????????????
                .list();
        if(audit.equals(login_name)){
            for (int p=0;p<listsl.size();p++){
                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(listsl.get(p).getProcessInstanceId().toString());
                if(!variables.get("taskType").toString().equals("????????????")){
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
                             if(times==null||times==""){
                                if (taskTy==null||taskTy==""){
                                    listTask.add(task);
                                }else {
                                    if (variables.get("taskType").toString().matches(".*"+taskTy+".*")){
                                        listTask.add(task);
                                    }
                                }
                            }else {
                                 String startTime=times.substring(0, times.indexOf(" - "));
                                 String endTime=times.substring(startTime.length()+3, times.length());
                                if (df.parse(startTime).compareTo(listsl.get(p).getCreateTime())==-1&&df.parse(endTime).compareTo(listsl.get(p).getCreateTime())==1){
                                    if (taskTy==null||taskTy==""){
                                        listTask.add(task);
                                    }else {
                                        if ((variables.get("taskType").toString().matches(".*"+taskTy+".*"))&&(df.parse(startTime).compareTo(listsl.get(p).getCreateTime())==-1&&df.parse(endTime).compareTo(listsl.get(p).getCreateTime())==1)){
                                            listTask.add(task);
                                        }
                                    }
                                }
                            }
                        }else {

                        }
                }
                }
            }
        }else {
            for (int p=0;p<listsl.size();p++){
                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(listsl.get(p).getProcessInstanceId().toString());
                if(!variables.get("taskType").toString().equals("????????????")) {
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
                            if(times==null||times==""){
                                if (taskTy==null||taskTy==""){
                                    listTask.add(task);
                                }else {
                                    if (variables.get("taskType").toString().matches(".*"+taskTy+".*")){
                                        listTask.add(task);
                                    }
                                }
                            }else {
                                String startTime=times.substring(0, times.indexOf(" - "));
                                String endTime=times.substring(startTime.length()+3, times.length());
                                if (df.parse(startTime).compareTo(listsl.get(p).getCreateTime())==-1&&df.parse(endTime).compareTo(listsl.get(p).getCreateTime())==1){
                                    if (taskTy==null||taskTy==""){
                                        listTask.add(task);
                                    }else {
                                        if ((variables.get("taskType").toString().matches(".*"+taskTy+".*"))&&(df.parse(startTime).compareTo(listsl.get(p).getCreateTime())==-1&&df.parse(endTime).compareTo(listsl.get(p).getCreateTime())==1)){
                                            listTask.add(task);
                                        }
                                    }
                                }
                            }
                        } else {

                        }
                    }
                }
            }
            for (int p=0;p<lists2.size();p++) {
                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(lists2.get(p).getProcessInstanceId().toString());
                if(!variables.get("taskType").toString().equals("????????????")) {
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
                    if(times==null||times==""){
                        if (taskTy==null||taskTy==""){
                            listTask.add(task);
                        }else {
                            if (variables.get("taskType").toString().matches(".*"+taskTy+".*")){
                                listTask.add(task);
                            }
                        }
                    }else {
                        String startTime=times.substring(0, times.indexOf(" - "));
                        String endTime=times.substring(startTime.length()+3, times.length());
                        if (df.parse(startTime).compareTo(listsl.get(p).getCreateTime())==-1&&df.parse(endTime).compareTo(listsl.get(p).getCreateTime())==1){
                            if (taskTy==null||taskTy==""){
                                listTask.add(task);
                            }else {
                                if ((variables.get("taskType").toString().matches(".*"+taskTy+".*"))&&(df.parse(startTime).compareTo(listsl.get(p).getCreateTime())==-1&&df.parse(endTime).compareTo(listsl.get(p).getCreateTime())==1)){
                                    listTask.add(task);
                                }
                            }
                        }
                    }
                }
            }
        }

        return listTask;
    }


    /**
     * ???????????????????????????
     */
    @RequestMapping("/able")
    @ResponseBody
    public List<AuditEntity> able(HttpSession session,String taskTy,String applyPer) {
        //1:??????ProcessEngine??????
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2?????????TaskService??????
        /* HttpSession session = sessionRequest.getSession();*/
        List<AuditEntity> lists=new ArrayList<>();
        String name = (String) session.getAttribute("userSn");
        List<Approver> audits=approverService.audit(name);
        TaskService taskServices=processEngine.getTaskService();
        List<Task> list = taskServices.createTaskQuery()//????????????????????????
                .taskAssignee(name)//????????????????????????
                .list();
        for(Task tk : list){
            if(!"_3".equals(tk.getTaskDefinitionKey())){
                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(tk.getProcessInstanceId());
                AuditEntity task=new AuditEntity();
                TaskService taskService=processEngine.getTaskService();
                Task li = taskService.createTaskQuery()//????????????????????????
                        .processInstanceId(tk.getProcessInstanceId())
                        .singleResult();
                HistoryService historyService = processEngine.getHistoryService();
                List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(li.getProcessInstanceId()).list();
                for (HistoricActivityInstance hai : hais) {
                    String historytaskId = hai.getTaskId();
                    List<Comment> comments = taskService.getTaskComments(historytaskId);
                    // ??????????????????????????????????????????????????????
                    if(comments!=null && comments.size()>0){
                        task.setComment(comments);
                    }
                }
                if (!variables.get("taskType").equals("????????????")){
                    task.setRole(variables.get("role").toString());
                    task.setAccount(variables.get("account").toString());
                    if (!variables.get("taskType").equals("????????????")) {
                        task.setActType(variables.get("actType").toString());
                    }
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
                task.setApplyPerson(variables.get("applyPersonName").toString());
                //??????
                if(taskTy==null||taskTy==""){
                    if(applyPer==null||applyPer=="") {
                        lists.add(task);
                    }else {
                        if (variables.get("applyPersonName").toString().matches(".*"+applyPer+".*")){
                            lists.add(task);
                        }
                    }
                }else {
                    if(applyPer==null||applyPer=="") {
                        if (variables.get("taskType").toString().matches(".*"+taskTy+".*")){
                            lists.add(task);
                        }
                    }else {
                        if (variables.get("applyPersonName").toString().matches(".*"+applyPer+".*")&&variables.get("taskType").toString().matches(".*"+taskTy+".*")){
                            lists.add(task);
                        }
                    }
                }
            }
        }
        return lists;
    }


    //????????????
    @RequestMapping("/repulse")
    @ResponseBody
    public List setTaskAssignee(@RequestParam("userName") String userName,@RequestParam("id") String pid,@RequestParam("approvalOpinion")String approvalOpinion,HttpSession session,String id,String repulseReason){
        List list1=new ArrayList();
        String name = (String) session.getAttribute("userSn");
        //1:??????ProcessEngine??????
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2?????????TaskService??????
        TaskService taskService=processEngine.getTaskService();
        Task list2 = taskService.createTaskQuery()//????????????????????????
                .processInstanceId(pid)
                .singleResult();
        System.out.println(list2.getId()+"***"+pid+"***"+approvalOpinion+"***"+userName);
        taskService.addComment(list2.getId(),pid,approvalOpinion);//????????????
        //taskService.setAssignee(list2.getId(),null);//??????????????????
        Map<String, Object> variables = new HashMap<>();
        //msg?????????????????????????????????bpmn???
        variables.put("msg", false);
        list2.setAssignee(userName);
        taskService.setVariableLocal(list2.getId(),"msg",false);
        taskService.complete(list2.getId(), variables);

        //??????????????????????????????
        VariableEntity variableEntity = new VariableEntity();
        variableEntity.setName("repulseReason");
        variableEntity.setProcInstId(id);
        String text = variableService.getTextByName(variableEntity);
        //????????????
        if (text != null) {
            VariableEntity variableEntity1 = new VariableEntity();
            variableEntity1.setProcInstId(id);
            variableEntity1.setName("repulseReason");
            variableEntity1.setText(approvalOpinion);
            variableService.updateTaskParam(variableEntity1);
            //???????????????
        } else {
            //??????????????????
            //?????????????????????????????????
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

    //?????????????????????????????????
    @RequestMapping("/SelAuditTask")
    @ResponseBody
    public List audit(@Param("id")String id, HttpServletRequest request,HttpSession session) {
        List<ActivitiEntity> list=new ArrayList<>();
        String login_name = (String) session.getAttribute("userSn");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService=processEngine.getTaskService();
        //???????????????????????????
       /* Task listClaim = taskService.createTaskQuery()//????????????????????????
                .processInstanceId(id)
                .singleResult();
        taskService.claim(listClaim.getId(),login_name);*/
        List<Approver> audits=approverService.audit(login_name);
        Map<String, Object> variables = processEngine.getRuntimeService().getVariables(id);
        ActivitiEntity task=new ActivitiEntity();
        Task listsl = taskService.createTaskQuery()//????????????????????????
                .processInstanceId(id)
                .singleResult();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(listsl.getProcessInstanceId()).list();
        for (HistoricActivityInstance hai : hais) {
            String historytaskId = hai.getTaskId();
            List<Comment> comments = taskService.getTaskComments(historytaskId);
            // 4?????????????????????????????????????????????????????????
            if(comments!=null && comments.size()>0){
                task.setComment(comments);
            }else {
                task.setComment(comments);
            }
        }
        if (!variables.get("taskType").toString().equals("????????????")){
            task.setTaskId(listsl.getId());
            task.setTaskName(listsl.getName());
            task.setTaskAssignee(listsl.getAssignee());
            task.setTaskCreateTime(df.format(listsl.getCreateTime()));
            task.setTaskExecutionId(listsl.getExecutionId());
            task.setTaskProcessInstanceId(listsl.getProcessInstanceId());
            task.setTaskApprovedPerson(audits.get(0).getAudit());
            task.setTaskApplyReason(variables.get("applyReason").toString());
            task.setTaskTypes(variables.get("taskType").toString());
            task.setTaskAccount(variables.get("account").toString());
            task.setTaskApp(variables.get("app").toString());
            task.setTaskApplyPerson(variables.get("applyPersonName").toString());
            if (!variables.get("taskType").toString().equals("????????????")){
                task.setTaskAccountOrg(variables.get("accountOrg").toString());
                task.setTaskActType(variables.get("actType").toString());
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
                //??????????????????value???
                for (int text=0;text<selectList.size();text++) {
                    for (int i = 0; i < accountField.size(); i++) {
                        if (accountField.get(i).getName().equals(selectListKey.get(text).toString())) {
                            DefaultsEntity defaultsEntity = new DefaultsEntity();
                            if (StringUtils.isEmpty(accountField.get(i).getCompant())) {

                            } else {
                                String compantStr = accountField.get(i).getCompant();
                                List<String> result = Arrays.asList(compantStr.split(","));
                                for (int c = 0; c < result.size(); c++) {
                                    String[] com = result.get(c).split("[=]");
                                    String keys = com[0];
                                    String values = com[1];
                                    if (keys.equals(selectListValue.get(text).toString())) {
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
        }else {
            task.setTaskOrgName(variables.get("orgName").toString());
            task.setPosition(variables.get("position").toString());
        }
        task.setTaskId(listsl.getId());
        task.setTaskName(listsl.getName());
        task.setTaskAssignee(listsl.getAssignee());
        task.setTaskCreateTime(df.format(listsl.getCreateTime()));
        task.setTaskExecutionId(listsl.getExecutionId());
        task.setTaskProcessInstanceId(listsl.getProcessInstanceId());
        task.setTaskApprovedPerson(audits.get(0).getAudit());
        task.setTaskRole(variables.get("role").toString());
        task.setTaskApplyReason(variables.get("applyReason").toString());

        task.setTaskTypes(variables.get("taskType").toString());

        task.setTaskApplyPerson(variables.get("applyPersonName").toString());
        list.add(task);
        System.out.println(list.toString());
        return list;
    }


    /**
     * ???????????????????????????
     *
     * @return
     */
    @RequestMapping("/AuditTask")
    @ResponseBody
    public List AuditTask(@RequestParam("id") String pid,@RequestParam("approvalOpinion") String approvalOpinion, HttpSession session, String uuid) throws Exception {
        /*String userId = (String) session.getAttribute("UserId");*/
        List<ActivitiEntity> li=new ArrayList<>();
        //1:??????ProcessEngine??????
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2?????????TaskService??????
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processInstanceId(pid)
                .singleResult();
        String processInstanceId = task.getProcessInstanceId();
        Map<String, Object> variables = processEngine.getRuntimeService().getVariables(processInstanceId);
        Map<String, Object> variable = new HashMap<>();
        //msg???????????????????????????????????????bpmn???
        variable.put("msg", true);
        //???????????????????????????????????????????????????????????????????????????
        VariableEntity variableEntity = variableService.getTaskDefByProcInstId(task.getId());

        if (variableEntity != null) {
            //?????????????????????????????????
            if (variableEntity.getProcDefId().contains("positionApply")) {
                String sn = null;
                String position = null;
                //??????????????????????????????
                VariableEntity variableEntity1 = new VariableEntity();
                variableEntity1.setProcInstId(processInstanceId);
                variableEntity1.setName("applyPerson");
                //???????????????
                sn = variableService.getTextByName(variableEntity1);

                //???????????????????????????
                VariableEntity variableEntity3 = new VariableEntity();
                variableEntity3.setProcInstId(processInstanceId);
                variableEntity3.setName("position");
                //?????????????????????
                position = variableService.getTextByName(variableEntity3);
                //???????????????????????????
                UserEntity userEntity1 = new UserEntity();
                userEntity1.setSn(variables.get("applyPerson").toString());
                UserEntity userEntity = userService.getUserByProperty(userEntity1);
                String ifo=iamInterface.oauth(uuid,iam.getKey(),iam.getPassword(),iam.getAddr(),iam.getUsername(),iam.getType(),iam.getCharset());
                if (StringUtils.isEmpty(ifo.toString())) {
                    System.out.println("uuid??????????????????????????????????????????");
                } else {

                    List<NameValuePair> params = Lists.newArrayList();
                    params.add(new BasicNameValuePair("uim-login-user-id", ifo.toString()));
                    params.add(new BasicNameValuePair("ids[0]", userEntity.getId() + "_" + defaultService.org(userEntity.getId())));
                    params.add(new BasicNameValuePair("newOrgId", variables.get("orgId").toString()));
                    //??????????????????
                    String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                    StringBuilder ifa = iamInterface.movenUser(str, user.getAddr(), user.getType());
                    System.out.println("======= ?????????????????????" + ifa.toString());
                    //??????????????????
                    List<String> positionList = Arrays.asList(position.split(","));
                    List<PositionEntity> userPositionEntityList = positionService.getPositionByUserId(userEntity.getId());
                    //????????????????????????????????????
                    Set<String> newSet = new HashSet<>();
                    Set<String> newSet2 = new HashSet<>();
                    for (int i = 0; i < positionList.size(); i++) {
                        newSet.add(positionList.get(i));
                        newSet2.add(positionList.get(i));
                    }
                    Set<String> userSet = new HashSet<>();
                    Set<String> userSet2 = new HashSet<>();
                    for (PositionEntity userPos : userPositionEntityList) {
                        userSet.add(userPos.getName());
                        userSet2.add(userPos.getName());
                    }
                    //?????????????????????????????????????????????????????????????????????
                    newSet.removeAll(userSet);
                    //?????????????????????????????????????????????????????????????????????
                    userSet2.removeAll(newSet2);
                    if (userPositionEntityList.size() == 0 || userPositionEntityList.toString().equals("[]")) {
                        for (int i = 0; i < positionList.size(); i++) {
                            String positionId = positionService.getPositionByName(positionList.get(i));
                            PositionEntity positionEntity = new PositionEntity();
                            positionEntity.setUserId(variables.get("userIds").toString());
                            positionEntity.setPositionId(positionId);
                            positionEntity.setOrgId(variables.get("orgId").toString());
                            //???????????????????????????add??????
                            int iden = positionService.addUserPosition(positionEntity);
                        }
                    } else {
                        Iterator delIte = userSet2.iterator();
                        while (delIte.hasNext()) {
                            String delPositionId = positionService.getPositionByName(delIte.next().toString());
                            positionService.deleteUserPos(userEntity.getId(), delPositionId);
                        }
                        Iterator addIte = newSet.iterator();
                        while (addIte.hasNext()) {
                            //?????????????????????
                            String positionId = positionService.getPositionByName(addIte.next().toString());
                            PositionEntity positionEntity = new PositionEntity();
                            positionEntity.setUserId(variables.get("userIds").toString());
                            positionEntity.setPositionId(positionId);
                            positionEntity.setOrgId(variables.get("orgId").toString());
                            //???????????????????????????add??????
                            int iden = positionService.addUserPosition(positionEntity);
                            //????????????
                            if (iden != 0) {
                                log.info("==========================" + sn + "??????" + position + "??????!");
                            }
                        }
                    }
                }
            } else {
                //??????
                String ifo=iamInterface.oauth(uuid,iam.getKey(),iam.getPassword(),iam.getAddr(),iam.getUsername(),iam.getType(),iam.getCharset());
                if (StringUtils.isEmpty(ifo.toString())) {
                    System.out.println("uuid??????????????????????????????????????????");
                } else {
                    if("????????????".equals(variables.get("taskType").toString())){
                        String pwds = "";
                        //????????????
                        List<ActEntity> accountField=defaultService.act(variables.get("app").toString());
                        for (int i=0;i<accountField.size();i++) {
                            if ("LOGIN_PWD".equals(accountField.get(i).getName()) ){
                                if (accountField.get(i).getDefaultValue()!=null||accountField.get(i).getDefaultValue()!="") {
                                        pwds = accountField.get(i).getDefaultValue();
                                } else {
                                    if (account.getRandomSwitch().equals("true") && !account.getImmobilizationSwitch().equals("true")) {
                                        //?????????
                                        PwdNum pwd = new PwdNum();
                                        pwds = pwd.Pwd(account.getPwdRank(), account.getStrLength(), pwds);

                                    } else if (account.getImmobilizationSwitch().equals("true") && !account.getRandomSwitch().equals("true")) {
                                        pwds = account.getPwdDefault();
                                    } else {
                                        pwds = "000000";
                                    }
                                }
                            }
                        }
                        System.out.println("???????????????"+pwds);
                        List<NameValuePair> params = Lists.newArrayList();
                        params.add(new BasicNameValuePair("uim-login-user-id",ifo.toString()));
                        params.add(new BasicNameValuePair("loginName", variables.get("account").toString()));
                        params.add(new BasicNameValuePair("loginPwd", pwds));
                        params.add(new BasicNameValuePair("loginConfirmPwd", pwds));
                        params.add(new BasicNameValuePair("appId", variables.get("appId").toString()));
                        params.add(new BasicNameValuePair("userId", variables.get("usersId").toString()));
                        params.add(new BasicNameValuePair("status", variables.get("status").toString()));
                        params.add(new BasicNameValuePair("acctType", variables.get("actType").toString()));
                        JSONObject textList = JSONObject.fromObject(variables.get("textList").toString());
                        JSONArray textListKey = JSONArray.fromObject(textList.keySet());
                        JSONArray textListValue = JSONArray.fromObject(textList.values());
                        List<DefaultsEntity> textList2=new ArrayList<>();
                        for (int text=0;text<textList.size();text++) {
                            String textName="extraAttrs["+textListKey.get(text).toString()+"]";
                            params.add(new BasicNameValuePair(textName, textListValue.get(text).toString()));
                        }
                        JSONObject passwordList = JSONObject.fromObject(variables.get("passwordList").toString());
                        JSONArray passwordListKey = JSONArray.fromObject(passwordList.keySet());
                        JSONArray passwordListValue = JSONArray.fromObject(passwordList.values());
                        List<DefaultsEntity> passwordList2=new ArrayList<>();
                        for (int text=0;text<passwordList.size();text++) {
                            String passwordName="extraAttrs["+passwordListKey.get(text).toString()+"]";
                            params.add(new BasicNameValuePair(passwordName, passwordListValue.get(text).toString()));
                        }
                        JSONObject selectList = JSONObject.fromObject(variables.get("selectList").toString());
                        JSONArray selectListKey = JSONArray.fromObject(selectList.keySet());
                        JSONArray selectListValue = JSONArray.fromObject(selectList.values());
                        for (int text=0;text<selectList.size();text++) {
                            String selectName="extraAttrs["+selectListKey.get(text).toString()+"]";
                            params.add(new BasicNameValuePair(selectName, selectListValue.get(text).toString()));
                        }
                        JSONObject dateList = JSONObject.fromObject(variables.get("dateList").toString());
                        JSONArray dateListKey = JSONArray.fromObject(dateList.keySet());
                        JSONArray dateListValue = JSONArray.fromObject(dateList.values());
                        for (int text=0;text<dateList.size();text++) {
                            String dateName="extraAttrs["+dateListKey.get(text).toString()+"]";
                            params.add(new BasicNameValuePair(dateName, dateListValue.get(text).toString()));
                        }
                        //??????????????????
                        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                        String ifa=iamInterface.accountSave(str,account.getAddr(),account.getType());
                        System.out.println("======= ?????????????????????" + ifa);

                    }else if("????????????".equals(variables.get("taskType").toString())){
                        List<ActEntity> list1=defaultService.actField(variables.get("app").toString(),variables.get("account").toString());
                        List<NameValuePair> params = Lists.newArrayList();
                        params.add(new BasicNameValuePair("uim-login-user-id",ifo.toString()));
                        params.add(new BasicNameValuePair("ids[0]", list1.get(0).getId().toString()));
                        //??????????????????
                        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                        String ifa=iamInterface.accountStart(str,account.getAddr(),account.getType());
                        System.out.println("======= ?????????????????????" + ifa);
                    }else if("????????????".equals(variables.get("taskType").toString())){
                        List<ActEntity> list1=defaultService.actFieldUp(variables.get("app").toString(),variables.get("account").toString());
                        List<NameValuePair> params = Lists.newArrayList();
                        params.add(new BasicNameValuePair("uim-login-user-id",ifo.toString()));
                        params.add(new BasicNameValuePair("loginName", variables.get("account").toString()));
                        params.add(new BasicNameValuePair("appId", variables.get("appId").toString()));
                        params.add(new BasicNameValuePair("userId", variables.get("usersId").toString()));
                        params.add(new BasicNameValuePair("status", variables.get("status").toString()));
                        params.add(new BasicNameValuePair("id", list1.get(0).getId().toString()));
                        params.add(new BasicNameValuePair("acctType", variables.get("actType").toString()));
                        JSONObject textList = JSONObject.fromObject(variables.get("textList").toString());
                        JSONArray textListKey = JSONArray.fromObject(textList.keySet());
                        JSONArray textListValue = JSONArray.fromObject(textList.values());
                        List<DefaultsEntity> textList2=new ArrayList<>();
                        for (int text=0;text<textList.size();text++) {
                            String textName="extraAttrs["+textListKey.get(text).toString()+"]";
                            params.add(new BasicNameValuePair(textName, textListValue.get(text).toString()));
                        }
                        JSONObject passwordList = JSONObject.fromObject(variables.get("passwordList").toString());
                        JSONArray passwordListKey = JSONArray.fromObject(passwordList.keySet());
                        JSONArray passwordListValue = JSONArray.fromObject(passwordList.values());
                        List<DefaultsEntity> passwordList2=new ArrayList<>();
                        for (int text=0;text<passwordList.size();text++) {
                            String passwordName="extraAttrs["+passwordListKey.get(text).toString()+"]";
                            params.add(new BasicNameValuePair(passwordName, passwordListValue.get(text).toString()));
                        }
                        JSONObject selectList = JSONObject.fromObject(variables.get("selectList").toString());
                        JSONArray selectListKey = JSONArray.fromObject(selectList.keySet());
                        JSONArray selectListValue = JSONArray.fromObject(selectList.values());
                        for (int text=0;text<selectList.size();text++) {
                            String selectName="extraAttrs["+selectListKey.get(text).toString()+"]";
                            params.add(new BasicNameValuePair(selectName, selectListValue.get(text).toString()));
                        }
                        JSONObject dateList = JSONObject.fromObject(variables.get("dateList").toString());
                        JSONArray dateListKey = JSONArray.fromObject(dateList.keySet());
                        JSONArray dateListValue = JSONArray.fromObject(dateList.values());
                        for (int text=0;text<dateList.size();text++) {
                            String dateName="extraAttrs["+dateListKey.get(text).toString()+"]";
                            params.add(new BasicNameValuePair(dateName, dateListValue.get(text).toString()));

                        }
                        //??????????????????
                        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                        System.out.println("======= ???????????????" + str);
                        String ifa=iamInterface.accountUpdate(str,account.getAddr(),account.getType());
                        System.out.println("======= ?????????????????????" + ifa);
                    }

                }
            }
        }
        processEngine.getTaskService()// ??????????????????????????????Service
                .complete(task.getId(),variable);

        return li;
    }



    /**
     * ?????????????????????
     *
     * @return
     */
    @RequestMapping("/deleteTask")
    @ResponseBody
    public int deleteTask (@RequestParam("id") String pid){
        int withdraw=0;
        //1:??????ProcessEngine??????
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2?????????HistoryService??????
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.deleteProcessInstance(pid, "??????");
        withdraw=1;
        return withdraw;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @RequestMapping("/withdraw")
    @ResponseBody
    public int withdraw (@RequestParam("id") String id, HttpSession session, HttpServletRequest request) throws
            Exception {
        int withdraw=0;
        //1:??????ProcessEngine??????
        /*        id="10049";*/
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2?????????TaskService??????
        TaskService taskService = processEngine.getTaskService();
        //2?????????HistoryService??????
        HistoryService historyService = processEngine.getHistoryService();
        //2?????????RuntimeService??????
        String name = (String) session.getAttribute("userSn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        Task task = taskService.createTaskQuery()
                /* .taskAssignee(name)*/
                .processInstanceId(id)
                .singleResult();//?????????
        System.out.println(task.getId());
        if (task == null) {
            /// throw new ServiceException("????????????????????????????????????????????????");
            System.out.println("????????????????????????????????????????????????");
            withdraw=1;
        } else {
            System.out.println("????????????");

            List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                    /* .taskAssignee(name)//?????????*/
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
                System.out.println("?????????????????????????????????????????????");
                withdraw=2;
            } else {
                System.out.println("????????????");
                RepositoryService repositoryService = processEngine.getRepositoryService();
                String processDefinitionId = myTask.getProcessDefinitionId();
                ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
                BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

                //??????
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

                //?????????????????????
                List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
                oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

                //??????????????????
                flowNode.getOutgoingFlows().clear();
                //???????????????
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
                //????????????
                taskService.complete(task.getId(), currentVariables);
                //???????????????
                flowNode.setOutgoingFlows(oriSequenceFlows);
                withdraw=3;
            }
        }
        return withdraw;
    }

    /*
     * ??????????????????
     * @return
     */

    @RequestMapping("/hisTask")
    @ResponseBody
    public List<HistoricEntity> queryDoneTasks (@RequestParam("id") String id,String applyPer,String taskTy,String taskStatus, HttpSession session){
        List<HistoricEntity> listTask=new ArrayList<>();
        String createTime="";
        //1:??????ProcessEngine??????
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2?????????HistoryService??????
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
                //????????????????????????
                if("startEvent".equals(tasks.getActivityType())){
                    System.out.println("?????????????????????"+df.format(tasks.getStartTime()));
                    createTime=df.format(tasks.getStartTime());
                }
                if("endEvent".equals(tasks.getActivityType())){
                    System.out.println("??????????????????"+tasks.getProcessInstanceId()+"###"+tasks.getActivityName());
                    List<HistoricVariableInstance> list = processEngine.getHistoryService()
                            .createHistoricVariableInstanceQuery() //?????????????????????????????????????????????
                            .processInstanceId(tasks.getProcessInstanceId())
                            .list();
                    if(list !=null && list.size()>0){
                        hisTask.setId(tasks.getId());
                        hisTask.setProcessInstanceId(tasks.getProcessInstanceId());
                        hisTask.setCreateTime(createTime);
                        hisTask.setEndTime(df.format(tasks.getEndTime()));
                        hisTask.setStatus("?????????");
                        List<ActEntity> accountField = null;
                        //??????????????????????????????????????????????????????????????????
                        for(HistoricVariableInstance historicVariableInstance:list){
                            if ("taskType".equals(historicVariableInstance.getVariableName())&&"????????????".equals(historicVariableInstance.getValue().toString())){
                                for(HistoricVariableInstance hvi:list) {
                                    List<Comment> comments = taskService.getTaskComments(hvi.getTaskId());
                                    //??????????????????????????????????????????????????????
                                    if (comments != null && comments.size() > 0) {
                                        hisTask.setComment(comments);
                                    } else {
                                        hisTask.setComment(comments);
                                    }
                                    if ("role".equals(hvi.getVariableName())) {
                                        hisTask.setRole(hvi.getValue().toString());
                                    }
                                    if ("applyReason".equals(hvi.getVariableName())) {
                                        hisTask.setApplyReason(hvi.getValue().toString());
                                    }
                                    if ("applyPersonName".equals(hvi.getVariableName())) {
                                        hisTask.setApplyPerson(hvi.getValue().toString());
                                    }
                                    if ("approvedPerson".equals(hvi.getVariableName())) {
                                        hisTask.setApprovedPerson(hvi.getValue().toString());
                                    }
                                    if ("taskType".equals(hvi.getVariableName())) {
                                        hisTask.setTaskType(hvi.getValue().toString());
                                    }
                                    if ("orgName".equals(hvi.getVariableName())) {
                                        hisTask.setOrgName(hvi.getValue().toString());
                                    }
                                    if ("position".equals(hvi.getVariableName())) {
                                        hisTask.setPosition(hvi.getValue().toString());
                                    }
                                }
                            }else if ("taskType".equals(historicVariableInstance.getVariableName())&&"????????????".equals(historicVariableInstance.getValue().toString())){
                                for(HistoricVariableInstance hvi:list) {
                                    List<Comment> comments = taskService.getTaskComments(hvi.getTaskId());
                                    //??????????????????????????????????????????????????????
                                    if (comments != null && comments.size() > 0) {
                                        hisTask.setComment(comments);
                                    } else {
                                        hisTask.setComment(comments);
                                    }
                                    if ("role".equals(hvi.getVariableName())) {
                                        hisTask.setRole(hvi.getValue().toString());
                                    }
                                    if("app".equals(hvi.getVariableName())){
                                        hisTask.setApp(hvi.getValue().toString());
                                        accountField=defaultService.act(hvi.getValue().toString());
                                    }
                                    if ("applyReason".equals(hvi.getVariableName())) {
                                        hisTask.setApplyReason(hvi.getValue().toString());
                                    }
                                    if ("applyPersonName".equals(hvi.getVariableName())) {
                                        hisTask.setApplyPerson(hvi.getValue().toString());
                                    }
                                    if ("approvedPerson".equals(hvi.getVariableName())) {
                                        hisTask.setApprovedPerson(hvi.getValue().toString());
                                    }
                                    if ("taskType".equals(hvi.getVariableName())) {
                                        hisTask.setTaskType(hvi.getValue().toString());
                                    }
                                    if("account".equals(hvi.getVariableName())){
                                        hisTask.setAccount(hvi.getValue().toString());
                                    }
                                }
                            }else if ("????????????".equals(historicVariableInstance.getValue().toString())||"????????????".equals(historicVariableInstance.getValue().toString())) {
                                for(HistoricVariableInstance hvi:list){
                                    List<Comment> comments = taskService.getTaskComments(hvi.getTaskId());
                                    //??????????????????????????????????????????????????????
                                    System.out.println(comments.size());
                                    if(comments!=null && comments.size()>0){
                                        hisTask.setComment(comments);
                                    }else {
                                        hisTask.setComment(comments);
                                    }
                                    if("role".equals(hvi.getVariableName())){
                                        hisTask.setRole(hvi.getValue().toString());
                                    }
                                    if("applyReason".equals(hvi.getVariableName())){
                                        hisTask.setApplyReason(hvi.getValue().toString());
                                    }
                                    if("applyPersonName".equals(hvi.getVariableName())){
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
                                    if("app".equals(hvi.getVariableName())){
                                        hisTask.setApp(hvi.getValue().toString());
                                        accountField=defaultService.act(hvi.getValue().toString());
                                    }
                                    if("account".equals(hvi.getVariableName())){
                                        hisTask.setAccount(hvi.getValue().toString());
                                    }
                                    if ("accountOrg".equals(hvi.getVariableName())) {
                                        hisTask.setAccountOrg(hvi.getValue().toString());
                                    }
                                    if ("actType".equals(hvi.getVariableName())) {
                                        hisTask.setActType(hvi.getValue().toString());
                                    }

                                    if ("textList".equals(hvi.getVariableName())) {
                                        JSONObject textLists = JSONObject.fromObject(hvi.getValue().toString());
                                        JSONArray textListKey = JSONArray.fromObject(textLists.keySet());
                                        JSONArray textListValue = JSONArray.fromObject(textLists.values());
                                        List<DefaultsEntity> textList2 = new ArrayList<>();
                                        for (int text = 0; text < textLists.size(); text++) {
                                            for (int i = 0; i < accountField.size(); i++) {
                                                if (accountField.get(i).getName().equals(textListKey.get(text).toString())) {
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
                                    if ("passwordList".equals(hvi.getVariableName())) {

                                        JSONObject passwordList = JSONObject.fromObject(hvi.getValue().toString());
                                        JSONArray passwordListKey = JSONArray.fromObject(passwordList.keySet());
                                        JSONArray passwordListValue = JSONArray.fromObject(passwordList.values());
                                        List<DefaultsEntity> passwordList2 = new ArrayList<>();
                                        for (int text = 0; text < passwordList.size(); text++) {
                                            for (int i = 0; i < accountField.size(); i++) {
                                                if (accountField.get(i).getName().equals(passwordListKey.get(text).toString())) {
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
                                    if ("selectList".equals(hvi.getVariableName())) {
                                        JSONObject selectList = JSONObject.fromObject(hvi.getValue().toString());
                                        JSONArray selectListKey = JSONArray.fromObject(selectList.keySet());
                                        JSONArray selectListValue = JSONArray.fromObject(selectList.values());
                                        List<DefaultsEntity> selectList2 = new ArrayList<>();
                                        //??????????????????value???
                                        for (int text = 0; text < selectList.size(); text++) {
                                            for (int i = 0; i < accountField.size(); i++) {
                                                if (accountField.get(i).getName().equals(selectListKey.get(text).toString())) {
                                                    DefaultsEntity defaultsEntity = new DefaultsEntity();
                                                    if (StringUtils.isEmpty(accountField.get(i).getCompant())) {

                                                    } else {
                                                        String compantStr = accountField.get(i).getCompant();
                                                        List<String> result = Arrays.asList(compantStr.split(","));
                                                        for (int c = 0; c < result.size(); c++) {
                                                            String[] com = result.get(c).split("[=]");
                                                            String keys = com[0];
                                                            String values = com[1];
                                                            if (keys.equals(selectListValue.get(text).toString())) {
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
                                    if ("dateList".equals(hvi.getVariableName())) {
                                        JSONObject dateList = JSONObject.fromObject(hvi.getValue().toString());
                                        JSONArray dateListKey = JSONArray.fromObject(dateList.keySet());
                                        JSONArray dateListValue = JSONArray.fromObject(dateList.values());
                                        List<DefaultsEntity> dateList2 = new ArrayList<>();
                                        for (int text = 0; text < dateList.size(); text++) {
                                            for (int i = 0; i < accountField.size(); i++) {
                                                if (accountField.get(i).getName().equals(dateListKey.get(text).toString())) {
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
                        }
                    }
                    if (taskStatus==null||taskStatus==""||"?????????".matches(".*"+taskStatus+".*")){
                        if(applyPer==null||applyPer==""){
                            if (taskTy==null||taskTy==""){
                                listTask.add(hisTask);
                            }else {
                                for(HistoricVariableInstance hvi:list) {
                                    if("taskType".equals(hvi.getVariableName())){
                                        if (hvi.getValue().toString().matches(".*"+taskTy+".*")){
                                            listTask.add(hisTask);
                                        }
                                    }
                                }
                            }
                        }else {
                            if(taskTy==null||taskTy==""){
                                for(HistoricVariableInstance hvi:list) {
                                    if("applyPersonName".equals(hvi.getVariableName())){
                                        if (hvi.getValue().toString().matches(".*"+applyPer+".*")){
                                            listTask.add(hisTask);
                                        }
                                    }
                                }
                            }else {
                                for(HistoricVariableInstance hvi:list) {
                                    if("taskType".equals(hvi.getVariableName())){
                                        for(HistoricVariableInstance hvi2:list) {
                                            if("applyPersonName".equals(hvi2.getVariableName())){
                                                if ((hvi.getValue().toString().matches(".*"+taskTy+".*"))&&(hvi2.getValue().toString().matches(".*"+applyPer+".*"))){
                                                    listTask.add(hisTask);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else if("startEvent".equals(tasks.getActivityType())){
                    HistoricActivityInstance unTasks =historyService.createHistoricActivityInstanceQuery()
                            .processInstanceId(hset)
                            .unfinished()//??????????????????(??????)
                            .singleResult();
                    //??????????????????
                    if(unTasks!=null){
                        System.out.println("?????????????????????"+unTasks.getProcessInstanceId()+"*****"+unTasks.getActivityName());
                        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                                .createHistoricVariableInstanceQuery() //?????????????????????????????????????????????
                                .processInstanceId(tasks.getProcessInstanceId())
                                .list();
                        if(list !=null && list.size()>0) {
                            hisTask.setId(tasks.getId());
                            hisTask.setProcessInstanceId(tasks.getProcessInstanceId());
                            hisTask.setCreateTime(df.format(tasks.getStartTime()));
                            hisTask.setEndTime("");
                            hisTask.setStatus("?????????");

                            List<ActEntity> accountField = null;
                            //?????????????????????????????????????????????????????????????????????
                            for (HistoricVariableInstance historicVariableInstance : list) {
                                System.out.println("???????????????"+historicVariableInstance.getValue().toString());
                                if ("????????????".equals(historicVariableInstance.getValue().toString())) {
                                    for (HistoricVariableInstance hvi : list) {
                                        List<Comment> comments = taskService.getTaskComments(hvi.getTaskId());
                                        //??????????????????????????????????????????????????????
                                        if (comments != null && comments.size() > 0) {
                                            hisTask.setComment(comments);
                                        } else {
                                            hisTask.setComment(comments);
                                        }
                                        if ("role".equals(hvi.getVariableName())) {
                                            hisTask.setRole(hvi.getValue().toString());
                                        }
                                        if ("applyReason".equals(hvi.getVariableName())) {
                                            hisTask.setApplyReason(hvi.getValue().toString());
                                        }
                                        if ("applyPersonName".equals(hvi.getVariableName())) {
                                            hisTask.setApplyPerson(hvi.getValue().toString());
                                        }
                                        if ("approvedPerson".equals(hvi.getVariableName())) {
                                            hisTask.setApprovedPerson(hvi.getValue().toString());
                                        }
                                        if ("taskType".equals(hvi.getVariableName())) {
                                            hisTask.setTaskType(hvi.getValue().toString());
                                        }
                                        if ("orgName".equals(hvi.getVariableName())) {
                                            hisTask.setOrgName(hvi.getValue().toString());
                                        }
                                        if ("position".equals(hvi.getVariableName())) {
                                            hisTask.setPosition(hvi.getValue().toString());
                                        }
                                    }
                                } else if ("????????????".equals(historicVariableInstance.getValue().toString())) {
                                    for (HistoricVariableInstance hvi : list) {
                                        List<Comment> comments = taskService.getTaskComments(hvi.getTaskId());
                                        //??????????????????????????????????????????????????????
                                        if (comments != null && comments.size() > 0) {
                                            hisTask.setComment(comments);
                                        } else {
                                            hisTask.setComment(comments);
                                        }
                                        if ("role".equals(hvi.getVariableName())) {
                                            hisTask.setRole(hvi.getValue().toString());
                                        }
                                        if ("app".equals(hvi.getVariableName())) {
                                            hisTask.setApp(hvi.getValue().toString());
                                            accountField = defaultService.act(hvi.getValue().toString());
                                        }
                                        if ("applyReason".equals(hvi.getVariableName())) {
                                            hisTask.setApplyReason(hvi.getValue().toString());
                                        }
                                        if ("applyPersonName".equals(hvi.getVariableName())) {
                                            hisTask.setApplyPerson(hvi.getValue().toString());
                                        }
                                        if ("approvedPerson".equals(hvi.getVariableName())) {
                                            hisTask.setApprovedPerson(hvi.getValue().toString());
                                        }
                                        if ("taskType".equals(hvi.getVariableName())) {
                                            hisTask.setTaskType(hvi.getValue().toString());
                                        }
                                        if ("account".equals(hvi.getVariableName())) {
                                            hisTask.setAccount(hvi.getValue().toString());
                                        }
                                    }
                                } else if ("????????????".equals(historicVariableInstance.getValue().toString())||"????????????".equals(historicVariableInstance.getValue().toString())) {
                                    for(HistoricVariableInstance hvi:list){
                                        List<Comment> comments = taskService.getTaskComments(hvi.getTaskId());
                                        //??????????????????????????????????????????????????????
                                        System.out.println(comments.size());
                                        if(comments!=null && comments.size()>0){
                                            hisTask.setComment(comments);
                                        }else {
                                            hisTask.setComment(comments);
                                        }

                                        if("role".equals(hvi.getVariableName())){
                                            hisTask.setRole(hvi.getValue().toString());
                                        }
                                        if ("app".equals(hvi.getVariableName())) {
                                            hisTask.setApp(hvi.getValue().toString());
                                            accountField = defaultService.act(hvi.getValue().toString());
                                        }
                                        if("applyReason".equals(hvi.getVariableName())){
                                            hisTask.setApplyReason(hvi.getValue().toString());
                                        }
                                        if("applyPersonName".equals(hvi.getVariableName())){
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
                                        if("accountOrg".equals(hvi.getVariableName())){
                                            hisTask.setAccountOrg(hvi.getValue().toString());
                                        }
                                        if("actType".equals(hvi.getVariableName())){
                                            hisTask.setActType(hvi.getValue().toString());
                                        }

                                        if ("account".equals(hvi.getVariableName())) {
                                            hisTask.setAccount(hvi.getValue().toString());
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
                                            //??????????????????value???
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

                                                hisTask.setDateLists(dateList2);
                                            }


                                        }

                                    }
                                }
                            }
                        }
                        if (taskStatus==null||taskStatus==""||"?????????".matches(".*"+taskStatus+".*")){
                            if(applyPer==null||applyPer==""){
                                if (taskTy==null||taskTy==""){
                                    listTask.add(hisTask);
                                }else {
                                    for(HistoricVariableInstance hvi:list) {
                                        if("taskType".equals(hvi.getVariableName())){
                                            if (hvi.getValue().toString().matches(".*"+taskTy+".*")){
                                                listTask.add(hisTask);
                                            }
                                        }
                                    }
                                }
                            }else {
                                if(taskTy==null||taskTy==""){
                                    for(HistoricVariableInstance hvi:list) {
                                        if("applyPersonName".equals(hvi.getVariableName())){
                                            if (hvi.getValue().toString().matches(".*"+applyPer+".*")){
                                                listTask.add(hisTask);
                                            }
                                        }
                                    }
                                }else {
                                    for(HistoricVariableInstance hvi:list) {
                                        if("taskType".equals(hvi.getVariableName())){
                                            for(HistoricVariableInstance hvi2:list) {
                                                if("applyPersonName".equals(hvi2.getVariableName())){
                                                    if ((hvi.getValue().toString().matches(".*"+taskTy+".*"))&&(hvi2.getValue().toString().matches(".*"+applyPer+".*"))){
                                                        listTask.add(hisTask);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return listTask;
    }
}
