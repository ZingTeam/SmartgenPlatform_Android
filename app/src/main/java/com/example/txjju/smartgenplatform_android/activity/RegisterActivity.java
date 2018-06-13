package com.example.txjju.smartgenplatform_android.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUserName,etPhone,etPwd;//注册信息文本框
    private TextView tvGoLogin;//登录入口
    private Button btnRegister;//注册按钮
    private String userName,phone,pwd;
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        // 检测网络
        if (!checkNetwork()) {
            Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        // 获取控件
        etUserName = findViewById(R.id.et_register_username);
        etPhone = findViewById(R.id.et_register_phone);
        etPwd = findViewById(R.id.et_register_pwd);
        tvGoLogin = findViewById(R.id.tv_register_goLogin);
        btnRegister = findViewById(R.id.btn_register_register);
        // 设置按钮监听器
        btnRegister.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
    }

    /**
     * 检测网络
     * @return 返回网络检测结果
     */
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    /**
     * 监听控件
     * @param view
     */
    @Override
    public void onClick(View view) {
        // 检测网络
        if (!checkNetwork()) {
            Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        switch (view.getId()){
            case R.id.tv_register_goLogin:
                Intent intent = new Intent(this,LoginActivity.class);
                intent.putExtra("requestActivity","register");
                startActivity(intent);//带参数的跳转
                this.finish();
                break;
            case R.id.btn_register_register:
                register();//注册业务
                break;
        }
    }

    /**
     * 注册，向后台发送请求，处理结果
     */
    private void register() {
        userName = etUserName.getText().toString();
        phone = etPhone.getText().toString();
        pwd = etPwd.getText().toString();

        //判断用户输入信息的合法性
        if(userName != null && !userName.equals("") && pwd != null && !pwd.equals("") && phone!= null && !phone.equals("")){
            if(userName.length() > 20 || userName.length() < 6){
                Toast.makeText(this, "用户名长度要求在6-20个字符以内", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isPhone(phone)){
                Toast.makeText(this, "电话号码不合法，请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isPwd(pwd)){
                Toast.makeText(this, "密码不合法，请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            //向后台发送请求发送
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
            FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
            formBody.add("userName",userName);//传递键值对参数
            formBody.add("userPhone",phone);//传递键值对参数
            formBody.add("userPassword",pwd);//传递键值对参数
            Request request = new Request.Builder()//创建Request 对象。
                    .url(Constant.USER_REGISTER)
                    .post(formBody.build())//传递请求体
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("RegisterActivity","获取数据失败了");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){//回调的方法执行在子线程
                        Log.i(TAG,"获取数据成功了");
                        Log.i(TAG,"response.code()=="+response.code());
                        Log.i(TAG,"response.body().string()=="+response.body().string());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {//调回到主线程
                                ToastUtils.Toast(RegisterActivity.this,"注册成功",0);
                                //跳转到登录页面
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                intent.putExtra("requestActivity","register");
                                startActivity(intent);//带参数的跳转
                                RegisterActivity.this.finish();
                            }
                        });
                    }
                }
            });
        }else{
            Toast.makeText(this, "注册信息不合法", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 判断电话号码的合法性
     * @param inputText
     * @return
     */
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }
    /**
     * 判断密码的合法性：密码由6-21字母和数字组成，且不能是纯数字或纯英文
     * @param inputText
     * @return
     */
    public static boolean isPwd(String inputText) {
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }
}
