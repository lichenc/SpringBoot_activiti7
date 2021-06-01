package com.example.tquan.controller;

import com.example.tquan.service.ImAppService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by tangmiaomiao on 2021/5/20 12:53
 */
@Controller
@ComponentScan(basePackages = {"com.example.tquan"})
//@RequestMapping("/audit")
public class ImAppController {
    @Autowired
    private ImAppService imAppService;
    private Log log = LogFactory.getLog(getClass());
    /**
     * 查询全部业务系统
     *
     * @param
     * @return
     */
    @RequestMapping("/audit")
    public void findAll(HttpServletRequest request) {
        System.out.println();
        List appList=imAppService.findAll();
        request.setAttribute("appList",appList);
        //return "audit";
    }

}
