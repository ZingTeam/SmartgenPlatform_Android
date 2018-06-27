package com.example.txjju.smartgenplatform_android.pojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 购物车表
 * 与用户表多对一关系
 * 与产品表多对多关系 通过中间表shoppingcart_product_relation表示关系 由购物车表维护关系
 */
public class Shoppingcart{

    private Integer id;//主键Id
    private String productName;//商品名（冗余字段）
    private Double productPrice;//商品价格（冗余字段）
    private String productPicture;//商品图片（冗余字段）
    private int productCount;//商品数量
    private String productMsg;//商品简介（冗余字段）
    private User user;//外键关系-User_id-用户表-多对一
    private Product product;//外键关系-Product_id-产品表-多对一
    boolean isChoosed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public String getProductMsg() {
        return productMsg;
    }

    public void setProductMsg(String productMsg) {
        this.productMsg = productMsg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shoppingcart(Integer id, String productName, Double productPrice, String productPicture, int productCount, String productMsg) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPicture = productPicture;
        this.productCount = productCount;
        this.productMsg = productMsg;
    }

    public Shoppingcart() {
    }
    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }
}