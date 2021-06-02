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
}
