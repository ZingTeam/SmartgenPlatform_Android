package com.example.txjju.smartgenplatform_android.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.fragment.MineFragment;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.view.CircleImageView;

public class AboutMeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack,ivUserHeadPortrait,ivUserName,ivUserPassword,ivUserSex;
    private CircleImageView imgUserHeadPortrait;
    private TextView tvUserName,tvUserPassword,tvUserPhone,tvUserSex;
    private Button btnSave;
    private String username="",userpassword="",userphone="";
    private int usersex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        init();
        getSpData();//获取用户个人信息
    }

    private void getSpData() {
//        User user = SPUtil.getUser(this);
//        username=user.getUser_name();
//        userpassword=user.getUser_password();
//        userphone=user.getUser_phone();
//        usersex=user.getUser_sex();
    }

    private void init() {
        ivBack=findViewById(R.id.iv_aboutMe_back);
        ivUserHeadPortrait=findViewById(R.id.iv_aboutMe_userHeadPortrait);
        tvUserName=findViewById(R.id.tv_aboutMe_userName);
        tvUserPassword=findViewById(R.id.tv_aboutMe_userPassword);
        tvUserPhone=findViewById(R.id.tv_aboutMe_userPhone);
        tvUserSex=findViewById(R.id.tv_aboutMe_userSex);
        btnSave=findViewById(R.id.btn_aboutMe_save);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.iv_aboutMe_back:
            Intent intent = new Intent();
            intent.setClass(AboutMeActivity.this, MainActivity.class);
            intent.putExtra("id", 3);
            startActivity(intent);
            break;
        }
    }
}
