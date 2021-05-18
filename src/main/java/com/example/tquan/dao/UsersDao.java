package com.example.tquan.dao;

import com.example.tquan.entity.UsersEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by chenjin on 2021/5/17 10:49
 */
@Mapper
public interface UsersDao {
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    UsersEntity getById(Integer id);

    /**
     * 添加用户
     *
     * @return
     */
    int addUser(UsersEntity usersEntity);

    /**
     * 用户登录
     *
     * @param username
     * @return
     */
    UsersEntity findUserByName(String username);
}
