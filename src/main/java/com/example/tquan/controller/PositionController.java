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
import java.util.*;

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
     * ??????????????????
     * @return
     */
    @RequestMapping("/getPositionList")
    public PositionEntity getpositionList(HttpSession session,String type,String uuid,String id){
       String userSn= String.valueOf(session.getAttribute("userSn"));
        String userId=session.getAttribute("UserId").toString();
       List<Map> positionList=new ArrayList<>();
        PositionEntity positionEntity=new PositionEntity();
        //??????????????????

       try{
           //?????????????????????????????????
           List<PositionEntity> positionEntityList= positionService.findAll();
           List<PositionEntity> userPositionEntityList=positionService.getPositionByUserId(userId);
           System.out.println(positionList.toString());
           positionEntity.setPositionEntityList(positionList);
           positionEntity.setUserSn(userSn);
           positionEntity.setApproverList(approverService.audit(userSn));
           //?????????????????????????????????
           if (type!=null){
               VariableEntity variableEntity=new VariableEntity(id,"applyReason");
               VariableEntity variableEntity1=new VariableEntity(id,"approvedPerson");
               VariableEntity variableEntity2=new VariableEntity(id,"position");
               VariableEntity variableEntity3=new VariableEntity(id,"orgName");
               VariableEntity variableEntity4=new VariableEntity(id,"orgId");
               VariableEntity variableEntity5=new VariableEntity(id,"role");
               //??????????????????
               String applyReason= variableService.getTextByName(variableEntity);
               //???????????????
               String approvedPerson=variableService.getTextByName(variableEntity1);
               //??????????????????
               String position=variableService.getTextByName(variableEntity2);
               String orgName=variableService.getTextByName(variableEntity3);
               String orgId=variableService.getTextByName(variableEntity4);
               String role=variableService.getTextByName(variableEntity5);
               positionEntity.setApplyReason(applyReason);
               positionEntity.setApprovedPerson(approvedPerson);
               positionEntity.setRole(role);
               positionEntity.setOrgId(orgId);
               positionEntity.setOrgName(orgName);
               List<String> positionArr = Arrays.asList(position.split(","));
               for (PositionEntity pos:positionEntityList){
                   Map map=new HashMap();
                   map.put("name",pos.getName());
                   map.put("value",pos.getName());
                   for (int r=0;r<positionArr.size();r++){
                       if (positionArr.get(r).equals(pos.getName())){
                           map.put("selected",true);
                       }
                   }
                   positionList.add(map);
               }
           }else {
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
           }
       }catch (Exception e){
           log.info("==========================???????????????"+e
           );
       }

        return positionEntity;
    }

    public String userOrg(String uuid,  HttpSession session) throws Exception{
        String userId=session.getAttribute("UserId").toString();
        String ifo=iamInterface.oauth(uuid,iam.getKey(),iam.getPassword(),iam.getAddr(),iam.getUsername(),iam.getType(),iam.getCharset());
        JSONObject resultJson = null;
        if(StringUtils.isEmpty(ifo)) {
            log.info("==========================uuid?????????");
        }else {
            //??????????????????????????????????????????
            List<NameValuePair> params = Lists.newArrayList();
            params.add(new BasicNameValuePair("id", userId));
            params.add(new BasicNameValuePair("uim-login-user-id", ifo));
            //??????????????????
            String userStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            StringBuilder ifus=iamInterface.userSelect(userStr,user.getAddr(),user.getType());
            resultJson = JSONObject.fromObject(ifus.toString());
        }
        return resultJson.get("orgId").toString();
    }

    /**
     * ??????????????????????????????
     */
    @RequestMapping("/userOrg")
    public List<Map> newOrgPosition(String org,String uuid, HttpSession session) throws Exception {
        String userId=session.getAttribute("UserId").toString();
        List<Map> positionList=new ArrayList<>();
        List<PositionEntity> userPositionEntityList=positionService.getPositionByUserId(userId);
        if(userOrg(uuid,session).toString().equals(org)){
            for (PositionEntity userPos:userPositionEntityList){
                Map map = new HashMap();
                map.put("name", userPos.getName());
                map.put("value", userPos.getName());
                map.put("selected",true);
                positionList.add(map);
            }
        }else {
            List<PositionEntity> positionEntityList= positionService.orgPosition(org);
            for (PositionEntity userPos:userPositionEntityList){
                String counts = positionService.orgPosCount(userOrg(uuid,session),userPos.getId());
                if ("0".equals(counts)){
                    Map map = new HashMap();
                    map.put("name", userPos.getName());
                    map.put("value", userPos.getName());
                    map.put("selected", true);
                    positionList.add(map);
                }
            }
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
     * ??????????????????
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
     * ??????????????????
     * @return
     */
    @RequestMapping("/addPositionProcess")
    public int addPositionProcess(String position,String role, String applyReason,String approvedPerson,String orgName,
                                  String orgId, HttpSession session, HttpServletRequest request){
        System.out.println(orgName);
        int iden=0;
       try{
           //?????????????????????????????????????????????
           if(position != null && applyReason != null && applyReason != "" && position != "" && approvedPerson != null && approvedPerson != ""){

               String approvedPersonStr=approvedPerson.substring(approvedPerson.indexOf("(")+1,approvedPerson.indexOf(")"));

               String userId=session.getAttribute("UserId").toString();
               //?????????????????????id
               String positionId=positionService.getPositionByName(position);
               //?????????????????????????????????
               PositionEntity positionEntity=new PositionEntity();
               positionEntity.setUserId(userId);
               positionEntity.setPositionId(positionId);

               //??????????????????
               PositionEntity positionEntity1= positionService.getInfo(positionEntity);

               //??????????????????????????????
             /*  if (positionEntity1==null){*/
                   RuntimeService runtimeService = processEngine.getRuntimeService();
                   String sn = (String) session.getAttribute("userSn");
                   Map<String,Object> map = new HashMap<String,Object>();
                   map.put("applyPerson",sn);
                   map.put("position",position);
                   map.put("applyReason",applyReason);
                   map.put("orgName",orgName);
                   map.put("orgId",orgId);
                   map.put("role",role);
                   map.put("userIds",userId);
                   map.put("taskType","????????????");
                   map.put("approvedPerson",approvedPersonStr);
                   ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("positionApply",map);
                   log.info("=========================="+sn+"?????????"+position+"???????????????");
                   iden=2;
                   findTask(/*firstResult,maxResults,*/sn,request);

                   //??????????????????????????????
               /*}else{
                   iden=1;
               }*/
           }else{
               log.info("==========================???????????????????????????????????????????????????");
           }
       }catch (Exception e){
           log.info("==========================???????????????????????????????????????????????????"+e);
           e.printStackTrace();
       }
       return iden;
    }
    /**
     *????????????????????????
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
                //??????????????????????????????
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
     * ????????????
     */
    public HttpServletRequest findTask(/*int firstResult,int maxResults,*/ String name,HttpServletRequest request) {
        //1:??????ProcessEngine??????

        //2?????????TaskService??????
        TaskService taskService=processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()//????????????????????????
                .taskAssignee(name)//????????????????????????
                .list();
        /*.listPage(firstResult, maxResults);*/
        request.setAttribute("task",list);
        reSubmit1(list.get(0).getId());
       /* if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("??????ID:" + task.getId());
                System.out.println("????????????:" + task.getName());
                System.out.println("?????????????????????:" + task.getCreateTime());
                System.out.println("??????????????????:" + task.getAssignee());
                System.out.println("????????????ID???" + task.getProcessInstanceId());
                System.out.println("????????????ID:" + task.getExecutionId());
                System.out.println("????????????ID:" + task.getProcessDefinitionId());

            }
        }*/
        return request;
    }
    /**
     * ???????????????????????????
     * @param id
     */
    @RequestMapping("/reSubmit")
    public int reSubmit(String id) {
        if (id!=null){


            /*HashMap<String, Object> variables = new HashMap<>();
            variables.put("days", days);*///userKey????????????????????????????????????
            // taskService.claim(taskid,"ZJ2");//???????????????
            //taskService.setAssignee(taskid, null);//????????????????????????
            /*taskId="72507";*/
            processEngine.getTaskService()// ??????????????????????????????Service
                    .complete(id);
            System.out.println("?????????????????????ID???" + id);
        }
        return 1;
    }

    public int reSubmit1(String id) {
        if (id!=null){


            /*HashMap<String, Object> variables = new HashMap<>();
            variables.put("days", days);*///userKey????????????????????????????????????
            // taskService.claim(taskid,"ZJ2");//???????????????
            //taskService.setAssignee(taskid, null);//????????????????????????
            /*taskId="72507";*/
            processEngine.getTaskService()// ??????????????????????????????Service
                    .complete(id);
            System.out.println("?????????????????????ID???" + id);
        }
        return 1;
    }
    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @RequestMapping("/backProcess")
    public int withdraw (@RequestParam("id") String id, HttpSession session) throws Exception{
        int iden=1;
        log.info("---------------------------------"+id);
        //1:??????ProcessEngine??????
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2?????????TaskService??????
        TaskService taskService=processEngine.getTaskService();
        //2?????????HistoryService??????
        HistoryService historyService=processEngine.getHistoryService();
        //2?????????RuntimeService??????
        String name = (String) session.getAttribute("userSn");
        //??????????????????????????????
        List<Approver> approvers= approverService.audit(name);
        String approverPerson=approvers.get(0).toString();
        String approvedPersonStr=approverPerson.substring(approverPerson.indexOf("(")+1,approverPerson.indexOf(")"));


        RuntimeService  runtimeService=processEngine.getRuntimeService();
       /* Task task = taskService.createTaskQuery()
                .taskAssignee(approvedPersonStr).singleResult();*/
        Task task=taskService.createTaskQuery().processInstanceId(id).singleResult();//?????????
        System.out.println(task.getId());
        if(task==null) {
            /// throw new ServiceException("????????????????????????????????????????????????");
            System.out.println("????????????????????????????????????????????????");
            iden=2;
        }else {
            System.out.println("????????????");
        }

        //LoginUser loginUser = SessionContext.getLoginUser();

        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
               /* .taskAssignee(name)//?????????*/
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
            System.out.println("?????????????????????????????????????????????");
        }else {
            System.out.println("????????????");
        }
        System.out.println(myTaskId);

        RepositoryService repositoryService =processEngine.getRepositoryService();
        String processDefinitionId = myTask.getProcessDefinitionId();
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        //??????
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
        /*        String name="002";//?????????*/
        Authentication.setAuthenticatedUserId(name);
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "??????");
        Map<String,Object> currentVariables = new HashMap<String,Object>();
        currentVariables.put("applier", name);
        //????????????
        taskService.complete(task.getId(),currentVariables);
        //???????????????
        flowNode.setOutgoingFlows(oriSequenceFlows);

        return iden;
    }

    /**
     * ????????????????????????
     * @return
     */
    @RequestMapping("/getPositionParamList")
    public List<VariableEntity> getPositionParamList(String startTime,String endTime,String approvedPerson,HttpSession session){
        List<VariableEntity> variableEntityList=new ArrayList<>();
       try{
           String sn=session.getAttribute("userSn").toString();
           VariableEntity variableEntity4=new VariableEntity();
           variableEntity4.setProcDefId("positionApply");
           //?????????????????????
           if(startTime !=null && startTime != ""){
               variableEntity4.setStartTime(startTime);
           }
           if(endTime!=null && endTime != ""){
               variableEntity4.setEndTime(endTime);
           }
           if (approvedPerson!= null &&approvedPerson!=""){
               variableEntity4.setApprovedPerson(approvedPerson);
           }

           //???????????????????????????ID
           List<VariableEntity> variableEntities=variableService.getProcessParamByName(variableEntity4);


           //??????????????????????????????
           for(VariableEntity variableEntity1:variableEntities){

               Map<String, Object> variables = processEngine.getRuntimeService().getVariables(variableEntity1.getProcInstId());
               if (variables.get("applyPerson")==sn||variables.get("applyPerson").equals(sn)){
                   for (Map.Entry<String, Object> entry : variables.entrySet()) {

                       variableEntity1.setApplyPerson(variables.get("applyPerson").toString());
                       variableEntity1.setApplyReason(variables.get("applyReason").toString());
                       variableEntity1.setTaskType(variables.get("taskType").toString());
                       variableEntity1.setApprovedPerson(variables.get("approvedPerson").toString());
                       variableEntity1.setPosition(variables.get("position").toString());
                       //??????????????????????????????
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
                   //??????list??????
                   variableEntityList.add(variableEntity1);
               }

           }
       }catch(Exception e){
            e.printStackTrace();
       }
        return variableEntityList;
    }

    /**
     * ??????????????????
     * @return
     */
    @RequestMapping("/updatePosition")
    public int updatePosition(String position, String applyReason,String orgName,String orgId,String role, String id,HttpSession session){
        int iden=0;

        try{
            //?????????????????????????????????????????????
            if(position != null && applyReason != null && applyReason != "" && position != "" ){

                String userId=session.getAttribute("UserId").toString();
                //?????????????????????id
                String positionId=positionService.getPositionByName(position);
                //?????????????????????????????????
                PositionEntity positionEntity=new PositionEntity();
                positionEntity.setUserId(userId);
                positionEntity.setPositionId(positionId);

                //??????????????????
                PositionEntity positionEntity1= positionService.getInfo(positionEntity);

                //??????????????????????????????
              /*  if (positionEntity1==null){*/
                    //??????????????????
                    VariableEntity variableEntity=new VariableEntity();
                    variableEntity.setProcInstId(id);
                    variableEntity.setName("position");
                    variableEntity.setText(position);
                    variableService.updateTaskParam(variableEntity);
                    //??????????????????
                    VariableEntity variableEntity1=new VariableEntity();
                    variableEntity1.setProcInstId(id);
                    variableEntity1.setName("applyReason");
                    variableEntity1.setText(applyReason);
                    variableService.updateTaskParam(variableEntity1);
                    //????????????????????????
                    VariableEntity variableEntity2=new VariableEntity();
                    variableEntity2.setProcInstId(id);
                    variableEntity2.setName("orgName");
                    variableEntity2.setText(orgName);
                    variableService.updateTaskParam(variableEntity2);
                    //????????????????????????id
                    VariableEntity variableEntity3=new VariableEntity();
                    variableEntity3.setProcInstId(id);
                    variableEntity3.setName("orgId");
                    variableEntity3.setText(orgId);
                    variableService.updateTaskParam(variableEntity3);
                    //????????????
                    VariableEntity variableEntity4=new VariableEntity();
                    variableEntity4.setProcInstId(id);
                    variableEntity4.setName("role");
                    variableEntity4.setText(role);
                    variableService.updateTaskParam(variableEntity4);
                    iden=2;
                    //??????????????????????????????
                /*}else{
                    iden=1;
                }*/
            }else{
                log.info("==========================?????????????????????");
            }
        }catch (Exception e){
            log.info("==========================?????????????????????"+e);
        }
        return iden;
    }

}



