package com.zachary.ticketgrabbingtool.commime.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.List;

public class ProductsModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<ProductModel> productModels;

    public List<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(List<ProductModel> productModels) {
        this.productModels = productModels;
    }

}
