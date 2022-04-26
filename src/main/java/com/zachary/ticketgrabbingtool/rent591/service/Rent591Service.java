package com.zachary.ticketgrabbingtool.rent591.service;

import com.zachary.ticketgrabbingtool.httpclient.client.MyHttpClient;
import com.zachary.ticketgrabbingtool.httpclient.model.HeaderModel;
import com.zachary.ticketgrabbingtool.httpclient.model.HttpClientResultModel_Rent591;
import com.zachary.ticketgrabbingtool.line.model.FlexMessageModel;
import com.zachary.ticketgrabbingtool.line.model.IMessageModel;
import com.zachary.ticketgrabbingtool.line.model.PushModel;
import com.zachary.ticketgrabbingtool.line.model.flex.*;
import com.zachary.ticketgrabbingtool.rent591.model.PostModel;
import com.zachary.ticketgrabbingtool.rent591.model.PostRequestModel;
import com.zachary.ticketgrabbingtool.rent591.model.PostsModel;
import com.zachary.ticketgrabbingtool.resource.CONSTANT;
import com.zachary.ticketgrabbingtool.resource.ConfigReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.*;

@Service
public class Rent591Service {

    private static final Logger logger = LoggerFactory.getLogger(Rent591Service.class);

    private static final String URL = ConfigReader.getString(CONSTANT.RENT_591_URL);
    private static final String USER_AGENT = ConfigReader.getString(CONSTANT.USER_AGENT);
    private static final String RENT_591_URLJUMPIP = ConfigReader.getString(CONSTANT.RENT_591_URLJUMPIP);
    private static final String RENT_591_URL = ConfigReader.getString(CONSTANT.RENT_591_URL);
    private static final String RENT_591_NOTFOUND_IMAGE = ConfigReader.getString(CONSTANT.RENT_591_NOTFOUND_IMAGE);

    private static final String ALTER_TEXT = "最新租屋資訊來囉~";

    @Autowired
    private MyHttpClient myHttpClient;

    public HttpClientResultModel_Rent591 getPosts(PostRequestModel postRequestModel) throws Exception {
        HttpClientResultModel_Rent591 httpClientResultModel = null;
        httpClientResultModel = initCookie();
        httpClientResultModel = customCookie(httpClientResultModel);
        httpClientResultModel = getPostsData(httpClientResultModel, postRequestModel);
        return httpClientResultModel;
    }

    public HttpClientResultModel_Rent591 getAllPosts(PostRequestModel postRequestModel) throws Exception {
        HttpClientResultModel_Rent591 httpClientResultModel = null;
        httpClientResultModel = getPosts(postRequestModel);
        PostsModel postsModel = httpClientResultModel.getPostsModel();
        int n = (int) Math.ceil((double) (postsModel.getTotalRows()/postsModel.getFirstRow()));

        for (int i = 1; i < n; i++) {
            httpClientResultModel = getPostsData(httpClientResultModel, postRequestModel);
        }

        return httpClientResultModel;
    }

    public List<BubbleModel> buildBubbles(List<PostModel> posts) {
//        logger.info("[top 10 posts]: {}", posts);
        List<BubbleModel> bubbles = new ArrayList<>();
        HashMap<String, TextModel> textTitleMap = new HashMap<>();
        List<TextModel> texts = new ArrayList<>();

        for (int i = 0; i < TextModel.TITLE_KEY.size(); i++) {
            TextModel textModel = new TextModel();
            textModel.setType(TextModel.TYPE);
            textModel.setText(TextModel.TITLE_VALUE.get(i));
            textModel.setColor(TextModel.COLOR.get(0));
            textModel.setSize(TextModel.SIZE.get(1));
            textModel.setFlex(1);
            textTitleMap.put(TextModel.TITLE_KEY.get(i), textModel);
        }

        // each bubble
        for(PostModel post: posts) {
            String aspectRatio = "150:98";
            List<ImageModel> images = new ArrayList<>();
            HashMap<String, BoxModel> boxMap = new HashMap<>();

            BoxModel imageVerticalBox = new BoxModel();
            imageVerticalBox.setType(BoxModel.TYPE);
            imageVerticalBox.setLayout(BoxModel.LAYOUT.get(0));
            imageVerticalBox.setContents(new ArrayList<IBoxContent>());

            int img_index = 0;
            for (int i = 0; i < 2; i++) {
                BoxModel imageHorizontalBox = new BoxModel();
                imageHorizontalBox.setType(BoxModel.TYPE);
                imageHorizontalBox.setLayout(BoxModel.LAYOUT.get(1));
                imageHorizontalBox.setContents(new ArrayList<IBoxContent>());

                for (int j = 0; j < 2; j++) {
                    ImageModel image = new ImageModel();
                    try {
                        image.setUrl(post.getPhotoList().get(img_index++));
                    } catch (IndexOutOfBoundsException e) {
                        image.setUrl(RENT_591_NOTFOUND_IMAGE);
                    } finally {
                        image.setType(ImageModel.TYPE);
                        image.setGravity(ImageModel.GRAVITY.get(0));
                        image.setSize(ImageModel.SIZE.get(0));
                        image.setAspectMode(ImageModel.ASPECT_MODE.get(0));
                        image.setAspectRatio(aspectRatio);
                    }
                    imageHorizontalBox.addContents(image);
                }
                imageVerticalBox.addContents(imageHorizontalBox);
            }

            BoxModel allTextBox = new BoxModel();
            allTextBox.setType(BoxModel.TYPE);
            allTextBox.setLayout(BoxModel.LAYOUT.get(0));
            allTextBox.setMargin(BoxModel.MARGIN.get(0));
            allTextBox.setSpacing(BoxModel.SPACING.get(0));
            allTextBox.setPaddingStart("20px");
            allTextBox.setPaddingBottom("20px");
            allTextBox.setContents(new ArrayList<IBoxContent>());

            for (String titleKey : TextModel.TITLE_KEY) {
                TextModel textModel = new TextModel();
                textModel.setType(TextModel.TYPE);
                if (titleKey.equals("price")) {
                    textModel.setText(post.getPrice());
                } else if (titleKey.equals("address")) {
                    textModel.setText(post.getLocation());
                } else if (titleKey.equals("section")) {
                    textModel.setText(post.getSection());
                } else if (titleKey.equals("kind")) {
                    textModel.setText(post.getKind());
                } else if (titleKey.equals("floor")) {
                    textModel.setText(post.getFloor());
                } else if (titleKey.equals("area")) {
                    textModel.setText(post.getArea());
                }
                textModel.setWrap(true);
                textModel.setColor(TextModel.COLOR.get(1));
                textModel.setFlex(5);

                BoxModel textBox = new BoxModel();
                textBox.setType(BoxModel.TYPE);
                textBox.setLayout(BoxModel.LAYOUT.get(2));
                textBox.setContents(new ArrayList<IBoxContent>(Arrays.asList(
                        textTitleMap.get(titleKey), textModel)));

                allTextBox.addContents(textBox);
            }

            TextModel title = new TextModel();
            title.setType(TextModel.TYPE);
            title.setText(post.getTitle());
            title.setWeight(TextModel.WEIGHT.get(0));
            title.setSize(TextModel.SIZE.get(0));

            BoxModel titleVerticalBox = new BoxModel();
            titleVerticalBox.setType(BoxModel.TYPE);
            titleVerticalBox.setLayout(BoxModel.LAYOUT.get(0));
            titleVerticalBox.setPaddingStart("20px");
            titleVerticalBox.setPaddingTop("20px");
            titleVerticalBox.setContents(new ArrayList<IBoxContent>(Arrays.asList(title)));

            ActionModel actionModel = new ActionModel();
            actionModel.setType(ActionModel.TYPE);
            actionModel.setLabel("Website");
            actionModel.setUri(RENT_591_URL+"/home/"+post.getPostId());

            BoxModel body = new BoxModel();
            body.setType(BoxModel.TYPE);
            body.setLayout(BoxModel.LAYOUT.get(0));
            body.setPaddingAll("0px");
            body.setAction(actionModel);
            body.setContents(new ArrayList<IBoxContent>(Arrays.asList(
                    imageVerticalBox, titleVerticalBox, allTextBox)));

            BubbleModel bubbleModel = new BubbleModel();
            bubbleModel.setType(BubbleModel.TYPE);
            bubbleModel.setBody(body);

            bubbles.add(bubbleModel);
        }

        return bubbles;
    }

    public PushModel buildPushModel(List<BubbleModel> bubbles) {
        CarouselModel carouselModel = new CarouselModel();
        carouselModel.setType(CarouselModel.TYPE);
        carouselModel.setContents(bubbles);

        FlexMessageModel flexMessageModel = new FlexMessageModel();
        flexMessageModel.setType(FlexMessageModel.TYPE);
        flexMessageModel.setAltText(ALTER_TEXT);
        flexMessageModel.setContents(carouselModel);

        PushModel pushModel = new PushModel();
        pushModel.setMessages(new ArrayList<IMessageModel>(Arrays.asList(flexMessageModel)));

        return pushModel;
    }

    private HttpClientResultModel_Rent591 homePage(HttpClientResultModel_Rent591 resourceHttpClientResultModel)
            throws Exception {
        HeaderModel headerModel = null;

        if (resourceHttpClientResultModel == null) {
            headerModel = new HeaderModel();
            headerModel.setHeaderList(new ArrayList<>(Arrays.asList(
                    new HashMap<String, String>(
                            // 加入User-Agent 不加會404
                            Map.of("key","User-Agent", "value", USER_AGENT)
                    )
            )));
        } else {
            headerModel = prepareHeaderModel(resourceHttpClientResultModel);
            BasicCookieStore cookieStore = new BasicCookieStore();
            for (Cookie cookie : headerModel.getCookieStore().getCookies()) {
                BasicClientCookie myCookie = null;
                if (cookie.getName().equals("urlJumpIp")) {
                    myCookie = new BasicClientCookie(cookie.getName(), RENT_591_URLJUMPIP);
                } else {
                    myCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
                }
                myCookie.setExpiryDate(cookie.getExpiryDate());
                myCookie.setDomain(cookie.getDomain());
                myCookie.setPath(cookie.getPath());
                myCookie.setVersion(cookie.getVersion());
                myCookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
                cookieStore.addCookie(myCookie);
            }
            headerModel.setCookieStore(cookieStore);
        }

        HttpClientResultModel_Rent591 httpClientResultModel = (HttpClientResultModel_Rent591) myHttpClient.doGet(
                URL, null, new HttpClientResultModel_Rent591(), headerModel);
        String errorMsg = "無法進入首頁";
        myHttpClient.checkResponse(httpClientResultModel.getResponse(), errorMsg);
        return httpClientResultModel;
    }

    public HttpClientResultModel_Rent591 initCookie() throws Exception {
        logger.info("初始化cookie...");
        return homePage(null);
    }

    public HttpClientResultModel_Rent591 customCookie(HttpClientResultModel_Rent591 resourceHttpClientResultModel)
            throws Exception {
        logger.info("客制化cookie...");
        return homePage(resourceHttpClientResultModel);
    }

    public HttpClientResultModel_Rent591 getPostsData(HttpClientResultModel_Rent591 resourceHttpClientResultModel,
                                                      PostRequestModel postRequestModel) throws Exception {
        logger.info("取得前30篇貼文");
        String url = URL + "/home/search/rsList";
        HeaderModel headerModel = prepareHeaderModel(resourceHttpClientResultModel);
        PostsModel postsModel = resourceHttpClientResultModel.getPostsModel();

        if (postsModel != null) {
            postRequestModel.setFirstRow(String.valueOf(postsModel.getFirstRow()));
            postRequestModel.setTotalRows(String.valueOf(postsModel.getTotalRows()));
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
            postsModel.setPosts(PostModelList);
        } else {
            int firstRow = postsModel.getFirstRow() + PostsModel.A_BATCH_POST_UNIT;
            postsModel.setFirstRow(firstRow);
            postsModel.addPost(PostModelList);
        }

        httpClientResultModel.setCsrfToken(headerModel.getHeaderList().get(0).get("value"));

        String errorMsg = "無法取得貼文";
        myHttpClient.checkResponse(httpClientResultModel.getResponse(), errorMsg);

        httpClientResultModel.setPostsModel(postsModel);
        return httpClientResultModel;
    }

    private HeaderModel prepareHeaderModel(HttpClientResultModel_Rent591 httpClientResultModel) throws Exception {
        HeaderModel headerModel = new HeaderModel();
        HttpResponse response = httpClientResultModel.getResponse();
        if (response == null) throw new Exception("response為空");

        String csrfToken = "";
        // 有 csrf token 直接抓出來用
        if (!StringUtils.isBlank(httpClientResultModel.getCsrfToken())) {
            csrfToken = httpClientResultModel.getCsrfToken();
        } else {
            // 從 html 標籤中拿 csrf token
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
