package com.yanxiu.demo.dao;

import com.yanxiu.demo.model.User;
import com.yanxiu.util.database.MysqlBaseDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by mashiwei on 2016/8/22.
 */
@Repository("userDao")
public class UserDao extends MysqlBaseDAO {
    private static final Log LOG = LogFactory.getLog(UserDao.class);

    @Resource(name = "demoTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String TABLE_NAME = "user";

    public User createUser(final User user) {
        final String sql = "insert into " + TABLE_NAME + " (fid,author,author_id,subject,is_group,xueke,xueduan," +
                "invisible,area_sheng,area_shi,area_quxian,school,forum_type,attachment) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, user.getUid());
                ps.setString(2, user.getPassport());
                ps.setString(3, user.getPassword());
                ps.setByte(4, user.getActiFlag());
                ps.setString(5, user.getPersonalId());
                ps.setString(6, user.getEmail());
                ps.setString(7, user.getMobile());
                return ps;
            }
        }, keyHolder);

        user.setUid(keyHolder.getKey().longValue());
        return user;
    }

    @Cacheable(value = "default", key = "'userDao_'+#uid")
    public User queryUser(Long uid) {
        String sql = String.format("select * from %s where uid = ?", TABLE_NAME);
        return queryUnique(sql, new BeanPropertyRowMapper<User>(User.class), uid);
    }

    @CacheEvict(value = "default", key = "'userDao_'+#user.getUid()")
    public int updateUser(User user) {
        String sql = String.format("update user set ");
        if(user.getPassport() != null) {
            sql += "passport = \"" + user.getPassport() +"\", ";
        }
        if(user.getPassword() != null) {
            sql += "password = \"" + user.getPassword() +"\", ";
        }
        if(user.getActiFlag() != null) {
            sql += "acti_flag = " + user.getActiFlag() +", ";
        }
        if(user.getPersonalId() != null) {
            sql += "personal_id = \"" + user.getPersonalId() +"\", ";
        }
        if(user.getEmail() != null) {
            sql += "email = \"" + user.getEmail() +"\", ";
        }
        if(user.getMobile() != null) {
            sql += "mobile = \"" + user.getMobile() +"\", ";
        }
        sql += "uid=uid where uid = " + user.getUid();
        return jdbcTemplate.update(sql);
    }
}
