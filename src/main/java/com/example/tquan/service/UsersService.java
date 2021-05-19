package com.example.tquan.service;

import com.example.tquan.dao.UsersDao;
import com.example.tquan.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Created by chenjin on 2021/5/17 10:51
 */
@Service
public class UsersService {

    @Autowired
    private UsersDao usersDao;
    @Autowired
    private PasswordEncoder passwordEncoder;   //security提供的加密接口

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public UsersEntity getById(Integer id) {
        return usersDao.getById(id);
    }

    /**
     * 添加用户
     *
     * @param usersEntity
     * @return
     */
    public int addUser(UsersEntity usersEntity) {
        int iden = 0;
        if (usersEntity.getUsername() != null && usersEntity.getPassword() != null) {
            usersEntity.setPassword(passwordEncoder.encode(usersEntity.getPassword()));
            iden = usersDao.addUser(usersEntity);
        }
        return iden;
    }

    /**
     * 用户登录
     *
     * @param usersEntity
     * @return
     */
    public UsersEntity findUserByName(UsersEntity usersEntity) {
        return usersDao.findUserByName(usersEntity.getUsername());
    }
}
