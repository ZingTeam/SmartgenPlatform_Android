package com.example.txjju.smartgenplatform_android.pojo;

/**
 * Created by Administrator on 2018/6/3.
 */

public class CreativeProject {
    int Creproject_id;
    int Expert_jobNumber;
    int User_id;
    int Company_id;
    String Creproject_title;
    String Creproject_content;
    String Creproject_label;
    String Creproject_picture;
    String Creproject_video;
    String Creproject_plan;
    int Creproject_classify;
    int Creproject_state;
    int Creproject_praise;
    String Creproject_modifyTime;
    String Creproject_releaseTime;
    String  Creproject_evaluateTime;
    int  Creproject_evaluateResult;
    String Creproject_evaluateOpinion;
    String condition;

    //多表查询的时候，需要用户的头像和用户名的信息
    String User_name;
    String User_headPortrait;

    public int getCreproject_id() {
        return Creproject_id;
    }
    public void setCreproject_id(int creproject_id) {
        Creproject_id = creproject_id;
    }
    public int getExpert_jobNumber() {
        return Expert_jobNumber;
    }
    public void setExpert_jobNumber(int expert_jobNumber) {
        Expert_jobNumber = expert_jobNumber;
    }
    public int getUser_id() {
        return User_id;
    }
    public void setUser_id(int user_id) {
        User_id = user_id;
    }
    public int getCompany_id() {
        return Company_id;
    }
    public void setCompany_id(int company_id) {
        Company_id = company_id;
    }
    public String getCreproject_title() {
        return Creproject_title;
    }
    public void setCreproject_title(String creproject_title) {
        Creproject_title = creproject_title;
    }
    public String getCreproject_content() {
        return Creproject_content;
    }
    public void setCreproject_content(String creproject_content) {
        Creproject_content = creproject_content;
    }
    public String getCreproject_label() {
        return Creproject_label;
    }
    public void setCreproject_label(String creproject_label) {
        Creproject_label = creproject_label;
    }
    public String getCreproject_picture() {
        return Creproject_picture;
    }
    public void setCreproject_picture(String creproject_picture) {
        Creproject_picture = creproject_picture;
    }
    public String getCreproject_video() {
        return Creproject_video;
    }
    public void setCreproject_video(String creproject_video) {
        Creproject_video = creproject_video;
    }
    public String getCreproject_plan() {
        return Creproject_plan;
    }
    public void setCreproject_plan(String creproject_plan) {
        Creproject_plan = creproject_plan;
    }
    public int getCreproject_classify() {
        return Creproject_classify;
    }
    public void setCreproject_classify(int creproject_classify) {
        Creproject_classify = creproject_classify;
    }
    public int getCreproject_state() {
        return Creproject_state;
    }
    public void setCreproject_state(int creproject_state) {
        Creproject_state = creproject_state;
    }
    public int getCreproject_praise() {
        return Creproject_praise;
    }
    public void setCreproject_praise(int creproject_praise) {
        Creproject_praise = creproject_praise;
    }
    public String getCreproject_modifyTime() {
        return Creproject_modifyTime;
    }
    public void setCreproject_modifyTime(String creproject_modifyTime) {
        Creproject_modifyTime = creproject_modifyTime;
    }
    public String getCreproject_releaseTime() {
        return Creproject_releaseTime;
    }
    public void setCreproject_releaseTime(String creproject_releaseTime) {
        Creproject_releaseTime = creproject_releaseTime;
    }
    public String getCreproject_evaluateTime() {
        return Creproject_evaluateTime;
    }
    public void setCreproject_evaluateTime(String creproject_evaluateTime) {
        Creproject_evaluateTime = creproject_evaluateTime;
    }
    public int getCreproject_evaluateResult() {
        return Creproject_evaluateResult;
    }
    public void setCreproject_evaluateResult(int creproject_evaluateResult) {
        Creproject_evaluateResult = creproject_evaluateResult;
    }
    public String getCreproject_evaluateOpinion() {
        return Creproject_evaluateOpinion;
    }
    public void setCreproject_evaluateOpinion(String creproject_evaluateOpinion) {
        Creproject_evaluateOpinion = creproject_evaluateOpinion;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }

    //多表查询的时候，需要用户的头像和用户名的信息
    public String getUser_name() {
        return User_name;
    }
    public void setUser_name(String User_name) {
        this.User_name = User_name;
    }

    public String getUser_headPortrait() {
        return User_headPortrait;
    }
    public void setUser_headPortrait(String User_headPortrait) {
        this.User_headPortrait = User_headPortrait;
    }
    @Override
    public String toString() {
        return "CreativeProject [Creproject_id=" + Creproject_id
                + ", Expert_jobNumber=" + Expert_jobNumber + ", User_id="
                + User_id + ", Company_id=" + Company_id
                + ", Creproject_title=" + Creproject_title
                + ", Creproject_content=" + Creproject_content
                + ", Creproject_label=" + Creproject_label
                + ", Creproject_picture=" + Creproject_picture
                + ", Creproject_video=" + Creproject_video
                + ", Creproject_plan=" + Creproject_plan
                + ", Creproject_classify=" + Creproject_classify
                + ", Creproject_state=" + Creproject_state
                + ", Creproject_praise=" + Creproject_praise
                + ", Creproject_modifyTime=" + Creproject_modifyTime
                + ", Creproject_releaseTime=" + Creproject_releaseTime
                + ", Creproject_evaluateTime=" + Creproject_evaluateTime
                + ", Creproject_evaluateResult=" + Creproject_evaluateResult
                + ", Creproject_evaluateOpinion=" + Creproject_evaluateOpinion
                + ", condition=" + condition + ", User_name=" + User_name
                + ", User_headPortrait=" + User_headPortrait + "]";
    }

}
