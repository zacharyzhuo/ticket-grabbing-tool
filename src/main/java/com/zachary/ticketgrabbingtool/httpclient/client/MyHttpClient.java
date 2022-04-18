package com.zachary.ticketgrabbingtool.httpclient.client;

import com.zachary.ticketgrabbingtool.httpclient.model.HeaderModel;
import com.zachary.ticketgrabbingtool.httpclient.model.HttpClientResultModel;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;

@Component
public class MyHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(MyHttpClient.class);

    public HttpClientResultModel doGet(String url, String paramJson,
                                       HttpClientResultModel httpClientResultModel,
                                       HeaderModel headerModel) throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        if (headerModel.getCookieStore() != null) {
            context.setAttribute(HttpClientContext.COOKIE_STORE, headerModel.getCookieStore());
        }

        HttpGet request = null;
        if (paramJson == null || paramJson.isEmpty()) {
            request = new HttpGet(url);
        } else {
            URIBuilder builder = new URIBuilder(url);
            JSONObject paramJsonObject = new JSONObject(paramJson);
            Iterator<String> it = paramJsonObject.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                builder.setParameter(key, String.valueOf(paramJsonObject.get(key)));
            }

            request = new HttpGet(builder.build());
        }

        // 修改cookie策略，避免Invalid cookie header警告
//        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
//        request.setConfig(requestConfig);

        if (headerModel != null && headerModel.getHeaderList() != null) {
            for(HashMap<String, String> headerItem: headerModel.getHeaderList()) {
                request.setHeader(headerItem.get("key"), headerItem.get("value"));
            }
        }

        HttpResponse response = client.execute(request, context);

        httpClientResultModel.setResponse(response);
        httpClientResultModel.setContext(context);
        return httpClientResultModel;
    }

    public HttpClientResultModel doPost(String url, String jsonStr,
                                        HttpClientResultModel httpClientResultModel,
                                        HeaderModel headerModel) throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        if (headerModel.getCookieStore() != null) {
            context.setAttribute(HttpClientContext.COOKIE_STORE, headerModel.getCookieStore());
        }

        HttpPost request = new HttpPost(url);
        // 修改cookie策略，避免Invalid cookie header警告
//        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
//        request.setConfig(requestConfig);

        if (headerModel != null && headerModel.getHeaderList() != null) {
            for(HashMap<String, String> headerItem: headerModel.getHeaderList()) {
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

        HttpResponse response = client.execute(request, context);

        httpClientResultModel.setResponse(response);
        httpClientResultModel.setContext(context);
        return httpClientResultModel;
    }

    public void checkResponse(HttpResponse response, String errorMsg) throws Exception {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) throw new Exception(errorMsg);
        if (response.getHeaders("set-cookie") == null) throw new Exception("無法取得cookie");
        for (Header header: response.getHeaders("set-cookie")) {
            logger.info(header.getValue().toString());
        }
    }
}
