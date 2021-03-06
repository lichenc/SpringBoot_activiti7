package com.example.tquan.dao;

import com.example.tquan.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by chenjin on 2021/5/17 10:49
 */
@Mapper
public interface AccountDao {
    /**
     * 根据id查询
     *
     * @param accountEntity
     * @return
     */
    List<AccountEntity> getByUserId(AccountEntity accountEntity);

    /**
     * 添加用户
     *
     * @return
     */
    int addUser(AccountEntity accountEntity);

    /**
     * 用户登录
     *
     * @param username
     * @return
     */
    AccountEntity findUserByName(String username);

    /**
     * 管理员登录
     * @param name
     * @return
     */
    AccountEntity adminLogin(String name);

    /**
     * 根据ID修改账号
     *
     * @param accountEntity
     * @return
     */
    int updateAccountById(AccountEntity accountEntity);

    /**
     * 添加账号
     * @param accountEntity
     * @return
     */
    int addAccount(AccountEntity accountEntity);

    /**
     * 获取密码规则参数
     * @param name
     * @return
     */
    int getAmPwdPolicy(String name);
}
