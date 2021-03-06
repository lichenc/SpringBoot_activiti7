package com.example.tquan.dao;

import com.example.tquan.entity.TaskEntity;
import com.example.tquan.entity.VariableAddEntity;
import com.example.tquan.entity.VariableEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by chenjin on 2021/6/9 22:13
 */
@Mapper
public interface VariableDao {
    /**
     * 根据流程名获取流程的参数
     * @param variableEntity
     * @return
     */
    List<VariableEntity>  getProcessParamByName(VariableEntity variableEntity);

    /**
     * 根据name获取text
     * @param variableEntity
     * @return
     */
    String getTextByName(VariableEntity variableEntity);

    /**
     * 根据taskid获取task内容
     * @param procInstId
     * @return
     */
    VariableEntity getTaskDefByProcInstId(String procInstId);

    /**
     * 修改流程参数
     * @param variableEntity
     * @return
     */
    int updateTaskParam(VariableEntity variableEntity);

    /**
     * 新增申请流程参数
     * @param variableAddEntity
     * @return
     */
    int addRepulseReason(VariableAddEntity variableAddEntity);

    /**
     * 获取历史流程的参数
     * @param taskEntity
     * @return
     */
    String getHistoryVariables(TaskEntity taskEntity);
}
