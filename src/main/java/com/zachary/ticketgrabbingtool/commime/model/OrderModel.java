package com.zachary.ticketgrabbingtool.commime.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

public class OrderModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private UserModel user;
    private ProductsModel products;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public ProductsModel getProducts() {
        return products;
    }

    public void setProducts(ProductsModel products) {
        this.products = products;
    }

}
