package com.zachary.ticketgrabbingtool.line.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.zachary.ticketgrabbingtool.line.client.MyLineMessagingClient;
import com.zachary.ticketgrabbingtool.line.model.EventModel;
import com.zachary.ticketgrabbingtool.resource.CONSTANT;
import com.zachary.ticketgrabbingtool.resource.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@LineMessageHandler
@RequestMapping
public class LineEventController {

    private static final Logger logger = LoggerFactory.getLogger(LineEventController.class);

    private static final String LINE_BOT_ADMIN_USERID = ConfigReader.getString(CONSTANT.LINE_BOT_ADMIN_USERID, true);

    @Autowired
    MyLineMessagingClient myLineMessagingClient;

    @EventMapping
    public TextMessage handleFollowEvent(FollowEvent event) {
        UserProfileResponse userProfileResponse = myLineMessagingClient.getUserProfile(event.getSource().getUserId());
        logger.info("[handleFollowEvent] user id: " + userProfileResponse.getUserId());
        String userName = userProfileResponse.getDisplayName();
        String message = "安安~ " + userName + " 我4胖達 嚶嚶";
        return new TextMessage(message);
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        logger.info("[handleTextMessageEvent] event: " + event);
        EventModel eventModel = new EventModel();
        eventModel.setUserId(event.getSource().getUserId());
        return new TextMessage(eventModel.toString());
    }

    @EventMapping
    public TextMessage handleDefaultMessageEvent(Event event) {
        logger.info("[handleDefaultMessageEvent] event: " + event);
        EventModel eventModel = new EventModel();
        eventModel.setUserId(event.getSource().getUserId());
        return new TextMessage(eventModel.toString());
    }
}
