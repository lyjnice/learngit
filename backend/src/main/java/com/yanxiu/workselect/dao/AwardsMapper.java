package com.yanxiu.workselect.dao;

import com.yanxiu.workselect.api.model.Awards;

public interface AwardsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Awards record);

    int insertSelective(Awards record);

    Awards selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Awards record);

    int updateByPrimaryKey(Awards record);
}