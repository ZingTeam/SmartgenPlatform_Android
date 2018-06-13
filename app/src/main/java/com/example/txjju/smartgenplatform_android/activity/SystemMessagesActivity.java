package com.example.txjju.smartgenplatform_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.txjju.smartgenplatform_android.R;

public class SystemMessagesActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_messages);
        init();
    }

    private void init() {
        ivBack=findViewById(R.id.iv_systemMessages_back);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {//返回主页面
        this.finish();
    }
}
