package com.example.tquan.service;

import com.example.tquan.dao.ImAppDao;
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
}
