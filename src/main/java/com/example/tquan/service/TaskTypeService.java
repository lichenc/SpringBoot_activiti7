package com.example.tquan.service;

import com.example.tquan.dao.TaskTypeDao;
import com.example.tquan.entity.TaskTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TaskTypeService {
    @Autowired
    private TaskTypeDao taskTypeDao;
    /**
     * 查询任务类型
     *
     * @param
     * @return
     */
    public List<TaskTypeEntity> accountTask() {
        return taskTypeDao.accountTask();
    }
}
