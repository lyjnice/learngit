package com.yanxiu.workselect.dao;

import com.yanxiu.util.database.MysqlBaseDAO;
import com.yanxiu.workselect.api.model.Project;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("projectWrapperDao")
public class ProjectWrapperDao extends MysqlBaseDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProjectWrapperDao.class);
    @Resource(name = "yanxiuTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Project> queryProjects(String ids, Long uid) {
        List<Project> projects = new ArrayList<>();
        if (StringUtils.isBlank(ids))
            return projects;
        String sql = " SELECT p.name, p.id FROM xiaoben.project p, xiaoben.project_user pu WHERE p.id = pu.projectid AND p.id IN ("
                + ids + ") AND pu.userid = ?";
        try {
            List<Map<String, Object>> list = queryForList(sql, uid);
            for (Map<String, Object> kv : list) {
                Project project = new Project();
                project.setId((Long) kv.get("id"));
                project.setName((String) kv.get("name"));
                projects.add(project);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return projects;
    }
    public List<Project> queryProjects(String ids) {
        List<Project> projects = new ArrayList<>();
        if (StringUtils.isBlank(ids))
            return projects;
        String sql = " SELECT name,id FROM xiaoben.project WHERE id IN (" + ids +") ";
        try {
            List<Map<String, Object>> list = queryForList(sql);
            logger.debug(list.size() + sql + ids);
            for (Map<String, Object> kv : list) {
                Project project = new Project();
                project.setId((Long) kv.get("id"));
                project.setName((String) kv.get("name"));
                projects.add(project);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return projects;
    }
    public long contains(String ids, Long uid) {
        try {
            String sql = " SELECT COUNT(*) FROM xiaoben.project_user WHERE projectid IN (" + ids + ") AND userid = ? ";
            logger.debug(uid + sql + ids);
            return queryForCount(sql, uid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }
}
