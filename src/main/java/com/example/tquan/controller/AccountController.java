package com.example.tquan.controller;

import com.example.tquan.entity.AccountEntity;
import com.example.tquan.service.AccountService;
import com.example.tquan.service.UserService;
import com.ninghang.core.security.UIM;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by chenjin on 2021/5/17 10:59
 */

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    private Log log = LogFactory.getLog(getClass());


    /**
     * 修改账号
     */
    @PostMapping("/updateAccount")
    public int updateAccount(String id, String loginPwd) {
        int iden = 0;
        //初始化对象，设置修改需要的参数
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        accountEntity.setLoginPwd(UIM.encode(loginPwd));
        //执行修改
        iden = accountService.updateAccountById(accountEntity);
        System.out.println(iden);
        if (iden != 0) {
            log.info("==========================账号ID:" + id + "的账号密码修改成功");
        } else {
            log.info("==========================账号ID:" + id + "的账号密码修改失败");
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

}
