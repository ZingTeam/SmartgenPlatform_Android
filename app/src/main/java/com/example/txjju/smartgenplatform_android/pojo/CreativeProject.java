package com.example.txjju.smartgenplatform_android.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 创意项目表
 * 为兼容web项目,有部分冗余,需后续优化
 * 与产品表一对多关系
 */
public class Creativeproject {

    private Integer expertJobNumber;//专家表主键
    private Integer userId;//用户表主键
    private Integer companyId;//公司表主键
    private String creprojectTitle;//创意项目标题
    private String creprojectContent;//创意项目简介
    private String creprojectLabel;//创意项目标签
    private String creprojectPicture;//创意项目图片路径
    private String creprojectVideo;//创意项目视频路径
    private String creprojectPlan;//创意项目计划书文件路径
    private Integer creprojectClassify;//创意项目分类 0-生活手工 1-家具家居 2-科技数码 3-艺术娱乐 4-医疗健康 5-户外运动 6-为其他
    private Integer creprojectState;//孵化状态 0-未孵化 1-孵化中 2-已孵化
    private Integer creprojectPraise;//点赞数
    private Timestamp creprojectModifyTime;//最后一次修改时间
    private Timestamp creprojectReleaseTime;//最后一次发布时间
    private Timestamp creprojectEvaluateTime;//最后一次评估时间
    private Integer creprojectEvaluateResult;//专家评估结果
    private String creprojectEvaluateOpinion;//专家评估意见
    private Set<Product> products = new HashSet<Product>();//外键关系-产品表-一对多关系
    private Integer id;//主键id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExpertJobNumber() {
        return expertJobNumber;
    }

    public void setExpertJobNumber(Integer expertJobNumber) {
        this.expertJobNumber = expertJobNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCreprojectTitle() {
        return creprojectTitle;
    }

    public void setCreprojectTitle(String creprojectTitle) {
        this.creprojectTitle = creprojectTitle;
    }

    public String getCreprojectContent() {
        return creprojectContent;
    }

    public void setCreprojectContent(String creprojectContent) {
        this.creprojectContent = creprojectContent;
    }

    public String getCreprojectLabel() {
        return creprojectLabel;
    }

    public void setCreprojectLabel(String creprojectLabel) {
        this.creprojectLabel = creprojectLabel;
    }

    public String getCreprojectPicture() {
        return creprojectPicture;
    }

    public void setCreprojectPicture(String creprojectPicture) {
        this.creprojectPicture = creprojectPicture;
    }

    public String getCreprojectVideo() {
        return creprojectVideo;
    }

    public void setCreprojectVideo(String creprojectVideo) {
        this.creprojectVideo = creprojectVideo;
    }

    public String getCreprojectPlan() {
        return creprojectPlan;
    }

    public void setCreprojectPlan(String creprojectPlan) {
        this.creprojectPlan = creprojectPlan;
    }

    public Integer getCreprojectClassify() {
        return creprojectClassify;
    }

    public void setCreprojectClassify(Integer creprojectClassify) {
        this.creprojectClassify = creprojectClassify;
    }

    public Integer getCreprojectState() {
        return creprojectState;
    }

    public void setCreprojectState(Integer creprojectState) {
        this.creprojectState = creprojectState;
    }

    public Integer getCreprojectPraise() {
        return creprojectPraise;
    }

    public void setCreprojectPraise(Integer creprojectPraise) {
        this.creprojectPraise = creprojectPraise;
    }

    public Timestamp getCreprojectModifyTime() {
        return creprojectModifyTime;
    }

    public void setCreprojectModifyTime(Timestamp creprojectModifyTime) {
        this.creprojectModifyTime = creprojectModifyTime;
    }

    public Timestamp getCreprojectReleaseTime() {
        return creprojectReleaseTime;
    }

    public void setCreprojectReleaseTime(Timestamp creprojectReleaseTime) {
        this.creprojectReleaseTime = creprojectReleaseTime;
    }

    public Timestamp getCreprojectEvaluateTime() {
        return creprojectEvaluateTime;
    }

    public void setCreprojectEvaluateTime(Timestamp creprojectEvaluateTime) {
        this.creprojectEvaluateTime = creprojectEvaluateTime;
    }

    public Integer getCreprojectEvaluateResult() {
        return creprojectEvaluateResult;
    }

    public void setCreprojectEvaluateResult(Integer creprojectEvaluateResult) {
        this.creprojectEvaluateResult = creprojectEvaluateResult;
    }

    public String getCreprojectEvaluateOpinion() {
        return creprojectEvaluateOpinion;
    }

    public void setCreprojectEvaluateOpinion(String creprojectEvaluateOpinion) {
        this.creprojectEvaluateOpinion = creprojectEvaluateOpinion;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}

