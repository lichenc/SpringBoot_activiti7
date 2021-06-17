package com.example.tquan.controller;

import com.example.tquan.service.ApproverService;
import com.example.tquan.service.ImAppService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Autowired
    private ApproverService approverService ;
    private Log log = LogFactory.getLog(getClass());
    /**
     * appList查询全部业务系统
     *
     * @param
     * @return
     */
   /* @RequestMapping("/apply")
    public void findAll(HttpServletRequest request, HttpSession session) {

        //return "/apply";
    }*/

}
