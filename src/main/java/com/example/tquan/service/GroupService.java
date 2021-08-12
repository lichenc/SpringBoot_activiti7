package com.example.tquan.service;

import com.example.tquan.dao.GroupDao;
import com.example.tquan.entity.GroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenjin on 2021/6/1 22:27
 */
@Service
public class GroupService {
    @Autowired
    private GroupDao groupDao;

    /**
     * 根据用户id获取用户组
     * @param userId
     * @return
     */
    public List<GroupEntity> getGroupByUserId(String userId){
        return groupDao.getGroupByUserId(userId);
    }

    /**
     * 获取默认组织id
     * @param name
     * @return
     */
    public String getDedaultId(String name){
        return groupDao.getDedaultId(name);
    }

    /**
     * 添加用户与组织关联
     * @param groupEntity
     * @return
     */
    public int addUserOrg(GroupEntity groupEntity){
        return groupDao.addUserOrg(groupEntity);
    }

}
