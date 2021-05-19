package com.example.tquan.controller;

import com.example.tquan.entity.AccountEntity;
import com.example.tquan.service.AccountService;
import com.ninghang.core.security.UIM;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenjin on 2021/5/17 10:59
 */
@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;   //security提供的加密接口

    private Log log = LogFactory.getLog(getClass());

    @GetMapping("/Hello1/{id}")
    public AccountEntity test(@PathVariable("id") Integer id) {
        System.out.println("id:" + id);
        return accountService.getById(id);
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
     * 用户登录
     *
     * @param accountEntity
     * @return
     */
    @PostMapping("/doLogin")
    public String login(AccountEntity accountEntity, HttpServletRequest request) {
        AccountEntity usersEntity1 = accountService.findUserByName(accountEntity);
        //判断用户是否存在
        if (usersEntity1 != null) {
            usersEntity1.setLoginPwd(UIM.decode(usersEntity1.getLoginPwd()));
            //校验密码
            // boolean matche = passwordEncoder.matches(usersEntity.getPassword(), usersEntity1.getPassword());
            //密码正确
            // if (matche == true) {
            if (accountEntity.getLoginPwd().equals(usersEntity1.getLoginPwd()) || accountEntity.getLoginPwd() == usersEntity1.getLoginPwd()) {
                log.info("==========================" + accountEntity.getLoginName() + "登录成功");
                request.setAttribute("loginabnormal", "200");
                return "loginPage";
            } else {
                log.info("==========================用户名或密码错误");
                request.setAttribute("loginabnormal", "201");
                return "loginPage";
            }
        } else {
            log.info("==========================用户不存在");
            request.setAttribute("loginabnormal", "202");
            return "loginPage";
        }
    }
}
