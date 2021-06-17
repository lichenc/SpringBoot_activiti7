package com.example.tquan.dao;

import com.example.tquan.entity.ImApp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by tangmiaomiao on 2021/5/20 12:49
 */
@Mapper
@Repository
public interface ImAppDao {
    /**
     * 查询全部业务系统
     *
     * @param
     * @return
     */

    List<ImApp> findAll();
    /**
     * 分页查询
     * @return
     */
    List selectPage();
    /**
     * 查询申请的应用Id
     *
     * @param
     * @return
     */
    List<ImApp> findApply(String name);
}
