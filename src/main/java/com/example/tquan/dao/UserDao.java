package com.example.tquan.dao;

import com.example.tquan.entity.FieldEntity;
import com.example.tquan.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenjin on 2021/5/20 15:02
 */
@Mapper
@Repository
public interface UserDao {
    /**
     * 根据参数查询用户
     *
     * @param userEntity
     * @return
     */
    UserEntity getUserByProperty(UserEntity userEntity);

    /**
     * 查询全部用户扩展字段
     *
     * @param
     * @return
     */

    List<FieldEntity> findAll();
}
