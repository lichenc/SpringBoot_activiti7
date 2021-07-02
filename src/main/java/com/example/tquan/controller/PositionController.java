package com.example.tquan.controller;

import com.example.tquan.entity.Approver;
import com.example.tquan.entity.PositionEntity;
import com.example.tquan.entity.VariableEntity;
import com.example.tquan.service.ApproverService;
import com.example.tquan.service.PositionService;
import com.example.tquan.service.UserService;
import com.example.tquan.service.VariableService;
import com.ninghang.core.util.StringUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.plugin2.message.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Type;
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
    private UserService userService;
    @Autowired
    private ApproverService approverService;


    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    private Log log = LogFactory.getLog(getClass());

    /**
     * 获取岗位列表
     * @return
     */
    @RequestMapping("/getPositionList")
    public PositionEntity getpositionList(HttpSession session,String type,String id){
       String userSn= String.valueOf(session.getAttribute("userSn"));
        PositionEntity positionEntity=new PositionEntity();
        //获取所有岗位
        List<PositionEntity> positionEntityList= positionService.findAll();
        positionEntity.setPositionEntityList(positionEntityList);
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

        return positionEntity;
    }

    /**
     * 添加岗位流程
     * @return
     */
    @RequestMapping("/addPositionProcess")
    public int addPositionProcess(String position, String applyReason,String approvedPerson, HttpSession session, HttpServletRequest request){
        int iden=0;

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
            if (positionEntity1==null){
                RuntimeService runtimeService = processEngine.getRuntimeService();
                String sn = (String) session.getAttribute("userSn");
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("applyPerson",sn);
                map.put("position",position);
                map.put("applyReason",applyReason);
                map.put("taskType","岗位新增");
                map.put("approvedPerson",approvedPersonStr);
                ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("positionApply",map);
                log.info("=========================="+sn+"申请了"+position+"岗位申请！");
                iden=2;

                findTask(/*firstResult,maxResults,*/sn,request);

                //用户已拥有申请的岗位
            }else{
                iden=1;
            }
        }else{
            log.info("==========================申请岗位或申请原因为空，添加失败！");
        }



        //1:得到ProcessEngine对象
       /* ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
        //2：得到TaskService对象
        TaskService taskService=processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee("001")//指定个人任务查询
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                request.setAttribute("task",task);
                log.info("==========================任务ID:" + task.getId());
                log.info("==========================任务名称:" + task.getName());
                log.info("==========================任务的创建时间:" + task.getCreateTime());
                log.info("==========================任务的办理人:" + task.getAssignee());
                log.info("==========================流程实例ID：" + task.getProcessInstanceId());
                log.info("==========================执行对象ID:" + task.getExecutionId());
                log.info("==========================流程定义ID:" + task.getProcessDefinitionId());
                processEngine.getTaskService()// 与正在执行任务相关的Service
                        .complete(task.getId());
                log.info("==========================完成任务：任务ID：" + task.getId());
            }
        }*/
       return iden;
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
        List<VariableEntity> variableEntityList=new ArrayList<>();

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
                VariableEntity variableEntity=new VariableEntity();
                variableEntity.setName("repulseReason");
                String text=variableService.getTextByName(variableEntity);
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
        return variableEntityList;
    }

    /**
     * 修改岗位信息
     * @return
     */
    @RequestMapping("/updatePosition")
    public int updatePosition(String position, String applyReason,String id,HttpSession session){
        int iden=0;

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
        return iden;
    }
}



