package com.zachary.ticketgrabbingtool.rent591.service;

import com.zachary.ticketgrabbingtool.httpclient.client.MyHttpClient;
import com.zachary.ticketgrabbingtool.httpclient.model.HeaderModel;
import com.zachary.ticketgrabbingtool.httpclient.model.HttpClientResultModel;
import com.zachary.ticketgrabbingtool.httpclient.model.HttpClientResultModel_Rent591;
import com.zachary.ticketgrabbingtool.rent591.model.PostModel;
import com.zachary.ticketgrabbingtool.rent591.model.PostRequestModel;
import com.zachary.ticketgrabbingtool.rent591.model.PostsModel;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.*;

@Service
public class Rent591Service {

    private static final Logger logger = LoggerFactory.getLogger(Rent591Service.class);

    @Value("${rent591.url}")
    private String URL;

    private final String USER_AGENT = "PostmanRuntime/7.29.0";

    @Autowired
    private MyHttpClient myHttpClient;

    public HttpClientResultModel_Rent591 initCookie() throws Exception {
        logger.info("初始化cookie");
        HeaderModel headerModel = new HeaderModel();
        headerModel.setHeaderList(
            new ArrayList<>(
                Arrays.asList(
                    new HashMap<String, String>(
                        // 加入User-Agent 不加會404
                        Map.of("key","User-Agent", "value", USER_AGENT)
                    )
                )
            )
        );
        HttpClientResultModel_Rent591 httpClientResultModel = (HttpClientResultModel_Rent591) myHttpClient.doGet(
                URL, null, new HttpClientResultModel_Rent591(), headerModel);
        String errorMsg = "無法進入首頁";
        myHttpClient.checkResponse(httpClientResultModel.getResponse(), errorMsg);
        return httpClientResultModel;
    }

    public HttpClientResultModel_Rent591 getPosts(HttpClientResultModel_Rent591 resourceHttpClientResultModel,
                                          PostRequestModel postRequestModel) throws Exception {
        logger.info("取得前30篇貼文");
        String url = URL + "/home/search/rsList";
        HeaderModel headerModel;
        PostsModel postsModel = resourceHttpClientResultModel.getPostsModel();

        if (postsModel == null) {
            headerModel = prepareHeaderModel(resourceHttpClientResultModel, false, null);
        } else {
            postRequestModel.setFirstRow(String.valueOf(postsModel.getFirstRow()));
            postRequestModel.setTotalRows(String.valueOf(postsModel.getTotalRows()));
            headerModel = prepareHeaderModel(resourceHttpClientResultModel, true, postsModel);
        }

        HttpClientResultModel_Rent591 httpClientResultModel = (HttpClientResultModel_Rent591) myHttpClient.doGet(
                url, postRequestModel.toString(), new HttpClientResultModel_Rent591(), headerModel);

        String responseString = EntityUtils.toString(
                httpClientResultModel.getResponse().getEntity(), StandardCharsets.UTF_8);
        JSONObject paramJsonObject = new JSONObject(responseString);
        JSONArray postList = (JSONArray) ((JSONObject) paramJsonObject.get("data")).get("data");

        if (postList.isEmpty() || postList.length() == 0) {
            throw new Exception("查無資料，請更換其他篩選條件");
        }

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

        if (postsModel == null) {
            int records = NumberFormat.getNumberInstance(
                    java.util.Locale.US).parse((String) paramJsonObject.get("records")).intValue();
            postsModel = new PostsModel();
            postsModel.setFirstRow(PostsModel.A_BATCH_POST_UNIT);
            postsModel.setTotalRows(records);
            postsModel.setPost(PostModelList);
            postsModel.setCsrfToken(headerModel.getHeaderList().get(0).get("value"));
        } else {
            int firstRow = postsModel.getFirstRow() + PostsModel.A_BATCH_POST_UNIT;
            postsModel.setFirstRow(firstRow);
            postsModel.addPost(PostModelList);
        }

        String errorMsg = "無法取得貼文";
        myHttpClient.checkResponse(httpClientResultModel.getResponse(), errorMsg);

        httpClientResultModel.setPostsModel(postsModel);
        return httpClientResultModel;
    }

    private HeaderModel prepareHeaderModel(HttpClientResultModel httpClientResultModel,
                                           boolean hasCsrfToken, PostsModel postsModel) throws Exception {
        HeaderModel headerModel = new HeaderModel();
        HttpResponse response = httpClientResultModel.getResponse();
        if (response == null) throw new Exception("response為空");

        String csrfToken = "";
        if (hasCsrfToken) {
            csrfToken = postsModel.getCsrfToken();
        } else {
            // get csrf-token
            String text = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            Document document = Jsoup.parse(text);
            csrfToken = document.select("meta[name=csrf-token]").attr("content");
        }

        List<HashMap<String, String>> headerList = new ArrayList<>();
        headerList.add(new HashMap<String, String>(Map.of("key","X-CSRF-TOKEN", "value", csrfToken)));
        headerList.add(new HashMap<String, String>(Map.of("key","User-Agent", "value", USER_AGENT)));

        headerModel.setCookieStore((BasicCookieStore) httpClientResultModel.getContext().getCookieStore());
        headerModel.setHeaderList(headerList);
        return headerModel;
    }

}
