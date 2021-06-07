package com.example.tquan.service;

import com.example.tquan.dao.TaskDao;
import com.example.tquan.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskDao taskDao;

    /**
     * 获取任务列表
     * @param taskEntity
     * @return
     */
    public  List<TaskEntity> getTaskListByProperty(TaskEntity taskEntity){
        return taskDao.getTaskListByProperty(taskEntity);
    }

    /**
     * 修改任务状态
     * @param id
     * @return
     */
    public int updateTask(String id){
        return taskDao.updateTask(id);
    }
}
