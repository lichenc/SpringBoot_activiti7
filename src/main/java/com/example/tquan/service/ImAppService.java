package com.example.tquan.service;

import com.example.tquan.dao.ImAppDao;
import com.example.tquan.entity.ActEntity;
import com.example.tquan.entity.ImApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tangmiaomiao on 2021/5/20 12:51
 */
@Service
public class ImAppService {
    @Autowired
    private ImAppDao imAppDao;
    /**
     * 查询全部业务系统
     *
     * @param
     * @return
     */
    public List<ImApp> findAll() {

        return imAppDao.findAll();
    }
    /**
     * 查询申请的应用id
     *
     * @param
     * @return
     */
    public List<ImApp> findApply(String nameApp) {
        return imAppDao.findApply(nameApp);
    }
    /**
     * 当前用户有禁用账号的应用
     *
     * @param
     * @return
     */
    public List<ImApp> actAppEn(String usId){
        return imAppDao.actAppEn(usId);
    }
    /**
     * 当前用户有启用账号的应用
     *
     * @param
     * @return
     */
    public List<ImApp> actAppUp(String usId){
        return imAppDao.actAppUp(usId);
    }

}
