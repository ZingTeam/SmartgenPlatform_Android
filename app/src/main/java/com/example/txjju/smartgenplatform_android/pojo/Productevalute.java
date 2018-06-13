package com.example.txjju.smartgenplatform_android.pojo;

import java.sql.Timestamp;

/**
 * 商品评价信息表
 * 与产品表多对一关系
 * 与用户表多对一关系
 * 与订单详情表一对一关系 关系交给商品评价信息表维护
 */
public class Productevalute {

    private Integer proevaluteNo;//评价编号
    private String proevaluteTitle;//评价标题
    private Byte proevaluteStar;//评价等地 0-差评 1-中评 2-好评
    private String proevaluteContent;//评价内容
    private Timestamp proevaluteTime;//评价时间
    private String proevalutePicture;//评价图片路径
    private Product product;//外键关系-Product_id-产品表-多对一
    private User user;//外键关系-User_id-用户表-多对一
    private Purchase purchase;//外键关系-订单表-一对一
    private Purchaseitem purchaseitem;//外键关系-Purchaseitem_id-订单详情表-一对一
    private Integer id;//主键id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProevaluteNo() {
        return proevaluteNo;
    }

    public void setProevaluteNo(Integer proevaluteNo) {
        this.proevaluteNo = proevaluteNo;
    }

    public String getProevaluteTitle() {
        return proevaluteTitle;
    }

    public void setProevaluteTitle(String proevaluteTitle) {
        this.proevaluteTitle = proevaluteTitle;
    }

    public Byte getProevaluteStar() {
        return proevaluteStar;
    }

    public void setProevaluteStar(Byte proevaluteStar) {
        this.proevaluteStar = proevaluteStar;
    }

    public String getProevaluteContent() {
        return proevaluteContent;
    }

    public void setProevaluteContent(String proevaluteContent) {
        this.proevaluteContent = proevaluteContent;
    }

    public Timestamp getProevaluteTime() {
        return proevaluteTime;
    }

    public void setProevaluteTime(Timestamp proevaluteTime) {
        this.proevaluteTime = proevaluteTime;
    }

    public String getProevalutePicture() {
        return proevalutePicture;
    }

    public void setProevalutePicture(String proevalutePicture) {
        this.proevalutePicture = proevalutePicture;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Purchaseitem getPurchaseitem() {
        return purchaseitem;
    }

    public void setPurchaseitem(Purchaseitem purchaseitem) {
        this.purchaseitem = purchaseitem;
    }
}
