package com.example.txjju.smartgenplatform_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.txjju.smartgenplatform_android.R;

public class ProjectDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        init();
    }

    private void init() {
        ivBack=findViewById(R.id.iv_projectDetails_back);
        ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_projectDetails_back:
                this.finish();
                break;
        }
    }
}
