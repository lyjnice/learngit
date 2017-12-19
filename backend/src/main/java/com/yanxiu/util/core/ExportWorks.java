package com.yanxiu.util.core;

import com.yanxiu.util.CSVUtils;
import com.yanxiu.workselect.api.commons.enums.WorkselectConstant;
import com.yanxiu.workselect.api.commons.vo.Pagination;
import com.yanxiu.workselect.api.dto.ActivityView;
import com.yanxiu.workselect.api.dto.UserView;
import com.yanxiu.workselect.api.dto.WorksView;
import com.yanxiu.workselect.api.model.Scores;
import com.yanxiu.workselect.api.service.ActivityStageService;
import com.yanxiu.workselect.api.service.UserResourceService;
import com.yanxiu.workselect.core.util.VMmapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/* Created by lmz on 2017/3/2. */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class ExportWorks {
    private static final Logger Log = LoggerFactory.getLogger(ExportWorks.class);
    private static final String urlPrefix = "http://pb.yanxiu.com/workselect/userResource/getWork.vm";
    @Resource(name = "userResourceService")
    private UserResourceService userResourceService;
    @Resource(name = "activityBackendService")
    private ActivityStageService activityService;

    private String scoresCSV(Long aid) {
        List<Scores> scores = userResourceService.getScores(aid);
        List<String> ls = new ArrayList<>();
        ls.add("作品ID");
        ls.add("活动ID");
        ls.add("专家ID");
        ls.add("专家名字");
        ls.add("是否已打分");
        ls.add("专家所打分数");
        ls.add("预览");
        ls.add("开始打分时间");
        ls.add("实际打分时间");
        StringBuilder xls;
        try {
            xls = new StringBuilder(CSVUtils.writeLine(ls));
        } catch (Exception e) {
            return e.toString();
        }
        HashMap<Long, String> users = new HashMap<>();
        for (Scores score : scores) {
            ls.clear();
            ls.add(score.getWorkId());
            ls.add(String.valueOf(score.getActivityId()));
            ls.add(String.valueOf(score.getExpertId()));
            if (!users.containsKey(score.getExpertId())) {
                UserView userView = userResourceService.queryUser(score.getActivityId(), score.getExpertId());
                users.put(score.getExpertId(), userView.getUserName());
            }
            ls.add(users.get(score.getExpertId()));
            ls.add(score.getCreateTime().equals(score.getUpdateTime()) ? "No" : "Yes");
            ls.add(String.valueOf(score.getExpertScore()));
            ls.add(hyperlink(urlPrefix + "?aid=" + score.getActivityId() + "&workId=" + score.getWorkId()));
            ls.add(String.valueOf(score.getCreateTime()));
            ls.add(String.valueOf(score.getUpdateTime()));
            try {
                xls.append(CSVUtils.writeLine(ls));
            } catch (Exception e) {
                return e.toString();
            }
        }
        return xls.toString();
    }
    private String gen(Long aid) {
        int MAX_DEFAULT_LIMIT = 99999;
        int DEFAULT_PAGE = 1;
        int AREA_COLUMN = 3;
        if(aid == null || aid <= 0) {
            return "error id";
        }
        Integer check;
        Integer limit;
        Integer page;
        check = WorkselectConstant.WORK_CHECK_SUCCESS;
        limit = MAX_DEFAULT_LIMIT;
        page = DEFAULT_PAGE;
        int offset = (page - 1) * limit;
        Log.info("to query list");
        Pagination<WorksView> worksViewPagination = userResourceService.queryActivityWorkPage(
                aid, limit, offset, null, null, null, null, null,
                check, null, null, null,null,null,null,null,
                null, null, null, false);
        List<WorksView>	list = worksViewPagination.getElements();
        if (list != null && list.size() > 0) {
            Log.info("to get avg score..");
            list = userResourceService.getWorkAvgScore(list);
        } else {
            return "no works";
        }
        List<String> ls = new ArrayList<>();
        ls.add("作品ID");
        ls.add("活动ID");
        ls.add("专家passport账号");
        ls.add("类型");
        ls.add("上传时间");
        ls.add("省");
        ls.add("市");
        ls.add("区县");
        ls.add("上传者");
        ls.add("作品名");
        ls.add("投票数");
        ls.add("专家打分");
        ls.add("学科");
        ls.add("学段");
        ls.add("下载");
        ls.add("预览");
        ls.add("简介");
        ls.add("手机号");
        ls.add("学校");
        ls.add("邮箱");
        ls.add("地址");
        StringBuilder xls;
        try {
            xls = new StringBuilder(CSVUtils.writeLine(ls));
        } catch (Exception e) {
            return e.toString();
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.info("to export list size: " + list.size());
        for (WorksView work : list) {
            ls.clear();
            ls.add(String.valueOf(work.getWorkId()));
            ls.add(String.valueOf(work.getActivityId()));
            ls.add("");
            ls.add(VMmapper.getCategoryName(aid, work.getCategory(), activityService));
            ls.add(formatter.format(work.getCreateTime()));
            String[] area = userResourceService.areaName(work.getAreaId()).split("-");
            int c = 0;
            for (String s : area) {
                ls.add(s);
                c++;
            }
            while (c < AREA_COLUMN) {
                ls.add("-");
                c++;
            }
            ls.add(work.getParticipant().getUserName());
            ls.add(work.getWorkName());
            ls.add(String.valueOf(work.getWorkAgree()));
            ls.add(String.valueOf(work.getExpertScore()));
            ls.add(userResourceService.subjectName(work.getXuekeId()));
            ls.add(userResourceService.sectionName(work.getXueduanId()));
            // 2017/5/1 empty download link, due to rms search api(getResourceListByResIds).
            ls.add(hyperlink(work.getDownloadUrl()));
            ls.add(hyperlink(urlPrefix + "?aid=" + work.getActivityId() + "&workId=" + work.getWorkId()));
            ls.add(work.getIntroduce());
            ls.add(work.getParticipant().getUserMobile());
            ls.add(work.getSchoolName());
            ls.add(work.getParticipant().getUserEmail());
            ls.add(work.getParticipant().getUserAddress());
            try {
                xls.append(CSVUtils.writeLine(ls));
            } catch (Exception e) {
                return e.toString();
            }
        }
        return xls.toString();
    }

    private String hyperlink(String url) {
        return StringUtils.isBlank(url) ? "" : String.format("=HYPERLINK(\"%s\")", url);
    }

    private void output(String content, String prefix) throws Exception {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(prefix + "_" + today + ".csv");
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content.getBytes("GBK"));
        outputStream.close();
        Log.info("finished @" + file.getAbsolutePath());
    }

    private void start(String[] args) {
        try {
            if (args.length < 1) {
                Log.info(ExportWorks.class + " all/<activity_id> [output_full_path_prefix, default /tmp/[activity_id]_[today].xls]");
                System.exit(-1);
            }
            List<Long> ids = new ArrayList<>();
            if (args[0].equals("all")) {
                Pagination<ActivityView> pagination = activityService.queryActivityPagination(99, 0, -1);
                for(ActivityView av : pagination.getElements()) {
                    ids.add(av.getActivityId());
                }
            } else {
                ids.add(Long.valueOf(args[0]));
            }
            String prefix = args.length > 1 ? args[1] : "/tmp/";
            for (Long id : ids) {
                Log.info("started @" + id);
                output(gen(id), prefix + id);
                output(scoresCSV(id), prefix + "scores_" + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        ExportWorks exporter = context.getBean(ExportWorks.class);
        exporter.start(args);
        System.exit(0);
    }
}
