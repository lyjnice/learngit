package com.yanxiu.passport.dao;

import com.yanxiu.util.database.MysqlBaseDAO;
import com.yanxiu.workselect.api.commons.vo.passport.PPUser;
import com.yanxiu.workselect.api.commons.vo.passport.PPUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

// by Administrator on 2016/8/26.

@SuppressWarnings({"SpringJavaAutowiringInspection", "unused"})
@Repository("ppUserWrapperDao")
public class PPUserWrapperDao extends MysqlBaseDAO {
    private static final Logger LOG = LoggerFactory.getLogger(PPUserWrapperDao.class);

    @Autowired
    public PPUserWrapperDao(PPUserMapper ppUserMapper) {
        this.ppUserMapper = ppUserMapper;
    }

    @Resource(name = "passportTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final PPUserMapper ppUserMapper;

    @Cacheable(value = "workselect", key = "'workselect_PPUser_' + #uid")
    public PPUser queryPPUser(Long uid) {
        return ppUserMapper.selectByPrimaryKey(uid);
    }

    // @Cacheable(value = "workselect", key = "'workselect_PPUserInfo_' + #uid")
    public PPUserInfo queryPPUserInfo(Long uid) {
        String sql = "select * from pp_user_info where UID = ?";
        return queryUnique(sql, new BeanPropertyRowMapper<>(PPUserInfo.class), uid);
    }
    public List<PPUser> getPPUser(List<String> passport){
        return ppUserMapper.selectByPassport(passport);
    }
}