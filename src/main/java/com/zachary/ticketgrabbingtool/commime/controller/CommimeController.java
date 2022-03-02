package com.zachary.ticketgrabbingtool.commime.controller;

import com.zachary.ticketgrabbingtool.commime.service.CommimeSerivce;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
public class CommimeController {

    @Autowired
    private CommimeSerivce commimeService;

    @RequestMapping("/")
    public String hello() {

        try {

            HashMap<String, Object> initResMap = commimeService.initCookie();
            HashMap<String, Object> loginResMap = commimeService.signIn(initResMap);
            HashMap<String, Object> getCartInfoResMap = commimeService.getCartInfo(loginResMap);
//            HashMap<String, Object> doAddItemsResMap = commimeService.doAddItems(loginResMap);
//            HttpResponse response = (HttpResponse) doAddItemsResMap.get("response");
//            System.out.println(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            commimeService.signOut(getCartInfoResMap);

//            HttpResponse response = (HttpResponse) loginResMap.get("response");
//            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return("[error]: " + e.getStackTrace());
        }

//        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
}
