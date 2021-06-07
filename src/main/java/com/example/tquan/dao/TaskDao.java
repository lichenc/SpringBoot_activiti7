package com.example.tquan.dao;

import com.example.tquan.entity.TaskEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by chenjin on 2021/6/7 11:05
 */
@Mapper
public interface TaskDao {
    /**
     * 根据状态查询任务
     * @return
     */
    List<TaskEntity> getTaskListByProperty(TaskEntity taskEntity);

    /**
     * 修改任务的状态
     * @param id
     * @return
     */
    int updateTask(String id);
}
