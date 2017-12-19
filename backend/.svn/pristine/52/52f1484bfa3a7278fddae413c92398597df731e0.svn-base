package com.yanxiu.util.core;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class HttpUtil {
	private static final Log logger = LogFactory.getLog(HttpUtil.class);

	public static String postJson(String json, String url) {
		
		String info = null;
		PostMethod post = null;
		InputStream in = null;
		try {
			MyHttpClientFactory myHttpClientFactory = MyHttpClientFactory.getInstance();
			HttpClient httpclient = myHttpClientFactory.getHttpClient();
			post = new PostMethod(url);
			post.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
			in = new ByteArrayInputStream(json.getBytes("UTF-8"));			
			post.setRequestEntity(new InputStreamRequestEntity(in));
			httpclient.executeMethod(post);
			int code = post.getStatusCode();
			if (code == 200) {
				info = post.getResponseBodyAsString();
			} else {// 如果返回非正常状态，直接设置info为”“
				logger.error("url return not 200. code="+code+",html="+post.getResponseBodyAsString());
				info = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(post != null){
				post.releaseConnection();
			}
		}
		return info;

	}

	public static String get(HashMap<String, String> params, String url) {
		String info = null;
		GetMethod get = null;
		try {
			MyHttpClientFactory myHttpClientFactory = MyHttpClientFactory
					.getInstance();
			HttpClient httpclient = myHttpClientFactory.getHttpClient();
			get = new GetMethod(url);
			get.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			if (params != null) {
				NameValuePair pairs[] = new NameValuePair[params.size()];
				int i = 0;
				for (String key : params.keySet()) {
					String v = params.get(key);
					NameValuePair pair = new NameValuePair(key, v);
					pairs[i] = pair;
					i++;
				}
				get.setQueryString(pairs);
			}
			httpclient.executeMethod(get);
			int code = get.getStatusCode();
			if (code == 200) {
				info = get.getResponseBodyAsString();
			} else {// 如果返回非正常状态，直接设置info为”“
				logger.error("url return not 200. code="+code+",html="+get.getResponseBodyAsString());
				info = "";
			}
		} catch (Exception e) {
			logger.error("url:"+url, e);
		} finally {
			if (get != null) {
				get.releaseConnection();
			}
		}
		return info;
	}
}
