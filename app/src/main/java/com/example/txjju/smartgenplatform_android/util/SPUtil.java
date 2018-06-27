package com.example.txjju.smartgenplatform_android.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.txjju.smartgenplatform_android.pojo.User;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * SharedPreference帮助类，获取及保存用户登录状态、用户个人信息
 */
public class SPUtil {

    private static final String SP_NAME = "SP_USER";

    /*保存用户登录状态及个人信息*/
    public static void saveUser(Context context, boolean isLogin, User user){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.putString("user", new Gson().toJson(user));//将实体对象转换为json字符串，因为SharedPreferences只能存储字符串
        editor.commit();
    }

    /*获取用户登录状态*/
    public static boolean isLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        return isLogin;
    }

    /*获取用户个人信息*/
    public static User getUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String userJson = sp.getString("user", "");
        try {
            if(userJson != null){
                User user = JsonUtil.getObjectFromJson(userJson, User.class);
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }/*获取用户个人信息*/
    public static void clearUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
