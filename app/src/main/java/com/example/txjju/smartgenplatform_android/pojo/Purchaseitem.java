package com.example.txjju.smartgenplatform_android.pojo;

import java.sql.Timestamp;



/**
 * 订单详情表
 * 与订单表多对一关系
 * 与产品表多对一关系
 * 与商品评价信息表一对一关系 关系交给商品评价信息表维护
 */
public class Purchaseitem{

    private Integer id;//主键Id
    private Integer purchaseitemCount;//商品购买数量
    private String purchaseitemMsg;//买家留言
    private Timestamp purchaseitemTime;//发货时间
    private String purchaseitemWay;//发货方式(哪家快递 快递/空运)
    private Double purchaseitemSinglePrice;//交易单价
    private Double purchaseitemPrice;//交易总价
    private String purchaseitemName;//商品名(冗余字段)
    private Byte purchaseitemStatus;//订单详情状态 0-未发货(默认) 1-已发货
    private Purchase purchase;//外键关系-Purchase_id-订单表-多对一
    private Product product;//外键关系-Product_id-产品表-多对一
    private Productevalute productevalute;//外键关系-商品评价信息表-一对一
    private String purchaseitemPicture;//商品图片(冗余字段)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPurchaseitemCount() {
        return purchaseitemCount;
    }

    public void setPurchaseitemCount(Integer purchaseitemCount) {
        this.purchaseitemCount = purchaseitemCount;
    }

    public String getPurchaseitemMsg() {
        return purchaseitemMsg;
    }

    public void setPurchaseitemMsg(String purchaseitemMsg) {
        this.purchaseitemMsg = purchaseitemMsg;
    }

    public Timestamp getPurchaseitemTime() {
        return purchaseitemTime;
    }

    public void setPurchaseitemTime(Timestamp purchaseitemTime) {
        this.purchaseitemTime = purchaseitemTime;
    }

    public String getPurchaseitemWay() {
        return purchaseitemWay;
    }

    public void setPurchaseitemWay(String purchaseitemWay) {
        this.purchaseitemWay = purchaseitemWay;
    }

    public Double getPurchaseitemSinglePrice() {
        return purchaseitemSinglePrice;
    }

    public void setPurchaseitemSinglePrice(Double purchaseitemSinglePrice) {
        this.purchaseitemSinglePrice = purchaseitemSinglePrice;
    }

    public Double getPurchaseitemPrice() {
        return purchaseitemPrice;
    }

    public void setPurchaseitemPrice(Double purchaseitemPrice) {
        this.purchaseitemPrice = purchaseitemPrice;
    }

    public String getPurchaseitemName() {
        return purchaseitemName;
    }

    public void setPurchaseitemName(String purchaseitemName) {
        this.purchaseitemName = purchaseitemName;
    }

    public Byte getPurchaseitemStatus() {
        return purchaseitemStatus;
    }

    public void setPurchaseitemStatus(Byte purchaseitemStatus) {
        this.purchaseitemStatus = purchaseitemStatus;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Productevalute getProductevalute() {
        return productevalute;
    }

    public void setProductevalute(Productevalute productevalute) {
        this.productevalute = productevalute;
    }

    public String getPurchaseitemPicture() {
        return purchaseitemPicture;
    }

    public void setPurchaseitemPicture(String purchaseitemPicture) {
        purchaseitemPicture = purchaseitemPicture;
    }

    public Purchaseitem(Integer id, Integer purchaseitemCount, String purchaseitemMsg, Double purchaseitemSinglePrice, Double purchaseitemPrice, String purchaseitemName, String purchaseitemPicture) {
        this.id = id;
        this.purchaseitemCount = purchaseitemCount;
        this.purchaseitemMsg = purchaseitemMsg;
        this.purchaseitemSinglePrice = purchaseitemSinglePrice;
        this.purchaseitemPrice = purchaseitemPrice;
        this.purchaseitemName = purchaseitemName;
        purchaseitemPicture = purchaseitemPicture;
    }

    public Purchaseitem() {
    }
}