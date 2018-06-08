package com.example.txjju.smartgenplatform_android.pojo;

/**
 * Created by Administrator on 2018/6/7.
 */

public class User {
    int User_id;
    String User_name;
    String User_phone;
    String User_password;
    int User_sex;
    String User_headPortrait;
    String User_IDNumber;
    String  User_location;
    int User_identity;
    String User_realName;
    String condition;

    public int getUser_id() {
        return User_id;
    }
    public void setUser_id(int user_id) {
        User_id = user_id;
    }
    public String getUser_name() {
        return User_name;
    }
    public void setUser_name(String user_name) {
        User_name = user_name;
    }
    public String getUser_phone() {
        return User_phone;
    }
    public void setUser_phone(String user_phone) {
        User_phone = user_phone;
    }
    public String getUser_password() {
        return User_password;
    }
    public void setUser_password(String user_password) {
        User_password = user_password;
    }
    public int getUser_sex() {
        return User_sex;
    }
    public void setUser_sex(int user_sex) {
        User_sex = user_sex;
    }
    public String getUser_headPortrait() {
        return User_headPortrait;
    }
    public void setUser_headPortrait(String user_headPortrait) {
        User_headPortrait = user_headPortrait;
    }
    public String getUser_IDNumber() {
        return User_IDNumber;
    }
    public void setUser_IDNumber(String user_IDNumber) {
        User_IDNumber = user_IDNumber;
    }
    public String getUser_location() {
        return User_location;
    }
    public void setUser_location(String user_location) {
        User_location = user_location;
    }
    public int getUser_identity() {
        return User_identity;
    }
    public void setUser_identity(int user_identity) {
        User_identity = user_identity;
    }
    public String getUser_realName() {
        return User_realName;
    }
    public void setUser_realName(String user_realName) {
        User_realName = user_realName;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    @Override
    public String toString() {
        return "UserInfo [User_id=" + User_id + ", User_name=" + User_name + ", User_phone=" + User_phone + ", User_password=" + User_password + ", User_sex=" + User_sex
                + ", User_headPortrait=" + User_headPortrait + ",User_IDNumber="+User_IDNumber+",User_location="+User_location+",User_identity="+User_identity+",User_realName="+User_realName+"]";
    }
}
