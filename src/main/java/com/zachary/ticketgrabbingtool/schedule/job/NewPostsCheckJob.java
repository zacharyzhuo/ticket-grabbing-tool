package com.zachary.ticketgrabbingtool.schedule.job;

import com.zachary.ticketgrabbingtool.line.client.MyLineMessagingClient;
import com.zachary.ticketgrabbingtool.line.model.FlexMessageModel;
import com.zachary.ticketgrabbingtool.line.model.IMessageModel;
import com.zachary.ticketgrabbingtool.line.model.PushModel;
import com.zachary.ticketgrabbingtool.line.model.flex.*;
import com.zachary.ticketgrabbingtool.rent591.cache.CacheDelegate;
import com.zachary.ticketgrabbingtool.rent591.model.PostModel;
import com.zachary.ticketgrabbingtool.rent591.model.PostRequestModel;
import com.zachary.ticketgrabbingtool.rent591.model.PostsModel;
import com.zachary.ticketgrabbingtool.rent591.service.Rent591Service;
import com.zachary.ticketgrabbingtool.resource.CONSTANT;
import com.zachary.ticketgrabbingtool.resource.ConfigReader;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

public class NewPostsCheckJob implements org.quartz.Job {

    private Logger logger = LoggerFactory.getLogger(NewPostsCheckJob.class);

    public static final int priority = 5;

    private static final String LINE_BOT_ADMIN_USERID = ConfigReader.getString(CONSTANT.LINE_BOT_ADMIN_USERID, true);
    private static final String LINE_BOT_WAN_USERID = ConfigReader.getString(CONSTANT.LINE_BOT_WAN_USERID, true);

    private static final String ALTER_TEXT = "最新租屋資訊來囉~";
    private static final String NULL_POST_MESSAGE = "目前沒有最新貼文~";

    @Autowired
    Rent591Service rent591Service;

    @Autowired
    MyLineMessagingClient myLineMessagingClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("NewPostsCheckJob: " + context.getJobDetail().getKey().getName() + " start.");

        PostRequestModel postRequestModel = preparePostRequestModel();
        PostsModel postsModel = getPosts(postRequestModel);

        if (postsModel != null) {
            List<PostModel> posts = filter(postsModel);
            List<BubbleModel> bubbles = null;

            if (posts.size() == 0) {
                bubbles = buildNullPostMessageBubble();
            } else {
                bubbles = rent591Service.buildBubbles(posts);
            }

            PushModel pushModel = rent591Service.buildPushModel(bubbles);

            List<String> pushTargets = new ArrayList<>();
            pushTargets.add(LINE_BOT_ADMIN_USERID);
            pushTargets.add(LINE_BOT_WAN_USERID);

            for (String to : pushTargets) {
                pushModel.setTo(to);
                pushLineMessage(pushModel);
            }
        }
    }

    private PostRequestModel preparePostRequestModel() {
        PostRequestModel postRequestModel = new PostRequestModel();
        postRequestModel.setIs_format_data("1");
        postRequestModel.setIs_new_list("1");
        postRequestModel.setType("1");
        postRequestModel.setRegion("1");

        postRequestModel.setKind("2");
        postRequestModel.setSection("3,5,7,4");
        postRequestModel.setRentprice("13000,20000");
        postRequestModel.setMultiNotice("all_sex,not_cover");
        postRequestModel.setShape("1,2");
        postRequestModel.setArea("8,15");
//        postRequestModel.setOrder("posttime");
//        postRequestModel.setOrderType("desc");

        return postRequestModel;
    }

    private PostsModel getPosts(PostRequestModel postRequestModel) {
        try {
            return rent591Service.getPosts(postRequestModel).getPostsModel();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    private List<PostModel> filter(PostsModel postsModel) {
        List<PostModel> posts = new ArrayList<>();

        for (PostModel post : postsModel.getPosts()) {
            PostModel newPost = CacheDelegate.getInstance().getPostModel(String.valueOf(post.getPostId()));
            if (newPost == null) {
                posts.add(post);
                CacheDelegate.getInstance().putPostModel(String.valueOf(post.getPostId()), post);
            }

            // 只取10個
            if (posts.size() == 10) {
                break;
            }
        }
        return posts;
    }

    private List<BubbleModel> buildNullPostMessageBubble() {
        TextModel text = new TextModel();
        text.setType(TextModel.TYPE);
        text.setText(NULL_POST_MESSAGE);
        text.setWeight(TextModel.WEIGHT.get(0));
        text.setAlign(TextModel.ALIGN.get(0));

        BoxModel body = new BoxModel();
        body.setType(BoxModel.TYPE);
        body.setLayout(BoxModel.LAYOUT.get(0));
        body.setContents(new ArrayList<IBoxContent>(Arrays.asList(text)));

        BubbleModel bubbleModel = new BubbleModel();
        bubbleModel.setType(BubbleModel.TYPE);
        bubbleModel.setBody(body);

        List<BubbleModel> bubbles = new ArrayList<>();
        bubbles.add(bubbleModel);

        return bubbles;
    }

    private PushModel mockBuildCarouselMsg() {
        TextModel text = new TextModel();
        text.setType(TextModel.TYPE);
        text.setText("test");

        BoxModel body = new BoxModel();
        body.setType(BoxModel.TYPE);
        body.setLayout(BoxModel.LAYOUT.get(0));
        body.setContents(new ArrayList<IBoxContent>(Arrays.asList(text)));

        BubbleModel bubbleModel = new BubbleModel();
        bubbleModel.setType(BubbleModel.TYPE);
        bubbleModel.setBody(body);

        CarouselModel carouselModel = new CarouselModel();
        carouselModel.setType(CarouselModel.TYPE);
        carouselModel.setContents(new ArrayList<BubbleModel>(Arrays.asList(bubbleModel)));

        FlexMessageModel flexMessageModel = new FlexMessageModel();
        flexMessageModel.setType(FlexMessageModel.TYPE);
        flexMessageModel.setAltText(ALTER_TEXT);
        flexMessageModel.setContents(carouselModel);

        PushModel pushModel = new PushModel();
        pushModel.setMessages(new ArrayList<IMessageModel>(Arrays.asList(flexMessageModel)));

        return pushModel;
    }

    private PushModel mockBuildBubbleMsg() {
        TextModel text = new TextModel();
        text.setType(TextModel.TYPE);
        text.setText("test");

        BoxModel body = new BoxModel();
        body.setType(BoxModel.TYPE);
        body.setLayout(BoxModel.LAYOUT.get(0));
        body.setContents(new ArrayList<IBoxContent>(Arrays.asList(text)));

        BubbleModel bubbleModel = new BubbleModel();
        bubbleModel.setType(BubbleModel.TYPE);
        bubbleModel.setBody(body);

        FlexMessageModel flexMessageModel = new FlexMessageModel();
        flexMessageModel.setType(FlexMessageModel.TYPE);
        flexMessageModel.setAltText(ALTER_TEXT);
        flexMessageModel.setContents(bubbleModel);

        PushModel pushModel = new PushModel();
        pushModel.setMessages(new ArrayList<IMessageModel>(Arrays.asList(flexMessageModel)));

        return pushModel;
    }

    private void pushLineMessage (PushModel pushModel) {
        try {
            myLineMessagingClient.push(pushModel);
        } catch (Exception e) {
            logger.error("Failed to push line message: {}", e.getMessage());
        }
    }

}
