package com.yanxiu.workselect.dao;

import com.yanxiu.workselect.api.model.Participant;
import com.yanxiu.workselect.api.model.ParticipantKey;

public interface ParticipantMapper {
    int deleteByPrimaryKey(ParticipantKey key);

    int insert(Participant record);

    int insertSelective(Participant record);

    Participant selectByPrimaryKey(ParticipantKey key);

    int updateByPrimaryKeySelective(Participant record);

    int updateByPrimaryKey(Participant record);
}