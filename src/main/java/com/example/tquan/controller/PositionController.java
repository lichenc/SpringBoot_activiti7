package com.example.tquan.controller;

import com.beust.jcommander.internal.Lists;
import com.example.tquan.entity.*;
import com.example.tquan.entity.TaskEntity;
import com.example.tquan.service.ApproverService;
import com.example.tquan.service.PositionService;
import com.example.tquan.service.TasksService;
import com.example.tquan.service.VariableService;
import com.example.tquan.util.IamInterface;
import com.ninghang.core.util.StringUtils;
import net.sf.json.JSONObject;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjin on 2021/6/8 9:54
 */
@RestController
public class PositionController {
    @Autowired
    private PositionService positionService;
    @Autowired
    private VariableService variableService;
    @Autowired
    private ApproverService approverService;
    @Autowired
    private TasksService tasksService;
    ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
    @Autowired
    private IamUserEntity user;
    @Autowired
    private IamOauthEntity iam;
    private Log log = LogFactory.getLog(getClass());
    IamInterface iamInterface=new IamInterface();


    /**
     * 获取岗位列表
     * @return
     */
    @RequestMapping("/getPositionList")
    public PositionEntity getpositionList(HttpSession session,String type,String uuid,String id,String orgId){
       String userSn= String.valueOf(session.getAttribute("userSn"));
        String userId=session.getAttribute("UserId").toString();
       List<Map> positionList=new ArrayList<>();
        PositionEntity positionEntity=new PositionEntity();
        //用户所属组织

       try{
           //获取所有岗位
           List<PositionEntity> positionEntityList= positionService.findAll();
           List<PositionEntity> userPositionEntityList=positionService.getPositionByUserId(userId);
           for (PositionEntity pos:positionEntityList){
               Map map=new HashMap();
               map.put("name",pos.getName());
               map.put("value",pos.getName());
               for (PositionEntity defPos:orgPosition(uuid,session)){
                    if(defPos.getPositionId().equals(pos.getId())){
                        map.put("selected",true);
                    }
               }
               for (PositionEntity userPos:userPositionEntityList){
                   if (userPos.getId().equals(pos.getId())){
                       map.put("selected",true);
                   }
               }
               positionList.add(map);
           }
           System.out.println(positionList.toString());
           positionEntity.setPositionEntityList(positionList);
           positionEntity.setUserSn(userSn);
           positionEntity.setApproverList(approverService.audit(userSn));
           //修改岗位申请，回显数据
           if (type!=null){
               VariableEntity variableEntity=new VariableEntity(id,"applyReason");
               VariableEntity variableEntity1=new VariableEntity(id,"approvedPerson");
               VariableEntity variableEntity2=new VariableEntity(id,"position");
               //查询申请原因
               String applyReason= variableService.getTextByName(variableEntity);
               //查询审批人
               String approvedPerson=variableService.getTextByName(variableEntity1);
               //查询申请岗位
               String position=variableService.getTextByName(variableEntity2);
               positionEntity.setApplyReason(applyReason);
               positionEntity.setApprovedPerson(approvedPerson);
               positionEntity.setPosition(position);
           }
       }catch (Exception e){
           log.info("==========================查询失败！"+e
           );
       }

        return positionEntity;
    }

    public String userOrg(String uuid,  HttpSession session) throws Exception{
        String userId=session.getAttribute("UserId").toString();
        String ifo=iamInterface.oauth(uuid,iam.getKey(),iam.getPassword(),iam.getAddr(),iam.getUsername(),iam.getType(),iam.getCharset());
        JSONObject resultJson = null;
        if(StringUtils.isEmpty(ifo)) {
            log.info("==========================uuid为空！");
        }else {
            //调用统权的接口，获取用户信息
            List<NameValuePair> params = Lists.newArrayList();
            params.add(new BasicNameValuePair("id", userId));
            params.add(new BasicNameValuePair("uim-login-user-id", ifo));
            //转换为键值对
            String userStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            StringBuilder ifus=iamInterface.userSelect(userStr,user.getAddr(),user.getType());
            resultJson = JSONObject.fromObject(ifus.toString());
        }
        return resultJson.get("orgId").toString();
    }

    /**
     * 根据选择组织查询岗位
     */
    @RequestMapping("/userOrg")
    public List<Map> newOrgPosition(String org,String uuid, HttpSession session) throws Exception {
        String userId=session.getAttribute("UserId").toString();
        List<Map> positionList=new ArrayList<>();
        System.out.println(userOrg(uuid,session).toString()+"====="+org);
        if(userOrg(uuid,session).toString().equals(org)){
            List<PositionEntity> userPositionEntityList=positionService.getPositionByUserId(userId);
            for (PositionEntity userPos:userPositionEntityList){
                Map map = new HashMap();
                map.put("name", userPos.getName());
                map.put("value", userPos.getName());
                map.put("selected",true);
                positionList.add(map);
            }
        }else {
            List<PositionEntity> positionEntityList= positionService.orgPosition(org);
            for (PositionEntity defPos:positionEntityList) {
                Map map = new HashMap();
                map.put("name", defPos.getName());
                map.put("value", defPos.getName());
                map.put("selected", true);
                positionList.add(map);
            }
        }
        return positionList;
    };
    /**
     * 组织默认岗位
     */
    public List<PositionEntity> orgPosition(String uuid, HttpSession session){
        List<PositionEntity> positionEntityList= null;
        try {
            positionEntityList = positionService.orgPosition(userOrg(uuid,session).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return positionEntityList;
    }

    /**
     * 添加岗位流程
     * @return
     */
    @RequestMapping("/addPositionProcess")
    public int addPositionProcess(String position, String applyReason,String approvedPerson,String orgName,
                                  String orgId, HttpSession session, HttpServletRequest request){
        System.out.println(orgName);
        int iden=0;
       try{
           //非空判断，防止越过前端非空验证
           if(position != null && applyReason != null && applyReason != "" && position != "" && approvedPerson != null && approvedPerson != ""){

               String approvedPersonStr=approvedPerson.substring(approvedPerson.indexOf("(")+1,approvedPerson.indexOf(")"));

               String userId=session.getAttribute("UserId").toString();
               //查询申请的岗位id
               String positionId=positionService.getPositionByName(position);
               //为用户查询岗位设置参数
               PositionEntity positionEntity=new PositionEntity();
               positionEntity.setUserId(userId);
               positionEntity.setPositionId(positionId);

               //查询用户岗位
               PositionEntity positionEntity1= positionService.getInfo(positionEntity);

               //用户未拥有申请的岗位
             /*  if (positionEntity1==null){*/
                   RuntimeService runtimeService = processEngine.getRuntimeService();
                   String sn = (String) session.getAttribute("userSn");
                   Map<String,Object> map = new HashMap<String,Object>();
                   map.put("applyPerson",sn);
                   map.put("position",position);
                   map.put("applyReason",applyReason);
                   map.put("orgName",orgName);
                   map.put("orgId",orgId);
                   map.put("userIds",userId);
                   map.put("taskType","用户移动");
                   map.put("approvedPerson",approvedPersonStr);
                   ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("positionApply",map);
                   log.info("=========================="+sn+"申请了"+position+"用户移动！");
                   iden=2;
                   findTask(/*firstResult,maxResults,*/sn,request);

                   //用户已拥有申请的岗位
               /*}else{
                   iden=1;
               }*/
           }else{
               log.info("==========================申请岗位或申请原因为空，添加失败！");
           }
       }catch (Exception e){
           log.info("==========================申请岗位或申请原因为空，添加失败！"+e);
           e.printStackTrace();
       }
       return iden;
    }
    /**
     *查询待重试的流程
     * @return
     */
    @RequestMapping("/waitTryAgain")
    public com.example.tquan.entity.TaskEntity waitTryAgainPage(com.example.tquan.entity.TaskEntity taskEntity, HttpSession session, String approvedPerson){
        if (approvedPerson!=null &&approvedPerson!=""){
            taskEntity.setApprovedPerson(approvedPerson);
        }
        String sn=session.getAttribute("userSn").toString();
        taskEntity.setApplyPerson(sn);
        try {
            List<com.example.tquan.entity.TaskEntity> taskEntities= tasksService.getWaitTryAgainTask(taskEntity);
            List<com.example.tquan.entity.TaskEntity> taskEntities1=new ArrayList<>();
            for(TaskEntity taskEntity2:taskEntities) {
                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(taskEntity2.getTaskType());
                taskEntity2.setApplyPerson(variables.get("applyPerson").toString());
                taskEntity2.setApprovedPerson(variables.get("approvedPerson").toString());
                taskEntity2.setTaskType(variables.get("taskType").toString());
                taskEntity2.setApplyReason(variables.get("applyReason").toString());
                //判断角色原因是否为空
                VariableEntity variableEntity=new VariableEntity();
                variableEntity.setProcInstId(taskEntity2.getId());
                variableEntity.setName("repulseReason");
                String text= variableService.getTextByName(variableEntity);
                if (text!=null){
                    taskEntity2.setRepulseReason(variables.get("repulseReason").toString());
                } else {
                    taskEntity2.setRepulseReason("");
                }
                taskEntities1.add(taskEntity2);
                taskEntity.setTaskEntities(taskEntities1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return taskEntity;
    }
    /**
     * 查询任务
     */
    public HttpServletRequest findTask(/*int firstResult,int maxResults,*/ String name,HttpServletRequest request) {
        //1:得到ProcessEngine对象

        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(name)//指定个人任务查询
                .list();
        /*.listPage(firstResult, maxResults);*/
        request.setAttribute("task",list);
        reSubmit1(list.get(0).getId());
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
     * 申请人完成申请任务
     * @param id
     */
    @RequestMapping("/reSubmit")
    public int reSubmit(String id) {
        if (id!=null){


            /*HashMap<String, Object> variables = new HashMap<>();
            variables.put("days", days);*///userKey在上文的流程变量中指定了
            // taskService.claim(taskid,"ZJ2");//指定办理人
            //taskService.setAssignee(taskid, null);//回退为组任务状态
            /*taskId="72507";*/
            processEngine.getTaskService()// 与正在执行任务相关的Service
                    .complete(id);
            System.out.println("完成任务：任务ID：" + id);
        }
        return 1;
    }

    public int reSubmit1(String id) {
        if (id!=null){


            /*HashMap<String, Object> variables = new HashMap<>();
            variables.put("days", days);*///userKey在上文的流程变量中指定了
            // taskService.claim(taskid,"ZJ2");//指定办理人
            //taskService.setAssignee(taskid, null);//回退为组任务状态
            /*taskId="72507";*/
            processEngine.getTaskService()// 与正在执行任务相关的Service
                    .complete(id);
            System.out.println("完成任务：任务ID：" + id);
        }
        return 1;
    }
    /**
     * 申请人撤回未审批的任务
     *
     * @return
     */
    @RequestMapping("/backProcess")
    public int withdraw (@RequestParam("id") String id, HttpSession session) throws Exception{
        int iden=1;
        log.info("---------------------------------"+id);
        //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        //2：得到HistoryService对象
        HistoryService historyService=processEngine.getHistoryService();
        //2：得到RuntimeService对象
        String name = (String) session.getAttribute("userSn");
        //获取当前用户的审批人
        List<Approver> approvers= approverService.audit(name);
        String approverPerson=approvers.get(0).toString();
        String approvedPersonStr=approverPerson.substring(approverPerson.indexOf("(")+1,approverPerson.indexOf(")"));


        RuntimeService  runtimeService=processEngine.getRuntimeService();
       /* Task task = taskService.createTaskQuery()
                .taskAssignee(approvedPersonStr).singleResult();*/
        Task task=taskService.createTaskQuery().processInstanceId(id).singleResult();//审核人
        System.out.println(task.getId());
        if(task==null) {
            /// throw new ServiceException("流程未启动或已执行完成，无法撤回");
            System.out.println("流程未启动或已执行完成，无法撤回");
            iden=2;
        }else {
            System.out.println("可以撤回");
        }

        //LoginUser loginUser = SessionContext.getLoginUser();

        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
               /* .taskAssignee(name)//撤回人*/
                .processInstanceId(id)
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
            iden=2;
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

        return iden;
    }

    /**
     * 获取岗位流程参数
     * @return
     */
    @RequestMapping("/getPositionParamList")
    public List<VariableEntity> getPositionParamList(String startTime,String endTime,String approvedPerson,HttpSession session){
        List<VariableEntity> variableEntityList=new ArrayList<>();
       try{
           String sn=session.getAttribute("userSn").toString();
           VariableEntity variableEntity4=new VariableEntity();
           variableEntity4.setProcDefId("positionApply");
           //按日期区间查找
           if(startTime !=null && startTime != ""){
               variableEntity4.setStartTime(startTime);
           }
           if(endTime!=null && endTime != ""){
               variableEntity4.setEndTime(endTime);
           }
           if (approvedPerson!= null &&approvedPerson!=""){
               variableEntity4.setApprovedPerson(approvedPerson);
           }

           //获取岗位流程的实例ID
           List<VariableEntity> variableEntities=variableService.getProcessParamByName(variableEntity4);


           //循环获取申请岗位参数
           for(VariableEntity variableEntity1:variableEntities){

               Map<String, Object> variables = processEngine.getRuntimeService().getVariables(variableEntity1.getProcInstId());
               if (variables.get("applyPerson")==sn||variables.get("applyPerson").equals(sn)){
                   for (Map.Entry<String, Object> entry : variables.entrySet()) {

                       variableEntity1.setApplyPerson(variables.get("applyPerson").toString());
                       variableEntity1.setApplyReason(variables.get("applyReason").toString());
                       variableEntity1.setTaskType(variables.get("taskType").toString());
                       variableEntity1.setApprovedPerson(variables.get("approvedPerson").toString());
                       variableEntity1.setPosition(variables.get("position").toString());
                       //判断角色原因是否为空
                       VariableEntity variableEntity=new VariableEntity();
                       variableEntity.setProcInstId(variableEntity1.getProcInstId());
                        variableEntity.setName("repulseReason");
                       String text= variableService.getTextByName(variableEntity);
                       if (text!=null){
                           variableEntity1.setRepulseReason(variables.get("repulseReason").toString());
                       }else {
                           variableEntity1.setRepulseReason("");
                       }
                   }
                   //最终list集合
                   variableEntityList.add(variableEntity1);
               }

           }
       }catch(Exception e){
            e.printStackTrace();
       }
        return variableEntityList;
    }

    /**
     * 修改岗位信息
     * @return
     */
    @RequestMapping("/updatePosition")
    public int updatePosition(String position, String applyReason,String id,HttpSession session){
        int iden=0;

        try{
            //非空判断，防止越过前端非空验证
            if(position != null && applyReason != null && applyReason != "" && position != "" ){

                String userId=session.getAttribute("UserId").toString();
                //查询申请的岗位id
                String positionId=positionService.getPositionByName(position);
                //为用户查询岗位设置参数
                PositionEntity positionEntity=new PositionEntity();
                positionEntity.setUserId(userId);
                positionEntity.setPositionId(positionId);

                //查询用户岗位
                PositionEntity positionEntity1= positionService.getInfo(positionEntity);

                //用户未拥有申请的岗位
                if (positionEntity1==null){
                    //修改申请岗位
                    VariableEntity variableEntity=new VariableEntity();
                    variableEntity.setProcInstId(id);
                    variableEntity.setName("position");
                    variableEntity.setText(position);
                    variableService.updateTaskParam(variableEntity);
                    //修改申请原因
                    VariableEntity variableEntity1=new VariableEntity();
                    variableEntity1.setProcInstId(id);
                    variableEntity1.setName("applyReason");
                    variableEntity1.setText(applyReason);
                    variableService.updateTaskParam(variableEntity1);

                    iden=2;
                    //用户已拥有申请的岗位
                }else{
                    iden=1;
                }
            }else{
                log.info("==========================岗位修改失败！");
            }
        }catch (Exception e){
            log.info("==========================岗位修改失败！"+e);
        }
        return iden;
    }

}



