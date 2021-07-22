package com.example.tquan.service;

import com.example.tquan.dao.ApproverDao;
import com.example.tquan.entity.ApplyEntity;
import com.example.tquan.entity.Approver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApproverService {
    @Autowired
    private ApproverDao approverDao;
    /**
     * 查询审核人
     *
     * @param
     * @return
     */
    public List<Approver> audit(String login_name) {

        return approverDao.audit(login_name);
    }
    /**
     * 查询申请人信息
     *
     * @param
     * @return
     */
    public List<ApplyEntity> apply(String sn) {

        return approverDao.apply(sn);
    }
    /**
     * 查询用户扩展字段
     *
     * @param
     * @return
     */
    public List<ApplyEntity> field() {

        return approverDao.field();
    }


    /**
     * 查询申请人全部信息
     *
     * @param
     * @return
     */
    public  ApplyEntity users(String accout) {

        return approverDao.users(accout);
    }

}
