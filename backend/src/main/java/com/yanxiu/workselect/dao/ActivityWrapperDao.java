package com.yanxiu.workselect.dao;

import com.yanxiu.resource.service.ResourceSearchClient;
import com.yanxiu.util.database.MysqlBaseDAO;
import com.yanxiu.workselect.api.commons.enums.ResultCodeEnum;
import com.yanxiu.workselect.api.commons.enums.WorkselectConstant;
import com.yanxiu.workselect.api.commons.enums.WorkselectEnum;
import com.yanxiu.workselect.api.commons.exception.WorkselectRuntimeException;
import com.yanxiu.workselect.api.commons.vo.Pagination;
import com.yanxiu.workselect.api.commons.vo.resource.ResourceData;
import com.yanxiu.workselect.api.commons.vo.resource.ResourceResult;
import com.yanxiu.workselect.api.dto.ActivityView;
import com.yanxiu.workselect.api.model.Navigation;
import com.yanxiu.workselect.api.model.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 *  by mashiwei on 2016/8/23.
 */
@SuppressWarnings({"SpringJavaAutowiringInspection", "SqlDialectInspection", "SqlNoDataSourceInspection", "SpringAutowiredFieldsWarningInspection"})
@Repository("activityWrapperDao")
public class ActivityWrapperDao extends MysqlBaseDAO {
    private static final Logger LOG = LoggerFactory.getLogger(ActivityWrapperDao.class);

    @Resource(name = "workselectTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ResourceSearchClient resourceSearchClient;

    private static final String TABLE_NAME = "activity";
    private static final String TABLE_USER = "participant";

    public Long createActivity(ActivityView activity) {
        activityMapper.insertSelective(activity);
        return activity.getActivityId();
    }

    // @Cacheable(value = "workselect", key = "'workselect_activity_' + #aid")
    public ActivityView queryActivity(Long aid) {
        ActivityView activityView = activityMapper.selectByPrimaryKey(aid);
        if (activityView == null) {
            throw new WorkselectRuntimeException(ResultCodeEnum.ACTIVITY_ID_WRONG, "活动不存在");
        }

        Long resVideoId = activityView.getVideoId();
        Long resPicId =activityView.getPictureId();
        List<Long> resIds =new ArrayList<>();
        resIds.add(resVideoId);
        resIds.add(resPicId);
        ResourceResult resourceResult = resourceSearchClient.getResourceListByResIds(resIds);
        if(resourceResult.getCode().equals(ResultCodeEnum.RESULT_SUCCESS.resultCode)) {
            for(ResourceData resourceData : resourceResult.getData()) {
                if(resourceData.getResId().equals(resVideoId) ){
                    activityView.setPreviewUrl(resourceData.getResPreviewUrl());
                    activityView.setResType(resourceData.getResType());
                    activityView.setTransCode(resourceData.getTransCode());
                }else if(resourceData.getResId().equals(resPicId) ){
                    activityView.setPictureUrl(resourceData.getResPreviewUrl());
                }
            }
        } else {
            throw new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "获取资源错误");
        }
        return activityView;
    }

    //@CacheEvict(value = "workselect", key = "'workselect_activity_' + #activity.getActivityId()")
    public int updateActivity(ActivityView activity) {
        return activityMapper.updateByPrimaryKeySelective(activity);
    }

    @SuppressWarnings("UnusedReturnValue")
    public int updateActivityStage(Long aid, Long stage_id, int stageNum) {
        if (aid == null || stage_id == null)
            return ResultCodeEnum.PARAM_ERROR.resultCode;
        ActivityView activity = new ActivityView();
        activity.setActivityId(aid);
        if(stageNum == ResultCodeEnum.STAGE1.resultCode) {
            activity.setStage1(stage_id);
        } else if(stageNum == ResultCodeEnum.STAGE2.resultCode) {
            activity.setStage2(stage_id);
        } else if(stageNum == ResultCodeEnum.STAGE3.resultCode) {
            activity.setStage3(stage_id);
        } else if(stageNum == ResultCodeEnum.STAGE4.resultCode) {
            activity.setStage4(stage_id);
        } else if(stageNum == ResultCodeEnum.STAGE5.resultCode) {
            activity.setStage5(stage_id);
        } else if(stageNum == ResultCodeEnum.STAGE6.resultCode) {
            activity.setStage6(stage_id);
        } else {
            return WorkselectEnum.STAGE_OTHER_ERROR;
        }
        return activityMapper.updateByPrimaryKeySelective(activity);
    }

    @SuppressWarnings("SameParameterValue")
    public Pagination<ActivityView> queryActivityPagination(int pageSize, int offset, String sortField, String order, Integer version) {
        if(StringUtils.isBlank(sortField)) {
            sortField = "start_time";
        }
        if(StringUtils.isBlank(order)) {
            order = "DESC";
        }
        String sortSql = " ORDER BY " + sortField + " " + order;
        String where = "";
        if (version != null && version >= 0) {
            where = " WHERE version = " + version;
        }
        String sql = "SELECT * FROM " + TABLE_NAME + where + sortSql;
        return queryPagination(sql, offset, pageSize, null, new BeanPropertyRowMapper<>(ActivityView.class));
    }

    public long queryActivityNum() {
        String sql = "select count(*) from " + TABLE_NAME;
        return queryForCount(sql);
    }

    @CacheEvict(value = "workselect", key = "'workselect_activity_' + #aid")
    public void updworkNumInActivity(Long aid, int status){
        String sql = "";
        if(status==WorkselectConstant.WORK_NUM_ADD){
            sql = "UPDATE " + TABLE_NAME + " SET workNum = workNum + 1 WHERE activity_id= "+aid;
        }else if(status==WorkselectConstant.WORK_NUM_SUB){
            sql = "UPDATE " + TABLE_NAME + " SET workNum = workNum - 1 WHERE activity_id= "+aid;
        }
        jdbcTemplate.execute(sql);
    }
    @CacheEvict(value = "workselect", key = "'workselect_activity_' + #aid")
    public int updJoinNumInActivity(Long aid){
        String sql = "UPDATE " + TABLE_NAME + " SET joinNum = joinNum + 1 WHERE activity_id= "+aid;
        jdbcTemplate.execute(sql);
        return 0;
    }
    public void updWorkNumInPartic(Long aid, Long uid, int status){
        String sql = "";
        if(status==WorkselectConstant.WORK_NUM_ADD){
            sql = "UPDATE " + TABLE_USER + " SET work_num = work_num + 1 WHERE activity_id= "+aid +" and user_id ="+uid;
        }else if(status==WorkselectConstant.WORK_NUM_SUB){
            sql = "UPDATE " + TABLE_USER + " SET work_num = work_num - 1 WHERE activity_id= "+aid+" and user_id ="+uid;
        }
        jdbcTemplate.execute(sql);
    }

    public Map<String, Object> activityStageInfo(Long aid) {
        String sql = "SELECT " +
                "stage1.stage_id as stage1_id, stage1.stage_name as stage1_name, stage1.start_time as stage1_stime, stage1.end_time as stage1_etime," +
                "stage2.stage_id as stage2_id, stage2.stage_name as stage2_name, stage2.start_time as stage2_stime, stage2.end_time as stage2_etime," +
                "stage3.stage_id as stage3_id, stage3.stage_name as stage3_name, stage3.start_time as stage3_stime, stage3.end_time as stage3_etime," +
                "stage4.stage_id as stage4_id, stage4.stage_name as stage4_name, stage4.start_time as stage4_stime, stage4.end_time as stage4_etime," +
                "stage5.stage_id as stage5_id, stage5.stage_name as stage5_name, stage5.start_time as stage5_stime, stage5.end_time as stage5_etime," +
                "stage6.stage_id as stage6_id, stage6.stage_name as stage6_name, stage6.start_time as stage6_stime, stage6.end_time as stage6_etime " +
                "from activity a,stage1,stage2,stage3,stage4,stage5,stage6 where " +
                "a.activity_id = " + aid + " and a.stage1 = stage1.stage_id and a.stage2 = stage2.stage_id and " +
                "a.stage3 = stage3.stage_id and a.stage4 = stage4.stage_id and a.stage5 = stage5.stage_id AND " +
                "a.stage6 = stage6.stage_id;";
        return queryUnique(sql);
    }

    public Long getVoteNumByIP(String IP, String startDate, String endDate) {
        String sql = "select count(*) from vote_record where voter_iP = '" + IP +
                "' and TO_DAYS(vote_time) >= " + "TO_DAYS('"+startDate+"') and " +
                "TO_DAYS(vote_time) <= TO_DAYS('"+endDate+"')";
        LOG.info("getVoteNumByIP:sql="+sql);
        return  queryForCount(sql);

    }

    public List<Map<String, Object>> getVoteRankByPeriod(Long aid, String startTime, String endTime, int  num) {
        String sql = String.format("SELECT work_id, COUNT(*) AS count FROM vote_record WHERE activity_id = %s AND " +
                "vote_time >= '%s' AND vote_time < '%s' GROUP BY work_id ORDER BY count DESC LIMIT %s",
                aid, startTime, endTime, num);
        LOG.info("getVoteRankByPeriod:sql="+sql);
        return queryForList(sql);
    }
    public List<Navigation> getNavigationList(Long aid) {
        String sql = "SELECT * FROM navigation WHERE activity_id = ? ORDER BY id";
        return queryForList(sql, new BeanPropertyRowMapper<>(Navigation.class), aid);
    }
    public List<Template> getTemplates(Long aid, int type) {
        String sql = "SELECT * FROM template WHERE activity_id = ? AND type = ?";
        return queryForList(sql, new BeanPropertyRowMapper<>(Template.class), aid, type);
    }
}
