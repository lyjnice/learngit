package com.yanxiu.workselect.dao;

import com.yanxiu.workselect.api.model.Stage1;

public interface Stage1Mapper {
    int deleteByPrimaryKey(Long stageId);

    int insert(Stage1 record);

    int insertSelective(Stage1 record);

    Stage1 selectByPrimaryKey(Long stageId);

    int updateByPrimaryKeySelective(Stage1 record);

    int updateByPrimaryKey(Stage1 record);
}