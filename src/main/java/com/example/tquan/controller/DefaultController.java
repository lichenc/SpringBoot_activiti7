package com.example.tquan.controller;

import com.beust.jcommander.internal.Lists;
import com.example.tquan.entity.*;
import com.example.tquan.service.ApproverService;
import com.example.tquan.service.DefaultService;
import com.example.tquan.service.ImAppService;
import com.example.tquan.service.TaskTypeService;
import com.example.tquan.util.IamInterface;
import com.ninghang.core.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.task.Comment;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class DefaultController {
    @Autowired
    private IamAccountEntity account;
    @Autowired
    private IamOauthEntity iam;
    @Autowired
    private DefaultService defaultService;
    @Autowired
    private ImAppService imAppService;
    @Autowired
    private TaskTypeService taskTypeService;
    @Autowired
    private ApproverService approverService ;
    private Log log = LogFactory.getLog(getClass());
    IamInterface iamInterface=new IamInterface();

    /**
     * 根据应用查询禁用的属性和默认值
     *
     * @param
     * @return
     */
    @RequestMapping("/act")
    @ResponseBody
    public List act(@Param("app") String app, HttpServletRequest request, HttpSession session) {
        String usId = (String) session.getAttribute("UserId");
        List<ActEntity> accountField=defaultService.act(app);
        List<DefaultsEntity> list=new ArrayList<>();
        for (int i=0;i<accountField.size();i++) {
            DefaultsEntity defaultsEntity = new DefaultsEntity();
            String defaultValues="";
                if (StringUtils.isEmpty(accountField.get(i).getDefaultValue())) {
                    defaultValues=accountField.get(i).getDefaultValue();
                } else {
                    String defVal = accountField.get(i).getDefaultValue();
                    String firstDef = defVal.substring(0, 1);
                    if (firstDef.equals("$")) {
                        String allField = defVal.substring(defVal.lastIndexOf(".") + 1, defVal.lastIndexOf("}"));
                        List<DefaultEntity> allDe = defaultService.allDefaults(usId, allField);
                        //用户和账号关联字段默认值
                        defaultValues=allDe.get(0).getAllField();

                    } else if (firstDef.equals("<")) {
                        String sqlField = defVal.substring(defVal.lastIndexOf("sql=") + 5, defVal.lastIndexOf("/>")-1);
                        String sqlCondition = sqlField.substring(sqlField.lastIndexOf("$"),sqlField.lastIndexOf("}")+1);
                        /*String sqlCondition = sqlField.substring(sqlField.lastIndexOf("{")+1,sqlField.lastIndexOf("."));
                        if("user".equals(sqlCondition)){
                            String userField = sqlField.substring(sqlField.lastIndexOf(".")+1,sqlField.lastIndexOf("?"));
                            System.out.println("===用户表字段:" +userField);
                            if("id".equals(userField)){
                            }
                        }*/
                        //组装sql
                        String sql = sqlField.replace(sqlCondition, "'"+usId+"'");
                        defaultValues=defaultService.fieldDefaultVal(sql);

                    } else {
                        //固定默认值
                        defaultValues=accountField.get(i).getDefaultValue();
                 }

                }
            defaultsEntity.setNames(accountField.get(i).getName());
            defaultsEntity.setRemarks(accountField.get(i).getRemark());
            defaultsEntity.setDefaultValues(defaultValues);
            defaultsEntity.setInputTypes(accountField.get(i).getInputType());
            defaultsEntity.setIsRequrieds(accountField.get(i).getIsRequried());
            defaultsEntity.setIsInserts(accountField.get(i).getIsInsert());
            defaultsEntity.setIsEdits(accountField.get(i).getIsEdit());
            if(StringUtils.isEmpty(accountField.get(i).getInputType())){
                defaultsEntity.setCompants(accountField.get(i).getCompant());
            }else {
                if(accountField.get(i).getInputType().equals("select")==true){

                    if(StringUtils.isEmpty(accountField.get(i).getCompant())){
                        defaultsEntity.setCompants(accountField.get(i).getCompant());
                    }else {
                        String compantStr = accountField.get(i).getCompant();
                        List<Map<String, Object>> compant= new ArrayList<>();
                        List<String> result = Arrays.asList(compantStr.split(","));
                        Map<String, Object> map = new HashMap<String, Object>();
                        for(int c=0;c<result.size();c++){
                            String [] com= result.get(c).split("[=]");
                             String keys= com[0];
                            String values= com[1];
                            map.put(keys,values);
                        }
                        compant.add(map);
                        defaultsEntity.setListCompants(compant);
                    }
                }else {
                    defaultsEntity.setCompants(accountField.get(i).getCompant());
                }

            }
            list.add(defaultsEntity);


            }
        System.out.println(list.toString());
        return list;
    }


    /**
     * 根据应用查询禁用的属性和默认值
     *
     * @param
     * @return
     */
    @RequestMapping("/actField")
    @ResponseBody
    public List actField(@Param("app") String app,@Param("act") String act, String uuid, HttpServletRequest request, HttpSession session) throws Exception {
        String usId = (String) session.getAttribute("UserId");
        List<ActEntity> accountField=defaultService.act(app);
        List<ImApp> listApp=imAppService.findApply(app);
        List<DefaultsEntity> list=new ArrayList<>();
        String ifo=iamInterface.oauth(uuid,iam.getKey(),iam.getPassword(),iam.getAddr(),iam.getUsername(),iam.getType(),iam.getCharset());
        if (StringUtils.isEmpty(ifo.toString())) {

        } else {
            List<NameValuePair> params = Lists.newArrayList();
            params.add(new BasicNameValuePair("appId", listApp.get(0).getId()));
            params.add(new BasicNameValuePair("loginName", act));
            params.add(new BasicNameValuePair("uim-login-user-id", ifo));
            //转换为键值对
            String strAccount = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            StringBuilder accountSelect=iamInterface.accountSelect (strAccount,account.getAddr(),account.getType());
            JSONObject resultJson = JSONObject.fromObject(accountSelect.toString());
            JSONArray rear=JSONArray.fromObject(resultJson.get("dataList").toString());
            JSONObject res = JSONObject.fromObject(rear.get(0).toString());
            List<NameValuePair> paramsId = Lists.newArrayList();
            paramsId.add(new BasicNameValuePair("id", res.get("id").toString()));
            paramsId.add(new BasicNameValuePair("uim-login-user-id", ifo));
            //转换为键值对
            String strAccountId = EntityUtils.toString(new UrlEncodedFormEntity(paramsId, Consts.UTF_8));
            StringBuilder accountSelectAll=iamInterface.accountSelectId(strAccountId,account.getAddr(),account.getType());
            JSONObject resultJsonAll = JSONObject.fromObject(accountSelectAll.toString());
            JSONObject resultJsonAllEx = JSONObject.fromObject(resultJsonAll.get("extraAttrs").toString());
            /*查询的IAM的扩展字段的key和value值;*/
            List key=new ArrayList();
            key= Arrays.asList(resultJsonAllEx.keySet().toArray());
            List val=new ArrayList();
            val= Arrays.asList(resultJsonAllEx.values().toArray());
            for (int i=0;i<accountField.size();i++) {
                    DefaultsEntity defaultsEntity = new DefaultsEntity();
                    String defaultValues="";
                        if("ACCT_TYPE".equals(accountField.get(i).getName()) ){
                            defaultValues=resultJsonAll.get("acctType").toString();
                        }else if ("ACCOUNT_ORG".equals(accountField.get(i).getName()) ){
                            defaultValues="";
                        }else {
                            for (int k=0;k<key.size();k++) {
                               if(key.get(k).equals(accountField.get(i).getName())){
                                    defaultValues=val.get(k).toString();
                                }
                            }

                        }
                    defaultsEntity.setNames(accountField.get(i).getName());
                    defaultsEntity.setRemarks(accountField.get(i).getRemark());
                    defaultsEntity.setDefaultValues(defaultValues);
                    defaultsEntity.setInputTypes(accountField.get(i).getInputType());
                    defaultsEntity.setIsRequrieds(accountField.get(i).getIsRequried());
                    defaultsEntity.setIsInserts(accountField.get(i).getIsInsert());
                    defaultsEntity.setIsEdits(accountField.get(i).getIsEdit());
                    if(StringUtils.isEmpty(accountField.get(i).getInputType())){
                        defaultsEntity.setCompants(accountField.get(i).getCompant());
                    }else {
                        if(accountField.get(i).getInputType().equals("select")==true){

                            if(StringUtils.isEmpty(accountField.get(i).getCompant())){
                                defaultsEntity.setCompants(accountField.get(i).getCompant());
                            }else {
                                String compantStr = accountField.get(i).getCompant();
                                List<Map<String, Object>> compant= new ArrayList<>();
                                List<String> result = Arrays.asList(compantStr.split(","));
                                Map<String, Object> map = new HashMap<String, Object>();

                                for(int c=0;c<result.size();c++){
                                    String [] com= result.get(c).split("[=]");
                                    String keys= com[0];
                                    String values= com[1];
                                    map.put(keys,values);
                                }
                                compant.add(map);
                                defaultsEntity.setListCompants(compant);
                            }
                        }else {
                            defaultsEntity.setCompants(accountField.get(i).getCompant());
                        }
                    }
                    list.add(defaultsEntity);
            }
        }
        System.out.println(list.toString());
        return list;
        }


    /**
     * 根据应用查询禁用的属性和默认值
     *
     * @param
     * @return
     */
    @RequestMapping("/againSelect")
    @ResponseBody
    public List again(@Param("id") String id, HttpServletRequest request, HttpSession session) {
        //1:得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Map<String, Object> variables = processEngine.getRuntimeService().getVariables(id);
        String usId = (String) session.getAttribute("UserId");
        String login_name = (String) session.getAttribute("userSn");
        List<ActivitiEntity> listTask=new ArrayList<>();
        //查询审批人
        List<Approver> audits=approverService.audit(login_name);
        List<ActEntity> accountField=defaultService.act(variables.get("app").toString());
        List<DefaultsEntity> list=new ArrayList<>();

        for (int i=0;i<accountField.size();i++) {
            DefaultsEntity defaultsEntity = new DefaultsEntity();
            String defaultValues="";
            String base="";
            if (StringUtils.isEmpty(accountField.get(i).getDefaultValue())) {
                defaultValues=accountField.get(i).getDefaultValue();
            } else {
                String defVal = accountField.get(i).getDefaultValue();
                String firstDef = defVal.substring(0, 1);
                if (firstDef.equals("$")) {
                    String allField = defVal.substring(defVal.lastIndexOf(".") + 1, defVal.lastIndexOf("}"));
                    List<DefaultEntity> allDe = defaultService.allDefaults(usId, allField);
                    log.info("关联字段:" + allField + "关联字段值:" + allDe.get(0).getAllField());
                    //用户和账号关联字段默认值
                    defaultValues=allDe.get(0).getAllField();

                } else if (firstDef.equals("<")) {
                    //sql查询默认值
                    String sqlField = defVal.substring(defVal.lastIndexOf("sql=") + 5, defVal.lastIndexOf("/>")-1);
                    String sqlCondition = sqlField.substring(sqlField.lastIndexOf("$"),sqlField.lastIndexOf("}")+1);
                    //组装sql
                    String sql = sqlField.replace(sqlCondition, "'"+usId+"'");
                    defaultValues=defaultService.fieldDefaultVal(sql);
                } else {
                    //固定默认值
                    defaultValues=accountField.get(i).getDefaultValue();
                }

            }
            if (!variables.get("taskType").toString().equals("帐号启用")) {
                JSONObject textList = JSONObject.fromObject(variables.get("textList").toString());
                JSONArray textListKey = JSONArray.fromObject(textList.keySet());
                JSONArray textListValue = JSONArray.fromObject(textList.values());
                List<DefaultsEntity> textList2 = new ArrayList<>();
                for (int text = 0; text < textList.size(); text++) {
                    if (accountField.get(i).getName().equals(textListKey.get(text).toString())) {
                        base = textListValue.get(text).toString();
                    }
                }
                JSONObject passwordList = JSONObject.fromObject(variables.get("passwordList").toString());
                JSONArray passwordListKey = JSONArray.fromObject(passwordList.keySet());
                JSONArray passwordListValue = JSONArray.fromObject(passwordList.values());
                List<DefaultsEntity> passwordList2 = new ArrayList<>();
                for (int text = 0; text < passwordList.size(); text++) {
                    if (accountField.get(i).getName().equals(passwordListKey.get(text).toString())) {
                        base = passwordListValue.get(text).toString();
                    }
                }
                JSONObject selectList = JSONObject.fromObject(variables.get("selectList").toString());
                JSONArray selectListKey = JSONArray.fromObject(selectList.keySet());
                JSONArray selectListValue = JSONArray.fromObject(selectList.values());
                for (int text = 0; text < selectList.size(); text++) {
                    if (accountField.get(i).getName().equals(selectListKey.get(text).toString())) {
                        base = selectListValue.get(text).toString();
                    }
                }
                JSONObject dateList = JSONObject.fromObject(variables.get("dateList").toString());
                JSONArray dateListKey = JSONArray.fromObject(dateList.keySet());
                JSONArray dateListValue = JSONArray.fromObject(dateList.values());
                for (int text = 0; text < dateList.size(); text++) {
                    if (accountField.get(i).getName().equals(dateListKey.get(text).toString())) {
                        base = dateListValue.get(text).toString();
                    }
                }
            }
            defaultsEntity.setId(id);
            defaultsEntity.setApprovedPerson(audits.get(0).getAudit());
            defaultsEntity.setApplyReason(variables.get("applyReason").toString());
            defaultsEntity.setRole(variables.get("role").toString());
            defaultsEntity.setTaskType(variables.get("taskType").toString());
            defaultsEntity.setAccount(variables.get("account").toString());
            defaultsEntity.setApplyPerson(variables.get("applyPersonName").toString());
            defaultsEntity.setApp(variables.get("app").toString());
            if (!variables.get("taskType").toString().equals("帐号启用")) {
                defaultsEntity.setAccountOrg(variables.get("accountOrg").toString());
                defaultsEntity.setActType(variables.get("actType").toString());
                defaultsEntity.setBasic(base);
                defaultsEntity.setNames(accountField.get(i).getName());
                defaultsEntity.setRemarks(accountField.get(i).getRemark());
                defaultsEntity.setDefaultValues(defaultValues);
                defaultsEntity.setInputTypes(accountField.get(i).getInputType());
                defaultsEntity.setIsRequrieds(accountField.get(i).getIsRequried());
                defaultsEntity.setIsInserts(accountField.get(i).getIsInsert());
                defaultsEntity.setIsEdits(accountField.get(i).getIsEdit());
                if (StringUtils.isEmpty(accountField.get(i).getInputType())) {
                    defaultsEntity.setCompants(accountField.get(i).getCompant());
                } else {
                    if (accountField.get(i).getInputType().equals("select") == true) {

                        if (StringUtils.isEmpty(accountField.get(i).getCompant())) {
                            defaultsEntity.setCompants(accountField.get(i).getCompant());
                        } else {
                            String compantStr = accountField.get(i).getCompant();
                            List<Map<String, Object>> compant = new ArrayList<>();
                            List<String> result = Arrays.asList(compantStr.split(","));
                            Map<String, Object> map = new HashMap<String, Object>();

                            for (int c = 0; c < result.size(); c++) {
                                String[] com = result.get(c).split("[=]");
                                String keys = com[0];
                                String values = com[1];
                                map.put(keys, values);
                            }
                            compant.add(map);
                            defaultsEntity.setListCompants(compant);
                        }
                    } else {
                        defaultsEntity.setCompants(accountField.get(i).getCompant());
                    }

                }
            }
            String taskTypeAll="";
            //查询任务类型
            List<ActivitiEntity> listTasks=new ArrayList<>();
            List<TaskTypeEntity> taskType=taskTypeService.accountTask();
            for(int t=0;t<taskType.size();t++){
                ActivitiEntity task=new ActivitiEntity();
                task.setTaskTypes(taskType.get(t).getName());
                listTasks.add(task);
            }
            defaultsEntity.setAudit(audits.get(0).getAudit());
            defaultsEntity.setTaskTypeAll(listTasks);
            //批注
            TaskService taskService=processEngine.getTaskService();
            Task li = taskService.createTaskQuery()//创建任务查询对象
                    .processInstanceId(id)
                    .singleResult();
            HistoryService historyService = processEngine.getHistoryService();
            List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(li.getProcessInstanceId()).list();
            ActivitiEntity task=new ActivitiEntity();
            for (HistoricActivityInstance hai : hais) {
                String historytaskId = hai.getTaskId();
                List<Comment> comments = taskService.getTaskComments(historytaskId);
                // 4）如果当前任务有批注信息，添加到集合中
                if(comments!=null && comments.size()>0){
                    defaultsEntity.setComment(comments);
                }else {
                    defaultsEntity.setComment(comments);
                }
            }
            //全部账号
            if ("帐号新增".equals(variables.get("taskType").toString())){
                //系统资源
                List<ImApp> appList = imAppService.findAll();
                defaultsEntity.setImApp(appList);
            } else if("帐号修改".equals(variables.get("taskType").toString())){
                String userId = (String) session.getAttribute("UserId");
                List<ImApp> appList =imAppService.actAppUp(userId);
                defaultsEntity.setImApp(appList);
                List<ActEntity> actDisable = defaultService.actDisable(variables.get("app").toString(), usId);
                defaultsEntity.setActAll(actDisable);
            }else if ("帐号启用".equals(variables.get("taskType").toString())){
                String userId = (String) session.getAttribute("UserId");
                List<ImApp> appList =imAppService.actAppEn(userId);
                defaultsEntity.setImApp(appList);
                List<ActEntity> actDisable = defaultService.actEnable(variables.get("app").toString(), usId);
                defaultsEntity.setActAll(actDisable);
            }
            list.add(defaultsEntity);
        }
        System.out.println(list.toString());
        return list;
    }


    /**
     * 根据应用查询账号
     *
     * @param
     * @return
     */
    @RequestMapping("/disableAcct")
    @ResponseBody
    public List<ActEntity> disableAcct( @Param("app") String app,HttpSession session) {
        String usId = (String) session.getAttribute("UserId");
        List<ActEntity> actDisable = defaultService.actDisable(app, usId);
        return actDisable;
    }


    /**
     * 根据应用查询禁用的账号
     *
     * @param
     * @return
     */
    @RequestMapping("/enable")
    @ResponseBody
    public List<ActEntity> enable( @Param("app") String app,HttpSession session) {
        String usId = (String) session.getAttribute("UserId");
        List<ActEntity> actDisable = defaultService.actEnable(app, usId);
        return actDisable;
    }

/**
 * 判断当前帐号是否存在
 *
 * @param
 * @return
 */
    @RequestMapping("/verifyAct")
    @ResponseBody
    public int actNum( @Param("accountName") String accountName,@Param("applyName") String applyName,HttpSession session) {

        int act=0;
        List actNums = defaultService.actNum(accountName, applyName);
        System.out.println(actNums.size());
        if(actNums.size()>0){
            actNums.get(0).toString();
            if("1".equals(actNums.get(0).toString())){
                return act+1;
            }else if("2".equals(actNums.get(0).toString())){
                return act+2;
            }
        }
        return actNums.size();
    }

        /**
         * 根据用户移动的信息查询账号
         *
         * @param
         * @return
         */
        @RequestMapping("/actMove")
        @ResponseBody
        public List<DefaultEntity> actMove( HttpSession session) {
            String usId = (String) session.getAttribute("UserId");
            List<DefaultEntity> acts=defaultService.actMove(usId);
            return acts;
        }


}
