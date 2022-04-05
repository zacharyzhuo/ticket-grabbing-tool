package com.zachary.ticketgrabbingtool.commime.controller;

import com.zachary.ticketgrabbingtool.commime.model.OrderModel;
import com.zachary.ticketgrabbingtool.commime.model.ProductModel;
import com.zachary.ticketgrabbingtool.commime.model.UserModel;
import com.zachary.ticketgrabbingtool.commime.service.CommimeSerivce;
import com.zachary.ticketgrabbingtool.httpclient.model.HttpClientResultModel_Commime;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
        List<ProductModel> products = orderModel.getProducts();

        try {
            HttpClientResultModel_Commime httpClientResultModel = null;
            httpClientResultModel = commimeService.initCookie();
            httpClientResultModel = commimeService.signIn(httpClientResultModel, userModel);
//            httpClientResultModel = commimeService.doAddItems(httpClientResultModel, products);
            httpClientResultModel = commimeService.getCartInfo(httpClientResultModel);
            commimeService.signOut(httpClientResultModel);
            return new ResponseEntity<>("ok", headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
        }
    }
}
