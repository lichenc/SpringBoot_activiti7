package com.example.tquan.service;

import com.example.tquan.dao.VariableDao;
import com.example.tquan.entity.PositionEntity;
import com.example.tquan.entity.VariableEntity;
import javafx.scene.chart.ValueAxis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenjin on 2021/6/9 22:20
 */
@Service
public class VariableService {
    @Autowired
    private VariableDao variableDao;

    /**
     * 根据流程名称获取参数
     * @param variableEntity
     * @return
     */
    public List<VariableEntity> getProcessParamByName(VariableEntity variableEntity){
        return variableDao.getProcessParamByName(variableEntity);
    }

    /**
     * 根据name获取text
     * @param variableEntity
     * @return
     */
    public String getTextByName(VariableEntity variableEntity){
        return variableDao.getTextByName(variableEntity);
    }

    /**
     * 根据taskId获取task内容
     * @param procInstId
     * @return
     */
    public VariableEntity getTaskDefByProcInstId(String procInstId){
        return variableDao.getTaskDefByProcInstId(procInstId);
    }
}
