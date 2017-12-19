package com.yanxiu.workselect.dao;

import com.yanxiu.workselect.api.model.Stage2;

public interface Stage2Mapper {
    int deleteByPrimaryKey(Long stageId);

    int insert(Stage2 record);

    int insertSelective(Stage2 record);

    Stage2 selectByPrimaryKey(Long stageId);

    int updateByPrimaryKeySelective(Stage2 record);

    int updateByPrimaryKey(Stage2 record);
}