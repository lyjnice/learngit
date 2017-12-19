package com.yanxiu.workselect.service;

import com.google.common.collect.Lists;
import com.yanxiu.passport.dao.PPUserWrapperDao;
import com.yanxiu.resource.service.ResourceSearchClient;
import com.yanxiu.school.dao.SchoolMapper;
import com.yanxiu.util.core.FileTypeUtil;
import com.yanxiu.util.core.HttpUtil;
import com.yanxiu.workselect.api.commons.enums.ResultCodeEnum;
import com.yanxiu.workselect.api.commons.enums.WorkselectConstant;
import com.yanxiu.workselect.api.commons.exception.WorkselectRuntimeException;
import com.yanxiu.workselect.api.commons.vo.Pagination;
import com.yanxiu.workselect.api.commons.vo.UserSession;
import com.yanxiu.workselect.api.commons.vo.passport.PPUser;
import com.yanxiu.workselect.api.commons.vo.passport.PPUserInfo;
import com.yanxiu.workselect.api.commons.vo.resource.ResourceData;
import com.yanxiu.workselect.api.commons.vo.resource.ResourceResult;
import com.yanxiu.workselect.api.dto.UserView;
import com.yanxiu.workselect.api.dto.WorksView;
import com.yanxiu.workselect.api.model.*;
import com.yanxiu.workselect.api.service.UserResourceService;
import com.yanxiu.workselect.dao.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * by mashiwei on 2016/8/25.
 */
@SuppressWarnings({"SpringJavaAutowiringInspection", "SpringAutowiredFieldsWarningInspection"})
@Service("userResourceServiceImpl")
public class UserResourceServiceImpl implements UserResourceService {
    private static final Logger LOG = LoggerFactory.getLogger(UserResourceServiceImpl.class);

    @Autowired
    private WorksMapper worksMapper;
    @Autowired
    private VoteMapperDao voteMapperDao;
    @Autowired
    private ParticipantMapper participantMapper;
    @Autowired
    private UserWorksWrapperDao userWorksWrapperDao;
    @Autowired
    private ActivityWrapperDao activityWrapperDao;
    @Autowired
    private PPUserWrapperDao ppUserWrapperDao;
    @Autowired
    private ProjectWrapperDao projectWrapperDao;
    @Autowired
    private ResourceSearchClient resourceSearchClient;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private AwardsMapper awardsMapper;
    @Autowired
    private ResourceTypeMapper resourceTypeMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    private static HashMap<Long, String> areaMap = null;
    private static HashMap<Long, String> categoryMap = null;    // grade 1300, version 1400
    private static HashMap<Long, String> subjectMap = new HashMap<>();
    private static HashMap<Long, String> stageMap = new HashMap<>();
    private static HashMap<Long, String> chapterMap = new HashMap<>();
    static {
        stageMap.put(10L, "小学");
        stageMap.put(20L, "初中");
        stageMap.put(30L, "高中");
        stageMap.put(40L, "学前");
        stageMap.put(60L, "中职");
        stageMap.put(92L, "班主任");
        stageMap.put(93L, "校长");
        stageMap.put(99L, "其他");
        subjectMap.put(10L, "语文");
        subjectMap.put(11L, "数学");
        subjectMap.put(12L, "英语");
        subjectMap.put(13L, "物理");
        subjectMap.put(14L, "化学");
        subjectMap.put(15L, "生物");
        subjectMap.put(16L, "地理");
        subjectMap.put(17L, "思想政治");
        subjectMap.put(18L, "历史");
        subjectMap.put(19L, "音乐");
        subjectMap.put(20L, "体育与健康");
        subjectMap.put(21L, "美术");
        subjectMap.put(22L, "科学");
        subjectMap.put(23L, "信息技术");
        subjectMap.put(24L, "道德与法治（品生品社）");
        subjectMap.put(25L, "道德与法治（思想品德）");
        subjectMap.put(26L, "综合实践");
        subjectMap.put(27L, "劳动与技术教育");
        subjectMap.put(28L, "职业技术教育");
        subjectMap.put(29L, "艺术");
        subjectMap.put(30L, "研究性学习");
        subjectMap.put(31L, "社区服务与社会实践");
        subjectMap.put(32L, "心理");
        subjectMap.put(33L, "德育");
        subjectMap.put(34L, "课外教育");
        subjectMap.put(35L, "教育科研");
        subjectMap.put(36L, "幼儿教育");
        subjectMap.put(37L, "跨领域的学习活动");
        subjectMap.put(38L, "藏文");
        subjectMap.put(39L, "通用技术");
        subjectMap.put(41L, "特殊教育");
        subjectMap.put(42L, "教辅及职高");
        subjectMap.put(60L, "中职");
        subjectMap.put(70L, "通识");
        subjectMap.put(89L, "历史与社会");
        subjectMap.put(90L, "其它外语");
        subjectMap.put(99L, "其它");
    }

    @Override
    public WorksView queryWork(Long workId) {
        WorksView worksView = userWorksWrapperDao.queryWork(workId);
        setResourceIDs(worksView);
        setParticipant(worksView);
        setResourceType(worksView);
        setCategory(worksView);
        return worksView;
    }

    @Override
    public List<WorksView> queryPackageWorks(Long packageId) {
        List<WorksView> worksViewList = userWorksWrapperDao.queryPackageWorks(packageId);
        for (WorksView worksView : worksViewList) {
            setResourceIDs(worksView);
            setParticipant(worksView);
            setResourceType(worksView);
            setCategory(worksView);
        }
        return worksViewList;
    }

    @Override
    public UserView queryUser(Long aid, Long uid) {
        UserView participant = userWorksWrapperDao.queryUser(aid, uid);
        if (participant != null)
            return participant;
        participant = new UserView();
        participant.setActivityId(aid);
        participant.setUserId(uid);
        participant.setRole(ResultCodeEnum.USER_ROLE_VISITOR.resultCode);
        participant.setWorkNum(0);
        if (aid > 0L) {
            Activity activity = activityWrapperDao.queryActivity(aid);
            if (activity.getStage1() == null || activity.getStage1() <= 0)
                participant.setRole(ResultCodeEnum.USER_ROLE_PARTICIPANT.resultCode);
        }

        PPUser ppUser = ppUserWrapperDao.queryPPUser(uid);
        if (ppUser != null) {
            participant.setUserMobile(ppUser.getMobile());
            participant.setUserEmail(ppUser.getEmail());
        }
        PPUserInfo ppUserInfo = ppUserWrapperDao.queryPPUserInfo(uid);
        if (ppUserInfo != null) {
            participant.setUserName(ppUserInfo.getRealName());
            participant.setXuekeId(ppUserInfo.getCourseId());
            participant.setXueduanId(ppUserInfo.getStuStageId());
            if(ppUserInfo.getGrade() != null){
                participant.setGradeId(ppUserInfo.getGrade().intValue());
            }
            if(ppUserInfo.getSchoolId() != null){
                participant.setSchoolId(ppUserInfo.getSchoolId().intValue());
            }
            participant.setAreaId(ppUserInfo.getCityId());
        }
        return participant;
    }

    @Override
    public Map<Long, ResourceData> resourceDataMap(List<Long> rids) {
        Map<Long, ResourceData> map = new HashMap<>();
        if (rids.size() == 0)
            return map;
        ResourceResult resourceResult = resourceSearchClient.getResourceListByResIds(rids);
        if (!resourceResult.getCode().equals(ResultCodeEnum.RESULT_SUCCESS.resultCode)) {
            LOG.error(resourceResult.getCode().toString(), new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "获取资源错误"));
            return map;
        }
        for (ResourceData resourceData : resourceResult.getData()) {
            map.put(resourceData.getResId(), resourceData);
        }
        return map;
    }

    @Override
    public Pagination<WorksView> queryActivityWorkPage(
            Long aid, Integer pageSize, int offset,String sortField, String order,
            Long xueke, Long xueduan, Long area, Integer check, Integer work_type,
            Long grade, Long volume, Long version, Long chap1, Long chap2, Long chap3,
            Long category, Long projectID, String name, Boolean isPackage) {
        Pagination<WorksView> worksViewPagination = userWorksWrapperDao.queryActivityWorkPage(
                aid, pageSize, offset,sortField, order, xueke, xueduan, area, check, work_type,
                grade, volume, version, chap1, chap2, chap3, category, projectID, name, isPackage);
        List<Long> ids = Lists.newArrayList();
        List<Long> picIds = Lists.newArrayList();
        for (WorksView worksView : worksViewPagination.getElements()) {
            if (!isPackage) {
                ids.add(worksView.getResourceId());
            }
            picIds.add(worksView.getPicResourceId());
            setParticipant(worksView);
            setAward(worksView);
            setCategory(worksView);
            setResourceType(worksView);
        }
        if (!isPackage) {
            if (ids.size() == 0) {
                return worksViewPagination;
            }
            Map<Long, ResourceData> map = resourceDataMap(ids);
            for (WorksView worksView : worksViewPagination.getElements()) {
                if (map.containsKey(worksView.getResourceId())) {
                    setWorkViewByResource(worksView, map.get(worksView.getResourceId()));
                }
            }
        }

        //设置封皮start========================================
        if(picIds.size()==0){
            return worksViewPagination;
        }
        ResourceResult picResult = resourceSearchClient.getResourceListByResIds(picIds);
        if(!picResult.getCode().equals(ResultCodeEnum.RESULT_SUCCESS.resultCode)) {
            throw new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "获取资源错误");
        }
        setWorkPicUrl(worksViewPagination,  picResult);
        //end=================================

        return worksViewPagination;
    }

    private void setWorkPicUrl(Pagination<WorksView> worksViewPagination, ResourceResult picResult) {
        Map<Long, ResourceData> map = new HashMap<>();
        for (ResourceData resourceData : picResult.getData()) {
            map.put(resourceData.getResId(), resourceData);
        }
        for(WorksView worksView : worksViewPagination.getElements()) {
            if (map.containsKey(worksView.getPicResourceId())) {
                worksView.setPicResUrl(map.get(worksView.getPicResourceId()).getResPreviewUrl());
            }
        }
    }

    @Override
    public Pagination<WorksView> queryUserWorkPage(
            Long aid, Long uid, Integer pageSize, int offset, String sortField, String order) {
        Pagination<WorksView> worksViewPagination = userWorksWrapperDao.queryUserWorkPage(aid, uid,
                pageSize, offset, sortField, order, "");
        List<Long> ids = Lists.newArrayList();
        List<Long> picIds = Lists.newArrayList();
        for(WorksView worksView : worksViewPagination.getElements()) {
            setParticipant(worksView);
            setResourceType(worksView);
            setCategory(worksView);
            ids.add(worksView.getResourceId());
            picIds.add(worksView.getPicResourceId());
        }
        if(ids.size() == 0) {
            return worksViewPagination;
        }

        ResourceResult resourceResult = resourceSearchClient.getResourceListByResIds(ids);
        if(!resourceResult.getCode().equals(ResultCodeEnum.RESULT_SUCCESS.resultCode)) {
            throw new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "获取资源错误");
        }

        for(WorksView worksView : worksViewPagination.getElements()) {
            for (ResourceData resourceData : resourceResult.getData()) {
                if (worksView.getResourceId().equals(resourceData.getResId()) ) {
                    setWorkViewByResource(worksView, resourceData);
                    break;
                }
            }
        }
        //设置封皮start========================================
        if(picIds.size()==0){
            return worksViewPagination;
        }
        ResourceResult picResult = resourceSearchClient.getResourceListByResIds(picIds);
        if(!resourceResult.getCode().equals(ResultCodeEnum.RESULT_SUCCESS.resultCode)) {
            throw new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "获取资源错误");
        }

        setWorkPicUrl(worksViewPagination,  picResult);
        //end=================================

        return worksViewPagination;
    }

    @Override
    public Pagination<WorksView> queryUserGoodPage(
            Long aid, String userName, Integer pageSize, int offset, String sortField, String order, boolean isPackage) {
        Pagination<WorksView> worksViewPagination = userWorksWrapperDao.queryUserGoodPage(
                aid, userName, pageSize, offset, sortField, order, isPackage);
        for (WorksView worksView : worksViewPagination.getElements()) {
            setParticipant(worksView);
            setAward(worksView);
        }
        setPicUrls(worksViewPagination);
        return worksViewPagination;
    }

    @Override
    public String queryUserAward(Long aid, UserSession userSession) {
        if (aid == null || aid <= 0 || userSession == null || userSession.getUser() == null || userSession.getUser().getUserId() == null)
            return "";
        return userWorksWrapperDao.queryUserAward(aid, userSession.getUser().getUserId());
    }

    private void setAward(WorksView worksView) {
        if (worksView.getFinalRank() != 0) {
            Awards award = awardsMapper.selectByPrimaryKey(Long.valueOf(worksView.getFinalRank()));
            if (award != null)
                worksView.setAwardName(award.getAwardName());
        }
    }

    @Override
    public Pagination<WorksView> queryUserPackagePage(
            Long aid, Long uid, Integer pageSize, int offset, String sortField, String order) {
        Pagination<WorksView> worksViewPagination = userWorksWrapperDao.queryUserPackagePage(aid, uid,
                pageSize, offset, sortField, order);
        setPicUrls(worksViewPagination);
        for (WorksView worksView : worksViewPagination.getElements()) {
            setAward(worksView);
        }
        return worksViewPagination;
    }

    private void setPicUrls(Pagination<WorksView> worksViewPagination) {
        List<Long> picIds = Lists.newArrayList();
        for(WorksView worksView : worksViewPagination.getElements()) {
            picIds.add(worksView.getPicResourceId());
        }
        if(picIds.size()==0){
            return;
        }
        ResourceResult picResult = resourceSearchClient.getResourceListByResIds(picIds);
        if(!picResult.getCode().equals(ResultCodeEnum.RESULT_SUCCESS.resultCode)) {
            throw new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "获取资源错误");
        }
        setWorkPicUrl(worksViewPagination,  picResult);
    }

    @Override
    public void setWorkViewByResource(WorksView worksView, ResourceData resourceData) {
        if(resourceData == null) {
            throw new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "获取资源不存在，resid=" + worksView.getResourceId());
        }
        worksView.setResName(resourceData.getResName());
        worksView.setResType(resourceData.getResType());
        worksView.setFileType(FileTypeUtil.getFileType(resourceData.getResType()));
        worksView.setResSize(resourceData.getResSize());
        worksView.setDownNum(resourceData.getDownNum());
        worksView.setReadNum(resourceData.getReadNum());
        worksView.setThumbnail(resourceData.getResThumb());
        worksView.setDownloadUrl(resourceData.getResDownloadUrl());
        worksView.setPreviewUrl(resourceData.getResPreviewUrl());
        worksView.setTypeId(resourceData.getTypeId());
        worksView.setResSource(resourceData.getResSource());
        worksView.setTransCode(resourceData.getTransCode());
    }

    @Override
    public int upWork(Works work){
        int result =worksMapper.insertSelective(work);
        if(result>0){
            activityWrapperDao.updworkNumInActivity(work.getActivityId(),WorkselectConstant.WORK_NUM_ADD);
            activityWrapperDao.updWorkNumInPartic(work.getActivityId(), work.getAuthorId(),WorkselectConstant.WORK_NUM_ADD);
        }
        return result;
    }

    @Override
    public Works upPackage(Works work){
        int result =worksMapper.insertSelective(work);
        if(result > 0){
            activityWrapperDao.updworkNumInActivity(work.getActivityId(),WorkselectConstant.WORK_NUM_ADD);
            activityWrapperDao.updWorkNumInPartic(work.getActivityId(), work.getAuthorId(),WorkselectConstant.WORK_NUM_ADD);
        }
        return work;
    }

    @Override
    public int upWorkInPackage(Works work){
        return worksMapper.insertSelective(work);
    }

    @Override
    public int view(Works work) {
        work.setViewCount(work.getViewCount() + 1);
        return worksMapper.updateByPrimaryKeySelective(work);
    }

    @Override
    public int Vote(VoteRecord voteRecord) {
        int result = voteMapperDao.createVote(voteRecord);
        if(result>0){
            Works work = worksMapper.selectByPrimaryKey(voteRecord.getWorkId());
            if(work!=null){
                work.setWorkAgree(work.getWorkAgree()+1);
                worksMapper.updateByPrimaryKeySelective(work);
            }
        }
        return result;
    }

    @Override
    public PPUserInfo queryPPUserInfo(Long uid) {
        return ppUserWrapperDao.queryPPUserInfo(uid);
    }

    @Override
    public int insertParticipant(Participant participant) {
        return participantMapper.insertSelective(participant);
    }

    @Override
    public int updateParticipant(Participant participant) {
        try {
            return participantMapper.updateByPrimaryKeySelective(participant);
        } catch (Exception e) {
            LOG.error("up user:" + e.getMessage());
            return participantMapper.insertSelective(participant);
        }
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
    public int deleteResource(Long workId, Long authorId, Long aid, Boolean inPackage) {
        int result = userWorksWrapperDao.deleteResource(workId, aid, authorId);
        if (!inPackage && result > 0) {
            activityWrapperDao.updworkNumInActivity(aid,WorkselectConstant.WORK_NUM_SUB);
            activityWrapperDao.updWorkNumInPartic(aid, authorId,WorkselectConstant.WORK_NUM_SUB);
        }
        return result;
    }

    @Override
    public void setResourceIDs(WorksView worksView) {
        if (worksView == null) {
            throw new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "资源不存在");
        }
        if (worksView.getResourceId() > 0) {
            ResourceResult resourceResult = resourceSearchClient.getResourceListByResId(worksView.getResourceId());
            if (resourceResult.getCode().equals(ResultCodeEnum.RESULT_SUCCESS.resultCode)) {
                for (ResourceData resourceData : resourceResult.getData()) {
                    if (worksView.getResourceId().equals(resourceData.getResId())) {
                        setWorkViewByResource(worksView, resourceData);
                        break;
                    }
                }
            } else {
                throw new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "获取资源错误");
            }
        }

        if (worksView.getPicResourceId() > 0) {
            //设置封皮start========================================
            ResourceResult picResult = resourceSearchClient.getResourceListByResId(worksView.getPicResourceId());
            if (picResult.getCode().equals(ResultCodeEnum.RESULT_SUCCESS.resultCode)) {
                for (ResourceData picData : picResult.getData()) {
                    if (worksView.getPicResourceId().equals(picData.getResId())) {
                        worksView.setPicResUrl(picData.getResPreviewUrl());
                        break;
                    }
                }

            } else {
                throw new WorkselectRuntimeException(ResultCodeEnum.RESOURCE_GET_ERROR, "获取资源错误");
            }
            //设置封皮end========================================
        }
    }

    @Override
    public void setParticipant(WorksView worksView) {
        Participant participant = queryUser(worksView.getActivityId(), worksView.getAuthorId());
        worksView.setSchoolName("");
        Integer schoolId = participant.getSchoolId();
        if(schoolId != null){
            School school = schoolMapper.selectByPrimaryKey(Long.parseLong(schoolId.toString()));
            if(school != null){
                worksView.setSchoolName(school.getName());
            }
        }
        worksView.setParticipant(participant);
    }

    private void setCategory(WorksView work) {
        try {
            work.setSubjectName(subjectName(work.getXuekeId()));
            work.setStageName(sectionName(work.getXueduanId()));
            work.setGradeName(categoryName(work.getGradeId()));
            work.setVolumeName(categoryName(work.getVolumeId()));
            work.setVersionName(categoryName(work.getVersionId()));
            work.setChapterName1(chapterName(work.getChapterId1()));
            work.setChapterName2(chapterName(work.getChapterId2()));
            work.setChapterName3(chapterName(work.getChapterId3()));
            work.setAreaName(areaName(work.getAreaId()));
        } catch (Exception e) {
            LOG.error(work.getWorkId() + " work: " + e.toString());
        }
    }
    private void setResourceType(WorksView work) {
        try {
            if (work.getResourceType() != WorkselectConstant.PACKAGE_TYPE) {
                ResourceType type = (ResourceType) resourceTypeMapper.selectByPrimaryKey(Long.valueOf(work.getResourceType()));
                work.setResourceTypeName(type.getName());
                work.setResourceTypeDesc(type.getDesc());
            }
            if (work.getCategory() > 0) {
                ListEntity cat = categoryMapper.selectByPrimaryKey(work.getCategory());
                work.setCategoryName(cat.getName());
            }
        } catch (Exception e) {
            LOG.warn(String.format("tc: %d %d %s", work.getResourceType(), work.getCategory(), e.getMessage()));
        }
    }
    @Override
    public List<ResourceType> queryTypes(Long aid) {
        Activity activity = activityWrapperDao.queryActivity(aid);
        Long stage2 = activity.getStage2();
        return userWorksWrapperDao.queryResourceType(aid, stage2);
    }
    @Override
    public List<ResourceType> queryDetailTypes(Long aid) {
        List<ResourceType> types = queryTypes(aid);
        List<Long> rids = new ArrayList<>();
        for (ResourceType type : types) {
            if (type.getResId() != null)
                rids.add(type.getResId());
        }
        Map<Long, ResourceData> map = resourceDataMap(rids);
        for (ResourceType type : types) {
            if (type.getResId() != null && map.containsKey(type.getResId()))
                type.setData(map.get(type.getResId()));
        }
        return types;
    }
    @Override
    public List<Category> queryCategories(Long aid) {
        Activity activity = activityWrapperDao.queryActivity(aid);
        Long stage2 = activity.getStage2();
        return userWorksWrapperDao.queryCategory(aid, stage2);
    }

    @Override
    public List<Awards> queryAwards(Long aid) {
        Activity activity = activityWrapperDao.queryActivity(aid);
        Long stage5Id = activity.getStage5();
        return userWorksWrapperDao.queryAwards(aid, stage5Id);
    }

    @Override
    public ResultCodeEnum setAwards(Integer award_id, List<Long> work_ids) {
        Awards awards = userWorksWrapperDao.queryAward(award_id);
        int works = (int) userWorksWrapperDao.queryAwarded(Long.valueOf(award_id));
        awards.setRemainCount(awards.getAwardCount() - works);
        if (work_ids.size() > awards.getRemainCount()) {
            return ResultCodeEnum.AWARD_REMAIN_ERROR;
        }
        ResultCodeEnum ret = ResultCodeEnum.RESULT_SUCCESS;
        for (Long wid : work_ids) {
            Works work = new Works();
            work.setWorkId(wid);
            work.setFinalRank(award_id);
            if (1 != worksMapper.updateByPrimaryKeySelective(work))
                ret = ResultCodeEnum.AWARD_WORK_ERROR;
            else
                awards.setRemainCount(awards.getRemainCount() - 1);
        }
        if (1 != awardsMapper.updateByPrimaryKeySelective(awards))
            ret = ResultCodeEnum.AWARD_UPDATE_ERROR;
        return ret;
    }

    @Override
    public int checkWork(Works work) {
        return worksMapper.updateByPrimaryKeySelective(work);
    }

    @Override
    public Pagination<UserView> queryUsers(Long aid, int limit, int offset, Long course, Long grade, Long project, Long area, String name) {
        Pagination<UserView> userView = userWorksWrapperDao.queryUsers(aid, limit, offset, course, grade, project, area, name);
        List<UserView> userList =  userView.getElements();
        for (UserView user :userList) {
            user.setAreaName(areaName(user.getAreaId()));
            user.setSubjectName(subjectName(user.getXuekeId()));
            user.setStageName(sectionName(user.getXueduanId()));
            String schoolId = user.getSchoolId().toString();
            School school = schoolMapper.selectByPrimaryKey(Long.parseLong(schoolId));
            if(school != null){
                user.setSchoolName(school.getName());
            }
            String pid = user.getProjectId().toString();
            List<Project> projects = projectWrapperDao.queryProjects(pid);
            if (projects.size() == 1) {
                user.setProjectName(projects.get(0).getName());
            }
        }
        userView.setElements(userList);
        return userView;
    }

    public List<WorksView> getWorkScore(List<WorksView> list,Long expertId){
        for (WorksView worksView : list) {
            Scores scores = userWorksWrapperDao.queryScore(worksView.getWorkId(), worksView.getActivityId(), expertId);
            if(scores !=null){
                worksView.setExpertScore(scores.getExpertScore());
            }else{
                worksView.setExpertScore((double)-1);
            }
        }
        return list;
    }

    public List<WorksView> getWorkAvgScore(List<WorksView> list){
        for (WorksView worksView : list) {
            Scores scores = userWorksWrapperDao.queryAvgScore(worksView.getWorkId(), worksView.getActivityId());
            if(scores !=null && scores.getWorkId()!=null){
                worksView.setExpertScore(scores.getExpertScore());
            }else{
                worksView.setExpertScore((double)-1);
            }
        }
        return list;
    }

    @Override
    public List<Scores> getScores(Long aid) {
        return userWorksWrapperDao.queryScores(aid);
    }

    private void json2map(JSONArray data, HashMap<Long, String> map) {
        for (Object aData : data) {
            JSONObject node = JSONObject.fromObject(aData);
            map.put(node.getLong("id"), node.getString("name"));
            JSONArray sub = JSONArray.fromObject(node.get("sub"));
            json2map(sub, map);
        }
    }

    private String chapterName(long cid) {
        if (chapterMap.containsKey(cid))
            return chapterMap.get(cid);
        HashMap<String, String> params = new HashMap<>();
        params.put("chapter_id", String.valueOf(cid));
        String res = HttpUtil.get(params, "http://api.rms.yanxiu.com/resource-chapter/info");
        JSONArray data = JSONArray.fromObject(JSONObject.fromObject(res).get("data"));
        json2map(data, chapterMap);
        return chapterMap.get(cid);
    }
    @Override
    public String areaName(long areaID) {
        if (areaMap == null) {
            HashMap<String, String> params = new HashMap<>();
            params.put("method", "area.list");
            params.put("source", "8");
            String res = HttpUtil.get(params, WorkselectConstant.BASIC_DATA_API);
            JSONArray data = JSONArray.fromObject(JSONObject.fromObject(res).get("data"));
            if (data.size() > 0) {
                areaMap = new HashMap<>();
                json2map(data, areaMap);
            }
        }
        return areaMap.get(areaID / 10000 * 10000) + "-" + areaMap.get(areaID / 100 * 100) + "-" + areaMap.get(areaID);
    }
    @Override
    public String categoryName(long cid) {
        if (categoryMap == null) {
            Integer [] roots = {1100,1200,1300,1400,1700};
            HashMap<String, String> params = new HashMap<>();
            params.put("method", "category.list");
            params.put("roots", StringUtils.join(roots, ','));
            String res = HttpUtil.get(params, WorkselectConstant.BASIC_DATA_API);
            JSONObject object = JSONObject.fromObject(JSONObject.fromObject(res).get("data"));
            JSONArray data = new JSONArray();
            for (Integer root : roots) {
                data.addAll(JSONArray.fromObject(object.get(root.toString())));
            }
            if (data.size() > 0) {
                categoryMap = new HashMap<>();
                json2map(data, categoryMap);
            }
        }
        return categoryMap.get(cid);
    }

    @Override
    public String subjectName(long subjectID) {
        if (subjectMap.containsKey(subjectID))
            return subjectMap.get(subjectID);
        return categoryName(subjectID);
    }

    @Override
    public String sectionName(long sectionID) {
        if (stageMap.containsKey(sectionID))
            return stageMap.get(sectionID);
        return categoryName(sectionID);
    }

    @Override
    public List<PPUser> getPPUser(List<String> passport) {
        return ppUserWrapperDao.getPPUser(passport);
    }
}
