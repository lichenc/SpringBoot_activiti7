package com.example.tquan.service;

import com.example.tquan.dao.TaskDao;
import com.example.tquan.entity.LineChartEntity;
import com.example.tquan.entity.ProcdefEntity;
import com.example.tquan.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksService {
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
     * @param taskEntity
     * @return
     */
    public int updateTask(TaskEntity taskEntity){
        return taskDao.updateTask(taskEntity);
    }

    /**
     * 统计任务数量
     * @param taskEntity
     * @return
     */
    public int getTaskCountByProperty(TaskEntity taskEntity){
        return  taskDao.getTaskCountByProperty(taskEntity);
    }

    /**
     * 获取历史任务数量
     * @return
     */
    public int getHistoryCount(TaskEntity taskEntity){
        return taskDao.getHistoryCount(taskEntity);
    }

    /**
     * 获取昨日流程新增数量
     * @return
     */
    public int getyesTodayCount(int count){
        return taskDao.getyesTodayCount(count);
    }

    /**
     * 获取折线图参数
     * @return
     */
    public List<LineChartEntity> getLineChartParam(){
        return  taskDao.getLineChartParam();
    }

    /**
     * 获取饼图参数
     * @return
     */
    public List<LineChartEntity> getPieChartParam(){
        return taskDao.getPieChartParam();
    }

    /**
     * 查询所有流程
     * @return
     */
    public List<ProcdefEntity> selectAllProcef(ProcdefEntity procdefEntity){
        return taskDao.selectAllProcef(procdefEntity);
    }

    /**
     * 修改流程状态
     * @param procdefEntity
     * @return
     */
    public int updateProcdefStatus(ProcdefEntity procdefEntity){
        return taskDao.updateProcdefStatus(procdefEntity);
    }
}
