package com.example.txjju.smartgenplatform_android.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;
import com.example.txjju.smartgenplatform_android.pojo.User;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
import com.example.txjju.smartgenplatform_android.util.SPUtil;
import com.example.txjju.smartgenplatform_android.util.ToastUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProjectDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    //1.输入“logt”，设置静态常量TAG
    private static final String TAG = "ProjectDetailsActivity";
    private User user;
    private int userId;//保存用户ID
    private String projectId;//保存项目ID
    private int collectClickCount=0;
    private Dialog dialog;
    private Button btnCancel,btnSure;

    private ImageView ivBack,ivProjectPicture,ivProjectUserHeadPortrait,ivProjectLike,ivProjectComment,ivProjectCollect;
    private TextView tvProjectTitle,tvProjectClassify,tvProjectPraise,tvProjectState,tvProjectPublicUserName,tvProjectPublicTime,tvProjectContent,tvProjectEvaluateTime,tvProjectEvaluateResult,tvProjectEvaluateOpinion,tvProjectLike,tvProjectComment,tvProjectCollect;
    private LinearLayout llProjectLike,llProjectComment,llProjectCollect;

    private String result;//装后台返回的数据的变量
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        //获取Intent对象
        Intent intent = getIntent();
        //取出key对应的value值
        projectId = intent.getStringExtra("creProjectId");
        Log.i(TAG,"跳转传过来的数据:"+projectId);
        init();
        // 检测网络
        if (!checkNetwork(this)) {
            Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        loadData(projectId); // 加载服务端数据

    }

    private void loadData(String projectId) {
        final ProgressDialog pgDialog = new ProgressDialog(this);
        pgDialog.setMessage("记载中，请稍后...");
        pgDialog.show();
        // 加载项目列表数据//向后台发送请求，验证用户信息
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("queryParam.condition","id="+projectId);//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.CREPROJECT_GET)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ProjectDetailsActivity","市场：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"市场：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"市场：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"市场：结果："+result);
                    //result = "{\"msg\":\"查询成功\",\"pageSize\":11,\"total\":11,\"page\":1,\"success\":true,\"datas\":[{\"companyId\":null,\"creprojectClassify\":1,\"creprojectContent\":\"这次设计的目的主要是解决婴儿车在使用过程中存在的存\\r\\n\\t\\t\\t\\t在的一些问题。例如，使用周期短，占地空间大等。在功能上使其多样化，能够有一\\r\\n\\t\\t\\t\\t个较长的使用周期陪伴婴儿的成长，在使用的舒适安全的方面也经过了设计，在减震 效果上进行了改良，三轮系的设计，使转弯更灵活。\",\"creprojectEvaluateOpinion\":\"\",\"creprojectEvaluateResult\":0,\"creprojectEvaluateTime\":\"2017-12-27\",\"creprojectLabel\":\"\",\"creprojectModifyTime\":\"2017-12-26\",\"creprojectPicture\":\"\\/SmartgenPlatform\\/img\\/sy6.png\",\"creprojectPlan\":\"\\/SmartgrnPatrform\\/web\\/file\",\"creprojectPraise\":0,\"creprojectReleaseTime\":\"2017-12-26\",\"creprojectState\":0,\"creprojectTitle\":\"多功能婴儿车\",\"creprojectVideo\":\"fgd\",\"expertJobNumber\":null,\"id\":null,\"products\":[],\"userId\":2}]}";
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"市场：测试");
                            BasePojo<Creativeproject> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ProjectDetailsActivity.this, result, new TypeToken<BasePojo<Creativeproject>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess() && basePojo.getTotal() > 0){   // 信息获取成功,有数据
                                        List<Creativeproject> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                        Log.i(TAG,"市场：结果:"+list.size());
                                        pgDialog.dismiss();
                                        //处理所获得数据
                                        if(list.get(0).getCreprojectState()==0){
                                            tvProjectState.setText("未孵化");
                                        }else{
                                            tvProjectState.setText("孵化中");
                                        }
                                        Glide.with(ProjectDetailsActivity.this).load(list.get(0).getCreprojectPicture()).placeholder(R.mipmap.base).into(ivProjectPicture);
                                        Glide.with(ProjectDetailsActivity.this).load(list.get(0).getCreprojectPicture()).placeholder(R.mipmap.base).into(ivProjectUserHeadPortrait);
                                        tvProjectTitle.setText(list.get(0).getCreprojectTitle());
                                        //0为生活手工 1为家具家居 2为科技数码 3为艺术娱乐 4为医疗健康 5为户外运动 6为其他
                                        switch(list.get(0).getCreprojectClassify()){
                                            case 0:
                                                tvProjectClassify.setText("生活手工");
                                                break;
                                            case 1:
                                                tvProjectClassify.setText("家具家居");
                                                break;
                                            case 2:
                                                tvProjectClassify.setText("科技数码");
                                                break;
                                            case 3:
                                                tvProjectClassify.setText("艺术娱乐");
                                                break;
                                            case 4:
                                                tvProjectClassify.setText("医疗健康");
                                                break;
                                            case 5:
                                                tvProjectClassify.setText("户外运动");
                                                break;
                                            case 6:
                                                tvProjectClassify.setText("其他");
                                                break;
                                        }
                                        tvProjectPraise.setText(String.valueOf(list.get(0).getCreprojectPraise())+"");
                                        tvProjectPublicUserName.setText(list.get(0).getCreprojectTitle());
                                        tvProjectPublicTime.setText(list.get(0).getCreprojectReleaseTime().split("T")[0]);
                                        tvProjectContent.setText("    "+list.get(0).getCreprojectContent().trim());
                                        tvProjectEvaluateTime.setText(list.get(0).getCreprojectEvaluateTime().split("T")[0]);
                                        //注意：把一个int型业务数据的设置setText（）方法中， 这样Android系统就会主动去资源文件当中寻找， 但是它不是一个资源文件ID， 所以就会报出这个bug。 将int型业务数据，转换成String类型(String.valueOf())即可。
                                        tvProjectEvaluateResult.setText("审核通过");
                                        tvProjectEvaluateOpinion.setText(list.get(0).getCreprojectEvaluateOpinion());
                                        Log.i(TAG,"转换");
                                       // initPraiseRequest();//初始化点赞状态请求

                                        //获取用户信息
                                        user = SPUtil.getUser(ProjectDetailsActivity.this);
                                        if(user != null){
                                            userId = user.getId();
                                            //userId = 2;//假数据
                                            Log.i(TAG,"用户ID"+userId);
                                            initPraiseRequest();//初始化点赞状态请求
                                        }
                                    }else{
                                        Log.i(TAG,"市场：后台传来失败了"+basePojo.getMsg());
                                       // ToastUtils.Toast(ProjectDetailsActivity.this,basePojo.getMsg(),0);
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
     * 检测网络
     * @return 返回网络检测结果
     */
    private boolean checkNetwork(Context context) {
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //判断网络是否连接
        if (manager.getActiveNetworkInfo()!=null){
            boolean flag=manager.getActiveNetworkInfo().isAvailable();
            if (flag){
                NetworkInfo.State state = manager.getActiveNetworkInfo().getState();
                if (state==NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

    private void init() {
        ivBack = findViewById(R.id.iv_projectDetails_back);
        ivProjectPicture = findViewById(R.id.iv_projectDetails_picture);
        ivProjectUserHeadPortrait = findViewById(R.id.iv_projectDetails_picture);
        ivProjectLike = findViewById(R.id.iv_projectDetails_like);
        ivProjectComment = findViewById(R.id.iv_projectDetails_comment);
        ivProjectCollect = findViewById(R.id.iv_projectDetails_collect);
        tvProjectTitle = findViewById(R.id.tv_projectDetails_title);
        tvProjectClassify = findViewById(R.id.tv_projectDetails_classify);
        tvProjectState = findViewById(R.id.tv_projectDetails_state);
        tvProjectPraise = findViewById(R.id.tv_projectDetails_like);
        tvProjectPublicUserName = findViewById(R.id.tv_projectDetails_public_userName);
        tvProjectPublicTime = findViewById(R.id.tv_projectDetails_public_time);
        tvProjectContent = findViewById(R.id.tv_projectDetails_content);
        tvProjectEvaluateTime = findViewById(R.id.tv_projectDetails_evaluateTime);
        tvProjectEvaluateResult = findViewById(R.id.tv_projectDetails_evaluateResult);
        tvProjectEvaluateOpinion = findViewById(R.id.tv_projectDetails_evaluateOpinion);
        tvProjectLike = findViewById(R.id.tv_projectDetails_like);
        tvProjectComment = findViewById(R.id.tv_projectDetails_comment);
        tvProjectCollect = findViewById(R.id.tv_projectDetails_collect);
        llProjectLike = findViewById(R.id.ll_projectDetails_like);
        llProjectComment = findViewById(R.id.ll_projectDetails_comment);
        llProjectCollect = findViewById(R.id.ll_projectDetails_collect);
        ivBack.setOnClickListener(this);
        llProjectLike.setOnClickListener(this);
        llProjectCollect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_projectDetails_back:
                this.finish();
                break;
            case R.id.ll_projectDetails_like:
                Log.i(TAG,"点赞了");
                // 检测网络
                if (!checkNetwork(ProjectDetailsActivity.this)) {
                    Toast toast = Toast.makeText(ProjectDetailsActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if(user == null){
                    //弹框显示让用户去登录
                    showWarningDialog();
                    return;
                }
                praiseRequest();
                break;
            case R.id.ll_projectDetails_collect:
                Log.i(TAG,"收藏了");
                // 检测网络
                if (!checkNetwork(ProjectDetailsActivity.this)) {
                    Toast toast = Toast.makeText(ProjectDetailsActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if(user == null){
                    //弹框显示让用户去登录
                    showWarningDialog();
                    return;
                }
                collectClickCount++;
                if(collectClickCount%2 == 0){
                    ToastUtils.Toast(ProjectDetailsActivity.this,"取消收藏",0);
                    Glide.with(ProjectDetailsActivity.this).load(R.mipmap.ic_collect_normal).placeholder(R.mipmap.ic_collect_normal).into(ivProjectCollect);
                    tvProjectCollect.setText("收藏");
                }else{
                    ToastUtils.Toast(ProjectDetailsActivity.this,"收藏",0);
                    Glide.with(ProjectDetailsActivity.this).load(R.mipmap.ic_collect_select).placeholder(R.mipmap.ic_collect_select).into(ivProjectCollect);
                    //tvProjectCollect.setText("已收藏");
                }
                //collectRequest();不发请求了，前端处理
                break;
            case R.id.warning_dialog_btn_cancel://取消按钮
                dialog.dismiss();
                break;
            case R.id.warning_dialog_btn_sure://确定按钮
                Log.i(TAG,"确定按钮");
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
                this.finish();
                break;
        }
    }

    private void showWarningDialog() {
        dialog = new Dialog(ProjectDetailsActivity.this, R.style.NormalDialogStyle);
        View view = View.inflate(ProjectDetailsActivity.this, R.layout.warning_dialog, null);
        btnCancel = view.findViewById(R.id.warning_dialog_btn_cancel);
        btnSure = view.findViewById(R.id.warning_dialog_btn_sure);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);

        btnCancel.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        dialog.show();
    }

    /**
     * 初始化点赞状态请求
     */
    private void initPraiseRequest() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("uId",Integer.toString(userId));//传递键值对参数
        formBody.add("creProjectId",projectId);//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象
                .url(Constant.CREPROJECT_GETBYUSERANDCREPROJECT)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ProjectDetailsActivity","项目详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"项目详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"项目详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"项目详情点赞初始化：结果："+result);
                    //result = "{\"msg\":\"查询成功\",\"pageSize\":11,\"total\":11,\"page\":1,\"success\":true,\"datas\":[{\"companyId\":null,\"creprojectClassify\":1,\"creprojectContent\":\"这次设计的目的主要是解决婴儿车在使用过程中存在的存\\r\\n\\t\\t\\t\\t在的一些问题。例如，使用周期短，占地空间大等。在功能上使其多样化，能够有一\\r\\n\\t\\t\\t\\t个较长的使用周期陪伴婴儿的成长，在使用的舒适安全的方面也经过了设计，在减震 效果上进行了改良，三轮系的设计，使转弯更灵活。\",\"creprojectEvaluateOpinion\":\"\",\"creprojectEvaluateResult\":0,\"creprojectEvaluateTime\":\"2017-12-27\",\"creprojectLabel\":\"\",\"creprojectModifyTime\":\"2017-12-26\",\"creprojectPicture\":\"\\/SmartgenPlatform\\/img\\/sy6.png\",\"creprojectPlan\":\"\\/SmartgrnPatrform\\/web\\/file\",\"creprojectPraise\":0,\"creprojectReleaseTime\":\"2017-12-26\",\"creprojectState\":0,\"creprojectTitle\":\"多功能婴儿车\",\"creprojectVideo\":\"fgd\",\"expertJobNumber\":null,\"id\":null,\"products\":[],\"userId\":2}]}";
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"项目详情：测试");
                            BasePojo<Creativeproject> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ProjectDetailsActivity.this, result, new TypeToken<BasePojo<Creativeproject>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        Log.i(TAG,"shulaing"+basePojo.getTotal());
                                       // Log.i(TAG,"haha"+basePojo.getDatas().get(0).getCreremarkPraise());
                                        if(basePojo.getTotal() > 0){ //该用户对该项目已进行点赞
                                            List<Creativeproject> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                            Log.i(TAG,"项目详情：结果:"+list.size());
                                            //处理数据
                                            if(list.get(0).getCreremarkPraise() == 1){//表明点过赞
                                                Glide.with(ProjectDetailsActivity.this).load(R.mipmap.ic_like_select).placeholder(R.mipmap.ic_like_select).into(ivProjectLike);
                                            }
                                        }else{
                                            Log.i(TAG,"项目详情：后台传来数据为空"+basePojo.getMsg());
                                           // ToastUtils.Toast(ProjectDetailsActivity.this,basePojo.getMsg(),0);
                                        }
                                    }else{
                                        Log.i(TAG,"项目详情：后台传来失败了"+basePojo.getMsg());
                                        //ToastUtils.Toast(ProjectDetailsActivity.this,basePojo.getMsg(),0);
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
     * 点赞请求
     */
    private void praiseRequest() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("uId",Integer.toString(userId));//传递键值对参数
        formBody.add("creProjectId",projectId);//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象
                .url(Constant.CREPROJECT_PARISE)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ProjectDetailsActivity","项目详情：获取数据失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"项目详情：查到了?");
                if(response.isSuccessful()){//回调的方法执行在子线程
                    Log.i(TAG,"项目详情：获取数据成功了");
                    Log.i(TAG,"response.code()=="+response.code());
                    //如果这里打印了response.body().string()，则下面赋值结果：result=null，
                    // 因为response.body().string()只能使用一次
                    // Log.i(TAG,"response.body().string()=="+response.body().string());
                    result = response.body().string();
                    Log.i(TAG,"项目详情：结果："+result);
                    //result = "{\"msg\":\"查询成功\",\"pageSize\":11,\"total\":11,\"page\":1,\"success\":true,\"datas\":[{\"companyId\":null,\"creprojectClassify\":1,\"creprojectContent\":\"这次设计的目的主要是解决婴儿车在使用过程中存在的存\\r\\n\\t\\t\\t\\t在的一些问题。例如，使用周期短，占地空间大等。在功能上使其多样化，能够有一\\r\\n\\t\\t\\t\\t个较长的使用周期陪伴婴儿的成长，在使用的舒适安全的方面也经过了设计，在减震 效果上进行了改良，三轮系的设计，使转弯更灵活。\",\"creprojectEvaluateOpinion\":\"\",\"creprojectEvaluateResult\":0,\"creprojectEvaluateTime\":\"2017-12-27\",\"creprojectLabel\":\"\",\"creprojectModifyTime\":\"2017-12-26\",\"creprojectPicture\":\"\\/SmartgenPlatform\\/img\\/sy6.png\",\"creprojectPlan\":\"\\/SmartgrnPatrform\\/web\\/file\",\"creprojectPraise\":0,\"creprojectReleaseTime\":\"2017-12-26\",\"creprojectState\":0,\"creprojectTitle\":\"多功能婴儿车\",\"creprojectVideo\":\"fgd\",\"expertJobNumber\":null,\"id\":null,\"products\":[],\"userId\":2}]}";
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//调回到主线程
                            // 解析Json字符串
                            Log.i(TAG,"项目详情：测试");
                            BasePojo<Creativeproject> basePojo = null;
                            try {
                                //解析数据
                                basePojo = JsonUtil.getBaseFromJson(
                                        ProjectDetailsActivity.this, result, new TypeToken<BasePojo<Creativeproject>>(){}.getType());
                                if(basePojo != null){
                                    if(basePojo.getSuccess()){
                                        if(basePojo.getMsg().equals("点赞成功")){//点赞
                                            loadData(projectId);//重新加载
                                            ToastUtils.Toast(ProjectDetailsActivity.this,basePojo.getMsg(),0);
                                            Glide.with(ProjectDetailsActivity.this).load(R.mipmap.ic_like_select).placeholder(R.mipmap.ic_like_select).into(ivProjectLike);

                                        }
                                        if(basePojo.getMsg().equals("取消点赞成功")){//取消点赞
                                            loadData(projectId);//重新加载
                                            ToastUtils.Toast(ProjectDetailsActivity.this,basePojo.getMsg(),0);
                                            Glide.with(ProjectDetailsActivity.this).load(R.mipmap.ic_like_normal).placeholder(R.mipmap.ic_like_normal).into(ivProjectLike);

                                        }
                                        /*if(basePojo.getTotal() > 0){ //信息获取成功,回传有数据
                                            List<Creativeproject> list  = basePojo.getDatas();  // 获取后台返回的创意项目信息
                                            Log.i(TAG,"项目详情：结果:"+list.size());
                                            //处理数据
                                            if(list.get(0).getCreremarkPraise() == 0){//取消点赞
                                                ToastUtils.Toast(ProjectDetailsActivity.this,basePojo.getMsg(),0);
                                                Glide.with(ProjectDetailsActivity.this).load(R.mipmap.ic_like_normal).placeholder(R.mipmap.ic_like_normal).into(ivProjectLike);
                                            }else{//点赞
                                                ToastUtils.Toast(ProjectDetailsActivity.this,basePojo.getMsg(),0);
                                                Glide.with(ProjectDetailsActivity.this).load(R.mipmap.ic_like_select).placeholder(R.mipmap.ic_like_select).into(ivProjectLike);
                                            }
                                        }else{
                                            Log.i(TAG,"项目详情：后台传来数据为空"+basePojo.getMsg());
                                            ToastUtils.Toast(ProjectDetailsActivity.this,basePojo.getMsg(),0);
                                        }*/
                                    }else{
                                        Log.i(TAG,"项目详情：后台传来失败了"+basePojo.getMsg());
                                        //ToastUtils.Toast(ProjectDetailsActivity.this,basePojo.getMsg(),0);
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
}
