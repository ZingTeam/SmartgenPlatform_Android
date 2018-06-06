package com.example.txjju.smartgenplatform_android.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.fragment.MineFragment;
import com.example.txjju.smartgenplatform_android.view.CircleImageView;

public class AboutMeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack,ivUserHeadPortrait,ivUserName,ivUserPassword,ivUserSex;
    private CircleImageView imgUserHeadPortrait;
    private TextView tvUserName,tvUserPassword,tvUserPhone,tvUserSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        init();
    }

    private void init() {
        ivBack=findViewById(R.id.iv_aboutMe_back);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent();
        intent.setClass(AboutMeActivity.this, MainActivity.class);
        intent.putExtra("id",3);
        startActivity(intent);
    }
}
