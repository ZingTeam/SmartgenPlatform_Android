package com.example.txjju.smartgenplatform_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.txjju.smartgenplatform_android.R;

public class PublicActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private Button btnGoProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        init();
    }

    private void init() {
        iv_back = findViewById(R.id.iv_public_back);
        btnGoProject = findViewById(R.id.btn_go_project);
        iv_back.setOnClickListener(this);
        btnGoProject.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_public_back:
                PublicActivity.this.finish();
                break;
            case R.id.btn_go_project:
                PublicActivity.this.finish();
                Intent intent = new Intent(PublicActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }

    }
}
