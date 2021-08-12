package com.example.tquan.dao;

import com.example.tquan.entity.GroupEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by chenjin on 2021/6/1 22:24
 */
@Mapper
public interface GroupDao {
    /**
     * 根据用户id查询用户组
     * @param userId
     * @return
     */
    List<GroupEntity> getGroupByUserId(String userId);

    /**
     * 获取默认组组织的id
     * @param name
     * @return
     */
    String getDedaultId(String name);

    /**
     * 添加用户与组织关联
     * @param groupEntity
     * @return
     */
    int addUserOrg(GroupEntity groupEntity);
}
