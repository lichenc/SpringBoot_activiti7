package com.example.tquan.controller;



import com.beust.jcommander.internal.Lists;
import com.example.tquan.entity.*;
import com.example.tquan.util.PwdNum;
import com.example.tquan.util.RsaUtil;
/*import net.sf.json.JSONObject;*/
import com.ninghang.core.util.StringUtils;
import net.sf.json.JSONObject;
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


import com.example.tquan.service.ApproverService;
import com.example.tquan.service.ImAppService;
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


import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller

//@RequestMapping("/audit")
public class ActiController{
    /**
     * form-data数据分隔符前缀
     */
    private static final String BOUNDARY_PREFIX = "----";
    /**
     * 回车换行,用于一行的结尾
     */
    private static final String LINE_END = "\r\n";
    @Autowired
    private IamOauthEntity iam;
    @Autowired
    private IamAccountEntity account;
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
    private ImAppService imAppService;

    @Autowired
    private ApproverService approverService ;
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
    public String start(/*int firstResult,int maxResults,*/ ActivitiEntity activiti,HttpServletRequest request,HttpSession session) {
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
        System.out.println("当前申请人账号："+sn);
        map.put("userId",sn);
        String name=activiti.getProposer();
        System.out.println(name);
        String quStr=name.substring(name.indexOf("(")+1,name.indexOf(")"));
        System.out.println(quStr);
        //查询申请人用户id
        List<ApplyEntity> apply=approverService.apply(sn);
        //查询申请的应用id
        List<ImApp> list=imAppService.findApply(activiti.getApp());

        map.put("user",quStr);
        map.put("name", activiti.getName());
        map.put("type", activiti.getType());
        map.put("app", activiti.getApp());
        map.put("appId", list.get(0).getId());
        map.put("role", activiti.getRole());
        map.put("account", activiti.getAccount());
        map.put("status", account.getStatus());
        map.put("usersId", apply.get(0).getUserId());
        map.put("description", activiti.getDescription());

        ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("myProcess_1",map);
        //String name=activiti.getProposer();
        findTask(/*firstResult,maxResults,*/sn,request);
        return "/apply";
    }



    /**
     * 查询任务
     */
    public HttpServletRequest findTask(/*int firstResult,int maxResults,*/ String name,HttpServletRequest request) {
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(name)//指定个人任务查询
                .list();
                /*.listPage(firstResult, maxResults);*/
        request.setAttribute("task",list);
        CompleteTask(list.get(0).getId());
       /* if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());

            }
        }*/
        return request;
    }

    /**
     * 查询全部任务
     * @return
     */
    @RequestMapping(value = "/selectApplyTast")
    public String findTasks(/*Integer firstResult, Integer maxResults, */String name, HttpServletRequest request, HttpServletRequest requests, HttpSession session) {
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        name="002";
        /*firstResult=0;
        maxResults=2;*/
        TaskService taskService=processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                //.processInstanceBusinessKey("myProcess_1")
                .taskAssignee(name)//指定个人任务查询
                .list();
               /* .listPage(firstResult, maxResults);*/
        request.setAttribute("task",list);
       // ImAppController apply=new ImAppController();

        List appList = imAppService.findAll();
        request.setAttribute("appList",appList);
        String login_name = (String) session.getAttribute("userSn");
        login_name="tmm";
        List audits=approverService.audit(login_name);
        request.setAttribute("audits",audits);
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());

            }
        }
        return "/apply";
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
     * 审核人查询全部任务
     * @return
     */
    @RequestMapping("/audit")
    public HttpServletRequest audit(/*String name, */HttpServletRequest request,HttpSession session) {
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
       /* HttpSession session = sessionRequest.getSession();*/
        String name = (String) session.getAttribute("userSn");
        TaskService taskService=processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(name)//指定个人任务查询
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
                .taskAssignee(name)
                .list();

        Task task = taskService.createTaskQuery().taskId(list.get(0).getId()).singleResult();
        taskService.setAssignee(list.get(0).getId(),null);//归还候选任务
       /* taskService.setAssignee(list.get(0).getId(),"wukong");//交办*/
        return "/audit";
    }

    //审核人拾取任务
    @RequestMapping("/pickup")
    public String claimTask(/*@RequestParam("id") String id,*/ HttpServletRequest request, HttpSession session){
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        String name = (String) session.getAttribute("userSn");
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(name)
                .list();

        List/*<Map<String, Object>>*/ result = new ArrayList<>();
        for(Task tk : list){
            System.out.println("Id："+tk.getId());
            System.out.println("Name："+tk.getName());
            System.out.println("Assignee："+tk.getAssignee());
           /* if(tk.getId().equals(id)==true){
                taskService.claim(tk.getId(),name);
            }*/
           /* Task task = taskService.createTaskQuery().taskId(tk.getId()).singleResult();
            System.out.println(id+"任务拾取成功");
            String processInstanceId = task.getProcessInstanceId();

            Map<String, Object> variables = processEngine.getRuntimeService().getVariables(processInstanceId);
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                System.out.println(entry+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
              *//*  variables*//*
            }*/
            //封装所有任务
          /*  result.add(variables.entrySet());
            result.add(tk.getId());
            result.add(tk.getName());
            result.add(tk.getCreateTime());*/
            /*result.set(4,tk.getId());*/
        }
        //返回所有任务
      /*  System.out.println(result.get(1));*/
        request.setAttribute("pickupTask",list);
        return "/audit";
    }



    /**
     * 审核人完成任务审核
     *
     * @return
     */
    @RequestMapping("/AuditTask")
    public String AuditTask(@RequestParam("id") String id, HttpSession session, String uuid) throws Exception {
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        String name = (String) session.getAttribute("userSn");
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(name)
                .list();
        System.out.println("审核通过的任务id："+list.get(0).getId());
        System.out.println("审核通过的任务id："+id);
        //账号属性查询
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        System.out.println(id+"任务拾取成功");
        String processInstanceId = task.getProcessInstanceId();
        Map<String, Object> variables = processEngine.getRuntimeService().getVariables(processInstanceId);
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            System.out.println(entry+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        processEngine.getTaskService()// 与正在执行任务相关的Service
                .complete(id);
        System.out.println("完成任务：任务ID：" + list.get(0).getId());
        System.out.println(variables.get("account").toString());
        System.out.println(variables.get("appId").toString());
        System.out.println(variables.get("usersId").toString());
        System.out.println(variables.get("status").toString());
        //开通账号
        oauth(uuid);
        if(StringUtils.isEmpty(oauth(uuid).toString())) {
            System.out.println("uuid为空，认证失败，无法开通账号");
        }else {
            String pwds="";
            if(account.getRandomSwitch().equals("true")&&!account.getImmobilizationSwitch().equals("true")){
                //随机数
                PwdNum pwd=new PwdNum();
                pwds=pwd.Pwd(account.getPwdRank(),account.getStrLength(),pwds);

            }else if (account.getImmobilizationSwitch().equals("true")&&!account.getRandomSwitch().equals("true")){
                pwds=account.getPwdDefault();
            }else {
                pwds="000000";
            }
            System.out.println("密码:"+pwds);
            List<NameValuePair> params = Lists.newArrayList();
            params.add(new BasicNameValuePair("uim-login-user-id", oauth(uuid).toString()));
            params.add(new BasicNameValuePair("loginName", variables.get("account").toString()));
            params.add(new BasicNameValuePair("loginPwd", pwds));
            params.add(new BasicNameValuePair("appId", variables.get("appId").toString()));
            params.add(new BasicNameValuePair("userId", variables.get("usersId").toString()));
            params.add(new BasicNameValuePair("status", variables.get("status").toString()));
            //转换为键值对
            String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            post(str);
        }
        return "/audit";
    }




    /**
     * 申请人删除任务
     *
     * @return
     */
    @RequestMapping("/deleteTask")
    public void deleteTask(HttpServletRequest request,HttpSession session) {
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到HistoryService对象
        RuntimeService  runtimeService=processEngine.getRuntimeService();
      /*  //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("001")
                .list();*/
        runtimeService.deleteProcessInstance("", "是的");
        //findTasks("001",request);

        //return "/apply";


    }


    /**
     * 申请人撤回未审批的任务
     *
     * @return
     */
    @RequestMapping("/withdraw")
    public String withdraw (String objId,HttpSession session) throws Exception{
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        //2：得到HistoryService对象
        HistoryService historyService=processEngine.getHistoryService();
        //2：得到RuntimeService对象
        String name = (String) session.getAttribute("userSn");
        RuntimeService  runtimeService=processEngine.getRuntimeService();
        Task task = taskService.createTaskQuery()
                .taskAssignee("tmm2").singleResult();//审核人
        System.out.println(task.getId());
        if(task==null) {
           /// throw new ServiceException("流程未启动或已执行完成，无法撤回");
            System.out.println("流程未启动或已执行完成，无法撤回");
        }else {
            System.out.println("可以撤回");
        }

        //LoginUser loginUser = SessionContext.getLoginUser();

        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(name)//撤回人
                .orderByTaskCreateTime()
                .asc()
                .list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        for(HistoricTaskInstance hti : htiList) {
           /* if(loginUser.getUsername().equals(hti.getAssignee())) {*/
                myTaskId = hti.getId();
                myTask = hti;
                break;
           /* }*/
        }
        if(null==myTaskId) {
            System.out.println("该任务非当前用户提交，无法撤回");
        }else {
            System.out.println("可以撤回");
        }
        System.out.println(myTaskId);

        RepositoryService repositoryService =processEngine.getRepositoryService();
        String processDefinitionId = myTask.getProcessDefinitionId();
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        //变量
		Map<String, VariableInstance> variables = runtimeService.getVariableInstances(task.getExecutionId());
        String myActivityId = null;
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                .executionId(myTask.getExecutionId()).finished().list();
        for(HistoricActivityInstance hai : haiList) {
            if(myTaskId.equals(hai.getTaskId())) {
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
/*        String name="002";//撤回人*/
        Authentication.setAuthenticatedUserId(name);
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "撤回");
        Map<String,Object> currentVariables = new HashMap<String,Object>();
        currentVariables.put("applier", name);
        //完成任务
        taskService.complete(task.getId(),currentVariables);
        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);

        return "/apply";
    }

    /**
     * 查询任务修改
     * @return
     */
    @RequestMapping(value="/updateFind",method = RequestMethod.POST,produces="application/json")
    @ResponseBody
    public List<Map<String, Object>> updateFind(/*@RequestParam("id") String id,*/ HttpServletRequest request) {

        Task task = processEngine.getTaskService().createTaskQuery().taskId(request.getParameter("id")).singleResult();
        //获得流程实例id
        String processInstanceId = task.getProcessInstanceId();
        Map<String, Object> variables = processEngine.getRuntimeService().getVariables(processInstanceId);
        Map<String,Object> map = new HashMap<String,Object>();
        JSONObject json=new JSONObject();
        List<Map<String, Object>> pdResult = new ArrayList<>();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            /*json.put()*/
            map.put("app",entry.getValue());
            map.put("role",entry.getValue());
            map.put("name",entry.getValue());
            map.put("type",entry.getValue());
            map.put("user",entry.getValue());
        }
        pdResult.add(map);
       /* map.put("name",request.getParameter("name"));
        map.put("age",request.getParameter("age"));*/
        System.out.println("sta"+pdResult.get(0).toString());
        return pdResult;
        /*Object value = processEngine.getRuntimeService().getVariable(processInstanceId, "user");
        System.out.println("Value = " + value);*/
    }







    /**
     * 历史任务查询
     * @return
     */
    @RequestMapping("/completeRecordsTask")
    public String queryDoneTasks(String assignee, HttpServletRequest request,HttpSession session) {
        //assignee="001";
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到HistoryService对象
        HistoryService historyService=processEngine.getHistoryService();
        String name = (String) session.getAttribute("userSn");
        List<HistoricTaskInstance> taskList  = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(name)
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




    /**
     * 获取token
     * @return
     * @throws Exception
     */
/*   @RequestMapping("/loginUser")*/
    public String oauth(String uuid)
            throws Exception {
        PublicKey publicKey = RsaUtil.string2PublicKey(iam.getKey());
        //用公钥加密
        byte[] publicEncrypt = RsaUtil.publicEncrypt(iam.getPassword().getBytes(), publicKey);
        //加密后的内容为Base64编码
        String rsa = RsaUtil.byte2Base64(publicEncrypt);
        System.out.println("加密的结果：" + rsa);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(iam.getAddr());
        StringBuilder result = new StringBuilder();
        //组装数据
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("username", iam.getUsername()));
        params.add(new BasicNameValuePair("password", rsa));
        //转换为键值对
        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        System.out.println(str);
        httppost.setEntity(new StringEntity(str, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", iam.getType()+iam.getCharset());
        HttpResponse response;
        response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    instream));
            String temp = "";
            while ((temp = br.readLine()) != null) {
                String str2 = new String(temp.getBytes(), "utf-8");
                result.append(str2).append("\r\n");
            }
            br.close();
            System.out.println("======== 获取到的uid信息：" + result);
            JSONObject resultJson = JSONObject.fromObject(result.toString());
            if(resultJson.get("success").toString().equals("true")){
                System.out.println("msg:"+resultJson.get("msg"));
                uuid=resultJson.get("msg").toString();
                return uuid;
            }else {
                System.out.println(resultJson.get("msg"));
                return uuid;
            }
        }
        return uuid;
    }



    /**
     * post请求
     */
  /*  @RequestMapping("/accountOpen")*/
    public void post(String str) throws Exception {
        System.out.println(str);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(account.getAddr());
        StringBuilder result = new StringBuilder();
        httppost.setEntity(new StringEntity(str, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", account.getType());
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    String str2 = new String(temp.getBytes(), "utf-8");
                    result.append(str2).append("\r\n");
                }
                br.close();
              JSONObject resultJson = JSONObject.fromObject(result.toString());
                if (resultJson.get("success").toString().equals("true")) {
                    System.out.println("msg:" + resultJson.get("msg"));
                   /* return new ResultCode(SUCCESS,resultJson.get("message").toString());*/
                } else {
                    System.out.println("======= 新增账号结果："+ resultJson.get("msg"));
                   /* return new ResultCode(FAIL,resultJson.get("message").toString());*/
                }
            }
        } catch (ClientProtocolException e) {
           /* return new ResultCode(FAIL, e.getMessage());*/
        } catch (IOException e) {
            /*return new ResultCode(FAIL, e.getMessage());*/
        }
       /* return new ResultCode(FAIL, "新增账号请求失败");*/
    }



}
