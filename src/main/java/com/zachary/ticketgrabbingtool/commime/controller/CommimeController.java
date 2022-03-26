package com.zachary.ticketgrabbingtool.commime.controller;

import com.zachary.ticketgrabbingtool.commime.model.OrderModel;
import com.zachary.ticketgrabbingtool.commime.model.ProductsModel;
import com.zachary.ticketgrabbingtool.commime.model.UserModel;
import com.zachary.ticketgrabbingtool.commime.service.CommimeSerivce;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/commime")
public class CommimeController {

    private static final Logger logger = LoggerFactory.getLogger(CommimeController.class);

    @Autowired
    private CommimeSerivce commimeService;

    @RequestMapping(value = "/buy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> buy(@RequestBody OrderModel orderModel) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "text/html; charset=utf-8");

        UserModel userModel = orderModel.getUser();
        ProductsModel productsModel = orderModel.getProducts();

        try {
            HashMap<String, Object> initResMap = commimeService.initCookie();
            HashMap<String, Object> loginResMap = commimeService.signIn(initResMap, userModel);
//            HashMap<String, Object> getCartInfoResMap = commimeService.getCartInfo(loginResMap);
//            HashMap<String, Object> doAddItemsResMap = commimeService.doAddItems(loginResMap);
//            HttpResponse response = (HttpResponse) doAddItemsResMap.get("response");
//            System.out.println(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            commimeService.signOut(loginResMap);

//            HttpResponse response = (HttpResponse) loginResMap.get("response");
//            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return new ResponseEntity<>("ok", headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(ExceptionUtils.getStackTrace(e), headers, HttpStatus.BAD_REQUEST);
        }
    }
}
