package com.example.tquan.dao;

import com.example.tquan.entity.ActVerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface ActVerDao {
    /**
     * 验证账号是否存在
     *
     * @param
     * @return
     */

    List<ActVerEntity> actVer(String appName,String userId,String appAccount);

}
