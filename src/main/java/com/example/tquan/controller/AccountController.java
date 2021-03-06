package com.example.tquan.controller;

import com.beust.jcommander.internal.Lists;
import com.example.tquan.entity.*;
import com.example.tquan.service.*;
import com.example.tquan.util.RsaUtil;
import com.ninghang.core.security.UIM;
import com.ninghang.core.util.StringUtils;
import net.sf.json.JSONObject;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.apache.commons.logging.Log;
import org.apache.http.*;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TasksService taskService;
    @Autowired
    private ApproverService approverService;
    @Autowired
    private IamOauthEntity iam;
    @Autowired
    private IamAccountEntity account;
    @Autowired
    private  IamUserEntity user;
    @Autowired
    private VariableService variableService;

    private Log log = LogFactory.getLog(getClass());
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    /**
     * ????????????
     */
    @PostMapping("/updateAccount")
    public int updateAccount(String id, String loginPwd,HttpServletRequest request) {
        int iden = 0;
         try {
             if(id != "" && id!=null && loginPwd !="" && loginPwd !=null){
                 //??????????????????????????????
                 int checkIden=checkPassword(loginPwd,request);
                 if (checkIden==0){
                     //?????????????????????????????????????????????
                     AccountEntity accountEntity = new AccountEntity();
                     accountEntity.setId(id);
                     accountEntity.setLoginPwd(UIM.encode(loginPwd));
                     //????????????
                     iden = accountService.updateAccountById(accountEntity);
                     if (iden != 0) {
                         log.info("==========================??????ID:" + id + "???????????????????????????");
                     } else {
                         log.info("==========================??????ID:" + id + "???????????????????????????");
                     }
                 }else{
                     iden=checkIden;
                 }

             }
         }catch (Exception e){
            e.printStackTrace();
         }
        return iden;
    }

    /**
     * ??????????????????
     */
    @PostMapping("/getAccountDetail")
    public AccountEntity getAccountDetail(String id) {
        List<AccountEntity> accountEntityList=null;
       try {
           AccountEntity accountEntity = new AccountEntity();
           accountEntity.setId(id);
           //??????????????????
            accountEntityList = accountService.getByUserId(accountEntity);
       }catch (Exception e){
           e.printStackTrace();
       }
        return accountEntityList.get(0);

    }

    /**
     * ??????????????????
     * @param session
     * @param request
     */
    @PostMapping("/getAccountList")
    public UserEntity getAccountList(HttpSession session, HttpServletRequest request,String uuid) throws Exception{
        UserEntity userEntity1=new UserEntity();

       try {
           //????????????session????????????id
           String userId=session.getAttribute("UserId").toString();
           log.info("==========================??????????????????id"+userId);

           //????????????????????????
           AccountEntity accountEntity=new AccountEntity();
           accountEntity.setUserId(userId);
           //??????????????????
           List<AccountEntity> accountEntityList= accountService.getByUserId(accountEntity);

           //????????????????????????
           UserEntity userEntity=new UserEntity();
           userEntity.setId(userId);
           userEntity1= userService.getUserByProperty(userEntity);
           //userEntity1.setCreateTime(df.format(userEntity1.getCreateTime(),toString()));

           //????????????????????????
           List<PositionEntity> positionEntityList=positionService.getPositionByUserId(userId);

           //?????????????????????
           List<GroupEntity> groupEntityList=groupService.getGroupByUserId(userId);

           if(accountEntityList.size()>0){
               //????????????????????????
               userEntity1.setAccountCount(accountEntityList.size());
               //??????????????????
               userEntity1.setAccountEntities(accountEntityList);
           }

           if(positionEntityList.size()>0){
               //????????????????????????
               userEntity1.setPositionEntityList(positionEntityList);
               userEntity1.setPositionCount(positionEntityList.size());
           }

           if (groupEntityList.size()>0){
               userEntity1.setGroupEntities(groupEntityList);
               userEntity1.setGroupCount(groupEntityList.size());
           }
       }catch (Exception e){
           e.printStackTrace();
       }
        return userEntity1;
    }

    /**
     * ??????????????????
     * @param uuid
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/getExtraAttrs")
    public StringBuilder getExtraAttrs(String uuid,HttpSession session){
        String userId=session.getAttribute("UserId").toString();

        StringBuilder stringBuilder=new StringBuilder();
        try {

            //??????????????????????????????????????????
            //????????????
            oauth(uuid);
            if(StringUtils.isEmpty(oauth(uuid))) {
                log.info("==========================uuid?????????");
            }else {
                List<NameValuePair> params = Lists.newArrayList();
                params.add(new BasicNameValuePair("id", userId));
                params.add(new BasicNameValuePair("uim-login-user-id", oauth(uuid)));
                //??????????????????
                String userStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                stringBuilder=post(userStr);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder;
    }

    /**
     * ?????????????????????????????????
     * @param approvedPerson
     * @return
     */
    @RequestMapping("/getAllTaskAdmin")
    public TaskEntity getAllTaskAdmin(String approvedPerson, TaskEntity taskEntity){
        //????????????????????????
        if (approvedPerson!=null &&approvedPerson!=""){
            taskEntity.setApprovedPerson(approvedPerson);
        }
        try {
            List<TaskEntity> taskEntities= taskService.getTaskListByProperty(taskEntity);
            List<TaskEntity> taskEntities1=new ArrayList<>();
            for(TaskEntity taskEntity2:taskEntities) {

                Map<String, Object> variables = processEngine.getRuntimeService().getVariables(taskEntity2.getTaskType());
                    taskEntity2.setApplyPerson(variables.get("applyPerson").toString());
                    taskEntity2.setApprovedPerson(variables.get("approvedPerson").toString());
                    taskEntity2.setTaskType(variables.get("taskType").toString());
                    taskEntity2.setApplyReason(variables.get("applyReason").toString());
                    VariableEntity variableEntity = new VariableEntity();
                    variableEntity.setName("repulseReason");
                    variableEntity.setProcInstId(taskEntity2.getId());
                    String text = variableService.getTextByName(variableEntity);
                    //????????????????????????
                    if (text != null) {
                        taskEntity2.setRepulseReason(text);
                    } else {
                        taskEntity2.setRepulseReason("");
                    }
                    taskEntities1.add(taskEntity2);
                }
            taskEntity.setTaskEntities(taskEntities1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return taskEntity;
    }

    /**
     * ????????????
     * @return
     */
    @PostMapping("/forgetPwd")
    public int forgetPwd(String userName,String newPassword,HttpServletRequest request){
        int iden = 0;
        try {
            //??????????????????????????????
            if (userName!=null && newPassword!=null && userName!="" && newPassword !=""){
                //??????????????????????????????
                int checkIden=checkPassword(newPassword,request);
                if (checkIden==0){
                    AccountEntity accountEntity=new AccountEntity();
                    accountEntity.setLoginName(userName);
                    AccountEntity accountEntity1= accountService.findUserByName(accountEntity);

                    accountEntity.setId(accountEntity1.getId());
                    accountEntity.setLoginPwd(UIM.encode(newPassword));
                    iden=accountService.updateAccountById(accountEntity);
                    if (iden != 0) {
                        log.info("==========================????????????" + userName + "???????????????????????????");
                    } else {
                        log.info("==========================????????????" + userName + "???????????????????????????");
                    }
                }else{
                    iden=checkIden;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return iden;
    }

    /**
     * ??????????????????
     * @param phone
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("/registerUser")
    public int register(String phone,String userName,String password,  HttpServletRequest request){
        int iden=0;
        try {
            if (phone!=null && phone!="" && userName !=null && userName !="" && password !=null && password!=""){

                UserEntity userEntity=new UserEntity();
                userEntity.setSn(userName);
                //?????????????????????????????????
                UserEntity userEntity1=userService.findUserByProperty(userEntity);
                //??????????????????????????????
                if (userEntity1==null){
                    //??????????????????????????????
                    int checkIden=checkPassword(password,request);
                    if (checkIden==0){
                        /**
                         * ????????????
                         */
                        userEntity.setTelephone(phone);
                        userEntity.setCreateTime(new Date());
                        int ids=(int) (Math.random() * 9000) + 1000;
                        userEntity.setId(String.valueOf(ids));
                        userEntity.setOptUser("admin");
                        userEntity.setStatus(1);
                        userEntity.setName(userName);
                        userEntity.setCompanySn("100001");

                        //??????????????????id
                        String userType= userService.getUserTypeIdByName("????????????");
                        userEntity.setUserTypeId(userType);
                        int userIden=userService.addUser(userEntity);
                        /**
                         * ????????????
                         */
                        AccountEntity accountEntity=new AccountEntity();
                        accountEntity.setId(String.valueOf((int) (Math.random() * 9000) + 1000));
                        accountEntity.setLoginName(userName);
                        accountEntity.setLoginPwd(UIM.encode(password));
                        accountEntity.setUserId(String.valueOf(ids));
                        accountEntity.setAcctType("1");
                        accountEntity.setStatus(1);
                        accountEntity.setCreateTime(new Date());
                        accountEntity.setAppId("100000065");
                        accountEntity.setOpenType(1);
                        accountEntity.setOptUser("admin");
                        accountEntity.setCompanySn("100001");
                        int accountIden=accountService.addAccount(accountEntity);
                        /**
                         * ??????????????????????????????????????????????????????????????????
                         */
                        //???????????????id
                        String orgId=groupService.getDedaultId("?????????");
                        GroupEntity groupEntity=new GroupEntity();
                        groupEntity.setId(orgId);
                        groupEntity.setSn(String.valueOf(ids));
                        //???????????????????????????
                        int groupIden=groupService.addUserOrg(groupEntity);
                        /**
                         * ??????????????????????????????
                         */
                        if (userIden!=0 && accountIden !=0 && groupIden!=0){
                            iden=2;
                            log.info("==========================????????????" + userName + "?????????????????????");
                        }else{
                            iden=3;
                            log.info("==========================????????????" + userName + "?????????????????????");
                        }
                    }else{
                        iden=checkIden;
                    }
                 //??????????????????
                }else{
                    iden=1;
                    log.info("==========================????????????" + userName + "????????????");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return iden;
    }

    /**
     * ?????????????????????????????????????????????????????????
     * @param password
     * @return
     */
    public int checkPassword(String password,  HttpServletRequest request){

        Pattern p;
        Matcher m;
        int iden=0;
        //??????????????????
        int pwdMaxLen=accountService.getAmPwdPolicy("PWD_MAX_LEN");
        //??????????????????
        int pwdMinLen=accountService.getAmPwdPolicy("PWD_MIN_LEN");
        //????????????????????????
        if(password.length()>pwdMaxLen || password.length()<pwdMinLen){
            return 11;
        }
        //??????????????????
        int pwdMinNumLen=accountService.getAmPwdPolicy("PWD_MIN_NUM_LEN");
        if (pwdMinNumLen!=0){
            p = Pattern.compile("[0-9]");
             m = p.matcher(password);
            //????????????
            if (m.find()) {

                //??????????????????
            }else{

                return 22;
            }
        }
        //????????????????????????
        int pwdMinSpacilCharLen =accountService.getAmPwdPolicy("PWD_MIN_SPACIL_CHAR_LEN");
        if (pwdMinSpacilCharLen!=0){
            String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~???@#???%??????&*????????????+|{}????????????????????????????????????]|\n|\r|\t";
             p = Pattern.compile(regEx);
             m = p.matcher(password);
             if (m.find()){

             }else{
                 return 33;
             }
        }
        //??????????????????
        int pwdMinCharLen=accountService.getAmPwdPolicy("PWD_MIN_CHAR_LEN");
        if (pwdMinCharLen!=0){
            p = Pattern.compile("[a-zA-Z]");
            if(p.matcher(password).find()){
            //????????????????????????
            }else {
                return 44;
            }
        }
        return iden;
    }

    /**
     * ????????????
     * @param id
     * @return
     */
    @PostMapping("/taskRetry")
    public int taskRetry(String id,HttpSession session){
        int iden=0;
       try {
           TaskEntity taskEntity=new TaskEntity();
           //???????????????
           List<Approver> audits=approverService.audit(session.getAttribute("userSn").toString());
           for(Approver approver:audits){
               String approvedPerson=approver.getAudit().substring(approver.getAudit().indexOf("(")+1,approver.getAudit().indexOf(")"));
               taskEntity.setApprovedPerson(approvedPerson);
           }
           taskEntity.setId(id);
            iden=taskService.updateTask(taskEntity);
           if (iden!=0){
               log.info("==========================taskID:" + id + "?????????????????????");
           }else {
               log.info("==========================taskID:" + id + "?????????????????????");
           }
       }catch (Exception e){
           e.printStackTrace();
       }
        return iden;
    }



    /**
     * ??????token
     * @return
     * @throws Exception
     */
    public String oauth(String uuid)
            throws Exception {
        PublicKey publicKey = RsaUtil.string2PublicKey(iam.getKey());

        //???????????????
        byte[] publicEncrypt = RsaUtil.publicEncrypt(iam.getPassword().getBytes(), publicKey);

        //?????????????????????Base64??????
        String rsa = RsaUtil.byte2Base64(publicEncrypt);

        log.info("==========================???????????????:" +rsa);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(iam.getAddr());
        StringBuilder result = new StringBuilder();

        //????????????
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("username", iam.getUsername()));
        params.add(new BasicNameValuePair("password", rsa));

        //??????????????????
        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));

        httppost.setEntity(new StringEntity(str, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", iam.getType()+iam.getCharset());

        HttpResponse response=httpclient.execute(httppost);
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

            if (br!=null){
                br.close();
            }

            log.info("==========================????????????uid??????:" +result);
            JSONObject resultJson = JSONObject.fromObject(result.toString());
            if(resultJson.get("success").toString().equals("true")){
                log.info("==========================msg:" +resultJson.get("msg"));
                uuid=resultJson.get("msg").toString();
                return uuid;
            }else {
                log.info("==========================msg:" +resultJson.get("msg"));
                return uuid;
            }
        }
        return uuid;
    }
    /**
     * ????????????????????????
     */
    public StringBuilder post(String str) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(user.getAddr());
        StringBuilder result = new StringBuilder();

        httppost.setEntity(new StringEntity(str, Charset.forName("UTF-8")));
        httppost.addHeader("Content-type", user.getType());
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
                if (br!=null){
                    br.close();
                }
            }
        } catch (ClientProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @PostMapping("/getAdminCount")
    public CountEntity getAdminCount(HttpSession session){
        CountEntity countEntity=new CountEntity();
        //???????????????
        TaskEntity taskEntity=new TaskEntity(null,null,null,"??????");
        int waitTryAgainCount=taskService.getTaskCountByProperty(taskEntity);
        //?????????????????????
        TaskEntity taskEntity1=new TaskEntity(null,null,null,"??????");
        int approvalCount=taskService.getTaskCountByProperty(taskEntity1);

        //???????????????
        int sumCount=waitTryAgainCount+approvalCount;

        //???????????????????????????
        TaskEntity taskEntity5=new TaskEntity(null,null,"today","??????");
        int todayWaitTryAgainCount=taskService.getTaskCountByProperty(taskEntity5);
        //???????????????????????????
        TaskEntity taskEntity6=new TaskEntity(null,null,"today","??????");
        int todayApprovalCount=taskService.getTaskCountByProperty(taskEntity6);

        //???????????????
        int todaySumCount=todayWaitTryAgainCount+todayApprovalCount;
        //?????????
        TaskEntity taskEntity2=new TaskEntity(null,null);
        int historyCount=taskService.getHistoryCount(taskEntity2);

        //?????????????????????
        TaskEntity taskEntity3=new TaskEntity("today",null);
        int todayHistoryCount=taskService.getHistoryCount(taskEntity3);

        //?????????????????????
        List<LineChartEntity> lineChartEntities=new ArrayList<>();
        lineChartEntities=taskService.getLineChartParam();

        //??????????????????
        List<LineChartEntity> pie=new ArrayList<>();
        pie=taskService.getPieChartParam();
        countEntity.setPieChartEntityList(pie);

        countEntity.setSumCount(sumCount);
        countEntity.setApprovalCount(approvalCount);
        countEntity.setWaitTryAgainCount(waitTryAgainCount);

        countEntity.setTodaySumCount(todaySumCount);
        countEntity.setTodayApprovalCount(todayApprovalCount);
        countEntity.setTodayWaitTryAgainCount(todayWaitTryAgainCount);

        countEntity.setHistoryCount(historyCount);
        countEntity.setTodayHistoryCount(todayHistoryCount);
        countEntity.setLineChartEntityList(lineChartEntities);
        return  countEntity;
    }
    /**
     * ??????????????????
     */
    @PostMapping("/getCount")
    public CountEntity getCount(HttpSession session){
        CountEntity countEntity=new CountEntity();
        String userSn=session.getAttribute("userSn").toString();

        //???????????????
        TaskEntity taskEntity=new TaskEntity(userSn,null,null,"??????");
        int waitTryAgainCount=taskService.getTaskCountByProperty(taskEntity);
        //?????????????????????
        TaskEntity taskEntity1=new TaskEntity(null,userSn,null,"??????");
        int approvalCount=taskService.getTaskCountByProperty(taskEntity1);

        //???????????????
        int sumCount=waitTryAgainCount+approvalCount;

        //???????????????????????????
        TaskEntity taskEntity5=new TaskEntity(userSn,null,"today","??????");
        int todayWaitTryAgainCount=taskService.getTaskCountByProperty(taskEntity5);
        //???????????????????????????
        TaskEntity taskEntity6=new TaskEntity(null,userSn,"today","??????");
        int todayApprovalCount=taskService.getTaskCountByProperty(taskEntity6);

        //???????????????
        int todaySumCount=todayWaitTryAgainCount+todayApprovalCount;
        //?????????
        TaskEntity taskEntity2=new TaskEntity(null,userSn);
        int historyCount=taskService.getHistoryCount(taskEntity2);

        //?????????????????????
        TaskEntity taskEntity3=new TaskEntity("today",userSn);
        int todayHistoryCount=taskService.getHistoryCount(taskEntity3);

        //?????????????????????
        List<LineChartEntity> lineChartEntities=new ArrayList<>();
        lineChartEntities=taskService.getLineChartParam();

        //??????????????????
        List<LineChartEntity> pie=new ArrayList<>();
        pie=taskService.getPieChartParam();
        countEntity.setPieChartEntityList(pie);

        countEntity.setSumCount(sumCount);
        countEntity.setApprovalCount(approvalCount);
        countEntity.setWaitTryAgainCount(waitTryAgainCount);

        countEntity.setTodaySumCount(todaySumCount);
        countEntity.setTodayApprovalCount(todayApprovalCount);
        countEntity.setTodayWaitTryAgainCount(todayWaitTryAgainCount);

        countEntity.setHistoryCount(historyCount);
        countEntity.setTodayHistoryCount(todayHistoryCount);
        countEntity.setLineChartEntityList(lineChartEntities);
        return countEntity;
    }
}


