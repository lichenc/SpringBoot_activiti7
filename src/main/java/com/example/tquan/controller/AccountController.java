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
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    public UserEntity getAccountList(HttpSession session, HttpServletRequest request,String uuid) throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

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
        //userEntity1.setCreateTime(df.format(userEntity1.getCreateTime(),toString()));

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
     * 获取扩展字段
     * @param uuid
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/getExtraAttrs")
    public StringBuilder getExtraAttrs(String uuid,HttpSession session) throws Exception{
        String userId=session.getAttribute("UserId").toString();

        StringBuilder stringBuilder=new StringBuilder();
        //调用统权的接口，获取扩展字段
        //开通账号
        oauth(uuid);
        if(StringUtils.isEmpty(oauth(uuid))) {
            log.info("==========================uuid为空！");
        }else {
            List<NameValuePair> params = Lists.newArrayList();
            params.add(new BasicNameValuePair("id", userId));
            params.add(new BasicNameValuePair("uim-login-user-id", oauth(uuid)));
            //转换为键值对
            String userStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            stringBuilder=post(userStr);
        }
       return stringBuilder;

    }

    /**
     *查询待重试的流程
     * @return
     */
    @RequestMapping("/waitTryAgain")
    public TaskEntity waitTryAgainPage(TaskEntity taskEntity,HttpServletRequest request,HttpSession session,String approvedPerson){
            if (approvedPerson!=null &&approvedPerson!=""){
            taskEntity.setApprovedPerson(approvedPerson);
        }
        taskEntity.setRev(2);
        String sn=session.getAttribute("userSn").toString();
        TaskEntity taskEntity1=new TaskEntity();
       try {
           List<TaskEntity> taskEntities= taskService.getTaskListByProperty(taskEntity);
           List<TaskEntity> taskEntities1=new ArrayList<>();
           for(TaskEntity taskEntity2:taskEntities) {

               Map<String, Object> variables = processEngine.getRuntimeService().getVariables(taskEntity2.getTaskType());
               if (variables.get("applyPerson").equals(sn) || variables.get("applyPerson") == sn) {
                   taskEntity2.setApplyPerson(variables.get("applyPerson").toString());
                   taskEntity2.setApprovedPerson(variables.get("approvedPerson").toString());
                   taskEntity2.setTaskType(variables.get("taskType").toString());
                   taskEntity2.setApplyReason(variables.get("applyReason").toString());
                   VariableEntity variableEntity = new VariableEntity();
                   variableEntity.setName("repulseReason");
                   String text = variableService.getTextByName(variableEntity);
                   //查询是否被打回过
                   if (text != null) {
                       taskEntity2.setRepulseReason(variables.get("repulseReason").toString());
                   } else {
                       taskEntity2.setRepulseReason("");
                   }
                   taskEntities1.add(taskEntity2);
               }
               taskEntity1.setTaskEntities(taskEntities1);
           }
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



    /**
     * 获取token
     * @return
     * @throws Exception
     */
    public String oauth(String uuid)
            throws Exception {
        PublicKey publicKey = RsaUtil.string2PublicKey(iam.getKey());

        //用公钥加密
        byte[] publicEncrypt = RsaUtil.publicEncrypt(iam.getPassword().getBytes(), publicKey);

        //加密后的内容为Base64编码
        String rsa = RsaUtil.byte2Base64(publicEncrypt);

        log.info("==========================加密的结果:" +rsa);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(iam.getAddr());
        StringBuilder result = new StringBuilder();

        //组装数据
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("username", iam.getUsername()));
        params.add(new BasicNameValuePair("password", rsa));

        //转换为键值对
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

            log.info("==========================获取到的uid信息:" +result);
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
     * 获取用户扩展字段
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


}


