package com.example.tquan.dao;

import com.example.tquan.entity.LineChartEntity;
import com.example.tquan.entity.ProcdefEntity;
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
     * @param taskEntity
     * @return
     */
    int updateTask(TaskEntity taskEntity);

    /**
     * 统计任务数量
     * @param taskEntity
     * @return
     */
    int getTaskCountByProperty(TaskEntity taskEntity);

    /**
     * 获取历史任务数量
     * @return
     */
    int getHistoryCount(TaskEntity taskEntity);

    /**
     * 获取昨日新增
     *
     * @return
     */
    int getyesTodayCount(int count);

    /**
     * 获取折线图参数
     * @return
     */
    List<LineChartEntity> getLineChartParam();

    /**
     * 获取饼图参数
     * @return
     */
    List<LineChartEntity> getPieChartParam();

    /**
     * 查询所有流程
     * @return
     */
    List<ProcdefEntity> selectAllProcef(ProcdefEntity procdefEntity);

    /**
     * 修改流程状态
     * @param procdefEntity
     * @return
     */
    int updateProcdefStatus(ProcdefEntity procdefEntity);
}
