package com.example.tquan.dao;

import com.example.tquan.entity.PositionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by chenjin on 2021/6/1 21:53
 */
@Mapper
public interface PositionDao {
    /**
     * 获取用户岗位
     * @param userId
     * @return
     */
    List<PositionEntity> getPositionByUserId(String userId);
}
