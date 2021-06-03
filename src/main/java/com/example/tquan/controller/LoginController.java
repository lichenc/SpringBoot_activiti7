package com.example.tquan.controller;

import com.example.tquan.entity.AccountEntity;
import com.example.tquan.entity.UserEntity;
import com.example.tquan.service.AccountService;
import com.example.tquan.service.TaskService;
import com.example.tquan.service.UserService;
import com.ninghang.core.security.UIM;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
                    session.setAttribute("userName",userEntity1.getName());
                    session.setAttribute("userSn",userEntity1.getSn());
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
     * 退出登录
     *
     * @return
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request,HttpSession session) {
        request.removeAttribute("userEntity");
        session.removeAttribute("UserId");
        return "/loginPage";
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

}
