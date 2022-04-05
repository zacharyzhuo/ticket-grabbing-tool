package com.zachary.ticketgrabbingtool.commime.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.List;

public class OrderModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private UserModel user;
    private List<ProductModel> products;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

}
