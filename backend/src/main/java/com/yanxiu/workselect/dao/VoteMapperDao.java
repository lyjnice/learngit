package com.yanxiu.workselect.dao;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.yanxiu.util.database.MysqlBaseDAO;
import com.yanxiu.workselect.api.model.VoteRecord;

@Repository("voteWrapperDao")
public class VoteMapperDao extends MysqlBaseDAO {
	@Autowired
	private VoteRecordMapper voteRecordMapper;

    private static final String TABLE_NAME = "vote_record";
	
    @Resource(name = "workselectTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public int createVote(VoteRecord voteRecord) {
        return voteRecordMapper.insertSelective(voteRecord);
    }
    public Long queryVoteByIP(String ip,Long aid) {
   	 String sql = "select count(*) from " + TABLE_NAME + " where activity_id="+aid+" and voter_iP = '"+ip+"'";
        return queryForCount(sql);
   }
    public Long queryVoteByIP(String ip,String date,Long aid) {
    	 String sql = "select count(*) from " + TABLE_NAME + " where activity_id="+aid+" and voter_iP = '"+ip+"'"
    			 +" and vote_time like '"+date+"%'"; 
         return queryForCount(sql);
    }
    public Long queryVoteByuserId(Long user_id,Long aid) { 
   	 String sql = "select count(*) from " + TABLE_NAME + " where activity_id="+aid+" and  user_id = "+user_id ;	 
      return queryForCount(sql);
   }
    public Long queryVoteByuserId(Long user_id,String date,Long aid) { 
      	 String sql = "select count(*) from " + TABLE_NAME + " where activity_id="+aid+" and user_id = "+user_id 
      			+" and vote_time like '"+date+"%'"; 
      	 return queryForCount(sql);
      }
}
