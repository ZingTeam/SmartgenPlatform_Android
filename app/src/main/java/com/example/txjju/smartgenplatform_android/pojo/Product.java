package com.example.txjju.smartgenplatform_android.pojo;


import java.util.HashSet;
import java.util.Set;

/**
 * 产品表
 * 与创意项目表多对一关系
 * 与公司表多对一关系
 * 与商品评价信息表一对多关系
 * 与订单详情表一对多关系
 * 与浏览历史表一对多关系
 * 与收藏表一对多关系
 * 与购物车表多对多关系 通过中间表shoppingcart_product_relation表示关系 由购物车表维护关系
 */
public class Product {

    private Integer productNo;//产品编号
    private String productName;//产品名
    private Double productPrice;//产品价格
    private Integer productClassify;//产品分类 0-生活手工 1-家具家居 2-科技数码 3-艺术娱乐 4-医疗健康 5-户外运动 6-其他
    private String productLabel;//产品标签
    private String productPicture;//产品图片路径
    private Byte productStatus;//产品类型 0-预购 1-直接购买 -1-下架商品
    private String productMsg;//产品简介
    private Double productOriginPrice;//产品成本价
    private Double productFreight;//产品运费
    private String productCount;//产品库存
    private String productSell;//产品销量
    private String productBuyCount;//产品销量
    private String productBestCount;//好评数量
    private String productMiddleCount;//中评数量
    private String productBadCount;//差评数量
    private String productRequireMoney;//(预购关系专属)需求钱数
    private String productCurrentMoney;//(预购关系专属)当前钱数
    private Double productCountPrice;//活动价格
    private String productOneMsg;//一句话简介
    private Creativeproject creativeproject;//外键关系-Creproject_id-创意项目表-多对一
    private Company company;//外键关系-Company_id-公司表-多对一
    private Set<Productevalute> productevalutes = new HashSet<Productevalute>();//外键关系-商品评价信息表-一对多
    private Set<Purchaseitem> purchaseitems = new HashSet<Purchaseitem>();//外键关系-订单详情表-一对多关系
    private Set<Browsinghistory> browsinghistories = new HashSet<Browsinghistory>();//外键关系-浏览历史表-一对多
    private Set<Collectioninfo> collectioninfos = new HashSet<Collectioninfo>();//外键关系-收藏表-一对多
    private Set<Shoppingcart> shoppingcarts = new HashSet<Shoppingcart>();//外键关系-shoppingcart_product_relation表-购物车表-多对多
    private Integer id;//主键id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductNo() {
        return productNo;
    }

    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
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

    public Integer getProductClassify() {
        return productClassify;
    }

    public void setProductClassify(Integer productClassify) {
        this.productClassify = productClassify;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(String productLabel) {
        this.productLabel = productLabel;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public Byte getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Byte productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductMsg() {
        return productMsg;
    }

    public void setProductMsg(String productMsg) {
        this.productMsg = productMsg;
    }

    public Double getProductOriginPrice() {
        return productOriginPrice;
    }

    public void setProductOriginPrice(Double productOriginPrice) {
        this.productOriginPrice = productOriginPrice;
    }

    public Double getProductFreight() {
        return productFreight;
    }

    public void setProductFreight(Double productFreight) {
        this.productFreight = productFreight;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getProductSell() {
        return productSell;
    }

    public void setProductSell(String productSell) {
        this.productSell = productSell;
    }

    public String getProductBuyCount() {
        return productBuyCount;
    }

    public void setProductBuyCount(String productBuyCount) {
        this.productBuyCount = productBuyCount;
    }

    public String getProductBestCount() {
        return productBestCount;
    }

    public void setProductBestCount(String productBestCount) {
        this.productBestCount = productBestCount;
    }

    public String getProductMiddleCount() {
        return productMiddleCount;
    }

    public void setProductMiddleCount(String productMiddleCount) {
        this.productMiddleCount = productMiddleCount;
    }

    public String getProductBadCount() {
        return productBadCount;
    }

    public void setProductBadCount(String productBadCount) {
        this.productBadCount = productBadCount;
    }

    public String getProductRequireMoney() {
        return productRequireMoney;
    }

    public void setProductRequireMoney(String productRequireMoney) {
        this.productRequireMoney = productRequireMoney;
    }

    public String getProductCurrentMoney() {
        return productCurrentMoney;
    }

    public void setProductCurrentMoney(String productCurrentMoney) {
        this.productCurrentMoney = productCurrentMoney;
    }

    public Double getProductCountPrice() {
        return productCountPrice;
    }

    public void setProductCountPrice(Double productCountPrice) {
        this.productCountPrice = productCountPrice;
    }

    public String getProductOneMsg() {
        return productOneMsg;
    }

    public void setProductOneMsg(String productOneMsg) {
        productOneMsg = productOneMsg;
    }

    public Creativeproject getCreativeproject() {
        return creativeproject;
    }

    public void setCreativeproject(Creativeproject creativeproject) {
        this.creativeproject = creativeproject;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Productevalute> getProductevalutes() {
        return productevalutes;
    }

    public void setProductevalutes(Set<Productevalute> productevalutes) {
        this.productevalutes = productevalutes;
    }

    public Set<Purchaseitem> getPurchaseitems() {
        return purchaseitems;
    }

    public void setPurchaseitems(Set<Purchaseitem> purchaseitems) {
        this.purchaseitems = purchaseitems;
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
}
