package com.zachary.ticketgrabbingtool.rent591.service;

import com.zachary.ticketgrabbingtool.httpclient.MyHttpClient;
import com.zachary.ticketgrabbingtool.rent591.model.PostModel;
import com.zachary.ticketgrabbingtool.rent591.model.PostRequestModel;
import com.zachary.ticketgrabbingtool.rent591.model.PostsModel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class Rent591Service {

    @Value("${rent591.url}")
    private String URL;

    private final String USER_AGENT = "PostmanRuntime/7.29.0";

    @Autowired
    private MyHttpClient myHttpClient;

    public HashMap<String, Object> initCookie() throws Exception {
        System.out.println("-> 初始化cookie");
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put("headerList", new ArrayList<>(
            Arrays.asList(new HashMap<String, String>(
                    // 加入User-Agent 不加會404
                    Map.of("key","User-Agent", "value", USER_AGENT)))));
        HashMap<String, Object> resMap = myHttpClient.doGet(URL, null, headerMap);
        HttpResponse response = (HttpResponse) resMap.get("response");
        String errorMsg = "無法進入首頁";
        myHttpClient.checkResponse(response, errorMsg);
        return resMap;
    }

    public HashMap<String, Object> getPosts(HashMap<String, Object> sourceResMap, PostRequestModel postRequestModel) throws Exception {
        System.out.println("-> 取得前30篇貼文");
        String url = URL + "/home/search/rsList";
        PostsModel postsModel;

        if (sourceResMap.containsKey("postsModel")) {
            postsModel = (PostsModel) sourceResMap.get("postsModel");
            postRequestModel.setFirstRow(String.valueOf(postsModel.getFirstRow()));
            postRequestModel.setTotalRows(String.valueOf(postsModel.getTotalRows()));
        }

        HashMap<String, Object> headerMap = prepareHeaderParams(sourceResMap);
        HashMap<String, Object> resMap = myHttpClient.doGet(url, postRequestModel.toString(), headerMap);

        HttpResponse response = (HttpResponse) resMap.get("response");
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        JSONObject paramJsonObject = new JSONObject(responseString);
        JSONArray postList = (JSONArray) ((JSONObject) paramJsonObject.get("data")).get("data");

        List<PostModel> PostModelList = new ArrayList<>();
        postList.forEach(postInfoObject -> {
            JSONObject postInfo = (JSONObject) postInfoObject;
            PostModel postModel = new PostModel();
            postModel.setPostId((int) postInfo.get("post_id"));
            postModel.setTitle((String) postInfo.get("title"));
            postModel.setKind((String) postInfo.get("kind_name"));
            postModel.setFloor((String) postInfo.get("floor_str"));
            postModel.setPrice((String) postInfo.get("price"));
            postModel.setArea((String) postInfo.get("area"));
            postModel.setSection((String) postInfo.get("section_name"));
            postModel.setStreet((String) postInfo.get("street_name"));
            postModel.setLocation((String) postInfo.get("location"));

            List<String> photoUrlList = new ArrayList<>();
            for (Object photoURL : (JSONArray) postInfo.get("photo_list")) {
                photoUrlList.add((String) photoURL);
            }
            postModel.setPhotoList(photoUrlList);
            PostModelList.add(postModel);
        });

        if (!sourceResMap.containsKey("postsModel")) {
            int records = Integer.valueOf((String) paramJsonObject.get("records"));
            postsModel = new PostsModel();
            postsModel.setFirstRow(PostsModel.A_BATCH_POST_UNIT);
            postsModel.setTotalRows(records);
            postsModel.setPost(PostModelList);
        } else {
            postsModel = (PostsModel) sourceResMap.get("postsModel");
            int firstRow = postsModel.getFirstRow() + PostsModel.A_BATCH_POST_UNIT;
            postsModel.setFirstRow(firstRow);
            postsModel.addPost(PostModelList);
        }

        String errorMsg = "無法取得貼文";
        myHttpClient.checkResponse(response, errorMsg);

        resMap.put("postsModel", postsModel);
        return resMap;
    }

    private HashMap<String, Object> prepareHeaderParams(HashMap<String, Object> resMap) throws Exception {
        HashMap<String, Object> headerMap = new HashMap<>();
        List<HashMap<String, String>> headerList = new ArrayList<>();

        HttpResponse response = (HttpResponse) resMap.get("response");
        if (response == null) throw new Exception("response為空");

        // get csrf-token
        String text = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        Document document = Jsoup.parse(text);
        String csrfToken = document.select("meta[name=csrf-token]").attr("content");

        HttpClientContext context = (HttpClientContext) resMap.get("context");
        BasicCookieStore cookieStore = new BasicCookieStore();
        String xsrfToken = "";
        for (Cookie cookie : context.getCookieStore().getCookies()) {
            cookieStore.addCookie(cookie);
        }

        headerList.add(new HashMap<String, String>(Map.of("key","X-CSRF-TOKEN", "value", csrfToken)));
        headerList.add(new HashMap<String, String>(Map.of("key","User-Agent", "value", USER_AGENT)));

        headerMap.put("cookieStore", cookieStore);
        headerMap.put("headerList", headerList);
        return headerMap;
    }

}
