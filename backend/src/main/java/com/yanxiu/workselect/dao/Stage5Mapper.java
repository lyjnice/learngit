package com.yanxiu.workselect.dao;

import com.yanxiu.workselect.api.model.Stage5;

public interface Stage5Mapper {
    int deleteByPrimaryKey(Long stageId);

    int insert(Stage5 record);

    int insertSelective(Stage5 record);

    Stage5 selectByPrimaryKey(Long stageId);

    int updateByPrimaryKeySelective(Stage5 record);

    int updateByPrimaryKey(Stage5 record);
}