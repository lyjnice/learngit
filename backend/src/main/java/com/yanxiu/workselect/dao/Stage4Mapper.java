package com.yanxiu.workselect.dao;

import com.yanxiu.workselect.api.model.Stage4;

public interface Stage4Mapper {
    int deleteByPrimaryKey(Long stageId);

    int insert(Stage4 record);

    int insertSelective(Stage4 record);

    Stage4 selectByPrimaryKey(Long stageId);

    int updateByPrimaryKeySelective(Stage4 record);

    int updateByPrimaryKey(Stage4 record);
}