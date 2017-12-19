package com.yanxiu.workselect.service;

import com.google.common.collect.Maps;
import com.yanxiu.resource.service.ResourceSearchClient;
import com.yanxiu.util.cache.RedisCache;
import com.yanxiu.workselect.api.commons.enums.Result;
import com.yanxiu.workselect.api.commons.enums.ResultCodeEnum;
import com.yanxiu.workselect.api.commons.enums.WorkselectEnum;
import com.yanxiu.workselect.api.commons.vo.Pagination;
import com.yanxiu.workselect.api.commons.vo.resource.ResourceData;
import com.yanxiu.workselect.api.dto.ActivityView;
import com.yanxiu.workselect.api.dto.WorksView;
import com.yanxiu.workselect.api.model.*;
import com.yanxiu.workselect.api.service.ActivityStageService;
import com.yanxiu.workselect.core.util.VMmapper;
import com.yanxiu.workselect.dao.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * created by mashiwei on 2016/8/23.
 */
@SuppressWarnings({"SpringJavaAutowiringInspection", "SpringAutowiredFieldsWarningInspection", "unused", "unchecked"})
@Service("activityBackendService")
public class ActivityStageServiceImpl implements ActivityStageService {
    private static final Logger LOG = LoggerFactory.getLogger(ActivityStageServiceImpl.class);

    @Autowired
    private ActivityWrapperDao activityWrapperDao;

    @Autowired
    private Stage1Mapper stage1Mapper;

    @Autowired
    private Stage2Mapper stage2Mapper;

    @Autowired
    private Stage3Mapper stage3Mapper;

    @Autowired
    private Stage4Mapper stage4Mapper;

    @Autowired
    private Stage5Mapper stage5Mapper;

    @Autowired
    private Stage6Mapper stage6Mapper;

    @Autowired
    private ParticipantMapper participantMapper;

    @Autowired
    private VoteMapperDao voteMapperDao;
    @Autowired
    private UserWorksWrapperDao userWorksWrapperDao;

    @Autowired
    private ResourceSearchClient resourceSearchClient;

    @Autowired
    private SchoolWrapperDao schoolWrapperDao;
    @Autowired
    private ProjectWrapperDao projectWrapperDao;

    @Autowired
    private AwardsMapper awardsMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ResourceTypeMapper resourceTypeMapper;
    @Autowired
    private NavigationMapper navigationMapper;
    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private ScoresMapper scoresMapper;

    @Autowired
    private UserResourceServiceImpl userResourceService;

    @Resource(name = "redisService")
    private RedisCache redisCache;

    @Override
    public Result loadActivities() {
        return VMmapper.initActivityMap(this);
    }
    @Override
    public Long createActivity(ActivityView activity) {
        return activityWrapperDao.createActivity(activity);
    }

    @Override
    public Long setListEntity(ListEntity type) {
        ListEntityMapper mapper;
        if (type instanceof Category)
            mapper = categoryMapper;
        else if (type instanceof ResourceType)
            mapper = resourceTypeMapper;
        else
            return -2L;

        if (type.getId() != null && type.getId() > 0) {
            if (StringUtils.isBlank(type.getName())) {
                Stage2 stage2 = (Stage2) queryStage(type.getStageId(), ResultCodeEnum.STAGE2.resultCode);
                if (stage2.getStartTime().after(new Date())) {
                    return -1L;
                }
                return (long) mapper.deleteByPrimaryKey(type.getId());
            }
            return (long) mapper.updateByPrimaryKeySelective(type);
        }
        if (StringUtils.isNotBlank(type.getName()))
            mapper.insertSelective(type);
        return type.getId();
    }
    @Override
    public Long setAward(Awards award) {
        if (award.getId() != null && award.getId() > 0) {
            if (StringUtils.isBlank(award.getAwardName()) || award.getAwardCount() == 0) {
                long works = userWorksWrapperDao.queryAwarded(award.getId());
                LOG.info(award.getId() + " removing affected " + works);
                return works > 0 ? 0 : (long) awardsMapper.deleteByPrimaryKey(award.getId());
            }
            return (long) awardsMapper.updateByPrimaryKeySelective(award);
        } else if (StringUtils.isNotBlank(award.getAwardName())) {
            awardsMapper.insertSelective(award);
            return award.getId();
        }
        return -1L;
    }

    @Override
    public Long createStage(Stage stage, int stageNum) {
        if(stageNum == ResultCodeEnum.STAGE1.resultCode) {
            stage1Mapper.insertSelective((Stage1)stage);
        } else if(stageNum == ResultCodeEnum.STAGE2.resultCode) {
            stage2Mapper.insertSelective((Stage2)stage);
        } else if(stageNum == ResultCodeEnum.STAGE3.resultCode) {
            stage3Mapper.insertSelective((Stage3)stage);
        } else if(stageNum == ResultCodeEnum.STAGE4.resultCode) {
            stage4Mapper.insertSelective((Stage4)stage);
        } else if(stageNum == ResultCodeEnum.STAGE5.resultCode) {
            stage5Mapper.insertSelective((Stage5)stage);
        } else if(stageNum == ResultCodeEnum.STAGE6.resultCode) {
            stage6Mapper.insertSelective((Stage6)stage);
        } else {
            return (long) WorkselectEnum.STAGE_CREATE_NUM_ERROR;
        }
        return stage.getStageId();
    }

    @Override
    public int updateStage(Object stage, int stageNum) {
        if(stageNum == ResultCodeEnum.STAGE1.resultCode) {
            return stage1Mapper.updateByPrimaryKeySelective((Stage1)stage);
        } else if(stageNum == ResultCodeEnum.STAGE2.resultCode) {
            return stage2Mapper.updateByPrimaryKeySelective((Stage2)stage);
        } else if(stageNum == ResultCodeEnum.STAGE3.resultCode) {
            return stage3Mapper.updateByPrimaryKeySelective((Stage3)stage);
        } else if(stageNum == ResultCodeEnum.STAGE4.resultCode) {
            return stage4Mapper.updateByPrimaryKeySelective((Stage4)stage);
        } else if(stageNum == ResultCodeEnum.STAGE5.resultCode) {
            return stage5Mapper.updateByPrimaryKeySelective((Stage5)stage);
        } else if(stageNum == ResultCodeEnum.STAGE6.resultCode) {
            return stage6Mapper.updateByPrimaryKeySelective((Stage6)stage);
        } else {
            return WorkselectEnum.STAGE_CREATE_NUM_ERROR;
        }
    }

    @Override
    public Result setStage(Stage stage, int stageNum, Long aid, Long stage_id, String name, String start, String end) {
        Result result = new Result();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Boolean successful = true;
        Long code = 0L;
        try {
            stage.setActivityId(aid);
            if (StringUtils.isNotBlank(name))
                stage.setStageName(name);
            if (StringUtils.isNotBlank(start))
                stage.setStartTime(simpleDateFormat.parse(start));
            if (StringUtils.isNotBlank(end))
                stage.setEndTime(simpleDateFormat.parse(end));
            if (stage_id != null && stage_id > 0) {
                stage.setStageId(stage_id);
                code = (long) updateStage(stage, stageNum);
                if (code != 1) {
                    ResultCodeEnum.STAGE_UPDATE_ERROR.setResult(result);
                    successful = false;
                }
            } else {
                code = createStage(stage, stageNum);
                if (code <= 0) {
                    ResultCodeEnum.STAGE_CREATE_ERROR.setResult(result);
                    successful = false;
                } else {
                    stage_id = code;
                }
            }
        } catch (ParseException pe) {
            result = new Result(ResultCodeEnum.FORMAT_ERROR);
            successful = false;
        }
        result.setData(code);
        if (successful)
            activityWrapperDao.updateActivityStage(aid, stage_id, stageNum);
        return result;
    }

    @Override
    public List<Project> getProjects(String pids, Long uid) {
        return projectWrapperDao.queryProjects(pids, uid);
    }
    @Override
    public ActivityView queryActivity(Long aid) {
        ActivityView activityView = activityWrapperDao.queryActivity(aid);
        ResultCodeEnum codeEnum = curStage(activityView);
        activityView.setCurStage(codeEnum.resultCode);
        activityView.setCurStageName(codeEnum.getError().getTitle());
        List<Project> projects = projectWrapperDao.queryProjects(activityView.getProjectId());
        activityView.setProjects(projects);
        return activityView;
    }

    @Override
    public Pagination<ActivityView> queryActivityPagination(int pageSize, int offset, Integer version) {
        Pagination<ActivityView> activityViewPagination = activityWrapperDao.queryActivityPagination(pageSize, offset, null, null, version);
        for (ActivityView activityView : activityViewPagination.getElements()) {
            ResultCodeEnum codeEnum = curStage(activityView);
            activityView.setCurStage(codeEnum.resultCode);
            activityView.setCurStageName(codeEnum.getError().getTitle());
        }
        return activityViewPagination;
    }

    public int updateActivity(ActivityView activity) {
        return activityWrapperDao.updateActivity(activity);
    }

    @Override
    public long queryActivityNum() {
        return activityWrapperDao.queryActivityNum();
    }

    private ResultCodeEnum curStage(ActivityView activityView) {
        long timeLong = new Date().getTime();
        if(activityView.getStage6()!=0) {
            Stage6 stage6 = stage6Mapper.selectByPrimaryKey(activityView.getStage6());
            if(stage6.getEndTime() != null && stage6.getEndTime().getTime() <= timeLong) {
                return ResultCodeEnum.STAGE_FINISH;
            } else if(stage6.getStartTime() !=null && stage6.getStartTime().getTime() <= timeLong) {
                return ResultCodeEnum.STAGE6;
            }
        }
        if(activityView.getStage5() != 0 && validStageDate(ResultCodeEnum.STAGE5.resultCode, activityView.getStage5())) {
            return ResultCodeEnum.STAGE5;
        }
        if(activityView.getStage4() != 0 && validStageDate(ResultCodeEnum.STAGE4.resultCode, activityView.getStage4())) {
            return ResultCodeEnum.STAGE4;
        }
        if(activityView.getStage3() != 0 && validStageDate(ResultCodeEnum.STAGE3.resultCode, activityView.getStage3())) {
            return ResultCodeEnum.STAGE3;
        }
        if(activityView.getStage2() != 0 && validStageDate(ResultCodeEnum.STAGE2.resultCode, activityView.getStage2())) {
            return ResultCodeEnum.STAGE2;
        }
        if(activityView.getStage1() != 0) {
            Stage1 stage1 = stage1Mapper.selectByPrimaryKey(activityView.getStage1());
            if(stage1.getStartTime().getTime() > timeLong) {
                return ResultCodeEnum.STAGE_NOT_START;
            } else if(stage1.getStartTime().getTime() <= timeLong) {
                return ResultCodeEnum.STAGE1;
            }
        }
        return ResultCodeEnum.STAGE_FIND_ERROR;
    }

    @Override
    public Stage queryStage(Long sid, int stageType) {
        if(stageType == ResultCodeEnum.STAGE1.resultCode) {
            return stage1Mapper.selectByPrimaryKey(sid);
        } else if(stageType == ResultCodeEnum.STAGE2.resultCode) {
            Stage stage = stage2Mapper.selectByPrimaryKey(sid);
            if (stage == null)
                return null;
            List<ResourceType> types = userResourceService.queryDetailTypes(stage.getActivityId());
            List<Category> categories = userResourceService.queryCategories(stage.getActivityId());
            ((Stage2) stage).setTypes(types);
            ((Stage2) stage).setCategories(categories);
            return stage;
        } else if(stageType == ResultCodeEnum.STAGE3.resultCode) {
            Stage stage = stage3Mapper.selectByPrimaryKey(sid);
            ((Stage3) stage).setVoteLogin(!((Stage3) stage).getVoteAnonymous());
            return stage;
        } else if(stageType == ResultCodeEnum.STAGE4.resultCode) {
            return stage4Mapper.selectByPrimaryKey(sid);
        } else if(stageType == ResultCodeEnum.STAGE5.resultCode) {
            Stage stage = stage5Mapper.selectByPrimaryKey(sid);
            List<Awards> awardsList = userResourceService.queryAwards(stage.getActivityId());
            ((Stage5) stage).setAwardsList(awardsList);
            return stage;
        } else if(stageType == ResultCodeEnum.STAGE6.resultCode) {
            return stage6Mapper.selectByPrimaryKey(sid);
        } else {
            LOG.error("queryStage: wrong stageType. stageType="+stageType);
            return null;
        }
    }

    @Override
    public Stage4 queryStage4(Long aid) {
        Activity activity = activityWrapperDao.queryActivity(aid);
        boolean isValid;
        if (activity == null) {
            return null;
        }
        return stage4Mapper.selectByPrimaryKey(activity.getStage4());
    }

    @Override
    public Boolean validStageDate(int stageNum, Long stageId) {
        Date now = new Date();
        Stage stage = queryStage(stageId, stageNum);
        return now.after(stage.getStartTime()) && (stage.getEndTime() == null || stage.getEndTime().getTime() == 0 ||
                now.before(stage.getEndTime()));
    }

    @Override
    public Map<Integer, Object> getVotePeriodInfo(Long aid, Long stageId, int top, int interval, int count) {
        Map<Integer, Object> result = Maps.newHashMap();
        String key = String.format("workselect_voteRank_%s_%s_%s_%s", aid, top, interval, count);
        if(redisCache.get(key) != null) {
            result = (Map<Integer, Object>) redisCache.get(key).get();
            return result;
        }
        // 一天的毫秒数
        int oneDay = 24 * 60 * 60 * 1000;
        Stage3 stage3 = stage3Mapper.selectByPrimaryKey(stageId);
        Date createDate = stage3.getStartTime();
        Date now = new Date();
        if (now.after(stage3.getEndTime())) {
            now = stage3.getEndTime();
        }
        // 投票开始时间到现在的天数
        int days = (int)((now.getTime() - createDate.getTime())/oneDay);
        long timeInterval = interval * oneDay;
        Date beginDate = createDate;
        Date endDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int intervalCount = 0;
        while (days > 0) {
            intervalCount += 1;
            if(interval > days) {
                days = 0;
                endDate = now;
            } else {
                days = days - interval;
                endDate = new Date(beginDate.getTime() + timeInterval);
            }
            String startStr = sdf.format(beginDate);
            String endStr = sdf.format(endDate);
            List<Map<String, Object>> list = activityWrapperDao.getVoteRankByPeriod(aid, startStr, endStr, top);
            for(Map<String, Object> mapItem : list) {
                Long workId = (Long)mapItem.get("work_id");
                WorksView work = userWorksWrapperDao.queryWork(workId);
                if (work == null) {
                    LOG.warn("invalid work: " + workId);
                    continue;
                }
                userResourceService.setResourceIDs(work);
                userResourceService.setParticipant(work);
                mapItem.put("work_info", work);
                Participant participant = userResourceService.queryUser(work.getActivityId(), work.getAuthorId());
                mapItem.put("participant", participant);
            }
            result.put(intervalCount, list);
            if(intervalCount >= count) {
                break;
            }
            beginDate =  new Date(beginDate.getTime() + timeInterval);
        }
        redisCache.putWithTime(key, result, 60 * 60);  // 1小時
        return result;
    }

    @Override
    public int validStage(Object stage, int stageNum) {
        if(stageNum == ResultCodeEnum.STAGE1.resultCode) {
            return validStage1((Stage1) stage);
        } else if(stageNum == ResultCodeEnum.STAGE2.resultCode) {
            return validStage2((Stage2) stage);
        } else {
            return ResultCodeEnum.STAGE_WRONG_NUM.resultCode;
        }
    }

    private int validStage1(Stage1 stage) {
        Stage1 stageLabel = stage1Mapper.selectByPrimaryKey(stage.getStageId());
        if (stageLabel == null
                || stageLabel.getUserName() && !stage.getUserName()
                || stageLabel.getUserMobile() && !stage.getUserMobile()
                || stageLabel.getUserEmail() && !stage.getUserEmail()
                || stageLabel.getUserArea() && !stage.getUserArea()
                || stageLabel.getUserSchool() && !stage.getUserSchool()
                || stageLabel.getUserXueke() && !stage.getUserXueke()
                || stageLabel.getUserXueduan() && !stage.getUserXueduan()
                || stageLabel.getUserAddress() && !stage.getUserAddress()) {
            return ResultCodeEnum.STAGE1_INVALID.resultCode;
        }
        return ResultCodeEnum.STAGE1_VALID.resultCode;
    }

    private int validStage2(Stage2 stage) {
        Stage2 stageLabel = stage2Mapper.selectByPrimaryKey(stage.getStageId());
        if(!stageLabel.getResourceType().contains("0")){
            if(!stageLabel.getResourceType().contains(stage.getResourceType())){
                return ResultCodeEnum.STAGE2_INVALID.resultCode;
            }
        }
        
      /*  后端不做校验 */
        if(stageLabel.getResourceNum() != 0){
            if(stageLabel.getResourceNum() < stage.getResourceNum()){
                return ResultCodeEnum.STAGE2_INVALID.resultCode;
            }
        }
        return ResultCodeEnum.STAGE2_VALID.resultCode;
    }

    private Result validStage3(Stage3 stage,VoteRecord record,int validNum) {
        Stage3 stageLabel = stage3Mapper.selectByPrimaryKey(stage.getStageId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String day = sdf.format(now);
        Long aid= record.getActivityId();
        boolean flagVisitor = stageLabel.getVoteVisitor();  //访客登录
        boolean flagAnonymous = stageLabel.getVoteAnonymous(); //是否允许匿名
        boolean flagParticipant = stageLabel.getVoteParticipant();//参与者
        if(ResultCodeEnum.USER_ROLE_VISITOR.resultCode==validNum){
            if(!flagVisitor){
                return new Result(ResultCodeEnum.VOTE_VISITOR);//不允许访客投票
            }
            int NumEveryUser = stageLabel.getVoteNumEveryUser();
            int NumTotalUser = stageLabel.getVoteNumTotalUser();
            long total= voteMapperDao.queryVoteByuserId(record.getUserId(),aid);
            long numEvery = voteMapperDao.queryVoteByuserId(record.getUserId(), day,aid);
            if(NumTotalUser !=0 && total>=NumTotalUser ){
                return new Result(ResultCodeEnum.VOTE_VISITOR_OUT);//超出最多投票数
            }
            if(NumEveryUser !=0 && numEvery >= NumEveryUser  ){
                return new Result(ResultCodeEnum.VOTE_VISITOR_OUT_DAY);//超出最多投票数
            }
            return new Result(ResultCodeEnum.STAGE3_VALID);
        }else if(validNum ==ResultCodeEnum.USER_ROLE_ANONYMOUS.resultCode){
            if(!flagAnonymous){
                return new Result(ResultCodeEnum.VOTE_ANONYMOUS);//不允许匿名用户投票
            }
            int NumEveryAnonymous = stageLabel.getVoteNumEveryAnonymous();
            int NumTotalAnonymous =  stageLabel.getVoteNumTotalAnonymous();
            long numEvery = voteMapperDao.queryVoteByIP(record.getVoterIp(), day,aid);
            long total   = voteMapperDao.queryVoteByIP(record.getVoterIp(),aid);
            if(NumTotalAnonymous !=0 &&  total >= NumTotalAnonymous){
                return new Result(ResultCodeEnum.VOTE_ANONYMOUS_OUT);//超出最多投票数
            }
            if(NumEveryAnonymous !=0 && numEvery >= NumEveryAnonymous){
                return new Result(ResultCodeEnum.VOTE_ANONYMOUS_OUT_DAY);//超出最多投票数
            }
            return new Result(ResultCodeEnum.STAGE3_VALID);

        }else if(validNum == ResultCodeEnum.USER_ROLE_PARTICIPANT.resultCode){
            if(!flagParticipant){
                return new Result(ResultCodeEnum.VOTE_PARTICIPANT_FORBIDDEN);//不允许参赛者投票
            }
            boolean flagSelf = stageLabel.getVoteMyself();
            WorksView workv = userWorksWrapperDao.queryWork(record.getWorkId());
            if( !flagSelf && workv.getAuthorId().equals(record.getUserId())){
                return new Result(ResultCodeEnum.VOTE_MYSELF); // 不允许参与者给自己投票
            }
            int NumEveryP = stageLabel.getVoteNumEveryUser();
            int NumTotalP = stageLabel.getVoteNumTotalUser();
            long numEvery = voteMapperDao.queryVoteByuserId(record.getUserId(), day,aid);
            long total    = voteMapperDao.queryVoteByuserId(record.getUserId(),aid);
            if(NumTotalP !=0 && total >= NumTotalP){
                return new Result(ResultCodeEnum.VOTE_VISITOR_OUT);//超出最多投票数
            }
            if(NumEveryP!=0 && numEvery >= NumEveryP){
                return new Result(ResultCodeEnum.VOTE_VISITOR_OUT_DAY);//超出最多投票数
            }
            return new Result(ResultCodeEnum.STAGE3_VALID);

        }else{
            //管理员和专家 目前都可投票
            int NumEveryUser = stageLabel.getVoteNumEveryUser();
            int NumTotalUser =  stageLabel.getVoteNumTotalUser();
            long total= voteMapperDao.queryVoteByuserId(record.getUserId(),aid);
            long numEvery = voteMapperDao.queryVoteByuserId(record.getUserId(), day,aid);
            if(NumTotalUser !=0 &&  total>=NumTotalUser ){
                return new Result(ResultCodeEnum.VOTE_VISITOR_OUT);//超出最多投票数
            }
            if(NumEveryUser !=0 && numEvery >= NumEveryUser  ){
                return new Result(ResultCodeEnum.VOTE_VISITOR_OUT_DAY);//超出最多投票数
            }
            return new Result(ResultCodeEnum.STAGE3_VALID);
        }
    }

    @Override
    public ResultCodeEnum validStage6(Long aid) {
        ActivityView activityView = queryActivity(aid);
        if (activityView.getStage6() == null || activityView.getStage6() < 0)
            return ResultCodeEnum.STAGE6_INVALID;
        Stage6 stage6 = stage6Mapper.selectByPrimaryKey(activityView.getStage6());
        if (stage6 == null || stage6.getStartTime() == null)
            return ResultCodeEnum.STAGE6_INVALID;
        Date now = new Date();
        if (now.before(stage6.getStartTime()))
            return ResultCodeEnum.STAGE_NOT_START;
        return ResultCodeEnum.RESULT_SUCCESS;
    }

    @Override
    public String getCategoryName(Long cid) {
        Category category = (Category) categoryMapper.selectByPrimaryKey(cid);
        return category == null ? "" : category.getName();
    }
    @Override
    public ArrayList<String> getTypes(Long aid) {
        List<ResourceType> types = userResourceService.queryTypes(aid);
        ArrayList<String> typeIDs = new ArrayList<>();
        for (ResourceType type : types) {
            typeIDs.add(String.valueOf(type.getId()));
        }
        return typeIDs;
    }

    @Override
    public long queryWorkNum(Long aid) {
        if (aid == null || aid <= 0)
            return 0L;
        return userWorksWrapperDao.queryWorkNum(aid);
    }
    @Override
    public Scores queryScore(Scores score) {
        return userWorksWrapperDao.queryScore(Long.parseLong(score.getWorkId()), score.getActivityId(), score.getExpertId());
    }

    @Override
    public boolean isGroupedJury(Stage4 stage4, Scores queryScore) {
        return queryScore != null && stage4 != null && stage4.getGroup();
    }

    //检查是否可打分
    @Override
    public Result canMark(Scores score, Scores queryScore, Stage4 stage4) {
        Result result = new Result();
        if (score.getExpertId() == null) {
            ResultCodeEnum.USER_ROLE_WRONG.setResult(result);
            return result;
        }
        if(stage4 == null || !validStageDate(ResultCodeEnum.STAGE4.resultCode,stage4.getStageId())){
            ResultCodeEnum.STAGE4_INVALID.setResult(result);
            return result;
        }
        //查询作品是否被打过分
        if (stage4.getGroup()) {
            if (queryScore == null) {
                ResultCodeEnum.MARK_WORK_GROUPED.setResult(result);
                return result;
            }
            if (!queryScore.getCreateTime().equals(queryScore.getUpdateTime())) {
                ResultCodeEnum.MARK_WORK_DONE.setResult(result);
                return result;
            }
        } else if (queryScore != null) {
            ResultCodeEnum.MARK_WORK_DONE.setResult(result);
            return result;
        }
        result.setCode(ResultCodeEnum.STAGE4_VALID.resultCode);
        return result;
    }

    @Override
    public Result mark(Scores score, Scores queryScore) {
        Stage4 stage4 = queryStage4(score.getActivityId());
        Result result = canMark(score, queryScore, stage4);
        if(result.getCode() != ResultCodeEnum.STAGE4_VALID.resultCode)
            return result;
        if (isGroupedJury(stage4, queryScore)) {
            score.setId(queryScore.getId());
            score.setUpdateTime(new Date());
            result.setCode(scoresMapper.updateByPrimaryKeySelective(score));
        } else {
            result.setCode(scoresMapper.insertSelective(score));
        }
        result.setData(score);
        if (result.getCode() == 1)
            ResultCodeEnum.RESULT_SUCCESS.setResult(result);
        return result;
    }

    //检查是否可投票
    @Override
    public Result canVote(VoteRecord record, int validNum){
        Activity activity = activityWrapperDao.queryActivity(record.getActivityId());
        if(activity==null){
            return new Result(ResultCodeEnum.ACTIVITY_ID_WRONG);
        }
        Stage3 stage3 = stage3Mapper.selectByPrimaryKey(activity.getStage3());
        if(stage3 == null || !validStageDate(ResultCodeEnum.STAGE3.resultCode, stage3.getStageId())){
            return new Result(ResultCodeEnum.STAGE3_INVALID);
        }
        return validStage3(stage3,record,validNum);
    }

    private boolean areaContains(long pattern, long value) {
        if (pattern % 10000 == 0)
            return value / 10000 * 10000 == pattern;
        if (pattern % 100 == 0)
            return value / 100 * 100 == pattern;
        return value == pattern;
    }
    private ResultCodeEnum limitCode(ActivityView av, Participant user) {
        if (av.getAreaId() != null && av.getAreaId() > 0 && !areaContains(av.getAreaId(), user.getAreaId())) {
            LOG.warn(av.getAreaId() + " area " + user.getAreaId());
            return ResultCodeEnum.USER_APPLY_WRONG5;
        }
        if (StringUtils.isNotBlank(av.getProjectId()) && !av.getProjectId().equals("0") &&
            projectWrapperDao.contains(av.getProjectId(), user.getUserId()) <= 0) {
            LOG.warn(av.getProjectId() + " project " + user.getUserId());
            return ResultCodeEnum.USER_APPLY_WRONG5;
        }
        return ResultCodeEnum.RESULT_SUCCESS;
    }

    private ResultCodeEnum selected(ActivityView av, Participant user) {
        boolean selectOnly = av.getRoleId() != 0L;
        boolean selected = (Objects.equals(user.getRole(), ResultCodeEnum.USER_ROLE_SELECTED.resultCode)
                || Objects.equals(user.getRole(), ResultCodeEnum.USER_ROLE_PARTICIPANT.resultCode));
        if (selectOnly && (!selected || !av.getActivityId().equals(user.getActivityId()))
                || !limitCode(av, user).success()) {
            LOG.warn(selectOnly + " select " + selected);
            return ResultCodeEnum.USER_APPLY_WRONG5;
        }
        return ResultCodeEnum.RESULT_SUCCESS;
    }

    @Override
    public ResultCodeEnum applyCode(ActivityView av, Participant user) {
        if (av == null)
            return ResultCodeEnum.ACTIVITY_ID_WRONG;
        if (user == null)
            return ResultCodeEnum.USER_APPLY_WRONG1;
        Stage1 stage1 = (Stage1)queryStage(av.getStage1(), ResultCodeEnum.STAGE1.resultCode);
        if (stage1 == null)
            return ResultCodeEnum.STAGE1_INVALID;
        Date now = new Date();
        if (stage1.getStartTime() != null && now.before(stage1.getStartTime()))
            return ResultCodeEnum.USER_APPLY_BEFORE;
        if (stage1.getEndTime() != null && now.after(stage1.getEndTime()))
            return ResultCodeEnum.USER_APPLY_AFTER;
        return selected(av, user);
    }
    @Override
    public ResultCodeEnum applyCode(Long aid, Participant participant) {
        ActivityView activity = queryActivity(aid);
        return applyCode(activity, participant);
    }

    //检查现阶段是否可上传资源
    @Override
    public Result canUpload(WorksView wv) {
        ActivityView activity = queryActivity(wv.getActivityId());
        if(activity==null){
            return new Result(ResultCodeEnum.ACTIVITY_ID_WRONG) ;
        }
        wv.getParticipant().setAreaId(wv.getAreaId());
        ResultCodeEnum result = selected(activity, wv.getParticipant());
        if (!result.success()) {
            return new Result(result);
        }
        boolean isValid;
        Stage2 stage2 = stage2Mapper.selectByPrimaryKey(activity.getStage2());
        isValid = validStageDate(ResultCodeEnum.STAGE2.resultCode,stage2.getStageId());
        if(!isValid){
            return new Result(ResultCodeEnum.STAGE2_INVALID);
        }
        if(!stage2.getResourceType().contains("0") && !stage2.getResourceType().contains("-")){
            if(!stage2.getResourceType().contains(String.valueOf(wv.getResourceType()))){
                return new Result(ResultCodeEnum.UP_WORK_TYPE_WRONG);
            }
        }
        if (stage2.getResourceNum() > 0 && stage2.getResourceNum() < wv.getParticipant().getWorkNum() + 1) {
            return new Result(ResultCodeEnum.UP_WORK_NUM_OUT);
        }
        if ((activity.getStage1() == null || activity.getStage1() <= 0)
                && wv.getParticipant().getCreateTime() == null) {
            participantMapper.insertSelective(wv.getParticipant());
        } else if (wv.getParticipant().getProjectId() != null && wv.getParticipant().getProjectId() > 0) {
            participantMapper.updateByPrimaryKeySelective(wv.getParticipant());
        }

        return new Result(ResultCodeEnum.RESULT_SUCCESS);
    }

    @Override
    public int uploadCode(ActivityView av, Participant user, Stage2 stage2) {
        if (user == null)
            return ResultCodeEnum.UP_WORK_NOLOGIN.resultCode;
        if (stage2 == null)
            stage2 = (Stage2)queryStage(av.getStage2(), ResultCodeEnum.STAGE2.resultCode);
        if (stage2 == null || !stage2.valid()) {
            return ResultCodeEnum.STAGE2_INVALID.resultCode;
        }
        Date now = new Date();
        if (now.before(stage2.getStartTime()))
            return ResultCodeEnum.UP_WORK_BEFORE.resultCode;
        else if (now.after(stage2.getEndTime()))
            return ResultCodeEnum.UP_WORK_AFTER.resultCode;
        ResultCodeEnum resultCodeEnum = limitCode(av, user);
        if (!resultCodeEnum.success())
            return resultCodeEnum.resultCode;
        return 0;
    }
    @Override
    public ResultCodeEnum delCode(ActivityView activity) {
        ResultCodeEnum codeEnum = ResultCodeEnum.RESULT_SUCCESS;
        Stage2 stage2 = (Stage2) queryStage(activity.getStage2(), ResultCodeEnum.STAGE2.resultCode);
        Stage4 stage4 = (Stage4) queryStage(activity.getStage4(), ResultCodeEnum.STAGE4.resultCode);
        if (stage2 == null) {
            codeEnum = ResultCodeEnum.STAGE2_INVALID;
        }
        if (stage4 == null) {
            codeEnum = ResultCodeEnum.STAGE4_INVALID;
        }
        Date now = new Date();
        if (stage2 != null && stage2.getEndTime() != null && stage2.getEndTime().before(now)
         || stage4 != null && stage4.getStartTime() != null && stage4.getStartTime().before(now)) {
            return ResultCodeEnum.RESOURCE_DELETE_FORBIDDEN;
        }
        return codeEnum;
    }

    @Override
    public long getServiceID() {
        return 0L;
    }

    @Override
    public String ping() {
        return null;
    }

    @Override
    public List<Map<String, Object>> querySchool(Long quxiancode) {
        return schoolWrapperDao.querySchool(quxiancode);
    }

    @Override
    public int updJoinNumInActivity(Long aid) {
        return  activityWrapperDao.updJoinNumInActivity(aid);
    }

    @Override
    public Map<String, Object> activityStageInfo(Long aid) {
        return activityWrapperDao.activityStageInfo(aid);
    }

    @Override
    public Long getVoteNumByIP(String IP) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long current=System.currentTimeMillis();
        Long timeStart;   // 00:00:00
        Long timeEnd;     // 23:59:59
        timeStart = current/(1000*60*60*24)*(1000*60*60*24) - TimeZone.getDefault().getRawOffset();
        timeEnd = timeStart+24*60*60*1000-1;
        Date dateStart = new Date(timeStart);
        Date dateEnd = new Date(timeEnd);
        String startStr = sdf.format(dateStart);
        String endStr = sdf.format(dateEnd);
        return activityWrapperDao.getVoteNumByIP(IP, startStr, endStr);
    }

    @Override
    public int setTemplate(Template template) {
        int ret = -1;
        try {
            boolean empty = StringUtils.isBlank(template.getKey()) || StringUtils.isBlank(template.getName());
            if (template.getId() != null && template.getId() > 0) {
                if (empty)
                    ret = templateMapper.deleteByPrimaryKey(template.getId());
                else
                    ret = templateMapper.updateByPrimaryKeySelective(template);
            } else if (!empty) {
                ret = templateMapper.insertSelective(template);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return ret;
    }
    @Override
    public List<Template> getTemplates(Long aid, int templateType) {
        List<Template> templates = activityWrapperDao.getTemplates(aid, templateType);
        List<Long> rids = new ArrayList<>();
        for (Template template : templates)
            if (template.getResId() != null)
                rids.add(template.getResId());
        Map<Long, ResourceData> map = userResourceService.resourceDataMap(rids);
        for (Template template : templates)
            if (template.getResId() != null && map.containsKey(template.getResId()))
                template.setData(map.get(template.getResId()));
        return templates;
    }
    @Override
    public int setNavigation(Navigation nav) {
        if (nav.getId() != null && nav.getId() > 0) {
            if (StringUtils.isBlank(nav.getName()))
                return navigationMapper.deleteByPrimaryKey(nav.getId());
            return navigationMapper.updateByPrimaryKeySelective(nav);
        }
        if (StringUtils.isNotBlank(nav.getName())) {
            return navigationMapper.insertSelective(nav);
        }
        return -1;
    }
    @Override
    public List<Navigation> getNavigationList(Long aid) {
        return activityWrapperDao.getNavigationList(aid);
    }
}
