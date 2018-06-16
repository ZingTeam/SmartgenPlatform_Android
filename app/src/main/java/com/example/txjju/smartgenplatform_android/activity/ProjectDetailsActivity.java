package com.example.txjju.smartgenplatform_android.activity;

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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.config.Constant;
import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.example.txjju.smartgenplatform_android.pojo.Creativeproject;
import com.example.txjju.smartgenplatform_android.util.JsonUtil;
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

    private ImageView ivBack;

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
        String projectId = intent.getStringExtra("id");
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
                Log.d("MainActivity","市场：获取数据失败了");
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
                                        /*projectList.clear();
                                        projectList.addAll(list);
                                        pgDialog.dismiss();
                                        projectAdapter.resetDatas();//刷新时，数据源置为空
                                        updateRecyclerView(0, PAGE_COUNT);// 通知适配器更新列表
                                        refreshLayout.finishRefresh();  //停止刷新*/
                                    }else{
                                        Log.i(TAG,"市场：后台传来失败了"+basePojo.getMsg());
                                        //ToastUtils.Toast(getActivity(),basePojo.getMsg(),0);
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
