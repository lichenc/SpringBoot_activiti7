package com.example.tquan.service;

import com.example.tquan.dao.AccountDao;
import com.example.tquan.entity.AccountEntity;
import com.ninghang.core.security.UIM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @param accountEntity
     * @return
     */
    public List<AccountEntity> getByUserId(AccountEntity accountEntity) {
        return accountDao.getByUserId(accountEntity);
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
     * @param accountEntity
     * @return
     */
    public AccountEntity findUserByName(AccountEntity accountEntity) {
        return accountDao.findUserByName(accountEntity.getLoginName());
    }

    /**
     * 管理员登录
     * @param name
     * @return
     */
    public AccountEntity  adminLogin(String name){
        return accountDao.adminLogin(name);
    }
    /**
     * 根据ID修改账号
     *
     * @param accountEntity
     * @return
     */
    public int updateAccountById(AccountEntity accountEntity) {
        return accountDao.updateAccountById(accountEntity);
    }

    /**
     * 添加账号
     * @param accountEntity
     * @return
     */
    public  int addAccount(AccountEntity accountEntity){
        return accountDao.addAccount(accountEntity);
    }

    /**
     * 获取密码规则参数
     * @param name
     * @return
     */
    public int getAmPwdPolicy(String name){
        return accountDao.getAmPwdPolicy(name);
    }
}
