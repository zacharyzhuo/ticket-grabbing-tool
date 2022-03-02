package com.zachary.ticketgrabbingtool.commime.service;

import com.zachary.ticketgrabbingtool.commime.model.ProductModel;
import com.zachary.ticketgrabbingtool.commime.model.UserDetailModel;
import com.zachary.ticketgrabbingtool.commime.model.UserModel;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

@Service
public class CommimeSerivce {

    private static String URL = "https://www.commi.me";

    public HashMap<String, Object> initCookie() throws Exception {
        System.out.println("-> 初始化cookie");
        HashMap<String, Object> resMap = doGet(URL, null);
        HttpResponse response = (HttpResponse) resMap.get("response");
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) throw new Exception("無法進入首頁");
        return resMap;
    }

    public HashMap<String, Object> signIn(HashMap<String, Object> sourceResMap) throws Exception {
        System.out.println("-> 登入並取得新cookie");
        String url = URL + "/api/users/sign_in";
        HashMap<String, Object> headerMap = prepareHeaderParams(sourceResMap);

        File jsonFile = new ClassPathResource("/commime/user.json").getFile();
        String jsonText = new String(Files.readAllBytes(jsonFile.toPath()));
        JSONObject json = (JSONObject) new JSONParser().parse(jsonText);
        JSONObject jsonUser = (JSONObject) json.get("user");

        UserDetailModel userDetailModel = new UserDetailModel();
        userDetailModel.setLocale_code((String) jsonUser.get("locale_code"));
        userDetailModel.setPassword((String) jsonUser.get("password"));
        userDetailModel.setMobile_phone_or_email((String) jsonUser.get("mobile_phone_or_email"));
        UserModel userModel = new UserModel();
        userModel.setUser(userDetailModel);
        System.out.println(userModel.toString());

        HashMap<String, Object> resMap = doPost(url, headerMap, userModel.toString());
        HttpResponse response = (HttpResponse) resMap.get("response");

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) throw new Exception("登入失敗");
        if (response.getHeaders("set-cookie") == null) throw new Exception("無法取得cookie");
        System.out.println(response.getHeaders("set-cookie")[0].getValue());

        return resMap;
    }

    public HashMap<String, Object> addItem(HashMap<String, Object> sourceResMap, ProductModel productModel) throws Exception {
        String url = URL + "/api/merchants/606fc211e91c7400679f7d06/cart/items";
        HashMap<String, Object> headerMap = prepareHeaderParams(sourceResMap);
        HashMap<String, Object> resMap = doPost(url, headerMap, productModel.toString());
        HttpResponse response = (HttpResponse) resMap.get("response");

        System.out.println(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) throw new Exception("無法將" + productModel.getProduct_id() + "加入購物車");
        System.out.println("已成功將" + productModel.getProduct_id() + "加入購物車");
        return resMap;
    }

    public HashMap<String, Object> doAddItems(HashMap<String, Object> sourceResMap) throws Exception {
        System.out.println("-> 將商品加入購物車");
        File jsonFile = new ClassPathResource("/commime/product.json").getFile();
        String jsonText = new String(Files.readAllBytes(jsonFile.toPath()));
        JSONArray jsonArr = (JSONArray) new JSONParser().parse(jsonText);

        HashMap<String, Object> resMap = sourceResMap;
        for (int i = 0; i < jsonArr.size(); i++) {
            ProductModel productModel = new ProductModel();
            JSONObject productJson = (JSONObject) jsonArr.get(i);
            productModel.setProduct_id((String) productJson.get("product_id"));
            productModel.setQuantity(((Long) productJson.get("quantity")).intValue());
            resMap = addItem(resMap, productModel);
        }
        return resMap;
    }

    public HashMap<String, Object> getCartInfo(HashMap<String, Object> sourceResMap) throws Exception {
        System.out.println("-> 查詢購物車資訊");
        String url = URL + "/api/merchants/606fc211e91c7400679f7d06/cart";
        HashMap<String, Object> headerMap = prepareHeaderParams(sourceResMap);
        HashMap<String, Object> resMap = doGet(URL, headerMap);
        HttpResponse response = (HttpResponse) resMap.get("response");
//        System.out.println(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
        System.out.println(response.getStatusLine());
//        int statusCode = response.getStatusLine().getStatusCode();
//        if (statusCode != HttpStatus.SC_OK) throw new Exception("無法進入首頁");
        return resMap;
    }

    public void signOut(HashMap<String, Object> sourceResMap) throws Exception {
        System.out.println("-> 登出");
        String url = URL + "/signout";
        HashMap<String, Object> headerMap = prepareHeaderParams(sourceResMap);

        HashMap<String, Object> resMap = doGet(url, headerMap);
        HttpResponse response = (HttpResponse) resMap.get("response");

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) throw new Exception("登出失敗");
        System.out.println(response.getStatusLine());
    }

    private HashMap<String, Object> doGet(String url, HashMap<String, Object> headerMap) throws Exception {
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

        resMap.put("response", client.execute(request, context));
        resMap.put("context", context);

        return resMap;
    }

    private HashMap<String, Object> doPost(String url, HashMap<String, Object> headerMap,
                                          String jsonStr) throws Exception {
        HashMap<String, Object> resMap = new HashMap<>();
        BasicCookieStore cookieStore = (BasicCookieStore) headerMap.get("cookieStore");
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        HttpClientContext context = HttpClientContext.create();
        HttpPost request = new HttpPost(url);

        // 修改cookie策略，避免Invalid cookie header警告
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        request.setConfig(requestConfig);

        request.setHeader("x-csrf-token", (String) headerMap.get("x-csrf-token"));
        request.setHeader("x-xsrf-token", (String) headerMap.get("x-xsrf-token"));

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

    private HashMap<String, Object> prepareHeaderParams(HashMap<String, Object> resMap) throws Exception {
        HashMap<String, Object> headerMap = new HashMap<>();

        HttpResponse response = (HttpResponse) resMap.get("response");
        if (response == null) throw new Exception("response為空");

        // get csrf-token
        String text = EntityUtils.toString(response.getEntity(), "utf-8");
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

        headerMap.put("cookieStore", cookieStore);
        headerMap.put("x-csrf-token", csrfToken);
        headerMap.put("x-xsrf-token", xsrfToken);

        return headerMap;
    }
}
