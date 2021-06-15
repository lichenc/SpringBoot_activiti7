package com.example.tquan.controller;

import com.example.tquan.entity.PositionEntity;
import com.example.tquan.entity.VariableEntity;
import com.example.tquan.service.PositionService;
import com.example.tquan.service.UserService;
import com.example.tquan.service.VariableService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.VariableScopeImpl;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private UserService userService;

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    private Log log = LogFactory.getLog(getClass());

    /**
     * 获取岗位列表
     * @return
     */
    @RequestMapping("/getPositionList")
    public PositionEntity getpositionList(HttpSession session){
        PositionEntity positionEntity=new PositionEntity();
        //获取所有岗位
        List<PositionEntity> positionEntityList= positionService.findAll();

        positionEntity.setPositionEntityList(positionEntityList);
        positionEntity.setUserSn(String.valueOf(session.getAttribute("userSn")));

        return positionEntity;
    }

    /**
     * 添加岗位流程
     * @return
     */
    @RequestMapping("/addPositionProcess")
    public int addPositionProcess(String position, String applyReason, HttpSession session, HttpServletRequest request){
        int iden=0;
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
           map.put("taskType","岗位申请");
           map.put("approvedPerson","001");  //审批人员，未确定，先使用默认001
           ExecutionEntity pi1 = (ExecutionEntity)runtimeService.startProcessInstanceByKey("positionApply",map);
           log.info("=========================="+sn+"申请了"+position+"岗位申请");
           iden=2;

           //用户已拥有申请的岗位
       }else{
            iden=1;
       }

       /* //1:得到ProcessEngine对象
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
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
     * 获取岗位流程参数
     * @return
     */
    @RequestMapping("/getPositionParamList")
    public List<VariableEntity> getPositionParamList(String startTime,String endTime){
        VariableEntity variableEntity4=new VariableEntity();
        variableEntity4.setProcDefId("positionApply");
        //按日期区间查找
        if(startTime !=null && startTime != ""){
            variableEntity4.setStartTime(startTime);
        }
        if(endTime!=null && endTime != ""){
            variableEntity4.setEndTime(endTime);
        }

        //获取岗位流程的实例ID
        List<VariableEntity> variableEntities=variableService.getProcessParamByName(variableEntity4);
        List<VariableEntity> variableEntityList=new ArrayList<>();
        //为查询text设置参数
        VariableEntity variableEntity=new VariableEntity();
        //接收查询text的参数
       String text=null;

        //循环获取申请岗位参数
        for(VariableEntity variableEntity1:variableEntities){
            VariableEntity variableEntity3=new VariableEntity();
            variableEntity.setProcInstId(variableEntity1.getProcInstId());
             for(int i=0;i<5;i++){
                 //设置审批人参数
                 if(i==0){
                     variableEntity.setName("approvedPerson");
                 }
                 //设置任务类型参数
                 if (i==1){
                     variableEntity.setName("taskType");
                 }
                 //设置申请原因参数
                 if (i==2){
                     variableEntity.setName("applyReason");
                 }
                 //设置岗位参数
                 if (i==3){
                     variableEntity.setName("position");
                 }
                 //设置申请人参数
                 if (i==4){
                     variableEntity.setName("applyPerson");
                 }
                  //获取参数对应的值
                 text=variableService.getTextByName(variableEntity);
                 //添加审批人对应的值
                 if (i==0){
                     variableEntity3.setApprovedPerson(text);
                 }
                 //添加任务类型对应的值
                 if(i==1){
                    variableEntity3.setTaskType(text);
                 }
                 //添加申请原因对应的值
                 if(i==2){
                    variableEntity3.setApplyReason(text);
                 }
                 //添加岗位对应的值
                 if(i==3){
                    variableEntity3.setPosition(text);
                 }
                 //添加申请人对应的值
                 if(i==4){
                    variableEntity3.setApplyPerson(text);
                 }

              }
            variableEntity3.setProcInstId(variableEntity1.getProcInstId());
            variableEntity3.setApplyCreateTime(variableEntity1.getApplyCreateTime());
            //最终list集合
            variableEntityList.add(variableEntity3);
        }

        return variableEntityList;
    }
}
