package com.example.tquan.dao;

import com.example.tquan.entity.ActEntity;
import com.example.tquan.entity.DefaultEntity;
import com.example.tquan.entity.DefaultsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DefaultDao {
    /**
     * 查询账号默认值
     *
     * @param
     * @return
     */

    List<DefaultEntity> defaults(String appId);
    /**
     * 查询默认值
     *
     * @param
     * @return
     */

    List<DefaultEntity> allDefaults(String usId,String allField);
    /**
     * 根据应用查询账号属性
     *
     * @param
     * @return
     */
    List<ActEntity> act(String app);
    /**
     * 根据应用查询账号属性
     *
     * @param
     * @return
     */
    List<ActEntity> actField(String app,String act);
    /**
     * 查询当前人应用的账号
     *
     * @param
     * @return
     */
    List<ActEntity> actDisable(String app,String usId);
    /**
     * 查询当前人应用内禁用的账号
     *
     * @param
     * @return
     */
    List<ActEntity> actEnable(String app,String usId);

    /**
     * 存所有字段默认值
     *
     * @param
     * @return
     */

    List<DefaultsEntity> listDefaults();
    /**
     * 存默认值
     *
     * @param
     * @return
     */

    DefaultsEntity defaulty();
    /**
     * 查询是否存在帐号
     *
     * @param
     * @return
     */
    List actNum(String accountName, String applyName);
}
