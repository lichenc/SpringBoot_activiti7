package com.example.tquan.service;

import com.example.tquan.dao.AccountDao;
import com.example.tquan.entity.AccountEntity;
import com.ninghang.core.security.UIM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by chenjin on 2021/5/17 10:51
 */
@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PasswordEncoder passwordEncoder;   //security提供的加密接口

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public AccountEntity getById(Integer id) {
        return accountDao.getById(id);
    }

    /**
     * 添加用户
     *
     * @param accountEntity
     * @return
     */
    public int addUser(AccountEntity accountEntity) {
        int iden = 0;
        if (accountEntity.getLoginName() != null && accountEntity.getLoginName() != null) {
            //springsecurity加密
            //usersEntity.setPassword(passwordEncoder.encode(usersEntity.getPassword()));
            accountEntity.setLoginPwd(UIM.encode(accountEntity.getLoginPwd()));
            iden = accountDao.addUser(accountEntity);
        }
        return iden;
    }

    /**
     * 用户登录
     *
     * @param usersEntity
     * @return
     */
    public AccountEntity findUserByName(AccountEntity usersEntity) {
        return accountDao.findUserByName(usersEntity.getLoginName());
    }
}
