package com.yanxiu.workselect.dao;

import com.yanxiu.util.database.MysqlBaseDAO;
import com.yanxiu.workselect.api.commons.enums.ResultCodeEnum;
import com.yanxiu.workselect.api.commons.enums.WorkselectConstant;
import com.yanxiu.workselect.api.commons.enums.WorkselectEnum;
import com.yanxiu.workselect.api.commons.vo.Pagination;
import com.yanxiu.workselect.api.dto.UserView;
import com.yanxiu.workselect.api.dto.WorksView;
import com.yanxiu.workselect.api.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * by mashiwei on 2016/8/25.
 */
@SuppressWarnings("unused")
@Repository("userWorksWrapperDao")
public class UserWorksWrapperDao extends MysqlBaseDAO {
    private static final Logger LOG = LoggerFactory.getLogger(UserWorksWrapperDao.class);

    @Resource(name = "workselectTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String WORK_TABLE = "works";
    private static final String USER_TABLE = "participant";
    private static final String AWARDS_TABLE = "awards";
    private static final String TYPES_TABLE = "resource_type";
    private static final String CATS_TABLE = "category";
    private static final String ACTIVITY_TABLE = "activity";
    private static final String STAGE3_TABLE = "stage3";
    private static final String SCORES_TABLE = "scores";

    private String areaSQL(Long area, String column) {
        if (area != null && area != 0) {
            if (area % 10000 == 0)
                return String.format(" AND FLOOR(%s/10000)*10000 = " + area, column);
            else if (area % 100 == 0)
                return String.format(" AND FLOOR(%s/100)*100 = " + area, column);
            else
                return " AND " + column + " = " + area;
        } else {
            return "";
        }
    }

    public Pagination<WorksView> queryActivityWorkPage(
            Long aid, int pageSize, int offset, String sortField, String order,
            Long xueke, Long xueduan, Long area, Integer check, Integer work_type,
            Long grade, Long volume, Long version, Long chap1, Long chap2, Long chap3,
            Long category, Long projectID, String name, Boolean isPackage) {
        if(StringUtils.isBlank(sortField)) {
            sortField = "create_time";
        }
        sortField = "w." + sortField;

        if(StringUtils.isBlank(order)) {
            order = "DESC";
        }

        String areaSql = areaSQL(area, "w.area_id");

        String eduSql ="";
        if(xueke != null && xueke!=-1){
            eduSql = " AND w.xueke_id = " + xueke ;
        }
        if(xueduan != null && xueduan!=-1){
            eduSql += " AND w.xueduan_id = " + xueduan;
        }
        if (grade != null && grade > 0)
            eduSql += " AND w.grade_id = " + grade;
        if (volume != null && volume > 0)
            eduSql += " AND w.volume_id = " + volume;
        if (version != null && version > 0)
            eduSql += " AND w.version_id = " + version;
        if (chap1 != null && chap1 > 0)
            eduSql += " AND w.chapter_id1 = " + chap1;
        if (chap2 != null && chap2 > 0)
            eduSql += " AND w.chapter_id2 = " + chap2;
        if (chap3 != null && chap3 > 0)
            eduSql += " AND w.chapter_id3 = " + chap3;

        String checkSql = "";
        if (check != null)
            checkSql = " AND w.check_status = " + check;

        String scoreSql = "";
        String awardSql = "";

        String sortSql = " ORDER BY " + sortField + " " + order;

        String categorySql = "";
        if(category != null && category > 0){
            categorySql = " AND w.category = " + category;
        }
        String nameSQL = "";
        if(StringUtils.isNoneBlank(name)){
            nameSQL = " AND w.work_name LIKE '%" + name + "%'";
        }
        String packageSql = "";
        if(isPackage) {
            packageSql = " AND w.resource_type = " + WorkselectConstant.PACKAGE_TYPE;
        }
        Integer candidate_type = WorkselectEnum.CANDIDATE_ALL;
        if (work_type == null)
            work_type = WorkselectConstant.WORK_TYPE_ALL;
        Integer top = 0;
        if (work_type == WorkselectConstant.WORK_TYPE_IN) {
            String sql = "SELECT s.* FROM " + STAGE3_TABLE + " s , " + ACTIVITY_TABLE
                    + " a WHERE s.stage_id = a.stage3 AND s.activity_id = a.activity_id AND a.activity_id = ?";
            Stage3 stage3 = queryUnique(sql, new BeanPropertyRowMapper<>(Stage3.class), aid);
            if (stage3 == null) {
                LOG.error("stage3 not set");
                return new Pagination<>();
            }
            candidate_type = stage3.getCandidate();
            if (candidate_type == WorkselectEnum.CANDIDATE_RANK) {
                sortSql = " ORDER BY w.final_score DESC, " + sortField + " " + order;
                top = stage3.getRanking();
            } else if (candidate_type == WorkselectEnum.CANDIDATE_VOTE) {
                scoreSql = " AND w.work_agree > " + stage3.getScore();
            }
        } else if (work_type == WorkselectConstant.WORK_TYPE_AWARDED) {
            /*
            String sql = "SELECT id FROM awards WHERE display = 1";
            List<Long> award_ids = queryForList(sql, new BeanPropertyRowMapper<>(Long.class));
            if (award_ids.size() > 0)
                awardSql = " AND w.final_rank IN (" + StringUtils.join(award_ids, ",") + ")";
            else
            */
            awardSql = " AND w.final_rank != 0";
        }

        String projectTable = "";
        String projectWhere = "";
        if (projectID != null && projectID > 0) {
            projectTable = USER_TABLE + " p, ";
            projectWhere = " p.user_id = w.author_id AND p.project_id = " + projectID + " AND p.activity_id = " + aid + " AND ";
        }
        String sql = "SELECT w.*, w.check_status AS `check` FROM " + projectTable + WORK_TABLE + " w WHERE " + projectWhere + " w.activity_id = " + aid
                + eduSql + checkSql + areaSql + scoreSql + awardSql + categorySql + packageSql + nameSQL + sortSql;
        LOG.debug("sql == "+sql);
        if (work_type == WorkselectConstant.WORK_TYPE_IN
                && candidate_type == WorkselectEnum.CANDIDATE_RANK
                && (offset + pageSize) > top) {
            if (offset > top)
                pageSize = 0;
            else
                pageSize = top - offset;
        }
        return queryPagination(sql, offset, pageSize,null,new BeanPropertyRowMapper<>(WorksView.class));
    }

    public  List<WorksView> queryGoodWork(Long aid, Long awardId) {
        String sql = "select  *,check_status as `check` from " + WORK_TABLE + " where activity_id ="+aid +" and final_rank = "+awardId;
        return queryForList(sql,new BeanPropertyRowMapper<>(WorksView.class));
    }

    public Pagination<WorksView> queryUserGoodPage(Long aid, String userName, int pageSize, int offset, String sortField, String order, boolean isPackage) {
        if(StringUtils.isBlank(sortField)) {
            sortField = "final_rank";
        }
        if(StringUtils.isBlank(order)) {
            order = "ASC";
        }
        String whereSQL = " AND w.final_rank != 0 ";
        if (isPackage)
            whereSQL += " AND w.resource_type = " + WorkselectConstant.PACKAGE_TYPE;
        String sortSql = " ORDER BY w." + sortField + " " + order;
        String sql = String.format("SELECT w.*, w.check_status AS `check` FROM %s w, %s p "
                        + " WHERE w.author_id = p.user_id AND p.user_name LIKE '%%%s%%' AND w.activity_id = %d "
                        + " AND w.check_status != %d " + whereSQL + sortSql,
                WORK_TABLE, USER_TABLE, userName, aid, WorkselectConstant.WORK_REMOVED);
        return queryPagination(sql, offset, pageSize, null, new BeanPropertyRowMapper<>(WorksView.class));
    }

    public String queryUserAward(Long aid, Long uid) {
        String sql = String.format(
                "SELECT a.* FROM %s a, %s w WHERE w.author_id = %d AND w.activity_id = %d AND w.final_rank = a.id ORDER BY w.final_rank ASC",
                AWARDS_TABLE, WORK_TABLE, uid, aid);
        Awards awards = queryFirst(sql, new BeanPropertyRowMapper<>(Awards.class));
        if (awards == null)
            return "";
        return awards.getAwardName();
    }

    public Pagination<WorksView> queryUserWorkPage(Long aid, Long uid, int pageSize, int offset, String sortField, String order, String where) {
        if(StringUtils.isBlank(sortField)) {
            sortField = "create_time";
        }
        if(StringUtils.isBlank(order)) {
            order = "DESC";
        }
        String whereSQL = StringUtils.isBlank(where) ? "" : " AND " + where;
        String sortSql = " ORDER BY " + sortField + " " + order;
        String sql = "SELECT *, check_status AS `check` FROM " + WORK_TABLE
                + " WHERE activity_id = " + aid + " AND author_id = " + uid + whereSQL
                + " AND check_status != " + WorkselectConstant.WORK_REMOVED + sortSql;
        return queryPagination(sql, offset, pageSize, null, new BeanPropertyRowMapper<>(WorksView.class));
    }

    public Pagination<WorksView> queryUserPackagePage(Long aid, Long uid, int pageSize, int offset, String sortField, String order) {
        return queryUserWorkPage(aid, uid, pageSize, offset, sortField, order, "resource_type = " + WorkselectConstant.PACKAGE_TYPE);
    }

    public Pagination<WorksView> queryUserGoodPackagePage(Long aid, Long uid, int pageSize, int offset, String sortField, String order) {
        return queryUserWorkPage(aid, uid, pageSize, offset, sortField, order, "final_rank != 0 AND resource_type = " + WorkselectConstant.PACKAGE_TYPE);
    }

    public Pagination<UserView> queryUsers(Long aid, int pageSize, int offset, Long course, Long grade, Long project, Long area, String name) {
        String courseSql = "";
        String gradeSql = "";
        String pSQL = "";
        String nameSQL = "";
        if (course != null && course > 0) {
            courseSql = " and xueke_id = " + course;
        }
        if (grade != null && grade > 0) {
            gradeSql = " and xueduan_id = " + grade;
        }
        if (project != null && project > 0) {
            pSQL = " AND project_id = " + project;
        }
        if (StringUtils.isNoneBlank(name)) {
            nameSQL = " AND user_name LIKE '%" + name + "%'";
        }
        String areaSQL = areaSQL(area, "area_id");
        // add by lxx ,query 已报名
        String sql = "select * from " + USER_TABLE + " where activity_id = " + aid +" and role = "
                + ResultCodeEnum.USER_ROLE_PARTICIPANT.resultCode + courseSql + gradeSql + pSQL + areaSQL + nameSQL;

        return queryPagination(sql, offset, pageSize, null, new BeanPropertyRowMapper<>(UserView.class));
    }

    // @Cacheable(value = "workselect", key = "'workselect_work_' + #workId")
    public WorksView queryWork(Long workId) {
        String sql = "SELECT *, check_status AS `check` FROM " + WORK_TABLE + " WHERE check_status != "
                + WorkselectConstant.WORK_REMOVED + " AND work_id = ?";
        return queryUnique(sql, new BeanPropertyRowMapper<>(WorksView.class), workId);
    }

    public List<WorksView> queryPackageWorks(Long packageId) {
        String sql = "SELECT *, check_status AS `check` FROM " + WORK_TABLE + " WHERE check_status != "
                + WorkselectConstant.WORK_REMOVED + " AND category = ?";
        return queryForList(sql, new BeanPropertyRowMapper<>(WorksView.class), packageId);
    }

    public List<UserView> queryUsers(String name, Long aid) {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE user_name = ?";
        if (aid != null && aid > 0)
            sql += " AND activity_id = " + aid;
        return queryForList(sql, new BeanPropertyRowMapper<>(UserView.class), name);
    }

    public UserView queryUser(Long aid, Long uid) {
        String sql = "select * from " + USER_TABLE + " where user_id = ? and activity_id = ?";
        return queryUnique(sql, new BeanPropertyRowMapper<>(UserView.class), uid, aid);
    }

    public int deleteResource(Long workId, Long aid, Long uid) {
        String sql ="UPDATE " + WORK_TABLE + " SET check_status = " + WorkselectConstant.WORK_REMOVED + " WHERE work_id=? AND activity_id=? AND author_id = ?";
        return update(sql, workId, aid, uid);
    }

    public List<ResourceType> queryResourceType(Long aid, Long stageId) {
        String sql="SELECT * FROM " + TYPES_TABLE + " WHERE activity_id=? AND stage_id=?";
        return queryForList(sql, new BeanPropertyRowMapper<>(ResourceType.class), aid,stageId);
    }

    public List<Category> queryCategory(Long aid, Long stageId) {
        String sql="SELECT * FROM " + CATS_TABLE + " WHERE activity_id=? AND stage_id=?";
        return queryForList(sql, new BeanPropertyRowMapper<>(Category.class), aid,stageId);
    }

    public long queryWorkNum(Long aid) {
        String sql ="SELECT COUNT(*) FROM " + WORK_TABLE + " WHERE activity_id = ? AND check_status != ?";
        return queryForCount(sql, aid, WorkselectConstant.WORK_REMOVED);
    }
    public long queryAwarded(Long award_id) {
        String sql ="SELECT COUNT(*) FROM " + WORK_TABLE + " WHERE final_rank = ? AND check_status != ?";
        return queryForCount(sql, award_id, WorkselectConstant.WORK_REMOVED);
    }

    public List<Awards> queryAwards(Long aid,Long stageId){
        String sql="select * from "+AWARDS_TABLE +" where activity_id=? and stage_id=?";
        return queryForList(sql, new BeanPropertyRowMapper<>(Awards.class), aid,stageId);
    }

    public Awards queryAward(Integer awardId){
        String sql="select * from "+AWARDS_TABLE +" where id=?";
        return queryUnique(sql, new BeanPropertyRowMapper<>(Awards.class), awardId);
    }

    public Scores queryScore(Long workId,Long aid,Long expertId){
        String sql="select * from "+SCORES_TABLE+" where activity_id=? and expert_id=? and work_id=?";
        return queryUnique(sql, new BeanPropertyRowMapper<>(Scores.class), aid,expertId,workId);
    }
    public Scores queryAvgScore(Long workId,Long aid){
        String sql="select work_id,activity_id,avg(expert_score) as expert_score  from "+SCORES_TABLE+" where activity_id=?  and work_id=?";
        return queryUnique(sql, new BeanPropertyRowMapper<>(Scores.class), aid,workId);
    }
    public List<Scores> queryScores(Long aid) {
        String sql = "SELECT * FROM " + SCORES_TABLE + " WHERE activity_id = ? ORDER BY expert_id, expert_score";
        return queryForList(sql, new BeanPropertyRowMapper<>(Scores.class), aid);
    }
}
