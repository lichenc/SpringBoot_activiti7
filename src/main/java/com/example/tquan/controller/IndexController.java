package com.example.tquan.controller;

import com.beust.jcommander.internal.Lists;
import com.example.tquan.entity.IamOauthEntity;
import com.example.tquan.entity.IamUserEntity;
import com.example.tquan.util.IamInterface;
import com.ninghang.core.util.StringUtils;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
/**
 * 页面访问控制
 * Created by chenjin on 2021/5/18 10:21
 */
@Controller
public class IndexController {
    @Autowired
    private IamUserEntity user;
    @Autowired
    private IamOauthEntity iam;
    private Log log = LogFactory.getLog(getClass());
    IamInterface iamInterface=new IamInterface();

    @GetMapping("/login")
    public String login() {
        return "/loginPage";
    }

    @GetMapping("/register")
    public String register() {
        return "/register";
    }

    @GetMapping("/personInfoPage")
    public String personInfo() {
        return "/personInfoPage";
    }

    @GetMapping("/waitTryAgainPage")
    public String waitTryAgainPage() {
        return "/waitTryAgainPage";
    }

    @GetMapping("/apply")
    public String apply() {
        return "/apply";
    }

    @GetMapping("/position")
    public String position(String uuid, HttpServletRequest request,  HttpSession session) throws Exception {
        String userId=session.getAttribute("UserId").toString();
        String ifo=iamInterface.oauth(uuid,iam.getKey(),iam.getPassword(),iam.getAddr(),iam.getUsername(),iam.getType(),iam.getCharset());
        if(StringUtils.isEmpty(ifo)) {
            log.info("==========================uuid为空！");
        }else {
            //调用统权的接口，获取用户信息
            List<NameValuePair> params = Lists.newArrayList();
            params.add(new BasicNameValuePair("id", userId));
            params.add(new BasicNameValuePair("uim-login-user-id", ifo));
            //转换为键值对
            String userStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
            StringBuilder ifus=iamInterface.userSelect(userStr,user.getAddr(),user.getType());
            JSONObject resultJson = JSONObject.fromObject(ifus.toString());
            request.setAttribute("userStr",resultJson);
        }
        return "/position";
    }

    @GetMapping("/audit")
    public String audit(){
        return "/audit";
    }
    @GetMapping("/console")
    public String console(){
        return "/console";
    }

    @GetMapping("/procdefPage")
    public String procdefPage(){
        return "/procdefPage";
    }

    @GetMapping("/addProcdefPage")
    public String addProcdefPage(){
        return "/addProcdefPage";
    }

    @GetMapping("/consoleTaskList")
    public String consoleTaskList(){
        return "/consoleTaskList";
    }

    @GetMapping("/hisTask")
    public String completeRecordsTask(){
        return "/hisTask";
    }
    @GetMapping("/forgetPwd")
    public String forgetPwd(){
        return "/forgetPwd";
    }
}
