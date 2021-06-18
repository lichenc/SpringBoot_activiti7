package com.example.tquan.dao;

import com.example.tquan.entity.Approver;
import com.example.tquan.entity.TaskTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TaskTypeDao {
    /**
     * 查询任务类型
     *
     * @param
     * @return
     */
    List<TaskTypeEntity> accountTask();

}
