package com.example.tquan.controller;

import com.example.tquan.entity.UsersEntity;
import com.example.tquan.service.UsersService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by chenjin on 2021/5/17 10:59
 */
@RestController
@RequestMapping(value =  "/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;   //security提供的加密接口

    private Log log = LogFactory.getLog(getClass());

    @GetMapping("/Hello1/{id}")
    public UsersEntity test(@PathVariable("id") Integer id) {
        System.out.println("id:" + id);
        return usersService.getById(id);
    }

    /**
     * 用户注册
     *
     * @param usersEntity
     * @return
     */
    @PostMapping("/doRegister")
    public String register(UsersEntity usersEntity) {
        int iden = usersService.addUser(usersEntity);
        if (iden == 0) {
            log.info("==========================注册失败");
            return "注册失败！";
        } else {
            log.info("==========================" + usersEntity.getUsername() + "注册成功");
            return "注册成功！";
        }
    }

    /**
     * 用户登录
     *
     * @param usersEntity
     * @return
     */
    @PostMapping("/doLogin")
    public String login(UsersEntity usersEntity) {
        UsersEntity usersEntity1 = usersService.findUserByName(usersEntity);
        //判断用户是否存在
        if (usersEntity1 != null) {
            //校验密码
            boolean matche = passwordEncoder.matches(usersEntity.getPassword(), usersEntity1.getPassword());
            //密码正确
            if (matche == true) {
                log.info("==========================" + usersEntity.getUsername() + "登录成功");
                return "登录成功！";
            } else {
                log.info("==========================用户名或密码错误");
                return "用户名或密码错误!";
            }
        } else {
            log.info("==========================用户不存在");
            return "用户不存在！";
        }
    }
}
