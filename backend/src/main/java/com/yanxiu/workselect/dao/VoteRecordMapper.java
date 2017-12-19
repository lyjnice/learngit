package com.yanxiu.workselect.dao;

import com.yanxiu.workselect.api.model.VoteRecord;

public interface VoteRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VoteRecord voteRecord);

    int insertSelective(VoteRecord voteRecord);

    VoteRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VoteRecord voteRecord);

    int updateByPrimaryKey(VoteRecord voteRecord);
}