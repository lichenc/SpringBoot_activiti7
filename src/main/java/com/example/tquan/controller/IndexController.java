package com.example.tquan.controller;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面访问控制
 * Created by chenjin on 2021/5/18 10:21
 */
@Controller
public class IndexController {

    @GetMapping("/login")
    public String login() {
        return "/loginPage";
    }

    @GetMapping("/register")
    public String register() {
        return "/registerPage";
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
    public String position(){
        return "/position";
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

}
