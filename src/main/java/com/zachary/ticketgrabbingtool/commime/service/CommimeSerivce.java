package com.zachary.ticketgrabbingtool.commime.service;

import com.zachary.ticketgrabbingtool.commime.model.ProductModel;
import com.zachary.ticketgrabbingtool.commime.model.UserModel;
import com.zachary.ticketgrabbingtool.httpclient.MyHttpClient;
import com.zachary.ticketgrabbingtool.resource.CONSTANT;
import com.zachary.ticketgrabbingtool.resource.ConfigReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommimeSerivce {

    private static final Logger logger = LoggerFactory.getLogger(CommimeSerivce.class);

    private static final String URL = ConfigReader.getString(CONSTANT.COMMIME_URL);
    private static final String localeCode = ConfigReader.getString(CONSTANT.COMMIME_USER_LOCALECODE);

    @Autowired
    private MyHttpClient myHttpClient;

    public HashMap<String, Object> initCookie() throws Exception {
        logger.info("初始化cookie");
        HashMap<String, Object> resMap = myHttpClient.doGet(URL, null, null);
        HttpResponse response = (HttpResponse) resMap.get("response");
        String errorMsg = "無法進入首頁";
        myHttpClient.checkResponse(response, errorMsg);
        return resMap;
    }

    public HashMap<String, Object> signIn(HashMap<String, Object> sourceResMap, UserModel userModel) throws Exception {
        logger.info("登入並取得新cookie");
        String url = URL + "/api/users/sign_in";
        HashMap<String, Object> headerMap = prepareHeaderParams(sourceResMap);

        userModel.getUser().setLocale_code(localeCode);

        HashMap<String, Object> resMap = myHttpClient.doPost(url, headerMap, userModel.toString());
        HttpResponse response = (HttpResponse) resMap.get("response");
        String errorMsg = "登入失敗";
        myHttpClient.checkResponse(response, errorMsg);
        return resMap;
    }

    public HashMap<String, Object> addItem(HashMap<String, Object> sourceResMap, ProductModel productModel) throws Exception {
        String url = URL + "/api/merchants/606fc211e91c7400679f7d06/cart/items";
        HashMap<String, Object> headerMap = prepareHeaderParams(sourceResMap);
        HashMap<String, Object> resMap = myHttpClient.doPost(url, headerMap, productModel.toString());
        HttpResponse response = (HttpResponse) resMap.get("response");
        String errorMsg = "無法將" + productModel.getProduct_id() + "加入購物車";
        myHttpClient.checkResponse(response, errorMsg);
        logger.info(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
        logger.info("已成功將" + productModel.getProduct_id() + "加入購物車");
        return resMap;
    }

    public HashMap<String, Object> doAddItems(HashMap<String, Object> sourceResMap) throws Exception {
        logger.info("將商品加入購物車");
        File jsonFile = new ClassPathResource("/commime/product.json").getFile();
        String jsonText = new String(Files.readAllBytes(jsonFile.toPath()));
        JSONArray jsonArr = new JSONArray(jsonText);

        HashMap<String, Object> resMap = sourceResMap;
        for (int i = 0; i < jsonArr.length(); i++) {
            ProductModel productModel = new ProductModel();
            JSONObject productJson = (JSONObject) jsonArr.get(i);
            productModel.setProduct_id((String) productJson.get("product_id"));
            productModel.setQuantity(((Long) productJson.get("quantity")).intValue());
            resMap = addItem(resMap, productModel);
        }
        return resMap;
    }

    public HashMap<String, Object> getCartInfo(HashMap<String, Object> sourceResMap) throws Exception {
        logger.info("查詢購物車資訊");
        String url = URL + "/api/merchants/606fc211e91c7400679f7d06/cart";
        HashMap<String, Object> headerMap = prepareHeaderParams(sourceResMap);
        HashMap<String, Object> resMap = myHttpClient.doGet(URL, null, headerMap);
        HttpResponse response = (HttpResponse) resMap.get("response");
        String errorMsg = "無法查詢購物車資訊";
        myHttpClient.checkResponse(response, errorMsg);
//        logger.info("CommimeSerivce: " + EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
        return resMap;
    }

    public void signOut(HashMap<String, Object> sourceResMap) throws Exception {
        logger.info("登出");
        String url = URL + "/signout";
        HashMap<String, Object> headerMap = prepareHeaderParams(sourceResMap);
        HashMap<String, Object> resMap = myHttpClient.doGet(url, null, headerMap);
        HttpResponse response = (HttpResponse) resMap.get("response");
//        String errorMsg = "登出失敗";
//        myHttpClient.checkResponse(response, errorMsg);
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
            if (cookie.getName().equals("XSRF-TOKEN")) {
                xsrfToken = cookie.getValue();
            }
            cookieStore.addCookie(cookie);
        }

        headerList.add(new HashMap<String, String>(Map.of("key","x-csrf-token", "value", csrfToken)));
        headerList.add(new HashMap<String, String>(Map.of("key","x-xsrf-token", "value", xsrfToken)));

        headerMap.put("cookieStore", cookieStore);
        headerMap.put("headerList", headerList);
        return headerMap;
    }

}
