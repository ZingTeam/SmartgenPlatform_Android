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
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etPhone,etpwd;
    private Button btnLogin;
    private TextView tvRegister;
    private String phone,pwd;

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    private static final String TAG = "LoginActivity";

    //回传数据的结果码:1代表成功，0代表失败
    private static final int RESULT_LOGIN = 1;
    private static final int REQUEST_REGISTER = 100;//注册请求码
    private static final int REQUEST_MINE = 99;//个人中心请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        // 检测网络
        if (!checkNetwork()) {
            Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        etPhone = findViewById(R.id.et_login_phone);
        etpwd = findViewById(R.id.et_login_pwd);
        tvRegister = findViewById(R.id.tv_login_register);
        btnLogin = findViewById(R.id.btn_login_login);
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
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
            case R.id.tv_login_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);//不带参数的跳转,并且没有回传
                this.finish();
                break;
            case R.id.btn_login_login:
                login();
                break;
        }
    }

    /**
     * 登录，验证用户信息，处理数据，保存数据到sharedPreference
     */
    private void login() {
        phone = etPhone.getText().toString();
        pwd = etpwd.getText().toString();
        if(pwd != null && !pwd.equals("") && phone!= null && !phone.equals("")){
            if(!isPhone(phone)){
                Toast.makeText(this, "电话号码不合法，请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isPwd(pwd)){
                Toast.makeText(this, "密码不合法，请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            //向后台发送请求，验证用户信息
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
            FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
            formBody.add("userPhone",phone);//传递键值对参数
            formBody.add("userPassword",pwd);//传递键值对参数
            Request request = new Request.Builder()//创建Request 对象。
                    .url(Constant.USER_LOGIN)
                    .post(formBody.build())//传递请求体
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("RegisterActivity","获取数据失败了");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG,"查到了?");
                    if(response.isSuccessful()){//回调的方法执行在子线程
                        Log.i(TAG,"获取数据成功了");
                        Log.i(TAG,"response.code()=="+response.code());
                        //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                        // 因为response.body().string()只能使用一次
                       // Log.i(TAG,"response.body().string()=="+response.body().string());
                        result = response.body().string();
                        Log.i(TAG,"结果："+result);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {//调回到主线程
                                // 解析Json字符串
                                Log.i(TAG,"测试");
                                BasePojo<User> basePojo = null;
                                try {
                                    //解析数据
                                    basePojo = JsonUtil.getBaseFromJson(
                                            LoginActivity.this, result, new TypeToken<BasePojo<User>>(){}.getType());
                                    if(basePojo != null){
                                        if(basePojo.getSuccess()){   // 登录成功
                                            User user = basePojo.getDatas().get(0);  // 获取后台返回的用户信息
                                            Log.i(TAG,"结果"+user.toString());
                                            SPUtil.saveUser(LoginActivity.this, true, user);//将用户信息保存到SharedPreferences
                                            ToastUtils.Toast(LoginActivity.this,"登录成功",0);
                                            //根据请求来源不同，跳转到不同页面
                                            Intent  judgeRuester = getIntent();
                                            //跳转到主页
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(intent);//不带参数的跳转
                                            LoginActivity.this.finish();
                                            String requestActivity = judgeRuester.getStringExtra("requestActivity");
                                            /*if(requestActivity != null){
                                                if(requestActivity.equals("mine")){//表明登录请求来源于个人中心，则做以下操作
                                                    Log.i(TAG,"要去个人中心");
                                                    setResult(RESULT_LOGIN);    // 跳转回个人中心
                                                    LoginActivity.this.finish();
                                                }else{//表明登录请求来源于注册，则做以下操作
                                                    //跳转到主页
                                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                    startActivity(intent);//不带参数的跳转
                                                    LoginActivity.this.finish();
                                                }
                                            }else{
                                                ToastUtils.Toast(LoginActivity.this,"登录跳转来源不明",0);
                                            }*/
                            }else{
                                   ToastUtils.Toast(LoginActivity.this,basePojo.getMsg(),0);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }else{
            Toast.makeText(this, "输入信息不能为空", Toast.LENGTH_SHORT).show();
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
