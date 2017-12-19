package com.yanxiu.util.core;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class MyHttpClientFactory {
	
	private static MyHttpClientFactory instance = new MyHttpClientFactory();
    private MultiThreadedHttpConnectionManager connectionManager;
    private HttpClient client;

    private MyHttpClientFactory() {
        init();
    }

    public static MyHttpClientFactory getInstance() {
        return instance;
    }

    public void init() {
        this.connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
        managerParams.setConnectionTimeout(1000);
        managerParams.setSoTimeout(10000);
        managerParams.setDefaultMaxConnectionsPerHost(128);
        managerParams.setMaxTotalConnections(8192);
        this.connectionManager.setParams(managerParams);
        this.client = new HttpClient(this.connectionManager);
    }

    public HttpClient getHttpClient() {
        if (this.client != null) {
            return this.client;
        }
        init();
        return this.client;
    }

}
