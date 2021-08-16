package com.example.tquan.service;

import com.example.tquan.dao.PositionDao;
import com.example.tquan.entity.PositionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenjin on 2021/6/1 21:54
 */
@Service
public class PositionService {
    @Autowired
    private PositionDao positionDao;
    /**
     * 获取用户岗位集合
     * @param userId
     * @return
     */
    public List<PositionEntity> getPositionByUserId(String userId){
        return positionDao.getPositionByUserId(userId);
    }

    /**
     * 获取所有岗位
     * @return
     */
    public List<PositionEntity> findAll(){
        return positionDao.findAll();
    }

    /**
     * 根据岗位名称获取岗位id
     * @param name
     * @return
     */
    public String getPositionByName(String name){
        return positionDao.getPositionByName(name);
    }

    /**
     * 为用户关联岗位
     * @param positionEntity
     * @return
     */
    public int addUserPosition(PositionEntity positionEntity){
        return positionDao.addUserPosition(positionEntity);
    }

    /**
     * 查询用户是否存在某个岗位
     * @param positionEntity
     * @return
     */
    public PositionEntity getInfo(PositionEntity positionEntity){
        return positionDao.getInfo(positionEntity);
    }
    /**
     * 根据组织查询岗位
     *
     * @return
     */
    public List<PositionEntity> orgPosition(String orgId){
        return positionDao.orgPosition(orgId);
    };
    /**
     * 根据申请的岗位删除没用的岗位
     *
     * @return
     */
    public int deleteUserPos(String userId,String positionId){

        return positionDao.deleteUserPos(userId,positionId);
    };
}
