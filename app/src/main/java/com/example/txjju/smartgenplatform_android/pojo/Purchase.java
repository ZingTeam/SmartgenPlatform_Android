package com.example.txjju.smartgenplatform_android.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 订单表
 * 与用户表多对一关系
 * 与订单详情表一对多关系
 */
public class Purchase {

    private String purchaseNo;//订单编号
    private Timestamp purchasePaymentTime;//支付时间
    private String purchasePatternOfPayment;//支付方式
    private Byte purchaseState;//订单状态 0-待付款 1-待发货 2-取消订单
    private Double purchasePrice;//产品交易总价
    private Set<Purchaseitem> purchaseitems = new HashSet<Purchaseitem>();//外键关系-订单详情表-一对多
    private User user;//外键关系-User_id-用户表-多对一
    private Integer id;//主键id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Timestamp getPurchasePaymentTime() {
        return purchasePaymentTime;
    }

    public void setPurchasePaymentTime(Timestamp purchasePaymentTime) {
        this.purchasePaymentTime = purchasePaymentTime;
    }

    public String getPurchasePatternOfPayment() {
        return purchasePatternOfPayment;
    }

    public void setPurchasePatternOfPayment(String purchasePatternOfPayment) {
        this.purchasePatternOfPayment = purchasePatternOfPayment;
    }

    public Byte getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(Byte purchaseState) {
        this.purchaseState = purchaseState;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Set<Purchaseitem> getPurchaseitems() {
        return purchaseitems;
    }

    public void setPurchaseitems(Set<Purchaseitem> purchaseitems) {
        this.purchaseitems = purchaseitems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
