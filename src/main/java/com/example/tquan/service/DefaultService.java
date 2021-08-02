package com.example.tquan.service;

import com.example.tquan.dao.DefaultDao;
import com.example.tquan.entity.ActEntity;
import com.example.tquan.entity.DefaultEntity;
import com.example.tquan.entity.DefaultsEntity;
import com.example.tquan.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DefaultService {
    @Autowired
    private DefaultDao defaultDao;
    /**
     * 查询账号默认值
     *
     * @param
     * @return
     */

    public List<DefaultEntity> defaults(String appId){
        return defaultDao.defaults(appId);
    }
    /**
     * 查询默认值
     *
     * @param
     * @return
     */

    public List<DefaultEntity> allDefaults(String usId,String allField){
        return defaultDao.allDefaults(usId,allField);
    }
    /**
     * 根据应用查询所有禁用的账号
     *
     * @param
     * @return
     */
    public List<ActEntity> act(String app){
        return  defaultDao.act(app);
    }
    /**
     * 根据应用查询所有禁用的账号
     *
     * @param
     * @return
     */
    public List<ActEntity> actField(String app,String act){
        return  defaultDao.actField(app,act);
    }
    /**
     * 查询当前人应用的账号
     *
     * @param
     * @return
     */
    public List<ActEntity> actDisable(String app,String usId){
        return defaultDao.actDisable(app,usId);
    }
    /**
     * 查询当前人应用禁用的账号
     *
     * @param
     * @return
     */
    public List<ActEntity> actEnable(String app,String usId){
        return defaultDao.actEnable(app,usId);
    }
    /**
     * 批量存默认值
     *
     * @param
     * @return
     */

    public List<DefaultsEntity> listDefaults(){
        return defaultDao.listDefaults();
    }
    /**
     * 存默认值
     *
     * @param
     * @return
     */

    public DefaultsEntity defaulty(){
        return defaultDao.defaulty();
    }
    /**
     * 查询是否存在帐号
     *
     * @param
     * @return
     */
    public List actNum(String accountName, String applyName){
        return defaultDao.actNum(accountName,applyName);
    }
    /**
     * 查询字段默认值
     *
     * @param
     * @return
     */
    public String fieldDefaultVal(String sql){
        return defaultDao.fieldDefaultVal(sql);
    }

}
