package com.example.txjju.smartgenplatform_android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txjju.smartgenplatform_android.R;
import com.example.txjju.smartgenplatform_android.view.CircleImageView;

public class AboutMeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack,ivUserHeadPortrait,ivUserName,ivUserPassword,ivUserSex;
    private CircleImageView imgUserHeadPortrait;
    private RelativeLayout rlUserHeadPortrait,rlUserName,rlUserPassword,rlUserSex;
    private TextView tvUserName,tvUserPassword,tvUserPhone,tvUserSex;
    private Button btnSave;
    private String username="",userpassword="",userphone="";
    private int usersex;
    private String[] sexArry = new String[]{"女", "男"};// 性别选择



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        init();
        getSpData();//获取用户个人信息

    }



    private void getSpData() {
//        User user = SPUtil.getUser(this);
//        username=user.getUser_name();
//        userpassword=user.getUser_password();
//        userphone=user.getUser_phone();
//        usersex=user.getUser_sex();
    }

    private void init() {
        ivBack=findViewById(R.id.iv_aboutMe_back);
        ivUserHeadPortrait=findViewById(R.id.iv_aboutMe_userHeadPortrait);
        tvUserName=findViewById(R.id.tv_aboutMe_userName);
        tvUserPassword=findViewById(R.id.tv_aboutMe_userPassword);
        tvUserPhone=findViewById(R.id.tv_aboutMe_userPhone);
        tvUserSex=findViewById(R.id.tv_aboutMe_userSex);
        rlUserHeadPortrait=findViewById(R.id.rl_headPortrait);
        rlUserName=findViewById(R.id.rl_name);
        rlUserPassword=findViewById(R.id.rl_password);
        rlUserSex=findViewById(R.id.rl_sex);
        btnSave=findViewById(R.id.btn_aboutMe_save);
        ivBack.setOnClickListener(this);
        rlUserHeadPortrait.setOnClickListener(this);
        ivUserHeadPortrait.setOnClickListener(this);
        rlUserName.setOnClickListener(this);
        rlUserPassword.setOnClickListener(this);
        rlUserSex.setOnClickListener(this);
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
                getNameDialogBox();
                break;
            case R.id.rl_password:
                getPasswordDialogBox();
                break;
            case R.id.rl_sex:
                getSexDialogBox();
                break;
            case R.id.btn_aboutMe_save:
                break;
        }
    }

    private void getPictureDialogBox() {
        new AlertDialog.Builder(this)//
                .setIcon(R.mipmap.gb)
                .setTitle("选择头像")//

                .setNegativeButton("相册", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })

                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
}
