package com.example.tquan.controller;

import com.example.tquan.entity.ProcdefEntity;
import com.example.tquan.service.TasksService;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.engine.*;
import org.activiti.bpmn.model.BpmnModel;

import org.activiti.engine.history.HistoricActivityInstance;

import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    public void getProcefPicture(HttpServletRequest request, HttpServletResponse response, String taskId) throws Exception{
      // try{
          InputStream imageStream = lookCurrentProcessImage(taskId);
           byte[] b = new byte[1024];
           int len;
           while ((len = imageStream.read(b, 0, 1024)) != -1) {
               response.getOutputStream().write(b, 0, len);
           }
       //}catch (Exception e){
          // log.info("==========================查询失败！"+e);
       //}
    }

    public InputStream lookCurrentProcessImage(String taskId) {
        InputStream imageStream1=null;

       try{
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

           TaskService taskService=processEngine.getTaskService();
           RepositoryService repositoryService=processEngine.getRepositoryService();
           HistoryService historyService=processEngine.getHistoryService();
          ProcessDiagramGenerator processDiagramGenerator=processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
           // 获取流程
           Task task=taskService.createTaskQuery().processInstanceId(taskId).singleResult();


           // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
           List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                   .processInstanceId(taskId).orderByHistoricActivityInstanceId().asc().list();

           // 已执行的节点ID集合
           List<String> executedActivityIdList = new ArrayList<String>();
           @SuppressWarnings("unused")
           int index = 1;
               for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                   executedActivityIdList.add(activityInstance.getActivityId());
                   log.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
                   index++;
               }

           BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
           BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(bpmnModel);
           bpmnAutoLayout.setTaskHeight(90);
           bpmnAutoLayout.setTaskWidth(200);

           bpmnAutoLayout.execute();

           imageStream1= processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,new ArrayList<>(), processEngine.getProcessEngineConfiguration().getActivityFontName(), processEngine.getProcessEngineConfiguration().getLabelFontName(),"宋体",null,1.0);


       }catch (Exception e){
             log.info("==========================查询失败！");
             e.printStackTrace();
       }
        return imageStream1;
    }


}
