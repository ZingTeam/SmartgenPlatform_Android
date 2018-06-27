package com.example.txjju.smartgenplatform_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.txjju.smartgenplatform_android.R;

public class PaySuccessActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivPaySuccessBack;
    private Button btnPaySuccessBackOrder,btnPaySuccessBackStore;
    private TextView tvPaySuccessUserName,tvPaySuccessUserPhone,tvPaySuccessUserAddress;
    private String userName,userPhone,userAddress;//保存用户数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        //获取Intent对象
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userPhone = intent.getStringExtra("userPhone");
        userAddress = intent.getStringExtra("userAddress");
        //取出key对应的value值
        init();
    }

    private void init() {
        tvPaySuccessUserName = findViewById(R.id.tv_paySuccess_userName);
        tvPaySuccessUserPhone = findViewById(R.id.tv_paySuccess_userPhone);
        tvPaySuccessUserAddress = findViewById(R.id.tv_paySuccess_userAddress);
        ivPaySuccessBack = findViewById(R.id.iv_paySuccess_back);
        btnPaySuccessBackOrder = findViewById(R.id.btn_paySuccess_backOrder);
        btnPaySuccessBackStore = findViewById(R.id.btn_paySuccess_backStore);
        tvPaySuccessUserName.setText(userName);
        tvPaySuccessUserPhone.setText(userPhone);
        tvPaySuccessUserAddress.setText(userAddress);
        ivPaySuccessBack.setOnClickListener(this);
        btnPaySuccessBackOrder.setOnClickListener(this);
        btnPaySuccessBackStore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_paySuccess_back://返回到创意商城
                PaySuccessActivity.this.finish();
                break;
            case R.id.btn_paySuccess_backOrder:
                Intent backOrderIntent = new Intent(PaySuccessActivity.this,OrderActivity.class);
                startActivity(backOrderIntent);
                PaySuccessActivity.this.finish();
                break;
            case R.id.btn_paySuccess_backStore:
                PaySuccessActivity.this.finish();
                break;
        }

    }
}
