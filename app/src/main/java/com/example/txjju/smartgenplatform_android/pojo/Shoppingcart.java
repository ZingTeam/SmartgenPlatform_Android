package com.example.txjju.smartgenplatform_android.pojo;

import java.util.HashSet;
import java.util.Set;

/**
 * 购物车表
 * 与用户表多对一关系
 * 与产品表多对多关系 通过中间表shoppingcart_product_relation表示关系 由购物车表维护关系
 */
public class Shoppingcart {

    private User user;//外键关系-User_id-用户表-多对一
    private Set<Product> products = new HashSet<Product>();//外键关系-shoppingcart_product_relation表-产品表-多对多
    private Integer id;//主键id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
