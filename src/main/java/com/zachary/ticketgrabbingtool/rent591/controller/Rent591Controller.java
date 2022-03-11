package com.zachary.ticketgrabbingtool.rent591.controller;

import com.zachary.ticketgrabbingtool.rent591.service.Rent591Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController()
@RequestMapping("/rent591")
public class Rent591Controller {

    @Autowired
    Rent591Service rent591Service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getPosts() {
        try {
            HashMap<String, Object> initResMap = rent591Service.initCookie();
            HashMap<String, Object> postsResMap = rent591Service.getPosts(initResMap);

            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return("[error]: " + e.getStackTrace());
        }
    }

}
