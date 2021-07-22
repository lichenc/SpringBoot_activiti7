package com.example.tquan.dao;

import com.example.tquan.entity.ApplyEntity;
import com.example.tquan.entity.Approver;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ApproverDao {
        /**
         * 查询审核人
         *
         * @param
         * @return
         */
        List<Approver> audit(String login_name);
        /**
         * 查询申请人信息
         *
         * @param
         * @return
         */
        List<ApplyEntity> apply(String sn);
        /**
         * 查询用户扩展字段
         *
         * @param
         * @return
         */
        List<ApplyEntity> field();
        /**
         * 申请人全部信息
         *
         * @param
         * @return
         */
        ApplyEntity users(String accout);
}
