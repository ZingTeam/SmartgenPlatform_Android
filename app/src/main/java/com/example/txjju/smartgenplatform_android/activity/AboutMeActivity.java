package com.example.txjju.smartgenplatform_android.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Product;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.ImageUtil;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.example.txjju.smartgenplatform_android.util.VerifyPermission;
import com.example.txjju.smartgenplatform_android.view.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AboutMeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack,ivUserHeadPortrait,ivUserName,ivUserPassword,ivUserSex;
    private CircleImageView imgUserHeadPortrait;
    private RelativeLayout rlUserHeadPortrait,rlUserName,rlUserPassword,rlUserSex;
    private TextView tvUserName,tvUserPassword,tvUserPhone,tvUserSex;
    private Button btnSave;
    private String username="",userpassword="",userphone="";
    private int usersex;
    private String[] sexArry = new String[]{"女", "男"};// 性别选择

    private static final int REQUEST_PHOTO_CAMERA = 1;    // 拍照请求
    private static final int REQUEST_PHOTO_ALBUM = 2;   // 读取本地相册请求
    private static final int RESULT_PERSON = 4;   // 个人信息页面修改成功的结果码
    private File tempFile;;    // 拍照的照片临时存放路径
    private Uri imageUri;   // 拍照的照片返回数据格式
    private Bitmap bitmap;
    private InputStream imgIs;
    private User user;
    private String key; // 存储图片上传到七牛的key

    private static final String TAG = "AboutMeActivity";

    private int userId;//保存用户ID

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler homeHandler = new Handler();
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        init();
        getSpData();//获取用户个人信息
        initPhotoError();   // 解决android7.0系统拍照时，报uri错误问题
    }

    private void initPhotoError() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }


    private void getSpData() {
        user = SPUtil.getUser(this);
        if(user == null){
            return;
        }
        userId = user.getId();
        tvUserName.setText(user.getUserName());
        tvUserPassword.setText(user.getUserPassword());
        tvUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tvUserPhone.setText(user.getUserPhone());
//        tvUserName.setText("小鱼");
//        tvUserPassword.setText("52142154");
//        tvUserPhone.setText("15270254615");
//        tvUserSex.setText("女");
        if(user.getUserSex()==null){
            tvUserSex.setText("暂无");
        }else{
            if (user.getUserSex() == 0) {
                tvUserSex.setText("男");

            } if (user.getUserSex() == 1) {
                tvUserSex.setText("女");
            }

        }
//        String url = user.getUserHeadPortrait();
//        if(url != null){
//            key = url.substring(url.indexOf("image"));  // 保存用户头像的key，用于更新头像时删除七牛原图片
//        }
//        Glide.with(this).load(url).placeholder(R.mipmap.account)
//                .into(imgUserHeadPortrait);
        Glide.with(this).load(user.getUserHeadPortrait()).placeholder(R.mipmap.qx)
               .into(imgUserHeadPortrait);
    }

    private void init() {
        ivBack=findViewById(R.id.iv_aboutMe_back);
        ivUserHeadPortrait=findViewById(R.id.iv_aboutMe_userHeadPortrait);
        imgUserHeadPortrait=findViewById(R.id.img_aboutMe_userHeadPortrait);
        tvUserName=findViewById(R.id.tv_aboutMe_userName);
        tvUserPassword=findViewById(R.id.tv_aboutMe_userPassword);
        tvUserPhone=findViewById(R.id.tv_aboutMe_userPhone);
        tvUserSex=findViewById(R.id.tv_aboutMe_userSex);
        rlUserHeadPortrait=findViewById(R.id.rl_headPortrait);
        rlUserName=findViewById(R.id.rl_name);
        rlUserPassword=findViewById(R.id.rl_password);
        rlUserSex=findViewById(R.id.rl_sex);
        btnSave=findViewById(R.id.btn_aboutMe_save);//保存
        ivBack.setOnClickListener(this);
        rlUserHeadPortrait.setOnClickListener(this);
        ivUserHeadPortrait.setOnClickListener(this);
        rlUserName.setOnClickListener(this);
        rlUserPassword.setOnClickListener(this);
        rlUserSex.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        VerifyPermission.verifyStoragePermission(this);  // 申请读写SD卡权限（读取和存入图片）
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.iv_aboutMe_back:
            this.finish();
            break;
            case R.id.rl_headPortrait:
                getPictureDialogBox();
                break;
            case R.id.rl_name:
                //getNameDialogBox();
                showUserNameDialog("请输入用户名",tvUserName);
                break;
            case R.id.rl_password:
               // getPasswordDialogBox();
                showuserPasswordDialog("请输入密码",tvUserPassword);
                break;
            case R.id.rl_sex:
                getSexDialogBox();
                break;
            case R.id.btn_aboutMe_save:
                uploadUserInfo();   // 发送用户信息到后台更新
                break;
        }
    }

    private void uploadData() {
        /*if(bitmap != null){
            // 在后台申请上传凭证token
            new AsyncHttpClient().post(Constant.TOKEN_URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String json = new String(responseBody);
                    Map<String, String> map = new Gson().fromJson(json,
                            new TypeToken<Map<String, String>>() {
                            }.getType());
                    String upToken = map.get("upToken");
                    if (upToken != null) {
                        // 将Bitmap转换为OutputStream对象
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        UploadManager uploadManager = new UploadManager();
                        String upkey = ImageUtil.getImageKey();
                        // 参数1 ： 上传的图片，byte[]格式
                        // 参数2 ：自定义图片名，必须唯一，可以使用时间戳
                        // 参数3 ： 上传的凭证，从后台获取
                        // 参数4 ：上传后的回调方法，如果上传成功七牛会将图片的完整路径返回
                        uploadManager.put(baos.toByteArray(), upkey, upToken, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (info.statusCode == 200) {
                                    String urlKey = "";
                                    try {
                                        urlKey = response.getString("key");
                                        Toast.makeText(AboutMeActivity.this, "完成上传",
                                                Toast.LENGTH_SHORT).show();
                                        // 图片上传成功后，将用户修改的信息封装在User对象，发送到服务端执行个人信息修改操作
                                        user.setUserHeadPortrait(Constant.BASE_URL + urlKey);
                                        uploadUserInfo();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(AboutMeActivity.this, "图片上传失败，请重试",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, null);
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }else{
            key = null;         // 头像不更新时，key不用传参
            uploadUserInfo();   // 头像不更新，只更新其他信息
        }*/
    }

    private void uploadUserInfo() {
        //params.put("key", key);     // 传给后台用于删除七牛原头像
        //向后台发送请求,修改用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("id",Integer.toString(userId));
        formBody.add("userName",(String)tvUserName.getText());
        formBody.add("userPhone",(String)tvUserPhone.getText());
        formBody.add("userPassword",(String)tvUserPassword.getText());
        if(tvUserSex.getText()!="暂无"){
            if(tvUserSex.getText() == "男"){
                formBody.add("userSex",Integer.toString(1));
            }
            if(tvUserSex.getText() == "女"){
                formBody.add("userSex",Integer.toString(0));
            }
        }
        formBody.add("userHeadPortrait","http://p0vpex4u9.bkt.clouddn.com/head.jpg");
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.USER_UPDATE)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AboutMeActivity","修改用户信息：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"修改用户信息：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"修改用户信息：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"修改用户信息：结果："+result);
                    homeHandler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"修改用户信息：测试");
                            BasePojo<Product> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        AboutMeActivity.this, result, new TypeToken<BasePojo<Product>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){   // 信息获取成功,有数据
                                        AboutMeActivity.this.finish();
                                        refleshUser();
                                       // Log.i(TAG,"修改用户信息：结果"+list.toString());
                                    }else{
                                        ToastUtils.Toast(AboutMeActivity.this,basePojo.getMsg(),0);
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

    private void refleshUser() {
        //向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userPhone",(String) tvUserPhone.getText());//传递键值对参数
        formBody.add("userPassword",(String) tvUserPassword.getText());//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.USER_LOGIN)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AboutMeActivity","获取数据失败了");
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
                                        AboutMeActivity.this, result, new TypeToken<BasePojo<User>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){   // 登录成功
                                        User user = basePojo.getDatas().get(0);  // 获取后台返回的用户信息
                                        Log.i(TAG,"结果"+user.toString());
                                        SPUtil.clearUser(AboutMeActivity.this);
                                        SPUtil.saveUser(AboutMeActivity.this, true, user);//将用户信息保存到SharedPreferences
                                        ToastUtils.Toast(AboutMeActivity.this,"修改成功",0);
                                        //根据请求来源不同，跳转到不同页面
                                        Intent  judgeRuester = getIntent();
                                        //跳转到主页
                                        Intent intent = new Intent(AboutMeActivity.this,MainActivity.class);
                                        startActivity(intent);//不带参数的跳转
                                        AboutMeActivity.this.finish();
                                    }else{
                                        ToastUtils.Toast(AboutMeActivity.this,basePojo.getMsg(),0);
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


    private void getPictureDialogBox() {
        new AlertDialog.Builder(this)//
                .setIcon(R.mipmap.gb)
                .setTitle("选择头像")//

                .setNegativeButton("相册", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choosePhoto();  // 选择相册
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();    // 选择相机
                        dialog.dismiss();
                    }
                }).show();
    }

    private void getNameDialogBox() {
        // 使用LayoutInflater来加载dialog_setname.xml布局
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View nameView = layoutInflater.inflate(R.layout.name_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // 使用setView()方法将布局显示到dialog
        alertDialogBuilder.setView(nameView);

        final EditText userInput =  nameView.findViewById(R.id.name_edit);
        final TextView name =  findViewById(R.id.tv_aboutMe_userName);
          userInput.setText(name.getText());//待修改值放入编辑框

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.gb)//设置标题的图片
                .setTitle("输入用户名")//设置对话框的标题
                .setView(nameView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = userInput.getText().toString();
                        name.setText(userInput.getText()); //显示重新设置的用户名
                        Toast.makeText(AboutMeActivity.this, content, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void getSexDialogBox() {

        final TextView sex =  findViewById(R.id.tv_aboutMe_userSex);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
        builder.setIcon(R.mipmap.gb);//设置标题的图片
        builder.setTitle("选择你的性别");//设置对话框的标题
        builder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                // showToast(which+"");
                sex.setText(sexArry[which]);
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            }
        });
        builder.show();// 让弹出框显示
    }

    private void getPasswordDialogBox() {
        // 使用LayoutInflater来加载dialog_setname.xml布局
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View passwordView = layoutInflater.inflate(R.layout.password_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // 使用setView()方法将布局显示到dialog
        alertDialogBuilder.setView(passwordView);

        final EditText userInput = passwordView.findViewById(R.id.password_edit);
        final TextView password =  findViewById(R.id.tv_aboutMe_userPassword);
         userInput.setText(password.getText());  //待修改值放入编辑框

         AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.gb)//设置标题的图片
                .setTitle("输入密码")//设置对话框的标题
                .setView(passwordView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = userInput.getText().toString();
                        password.setText(userInput.getText()); //显示重新设置的密码
                        Toast.makeText(AboutMeActivity.this, content, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
    /**创建存储拍照照片的文件夹，并调起相机**/
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调起相机
        // 判断存储卡是否可以用，可用进行存储
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(), filename + ".jpg");     // 指定图片文件名
            imageUri = Uri.fromFile(tempFile);   // 从文件中创建uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 指定拍照返回的uri
        }
        startActivityForResult(intent, REQUEST_PHOTO_CAMERA);
    }
    private void choosePhoto() {
        // 打开选择图片的界面
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
    }
    /**
     * 拍照或者在相册中选择图片后，图片以URI格式返回
     **/
    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        if (req == REQUEST_PHOTO_CAMERA && res == RESULT_OK) {
            try {
                imgIs = new FileInputStream(tempFile);
                bitmap = BitmapFactory.decodeStream(imgIs);  // 通过图片工厂将输入流转换为Bitmap对象，这里得到的是原图
                // ImageView显示不了超大图片，改用Glide加载手机图片
                Glide.with(this).load(tempFile)
                        .placeholder(R.mipmap.account).into(imgUserHeadPortrait);
            } catch (Exception e) {
                Toast.makeText(this, "获取照片失败", Toast.LENGTH_SHORT).show();
            }
        } else if (req == REQUEST_PHOTO_ALBUM && res == RESULT_OK) {
            try {
                // 该uri是上一个Activity返回的
                Uri uri = data.getData();
                imgIs = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(imgIs);
                if (bitmap != null) {
                    imgUserHeadPortrait.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                Toast.makeText(this, "获取照片失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**编辑个人信息**/
    private void showuserPasswordDialog(String hint, final TextView tv) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View nameView = layoutInflater.inflate(R.layout.name_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // 使用setView()方法将布局显示到dialog
        alertDialogBuilder.setView(nameView);

        final EditText userInput =  nameView.findViewById(R.id.name_edit);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.gb)//设置标题的图片
                .setTitle(hint)//设置对话框的标题
                .setView(nameView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = userInput.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(AboutMeActivity.this,
                                    "输入信息不能为空！", Toast.LENGTH_SHORT).show();
                        } else {
                            if(!isPwd(input)){
                                Toast.makeText(AboutMeActivity.this, "密码不合法，请重新输入", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            tvUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            tv.setText(input);
                            switch (tv.getId()){
                                case R.id.tv_aboutMe_userName :
                                    user.setUserName(input);
                                    break;
                                case R.id.tv_aboutMe_userPassword :
                                   tvUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    user.setUserPassword(input);
                                    break;
                            }
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    /**编辑个人信息**/
    private void showUserNameDialog(String hint, final TextView tv) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View nameView = layoutInflater.inflate(R.layout.name_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // 使用setView()方法将布局显示到dialog
        alertDialogBuilder.setView(nameView);

        final EditText userInput =  nameView.findViewById(R.id.name_edit);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.gb)//设置标题的图片
                .setTitle(hint)//设置对话框的标题
                .setView(nameView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = userInput.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(AboutMeActivity.this,
                                    "输入信息不能为空！", Toast.LENGTH_SHORT).show();
                        } else {
                            if(input.length() > 20 || input.length() < 6){
                                Toast.makeText(AboutMeActivity.this, "用户名长度要求在6-20个字符以内", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            tvUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            tv.setText(input);
                            switch (tv.getId()){
                                case R.id.tv_aboutMe_userName :
                                    user.setUserName(input);
                                    break;
                                case R.id.tv_aboutMe_userPassword :
                                    tvUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    user.setUserPassword(input);
                                    break;
                            }
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
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

