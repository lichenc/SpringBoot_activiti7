package com.example.tquan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面访问控制
 * Created by chenjin on 2021/5/18 10:21
 */
@Controller
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "/index";
    }

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
}
