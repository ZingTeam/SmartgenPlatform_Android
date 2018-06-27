package com.example.txjju.smartgenplatform_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.util.SPUtil;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        ivBack=findViewById(R.id.iv_setting_back);
        btnExit=findViewById(R.id.btn_setting_exit);
        ivBack.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_setting_back:
                this.finish();
                break;
            case R.id.btn_setting_exit:
                SPUtil.clearUser(this);
                Log.i("msg","退出登录啦");
                this.finish();
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                intent.putExtra("requestActivity","goLogin");
                startActivity(intent);
                break;
        }
    }
}
