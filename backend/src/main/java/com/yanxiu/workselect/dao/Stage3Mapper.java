package com.yanxiu.workselect.dao;

import com.yanxiu.workselect.api.model.Stage3;

public interface Stage3Mapper {
    int deleteByPrimaryKey(Long stageId);

    int insert(Stage3 record);

    int insertSelective(Stage3 record);

    Stage3 selectByPrimaryKey(Long stageId);

    int updateByPrimaryKeySelective(Stage3 record);

    int updateByPrimaryKey(Stage3 record);
}