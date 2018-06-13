package com.example.txjju.smartgenplatform_android.pojo;


import java.util.HashSet;
import java.util.Set;

/**
 * 公司表
 * 与产品表一对多关系
 */
public class Company {

    private String companyName;//公司名字
    private String companyUserName;//公司用户名
    private String companyPhone;//公司手机号码
    private String companyCharterNumber;//公司认证编号
    private String companyPassword;//公司密码
    private String companyLogo;//公司logo图片路径
    private String companyLocation;//公司所在地
    private Integer companyIdentity;//公司营业执照编号
    private Set<Product> products = new HashSet<Product>();//外键关系-产品表-一对多
    private Integer id;//主键id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(String companyUserName) {
        this.companyUserName = companyUserName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyCharterNumber() {
        return companyCharterNumber;
    }

    public void setCompanyCharterNumber(String companyCharterNumber) {
        this.companyCharterNumber = companyCharterNumber;
    }

    public String getCompanyPassword() {
        return companyPassword;
    }

    public void setCompanyPassword(String companyPassword) {
        this.companyPassword = companyPassword;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public Integer getCompanyIdentity() {
        return companyIdentity;
    }

    public void setCompanyIdentity(Integer companyIdentity) {
        this.companyIdentity = companyIdentity;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
