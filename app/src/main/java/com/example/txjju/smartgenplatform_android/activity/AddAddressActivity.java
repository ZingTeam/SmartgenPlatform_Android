package com.example.txjju.smartgenplatform_android.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Purchaseaddress;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
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

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName,tvPhone,tvAddress;
    private String name,phone,address;
    private Button btnSave;
    public final static String TAG = "AddAddressActivity";
    private User user;
    private int userId;//保存用户ID
    public String productId;//保存产品ID
    private static final int RESULT_ADDRESSLIST = 1;//1代表成功，0代表失败

    private String addressResult,result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        init();
        //获取用户信息
        user = SPUtil.getUser(AddAddressActivity.this);
        if(user != null){
            userId = user.getId();
            //userId = 2;//假数据
            Log.i(TAG,"产品用户ID"+userId);
        }
    }

    private void init() {
        tvName = findViewById(R.id.tv_add_address_name);
        tvPhone = findViewById(R.id.tv_edit_address_phone);
        tvAddress = findViewById(R.id.tv_add_address_address);
        btnSave = findViewById(R.id.btn_add_address_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_add_address_save:
                name = tvName.getText().toString();
                phone = tvPhone.getText().toString();
                address = tvAddress.getText().toString();
                if(name == null){
                    Toast.makeText(this, "收货人不能为空", Toast.LENGTH_SHORT).show();
                }
                if(phone == null){
                    Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                }
                if(address == null){
                    Toast.makeText(this, "收货地址不能为空", Toast.LENGTH_SHORT).show();
                }
                if(!isPhone(phone)){
                    Toast.makeText(this, "电话号码不合法，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                addAddress();
                break;
        }
    }

    private void addAddress() {
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        Log.i(TAG,"提交订单用户ID"+userId);
        formBody.add("user.id",Integer.toString(userId));//获取用户收货详细信息
        formBody.add("puraddressUserName",name);//获取用户收货详细信息
        formBody.add("puraddressUserPhone",phone);//获取用户收货详细信息
        formBody.add("puraddressAddress",address);//获取用户收货详细信息
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.USER_PURCHASEADDRESS)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AddAddressActivity","保存收货地址：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"保存收货地址：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"保存收货地址：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    addressResult = response.body().string();
                    Log.i(TAG,"保存收货地址：结果："+addressResult);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"保存收货地址：测试");
                            BasePojo<Purchaseaddress> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        AddAddressActivity.this, addressResult, new TypeToken<BasePojo<Purchaseaddress>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){   // 信息获取成功,有数据
                                        AddAddressActivity.this.finish();
                                        //Intent intent = new Intent(AddAddressActivity.this,UpdateAdressActivity.class);
                                        //startActivity(intent);
                                        setResult(RESULT_ADDRESSLIST);
                                        ToastUtils.Toast(AddAddressActivity.this,"新增成功",0);
                                    }else{
                                        ToastUtils.Toast(AddAddressActivity.this,"新增失败",0);
                                        //ToastUtils.Toast(AddAddressActivity.this,basePojo.getMsg(),0);
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
}
