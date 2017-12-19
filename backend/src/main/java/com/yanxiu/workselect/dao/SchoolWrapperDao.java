package com.yanxiu.workselect.dao;

import com.yanxiu.util.database.MysqlBaseDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/* Created by lxx on 2016/9/18. */
@Repository("schoolWrapperDao")
public class SchoolWrapperDao extends MysqlBaseDAO {
    @Resource(name = "schoolTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> querySchool(Long quxiancode){
        String sql ="select name,id from user.school where regionId=? and status=?";
        return queryForList(sql,quxiancode,1);
    }
}
