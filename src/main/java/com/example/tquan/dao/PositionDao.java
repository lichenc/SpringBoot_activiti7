package com.example.tquan.dao;

import com.example.tquan.entity.PositionEntity;
import com.example.tquan.entity.VariableEntity;
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

    /**
     * 获取所有岗位
     * @return
     */
    List<PositionEntity> findAll();

    /**
     * 根据岗位名称获取岗位id
     * @param name
     * @return
     */
    String getPositionByName(String name);

    /**
     * 为用户关联岗位
     * @param positionEntity
     */
    int addUserPosition(PositionEntity positionEntity);

    /**
     * 查询用户是否有某个岗位
     * @param positionEntity
     * @return
     */
    PositionEntity getInfo(PositionEntity positionEntity);
}
