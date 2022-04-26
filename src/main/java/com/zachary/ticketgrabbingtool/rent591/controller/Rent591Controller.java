package com.zachary.ticketgrabbingtool.rent591.controller;

import com.zachary.ticketgrabbingtool.httpclient.model.HttpClientResultModel_Rent591;
import com.zachary.ticketgrabbingtool.rent591.model.PostRequestModel;
import com.zachary.ticketgrabbingtool.rent591.service.Rent591Service;
import com.zachary.ticketgrabbingtool.resource.CONSTANT;
import com.zachary.ticketgrabbingtool.resource.ConfigReader;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/rent591")
public class Rent591Controller {

    private static final Logger logger = LoggerFactory.getLogger(Rent591Controller.class);

    private static final String LINE_BOT_ADMIN_USERID = ConfigReader.getString(CONSTANT.LINE_BOT_ADMIN_USERID, true);

    @Autowired
    Rent591Service rent591Service;

    @RequestMapping(value = "/getPosts", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> getPosts(@RequestBody PostRequestModel postRequestModel) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "text/html; charset=utf-8");

        postRequestModel.setIs_format_data("1");
        postRequestModel.setIs_new_list("1");
        postRequestModel.setType("1");
        postRequestModel.setRegion("1");

        try {
            HttpClientResultModel_Rent591 res = rent591Service.getPosts(postRequestModel);
            return new ResponseEntity<>(res.getPostsModel().toString(), headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/getAllPosts", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> getAllPosts(@RequestBody PostRequestModel postRequestModel) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "text/html; charset=utf-8");

        postRequestModel.setIs_format_data("1");
        postRequestModel.setIs_new_list("1");
        postRequestModel.setType("1");
        postRequestModel.setRegion("1");

        try {
            HttpClientResultModel_Rent591 res = rent591Service.getAllPosts(postRequestModel);
            return new ResponseEntity<>(res.getPostsModel().toString(), headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
        }
    }

}
