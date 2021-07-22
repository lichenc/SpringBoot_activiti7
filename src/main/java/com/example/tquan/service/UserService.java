package com.example.tquan.service;

import com.example.tquan.dao.UserDao;
import com.example.tquan.entity.FieldEntity;
import com.example.tquan.entity.PositionEntity;
import com.example.tquan.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenjin on 2021/5/20 15:04
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    /**
     * 根据参数查询用户
     *
     * @param userEntity
     * @return
     */
    public UserEntity getUserByProperty(UserEntity userEntity) {
        userEntity = userDao.getUserByProperty(userEntity);
        return userEntity;
    }

    /**
     * 查询全部用户扩展字段
     *
     * @param
     * @return
     */

    public List<FieldEntity> findAll(){
        return userDao.findAll();
    }
}
