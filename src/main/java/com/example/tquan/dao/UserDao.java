package com.example.tquan.dao;

import com.example.tquan.entity.PositionEntity;
import com.example.tquan.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by chenjin on 2021/5/20 15:02
 */
@Mapper
public interface UserDao {
    /**
     * 根据参数查询用户
     *
     * @param userEntity
     * @return
     */
    UserEntity getUserByProperty(UserEntity userEntity);


}
