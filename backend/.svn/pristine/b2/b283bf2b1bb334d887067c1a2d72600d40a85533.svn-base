package com.yanxiu.resource.service;

import com.google.common.collect.Lists;
import com.yanxiu.util.core.HttpUtil;
import com.yanxiu.util.core.JsonUtils;
import com.yanxiu.workselect.api.commons.enums.WorkselectConstant;
import com.yanxiu.workselect.api.commons.vo.resource.ResourceResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author yueting
 * @version 创建时间:2015年10月15日
 */
@SuppressWarnings({"SameParameterValue", "unused"})
@Repository("resourceSearchClient")
public class ResourceSearchClient {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceSearchClient.class);

	public ResourceResult getResourceListByResId(Long resId) {
        List<Long> ids = Lists.newArrayList();
        ids.add(resId);
        return getResourceList("PcCommunityRid", null, null, null, null, null,
                ids, null, null, null, 0);
    }
	public ResourceResult getResourceListByResIds(List<Long> resIds){
		return getResourceList("PcCommunityRid", null, null, null, null, null,
                resIds, null, null, null, 0);
	}
    public ResourceResult getResourceListByResIdStr(String resIdStr) {
        String[] idStrs = StringUtils.split(resIdStr, ",");
        List<Long> ids = Lists.newArrayList();
        for (String id : idStrs) {
            if (StringUtils.isNotBlank(id)) {
                ids.add(new Long(id));
            }
        }
        return getResourceList("PcCommunityRid", null, null, null, null, null,
                ids, null, null, null, 0);
    }

	/**
	 * 获取资源
	 */
	private ResourceResult getResourceList(String interf, String categoryIds, Long chapterId, Long userId, String typeId, Integer shareType, List<Long> resIds, String sort, Integer page, Integer pageSize, Integer offset) {
		HashMap<String,String> params = new HashMap<>();
		params.put("interf", StringUtils.isNotBlank(interf) ? interf : "PcCommunityRid");
		if(resIds != null && resIds.size() > 0){
			params.put("res_id", StringUtils.join(resIds, ","));
			params.put("page_size", Integer.toString(resIds.size()));
		}
		params.put("source_id", "5,6,7,8,9");

		String param = JsonUtils.toJson(params);
		LOG.debug("param=" + param);
		String url = WorkselectConstant.RESOURCE_SEARCH_API;
		String json = HttpUtil.postJson(param, url);
		ResourceResult resourceResult = null;
		try {
			resourceResult = JsonUtils.toObject(json, ResourceResult.class);
		} catch (Exception ignored) {
			LOG.error(ignored.toString());
		}
		if(resourceResult == null){
			resourceResult = new ResourceResult(ResourceResult.STATUS_REMOTE_SERVER_ERROR, "服务异常");
			LOG.error("remote server error " + url);
		}
        LOG.debug(resourceResult.toString());
		return resourceResult;
	}
}
