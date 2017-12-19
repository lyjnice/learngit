package com.yanxiu.area.dao;

import com.yanxiu.util.database.MysqlBaseDAO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

// Created by lmz on 2016/11/11.
@Repository("areaDao")
public class AreaDao extends MysqlBaseDAO {
    @Resource(name = "areaTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String TABLE_NAME = "Area";

    @Cacheable(value = "default", key = "'areaDao_'+#areaId")
    public String queryFullName(Long areaId) {
        String sql = String.format("SELECT seqName FROM %s WHERE areaId = ?", TABLE_NAME);
        Map<String, Object> map = queryUnique(sql, areaId);
        if (map != null && map.containsKey("seqName"))
            return map.get("seqName").toString();
        else
            return areaId.toString();
    }
}
