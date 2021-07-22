package com.example.tquan.controller;

import com.example.tquan.entity.*;
import com.example.tquan.service.ApproverService;
import com.example.tquan.service.ImAppService;
import com.example.tquan.service.TaskTypeService;
import com.ninghang.core.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangmiaomiao on 2021/5/20 12:53
 */
@Controller
public class ImAppController {

    @Autowired
    private ImAppService imAppService;
    @Autowired
    private TaskTypeService taskTypeService;
    @Autowired
    private ApproverService approverService ;
    private Log log = LogFactory.getLog(getClass());
    /**
     * appList查询全部业务系统
     *
     * @param
     * @return
     */
    @RequestMapping("/actApply")
    @ResponseBody
    public List<ImApp> findAll(@Param("aName") String aName, HttpServletRequest request, HttpSession session) {
        System.out.println(aName.toString());
        if(aName.equals("帐号新增")==true){
            List<ImApp> appList = imAppService.findAll();
            return appList;
        }else if(aName.equals("帐号修改")==true){
            String userId = (String) session.getAttribute("UserId");
            List<ImApp> appList =imAppService.actAppUp(userId);
            return appList;
        }else if(aName.equals("帐号启用")==true){
            String userId = (String) session.getAttribute("UserId");
            List<ImApp> appList =imAppService.actAppEn(userId);
            return appList;
        }

        return null;
    }
    /**
     * 根据任务类型查询有账号的应用
     *
     * @param
     * @return
     */
   /* @RequestMapping("/appName")*/
    public void actApp(HttpServletRequest request, HttpSession session) {

        //return "/apply";
    }
    /**
     * 查询表单基础信息
     *
     * @param
     * @return
     */
     @RequestMapping("/selectForm")
     @ResponseBody
    public List<ActivitiEntity> selectForm(HttpServletRequest request, HttpSession session) {
         String login_name = (String) session.getAttribute("userSn");
         List<ActivitiEntity> listTask=new ArrayList<>();

         //查询审批人
        List<Approver> audits=approverService.audit(login_name);

        //查询任务类型
        List<TaskTypeEntity> taskType=taskTypeService.accountTask();

        for(int t=0;t<taskType.size();t++){
            ActivitiEntity task=new ActivitiEntity();
            task.setTaskApprovedPerson(audits.get(0).getAudit());
            task.setTaskTypes(taskType.get(t).getName());
            listTask.add(task);
        }
        return listTask;
    }

}
