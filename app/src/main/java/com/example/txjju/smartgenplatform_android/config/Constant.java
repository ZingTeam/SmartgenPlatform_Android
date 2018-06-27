package com.example.txjju.smartgenplatform_android.config;

/**
 * Created by 48305 on 2018/5/25.
 */

public class Constant {

    public static final String BASE_URL = "http://193.112.124.230:9696/SmartgenPlatformWithSSH_war";
    //public static final String BASE_URL = "http://192.168.2.132:8080";
    /**用户注册接口**/
    public static final String USER_REGISTER = BASE_URL + "/UserAction_register";
    /**用户登录接口**/
    public static final String USER_LOGIN = BASE_URL + "/UserAction_login";
    /**创意项目查询接口**/
    public static final String CREPROJECT_GET = BASE_URL + "/CreativeprojectAction_getCreativeprojectList";
    /**创意产品查询接口**/
    public static final String PRODUCT_GET = BASE_URL + "/ProductAction_getProductList";
    /**创意项目点赞接口**/
    public static final String CREPROJECT_PARISE = BASE_URL + "/CreativeprojectAction_parise";
    /**初始获取创意项目点赞情况接口**/
    public static final String CREPROJECT_GETBYUSERANDCREPROJECT = BASE_URL + "/CreativeremarkAction_getByUserAndCreProject";
    /**创意产品收藏接口**/
    public static final String PRODUCT_COLLECT = BASE_URL + "/CollectioninfoAction_doCollection";
    /**初始获取创意产品收藏接口**/
    public static final String PRODUCT_INITCOLLECT = BASE_URL + "/CollectioninfoAction_getCount";
    /**获取用户收藏创意产品列表接口**/
    public static final String PRODUCT_COLLECTLIST = BASE_URL + "/CollectioninfoAction_getProductByUserId";
    /**获取用户收货地址列表接口**/
    public static final String PRODUCT_PURCHASEADDRESS = BASE_URL + "/PurchaseaddressAction_getList";
    /**订单生成接口**/
    public static final String PRODUCT_CREATORDER = BASE_URL + "/PurchaseAction_purchse";
    /**获取订单详情接口**/
    public static final String PRODUCT_GETORDERDETAILS = BASE_URL + "/PurchaseAction_getPurchase";
    /**取消订单详情接口**/
    public static final String PRODUCT_CANCELORDER = BASE_URL + "/PurchaseAction_cancel";
    /**购买产品添加到购物车接口**/
    public static final String PRODUCT_CREATESHOPPINGCART = BASE_URL + "/ShoppingcartAction_addShoppingcart";
    /**获取购物车列表接口**/
    public static final String PRODUCT_GETCARTLIST = BASE_URL + "/ShoppingcartAction_getShoppingcart";
    /**删除购物车部分列表接口**/
    public static final String PRODUCT_DETELECART = BASE_URL + "/ShoppingcartAction_deleteShoppingcart";
    /**订单生成接口**/
   // public static final String PRODUCT_BENDICREATORDER = BASE_URL + "/PurchaseAction_purchse";
}
