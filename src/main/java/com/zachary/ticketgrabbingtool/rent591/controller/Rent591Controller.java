package com.zachary.ticketgrabbingtool.rent591.controller;

import com.zachary.ticketgrabbingtool.rent591.model.PostRequestModel;
import com.zachary.ticketgrabbingtool.rent591.model.PostsModel;
import com.zachary.ticketgrabbingtool.rent591.service.Rent591Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController()
@RequestMapping("/rent591")
public class Rent591Controller {

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
        postRequestModel.setShowMore("1");

        try {
            HashMap<String, Object> initResMap = rent591Service.initCookie();
            HashMap<String, Object> postsResMap = rent591Service.getPosts(initResMap, postRequestModel);
            PostsModel postsModel = (PostsModel) postsResMap.get("postsModel");
            return new ResponseEntity<>(postsModel.getPost().toString(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getStackTrace(), headers, HttpStatus.BAD_REQUEST);
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
        postRequestModel.setShowMore("1");

        try {
            HashMap<String, Object> initResMap = rent591Service.initCookie();
            HashMap<String, Object> postsResMap = rent591Service.getPosts(initResMap, postRequestModel);
            PostsModel postsModel = (PostsModel) postsResMap.get("postsModel");
            int n = (int) Math.ceil((double) (postsModel.getTotalRows()/postsModel.getFirstRow()));

            for (int i = 0; i < n; i++) {
                postsResMap = rent591Service.getPosts(postsResMap, postRequestModel);
            }

            postsModel = (PostsModel) postsResMap.get("postsModel");
            return new ResponseEntity<>(postsModel.getPost().toString(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getStackTrace(), headers, HttpStatus.BAD_REQUEST);
        }
    }

}
