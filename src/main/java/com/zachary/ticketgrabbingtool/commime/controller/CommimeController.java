package com.zachary.ticketgrabbingtool.commime.controller;

import com.zachary.ticketgrabbingtool.commime.model.OrderModel;
import com.zachary.ticketgrabbingtool.commime.model.ProductsModel;
import com.zachary.ticketgrabbingtool.commime.model.UserModel;
import com.zachary.ticketgrabbingtool.commime.service.CommimeSerivce;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequestMapping("/commime")
public class CommimeController {

    @Autowired
    private CommimeSerivce commimeService;

    @RequestMapping(value = "/buy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String buy(@RequestBody OrderModel orderModel) {
        try {

            UserModel userModel = orderModel.getUser();
            ProductsModel productsModel = orderModel.getProducts();

            HashMap<String, Object> initResMap = commimeService.initCookie();
            HashMap<String, Object> loginResMap = commimeService.signIn(initResMap, userModel);
//            HashMap<String, Object> getCartInfoResMap = commimeService.getCartInfo(loginResMap);
//            HashMap<String, Object> doAddItemsResMap = commimeService.doAddItems(loginResMap);
//            HttpResponse response = (HttpResponse) doAddItemsResMap.get("response");
//            System.out.println(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            commimeService.signOut(loginResMap);

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
