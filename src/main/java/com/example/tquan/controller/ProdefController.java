package com.example.tquan.controller;

import com.example.tquan.entity.ProcdefEntity;
import com.example.tquan.service.TasksService;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.bpmn.model.BpmnModel;

import org.activiti.engine.history.HistoricActivityInstance;

import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * Created by chenjin on 2021/7/8 17:50
 */
@RestController
public class ProdefController {
    @Autowired
    private TasksService tasksService;

    private Log log = LogFactory.getLog(getClass());
    /**
     * 查询所有流程
     * @return
     */
    @PostMapping("/selectAllProcdef")
    public List<ProcdefEntity> selectAllProcdef(String name,String keyName){
        //设置查询参数
        ProcdefEntity procdefEntity=new ProcdefEntity();
        procdefEntity.setName(name);
        procdefEntity.setKeyName(keyName);
        //查询所有流程
        List<ProcdefEntity> procdefEntities=tasksService.selectAllProcef(procdefEntity);
        return  procdefEntities;
    }

    /**
     * 激活挂起流程
     * @param id
     * @param type
     * @return
     */
    @PostMapping("/updateProcdefStatus")
    public int updateProcdefStatus(String id,String type){
        int iden=0;
        if (id!=null && type !=null && id!="" && type!=""){
            ProcdefEntity procdefEntity=new ProcdefEntity();
            procdefEntity.setProcdefId(id);
            //判断是激活流程还是挂起流程
            if(type=="激活" || type.equals("激活")){
                procdefEntity.setStatus("1");

            }else{
                procdefEntity.setStatus("2");
            }
            iden=tasksService.updateProcdefStatus(procdefEntity);
        }
        return iden;
    }
    /**
     * 查看流程图
     * @param request
     * @param response
     * @param taskId
     * @throws Exception
     */
    @GetMapping("/getProcefPicture")
    public void getProcefPicture(HttpServletRequest request, HttpServletResponse response, String taskId){
        try{
            InputStream imageStream = lookCurrentProcessImage(taskId);
            byte[] b = new byte[1024];
            int len;
            while ((len = imageStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        }catch (Exception e){
            log.info("==========================查询失败！"+e);
            e.printStackTrace();
        }
    }

    /**
     * 获取流程图
     * @param taskId
     * @return
     */
    public InputStream lookCurrentProcessImage(String taskId) {
        InputStream imageStream1=null;

        try{
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            TaskService taskService=processEngine.getTaskService();
            RepositoryService repositoryService=processEngine.getRepositoryService();
            HistoryService historyService=processEngine.getHistoryService();
            ProcessDiagramGenerator processDiagramGenerator=processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
            // 获取流程
            List<Task> task=taskService.createTaskQuery().processInstanceId(taskId).list();


            // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
            List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(taskId).orderByHistoricActivityInstanceId().desc().list();

            // 已执行的节点ID集合
            List<String> executedActivityIdList = new ArrayList<String>();
            @SuppressWarnings("unused")
            int index = 1;
            for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                executedActivityIdList.add(activityInstance.getActivityId());
                log.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
                index++;
            }
            List<String> highLightedFlows=new ArrayList<>();

            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.get(0).getProcessDefinitionId());

            highLightedFlows = getHighLightedFlows(bpmnModel,historicActivityInstanceList);

            BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(bpmnModel);
            bpmnAutoLayout.setTaskHeight(90);
            bpmnAutoLayout.setTaskWidth(200);

            bpmnAutoLayout.execute();

            imageStream1= processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,highLightedFlows, processEngine.getProcessEngineConfiguration().getActivityFontName(), processEngine.getProcessEngineConfiguration().getLabelFontName(),"宋体",null,1.0);

        }catch (Exception e){
            log.info("==========================查询失败！");
            e.printStackTrace();
        }
        return imageStream1;
    }
    /**
     * 获取已经流转的线
     *
     * @param bpmnModel
     * @param historicActivityInstances
     * @return
     */
    private static List<String> getHighLightedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        // 高亮流程已发生流转的线id集合
        List<String> highLightedFlowIds = new ArrayList<>();
        // 全部活动节点
        List<FlowNode> historicActivityNodes = new ArrayList<>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstances = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            historicActivityNodes.add(flowNode);
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstances.add(historicActivityInstance);
            }
        }

        FlowNode currentFlowNode = null;
        FlowNode targetFlowNode = null;
        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstances) {
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlows = currentFlowNode.getOutgoingFlows();

            /**
             * 遍历outgoingFlows并找到已已流转的 满足如下条件认为已已流转： 1.当前节点是并行网关或兼容网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最早的流转节点视为有效流转
             */
            if ("parallelGateway".equals(currentActivityInstance.getActivityType()) || "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配流程目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicActivityNodes.contains(targetFlowNode)) {
                        highLightedFlowIds.add(targetFlowNode.getId());
                    }
                }
            } else {
                List<Map<String, Object>> tempMapList = new ArrayList<>();
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
                        if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("highLightedFlowId", sequenceFlow.getId());
                            map.put("highLightedFlowStartTime", historicActivityInstance.getStartTime().getTime());
                            tempMapList.add(map);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(tempMapList)) {
                    // 遍历匹配的集合，取得开始时间最早的一个
                    long earliestStamp = 0L;
                    String highLightedFlowId = null;
                    for (Map<String, Object> map : tempMapList) {
                        long highLightedFlowStartTime = Long.valueOf(map.get("highLightedFlowStartTime").toString());
                        if (earliestStamp == 0 || earliestStamp >= highLightedFlowStartTime) {
                            highLightedFlowId = map.get("highLightedFlowId").toString();
                            earliestStamp = highLightedFlowStartTime;
                        }
                    }

                    highLightedFlowIds.add(highLightedFlowId);
                }

            }

        }
        return highLightedFlowIds;
    }

}
