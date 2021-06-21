package com.example.tquan.controller;

import com.example.tquan.entity.*;
import com.example.tquan.service.*;
import com.ninghang.core.security.UIM;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjin on 2021/5/17 10:59
 */

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ApproverService approverService;

    private Log log = LogFactory.getLog(getClass());
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    /**
     * 修改账号
     */
    @PostMapping("/updateAccount")
    public int updateAccount(String id, String loginPwd) {
        int iden = 0;
         try {
             //初始化对象，设置修改需要的参数
             AccountEntity accountEntity = new AccountEntity();
             accountEntity.setId(id);
             accountEntity.setLoginPwd(UIM.encode(loginPwd));
             //执行修改
             iden = accountService.updateAccountById(accountEntity);
             if (iden != 0) {
                 log.info("==========================账号ID:" + id + "的账号密码修改成功");
             } else {
                 log.info("==========================账号ID:" + id + "的账号密码修改失败");
             }
         }catch (Exception e){
            e.printStackTrace();
         }
        return iden;
    }

    /**
     * 获取账号详情
     */
    @PostMapping("/getAccountDetail")
    public AccountEntity getAccountDetail(String id) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        //查询账号详情
        List<AccountEntity> accountEntityList = accountService.getByUserId(accountEntity);
        return accountEntityList.get(0);

    }

    /**
     * 获取账号列表
     * @param session
     * @param request
     */
    @PostMapping("/getAccountList")
    public UserEntity getAccountList(HttpSession session, HttpServletRequest request) throws Exception{
        //获取存在session里的用户id
        String userId=session.getAttribute("UserId").toString();
        log.info("==========================当前登录用户id"+userId);

        //设置查询帐号参数
        AccountEntity accountEntity=new AccountEntity();
        accountEntity.setUserId(userId);
        //获取账号列表
       List<AccountEntity> accountEntityList= accountService.getByUserId(accountEntity);

        //设置查询用户参数
        UserEntity userEntity=new UserEntity();
        userEntity.setId(userId);
        UserEntity userEntity1= userService.getUserByProperty(userEntity);

        //获取用户岗位集合
        List<PositionEntity> positionEntityList=positionService.getPositionByUserId(userId);

        //获取用户组集合
        List<GroupEntity> groupEntityList=groupService.getGroupByUserId(userId);

        if(accountEntityList.size()>0){
            //添加账号记录条数
            userEntity1.setAccountCount(accountEntityList.size());
            //添加账号集合
            userEntity1.setAccountEntities(accountEntityList);
        }

        if(positionEntityList.size()>0){
            //添加用户岗位集合
            userEntity1.setPositionEntityList(positionEntityList);
            userEntity1.setPositionCount(positionEntityList.size());
        }

        if (groupEntityList.size()>0){
            userEntity1.setGroupEntities(groupEntityList);
            userEntity1.setGroupCount(groupEntityList.size());
        }
        return userEntity1;


    }
    /**
     *查询待重试的流程
     * @return
     */
    @RequestMapping("/waitTryAgain")
    public TaskEntity waitTryAgainPage(TaskEntity taskEntity,HttpServletRequest request){
        TaskEntity taskEntity1=new TaskEntity();
       try {
           List<TaskEntity> taskEntities= taskService.getTaskListByProperty(taskEntity);
           List<TaskEntity> taskEntities1=new ArrayList<>();
           for(TaskEntity taskEntity2:taskEntities){
               TaskEntity taskEntity3=new TaskEntity();
               Map<String, Object> variables = processEngine.getRuntimeService().getVariables(taskEntity2.getTaskType());
               taskEntity3.setApplyPerson(variables.get("applyPerson").toString());
               taskEntity3.setApprovedPerson(variables.get("approvedPerson").toString());
               taskEntity3.setTaskType(variables.get("taskType").toString());
               taskEntity3.setApplyReason(variables.get("applyReason").toString());
               taskEntity3.setEvent(taskEntity2.getEvent());
               taskEntity3.setEventType(taskEntity2.getEventType());

               taskEntities1.add(taskEntity3);
           }
               taskEntity1.setTaskEntities(taskEntities1);

       }catch (Exception e){
           e.printStackTrace();
       }
        return taskEntity1;
    }

    /**
     * 任务重试
     * @param id
     * @return
     */
    @PostMapping("/taskRetry")
    public int taskRetry(String id,HttpSession session){
        int iden=0;
       try {
           TaskEntity taskEntity=new TaskEntity();
           //查询审批人
           List<Approver> audits=approverService.audit(session.getAttribute("userSn").toString());
           for(Approver approver:audits){
               String approvedPerson=approver.getAudit().substring(approver.getAudit().indexOf("(")+1,approver.getAudit().indexOf(")"));
               taskEntity.setApprovedPerson(approvedPerson);
           }
           taskEntity.setId(id);
            iden=taskService.updateTask(taskEntity);
           if (iden!=0){
               log.info("==========================taskID:" + id + "任务重试成功！");
           }else {
               log.info("==========================taskID:" + id + "任务重试失败！");
           }
       }catch (Exception e){
           e.printStackTrace();
       }
        return iden;
    }

}


