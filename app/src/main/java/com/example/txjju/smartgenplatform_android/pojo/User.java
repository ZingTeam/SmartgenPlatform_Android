
package com.example.txjju.smartgenplatform_android.pojo;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户表
 * 与收货地址表一对多关系
 * 与商品评价信息表一对多关系
 * 与订单表一对多关系
 * 与浏览历史表一对多关系
 * 与收藏表一对多关系
 * 与购物车表一对多关系
 */
public class User {

    private String userRealName;//用户真实姓名
    private String userName;//用户账户名
    private String userPhone;//用户手机号 唯一约束
    private String userPassword;//用户密码
    private Integer userSex;//用户性别 0-男 1-女
    private String userHeadPortrait;//头像图片路径
    private String userIdNumber;//用户身份证号
    private String userLocation;//用户所在地
    private Integer userIdentity;//用户是否通过真人认证 0-未认证 1-已认证
    private Set<Purchaseaddress> purchaseaddresses = new HashSet<Purchaseaddress>();//外键关系-收货地址表-一对多
    private Set<Productevalute> productevalutes = new HashSet<Productevalute>();//外键关系-商品评价信息表-一对多
    private Set<Purchase> purchases = new HashSet<Purchase>();//外键关系-订单表-一对多
    private Set<Browsinghistory> browsinghistories = new HashSet<Browsinghistory>();//外键关系-浏览历史表-一对多
    private Set<Collectioninfo> collectioninfos = new HashSet<Collectioninfo>();//外键关系-收藏表-一对多
    private Set<Shoppingcart> shoppingcarts = new HashSet<Shoppingcart>();//外键关系-购物车表-一对多
    private Integer id;//主键id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public String getUserHeadPortrait() {
        return userHeadPortrait;
    }

    public void setUserHeadPortrait(String userHeadPortrait) {
        this.userHeadPortrait = userHeadPortrait;
    }

    public String getUserIdNumber() {
        return userIdNumber;
    }

    public void setUserIdNumber(String userIdNumber) {
        this.userIdNumber = userIdNumber;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Set<Purchaseaddress> getPurchaseaddresses() {
        return purchaseaddresses;
    }

    public void setPurchaseaddresses(Set<Purchaseaddress> purchaseaddresses) {
        this.purchaseaddresses = purchaseaddresses;
    }

    public Set<Productevalute> getProductevalutes() {
        return productevalutes;
    }

    public void setProductevalutes(Set<Productevalute> productevalutes) {
        this.productevalutes = productevalutes;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Set<Browsinghistory> getBrowsinghistories() {
        return browsinghistories;
    }

    public void setBrowsinghistories(Set<Browsinghistory> browsinghistories) {
        this.browsinghistories = browsinghistories;
    }

    public Set<Collectioninfo> getCollectioninfos() {
        return collectioninfos;
    }

    public void setCollectioninfos(Set<Collectioninfo> collectioninfos) {
        this.collectioninfos = collectioninfos;
    }

    public Set<Shoppingcart> getShoppingcarts() {
        return shoppingcarts;
    }

    public void setShoppingcarts(Set<Shoppingcart> shoppingcarts) {
        this.shoppingcarts = shoppingcarts;
    }

    @Override
    public String toString() {
        return "User{" +
                "userRealName='" + userRealName + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userSex=" + userSex +
                ", userHeadPortrait='" + userHeadPortrait + '\'' +
                ", userIdNumber='" + userIdNumber + '\'' +
                ", userLocation='" + userLocation + '\'' +
                ", userIdentity=" + userIdentity +
                '}';
    }

    public User(String userRealName, String userName, String userPhone, String userPassword, Integer userSex, String userHeadPortrait, String userIdNumber, String userLocation, Integer userIdentity) {
        this.userRealName = userRealName;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
        this.userSex = userSex;
        this.userHeadPortrait = userHeadPortrait;
        this.userIdNumber = userIdNumber;
        this.userLocation = userLocation;
        this.userIdentity = userIdentity;
    }

    public User() {
    }
}
