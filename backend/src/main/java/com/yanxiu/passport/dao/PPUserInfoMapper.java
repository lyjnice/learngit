package com.yanxiu.passport.dao;

import com.yanxiu.workselect.api.commons.vo.passport.PPUserInfo;

public interface PPUserInfoMapper {
    int insert(PPUserInfo record);

    int insertSelective(PPUserInfo record);
}