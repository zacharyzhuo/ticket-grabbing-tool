package com.zachary.ticketgrabbingtool.commime.service;

import com.zachary.ticketgrabbingtool.httpclient.model.HeaderModel;
import com.zachary.ticketgrabbingtool.commime.model.ProductModel;
import com.zachary.ticketgrabbingtool.commime.model.UserModel;
import com.zachary.ticketgrabbingtool.httpclient.client.MyHttpClient;
import com.zachary.ticketgrabbingtool.httpclient.model.HttpClientResultModel;
import com.zachary.ticketgrabbingtool.httpclient.model.HttpClientResultModel_Commime;
import com.zachary.ticketgrabbingtool.resource.CONSTANT;
import com.zachary.ticketgrabbingtool.resource.ConfigReader;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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

    public HttpClientResultModel_Commime initCookie() throws Exception {
        logger.info("初始化cookie");
        HttpClientResultModel_Commime httpClientResultModel = (HttpClientResultModel_Commime) myHttpClient.doGet(
                URL, null, new HttpClientResultModel_Commime(), null);
        String errorMsg = "無法進入首頁";
        myHttpClient.checkResponse(httpClientResultModel.getResponse(), errorMsg);
        return httpClientResultModel;
    }

    public HttpClientResultModel_Commime signIn(HttpClientResultModel_Commime resourceHttpClientResultModel,
                                                UserModel userModel) throws Exception {
        logger.info("登入並取得新cookie");
        String url = URL + "/api/users/sign_in";
        HeaderModel headerModel = prepareHeaderModel(resourceHttpClientResultModel, true);

        userModel.getUser().setLocale_code(localeCode);

        HttpClientResultModel_Commime httpClientResultModel = (HttpClientResultModel_Commime) myHttpClient.doPost(
                url, userModel.toString(), new HttpClientResultModel_Commime(), headerModel);
        String errorMsg = "登入失敗";
        myHttpClient.checkResponse(httpClientResultModel.getResponse(), errorMsg);

        httpClientResultModel.setCsrfToken(headerModel.getHeaderList().get(0).get("value"));
        return httpClientResultModel;
    }

    private HttpClientResultModel_Commime addItem(HttpClientResultModel_Commime resourceHttpClientResultModel,
                                                 ProductModel productModel) throws Exception {
        String url = URL + "/api/merchants/606fc211e91c7400679f7d06/cart/items";
        HeaderModel headerModel = prepareHeaderModel(resourceHttpClientResultModel, false);

        HttpClientResultModel_Commime httpClientResultModel = (HttpClientResultModel_Commime) myHttpClient.doPost(
                url, productModel.toString(), new HttpClientResultModel_Commime(), headerModel);

        String errorMsg = "無法將" + productModel.getProduct_id() + "加入購物車";
        myHttpClient.checkResponse(httpClientResultModel.getResponse(), errorMsg);

        httpClientResultModel.setCsrfToken(headerModel.getHeaderList().get(0).get("value"));

        logger.info(EntityUtils.toString(httpClientResultModel.getResponse().getEntity(), StandardCharsets.UTF_8));
        logger.info("已成功將" + productModel.getProduct_id() + "加入購物車");
        return httpClientResultModel;
    }

    public HttpClientResultModel_Commime doAddItems(HttpClientResultModel_Commime resourceHttpClientResultModel,
                                                    List<ProductModel> products) throws Exception {
        HttpClientResultModel_Commime httpClientResultModel = resourceHttpClientResultModel;
        for (ProductModel product: products) {
            httpClientResultModel = addItem(httpClientResultModel, product);
        }
        return httpClientResultModel;
    }

    public HttpClientResultModel_Commime getCartInfo(
            HttpClientResultModel_Commime resourceHttpClientResultModel) throws Exception {
        logger.info("查詢購物車資訊");
        String url = URL + "/api/merchants/606fc211e91c7400679f7d06/cart";

        HeaderModel headerModel = prepareHeaderModel(resourceHttpClientResultModel, false);
        HttpClientResultModel_Commime httpClientResultModel = (HttpClientResultModel_Commime) myHttpClient.doGet(
                url, null, new HttpClientResultModel_Commime(), headerModel);

        String errorMsg = "無法查詢購物車資訊";
        myHttpClient.checkResponse(httpClientResultModel.getResponse(), errorMsg);

        httpClientResultModel.setCsrfToken(headerModel.getHeaderList().get(0).get("value"));

        logger.info(EntityUtils.toString(httpClientResultModel.getResponse().getEntity(), StandardCharsets.UTF_8));
        return httpClientResultModel;
    }

    public void signOut(HttpClientResultModel resourceHttpClientResultModel) throws Exception {
        logger.info("登出");
        String url = URL + "/signout";
        HeaderModel headerModel = prepareHeaderModel(resourceHttpClientResultModel, false);
        myHttpClient.doGet(url, null, new HttpClientResultModel_Commime(), headerModel);
    }

    private HeaderModel prepareHeaderModel(HttpClientResultModel httpClientResultModel,
                                           boolean isCsrfTokenGetFromHtml) throws Exception {
        HeaderModel headerModel = new HeaderModel();
        HttpResponse response = httpClientResultModel.getResponse();
        if (response == null) throw new Exception("response為空");

        String csrfToken = "";
        if (!isCsrfTokenGetFromHtml) {
            csrfToken = httpClientResultModel.getCsrfToken();
        } else {
            // get csrf-token
            String text = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            Document document = Jsoup.parse(text);
            csrfToken = document.select("meta[name=csrf-token]").attr("content");
        }

        BasicCookieStore cookieStore = new BasicCookieStore();
        String xsrfToken = "";
        for (Cookie cookie : httpClientResultModel.getContext().getCookieStore().getCookies()) {
            if (cookie.getName().equals("XSRF-TOKEN")) {
                xsrfToken = cookie.getValue();
            }
            cookieStore.addCookie(cookie);
        }

        if (csrfToken.equals("")) {
            throw new Exception("無法取得 CSRF-TOKEN");
        }

        if (xsrfToken.equals("")) {
            throw new Exception("無法取得 CSRF-TOKEN");
        }

        List<HashMap<String, String>> headerList = new ArrayList<>();
        headerList.add(new HashMap<String, String>(Map.of("key","x-csrf-token", "value", csrfToken)));
        headerList.add(new HashMap<String, String>(Map.of("key","x-xsrf-token", "value", xsrfToken)));

        headerModel.setCookieStore(cookieStore);
        headerModel.setHeaderList(headerList);
        return headerModel;
    }

}
