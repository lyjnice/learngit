package com.yanxiu.passport.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanxiu.workselect.api.commons.vo.passport.PPUser;

public interface PPUserMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(PPUser record);

    int insertSelective(PPUser record);

    PPUser selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(PPUser record);

    int updateByPrimaryKey(PPUser record);
    
    List<PPUser> selectByPassport(@Param("passport") List<String> passport);
} 