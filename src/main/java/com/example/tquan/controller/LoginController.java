package com.example.tquan.controller;

import com.beust.jcommander.internal.Lists;
import com.example.tquan.entity.AccountEntity;
import com.example.tquan.entity.IamOauthEntity;
import com.example.tquan.entity.UserEntity;
import com.example.tquan.service.AccountService;
import com.example.tquan.service.UserService;
import com.example.tquan.util.RsaUtil;
import com.ninghang.core.security.UIM;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.util.List;

/**
 * Created by chenjin on 2021/5/20 15:07
 */
@Controller
public class LoginController {
    //日志类
    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;
    @Autowired
    private IamOauthEntity iam;

    /**
     * 管理员登录
     * @return
     */
    @PostMapping("/doConsoleLogin")
    public String doConsoleLogin(AccountEntity accountEntity,HttpSession session,HttpServletRequest request) throws Exception{
        JSONObject resultJson=null;
        String co=null;
        //判断管理员是否存在

        PublicKey publicKey = RsaUtil.string2PublicKey(iam.getKey());

        //用公钥加密
        byte[] publicEncrypt = RsaUtil.publicEncrypt(accountEntity.getLoginPwd().getBytes(), publicKey);

        //加密后的内容为Base64编码
        String rsa = RsaUtil.byte2Base64(publicEncrypt);

        log.info("==========================加密的结果:" +rsa);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(iam.getAddr());
        StringBuilder result = new StringBuilder();

        //组装数据
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("username", accountEntity.getLoginName()));
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
             resultJson = JSONObject.fromObject(result.toString());
            String code=resultJson.get("code").toString();
            if (code.equals("100000001")){
                request.setAttribute("adminName",accountEntity.getLoginName());
                log.info("=========================="+accountEntity.getLoginName()+"登录成功！");
                co= "/consoleIndex";
            }else if (code.equals("100000003")){
                request.setAttribute("loginabnormal","100000003");
                log.info("==========================用户名或密码错误!");
                co= "/console";
            }else if(code.equals("100000005")){
                log.info("==========================用户名或密码错误!");
                request.setAttribute("loginabnormal","100000005");
                co= "/console";
            }else if (code.equals("100000006")){
                log.info("==========================账号被禁用!");
                request.setAttribute("loginabnormal","100000006");
                co= "/console";
            }else{
                log.info("==========================登录失败!");
                request.setAttribute("loginabnormal","100000007");
                co= "/console";
            }
        }
        return co;
    }

    /**
     * 用户登录
     *
     * @param accountEntity
     * @return
     */
    @PostMapping("/doLogin")
    public String login(AccountEntity accountEntity, HttpSession session,HttpServletRequest request) {
        AccountEntity accountEntity1 = accountService.findUserByName(accountEntity);
        //判断用户是否存在
        if (accountEntity1 != null) {
            accountEntity1.setLoginPwd(UIM.decode(accountEntity1.getLoginPwd()));

            // boolean matche = passwordEncoder.matches(usersEntity.getPassword(), usersEntity1.getPassword());
            //密码正确
            // if (matche == true) {
            //校验密码
            if (accountEntity.getLoginPwd().equals(accountEntity1.getLoginPwd()) || accountEntity.getLoginPwd() == accountEntity1.getLoginPwd()) {
                log.info("==========================" + accountEntity.getLoginName() + "登录成功");
                request.setAttribute("loginabnormal", "200");
                //获取登录账号的个人详细信息
                UserEntity userEntity1 = new UserEntity();
                userEntity1.setId(accountEntity1.getUserId());
                UserEntity userEntity = userService.getUserByProperty(userEntity1);
                //判断是否查询到了用户
                if (userEntity != null) {
                    request.setAttribute("userName", userEntity.getName());
                    session.setAttribute("UserId",accountEntity1.getUserId());
                    session.setAttribute("userName",userEntity.getName());
                    session.setAttribute("userSn",userEntity.getSn());
                    //session.setMaxInactiveInterval(3600);
                    return "/index";
                } else {
                    log.info("==========================账号没有对应的用户");
                    return "/loginPage";
                }
            } else {
                log.info("==========================用户名或密码错误");
                request.setAttribute("loginabnormal", "201");
                return "/loginPage";
            }
        } else {
            log.info("==========================用户不存在");
            request.setAttribute("loginabnormal", "202");
            return "/loginPage";
        }
    }



    /**
     * 用户注册
     *
     * @param accountEntity
     * @return
     */
    @PostMapping("/doRegister")
    public void register(AccountEntity accountEntity) {
        int iden = accountService.addUser(accountEntity);
        if (iden == 0) {
            log.info("==========================注册失败");

        } else {
            log.info("==========================" + accountEntity.getLoginName() + "注册成功");

        }
    }
    /**
     * 退出登录
     *
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpSession session) {
        session.removeAttribute("UserId");
        session.removeAttribute("userName");
        session.removeAttribute("userSn");
        return "/loginPage";
    }

    /**
     * 管理员退出登录
     * @param session
     * @return
     */
    @GetMapping("/consoleLogout")
    public String consoleLogout(HttpSession session){
        return "/console";
    }

}
