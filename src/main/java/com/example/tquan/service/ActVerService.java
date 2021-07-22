package com.example.tquan.service;

import com.example.tquan.dao.ActVerDao;
import com.example.tquan.entity.ActVerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActVerService {
    @Autowired
    private ActVerDao actVerDao;
    /**
     * 查询全部业务系统
     *
     * @param
     * @return
     */
    public List<ActVerEntity> actVer(String appName,String userId,String appAccount) {

        return actVerDao.actVer(appName,userId,appAccount);
    }

}
