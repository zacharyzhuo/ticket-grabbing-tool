package com.zachary.ticketgrabbingtool.line.client;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.zachary.ticketgrabbingtool.httpclient.client.MyHttpClient;
import com.zachary.ticketgrabbingtool.httpclient.model.HeaderModel;
import com.zachary.ticketgrabbingtool.httpclient.model.HttpClientResultModel_LineBot;
import com.zachary.ticketgrabbingtool.line.model.PushModel;
import com.zachary.ticketgrabbingtool.resource.CONSTANT;
import com.zachary.ticketgrabbingtool.resource.ConfigReader;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class MyLineMessagingClient {

    private static final Logger logger = LoggerFactory.getLogger(MyLineMessagingClient.class);

    private static final String CHANNEL_TOKEN = ConfigReader.getString(CONSTANT.LINE_BOT_CHANNEL_TOKEN, true);
    private static final String URL = ConfigReader.getString(CONSTANT.LINE_BOT_URL);

    @Autowired
    MyHttpClient myHttpClient;

    public UserProfileResponse getUserProfile(String userId) {
        logger.info("查詢[{}]user profile...", userId);
        final LineMessagingClient client = LineMessagingClient
                .builder(CHANNEL_TOKEN)
                .build();

        final UserProfileResponse userProfileResponse;
        try {
            userProfileResponse = client.getProfile(userId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        return userProfileResponse;
    }

    public void push(PushModel pushModel) throws Exception {
        String url = URL + "/message/push";
        String token = "Bearer " + CHANNEL_TOKEN;
        HeaderModel headerModel = new HeaderModel();
        headerModel.setHeaderList(new ArrayList<>(Arrays.asList(
                new HashMap<String, String>(
                        // 加入User-Agent 不加會404
                        Map.of("key","Authorization", "value", token)
                )
        )));

        HttpClientResultModel_LineBot httpClientResultModel = (HttpClientResultModel_LineBot) myHttpClient.doPost(
                url, pushModel.toString(), new HttpClientResultModel_LineBot(), headerModel);

        HttpResponse response = httpClientResultModel.getResponse();
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != HttpStatus.SC_OK) {
            String resStr = EntityUtils.toString(
                    httpClientResultModel.getResponse().getEntity(), StandardCharsets.UTF_8);
            throw new Exception(resStr);
        } else {
            logger.info("成功推播訊息給[{}]", pushModel.getTo());
        }

    }
}
