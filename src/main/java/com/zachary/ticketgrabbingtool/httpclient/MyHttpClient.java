package com.zachary.ticketgrabbingtool.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class MyHttpClient {

    public HashMap<String, Object> doGet(String url, HashMap<String, Object> headerMap) throws Exception {
        // 存放response & context
        HashMap<String, Object> resMap = new HashMap<>();
        HttpClient client = null;
        if (headerMap != null) {
            BasicCookieStore cookieStore = (BasicCookieStore) headerMap.get("cookieStore");
            client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        } else {
            client = HttpClients.createDefault();
        }
        HttpClientContext context = HttpClientContext.create();
        HttpGet request = new HttpGet(url);

        // 修改cookie策略，避免Invalid cookie header警告
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        request.setConfig(requestConfig);

        if (headerMap != null && headerMap.containsKey("headerList") && headerMap.get("headerList") != null) {
            for(HashMap<String, String> headerItem: (List<HashMap>) headerMap.get("headerList")) {
                request.setHeader(headerItem.get("key"), headerItem.get("value"));
            }
        }

        resMap.put("response", client.execute(request, context));
        resMap.put("context", context);
        return resMap;
    }

    public HashMap<String, Object> doPost(String url, HashMap<String, Object> headerMap,
                                           String jsonStr) throws Exception {
        HashMap<String, Object> resMap = new HashMap<>();
        BasicCookieStore cookieStore = (BasicCookieStore) headerMap.get("cookieStore");
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        HttpClientContext context = HttpClientContext.create();
        HttpPost request = new HttpPost(url);

        // 修改cookie策略，避免Invalid cookie header警告
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        request.setConfig(requestConfig);

        if (headerMap.containsKey("headerList") && headerMap.get("headerList") != null) {
            for(HashMap<String, String> headerItem: (List<HashMap>) headerMap.get("headerList")) {
                request.setHeader(headerItem.get("key"), headerItem.get("value"));
            }
        }

        if (jsonStr != null) {
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");

            // set json params
            StringEntity entity = new StringEntity(jsonStr);
            request.setEntity(entity);
        }

        resMap.put("response", client.execute(request, context));
        resMap.put("context", context);
        return resMap;
    }

    public void checkResponse(HttpResponse response, String errorMsg) throws Exception {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) throw new Exception(errorMsg);
        if (response.getHeaders("set-cookie") == null) throw new Exception("無法取得cookie");
        System.out.println(response.getHeaders("set-cookie")[0].getValue());
    }
}
