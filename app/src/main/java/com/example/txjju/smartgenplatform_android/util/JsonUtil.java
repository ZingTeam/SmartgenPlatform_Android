package com.example.txjju.smartgenplatform_android.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.example.txjju.smartgenplatform_android.pojo.BasePojo;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Json解析工具类
 */

public class JsonUtil {

    /**
     * 将Json字符串解析为Object对象
     * @param json : 待解析的Json字符串
     * @param tClass : Object对象的类型
     */
    public static <T> T getObjectFromJson(String json, Class<T> tClass) throws IOException {
        // 解析Json字符串
        Gson gson = new Gson();
        T t = gson.fromJson(json, tClass);
        return t;
    }

    /**
     * 将Json字符串解析为List集合
     */
    public static <T> List<T> getListFromJson(String json, Type type) throws IOException {
        // 解析Json字符串
        Gson gson = new Gson();
        List<T> list = gson.fromJson(json, type);   // 解析Json
        return list;
    }

    /**
     * 按照后台封装Json的格式进行解析
     */
    public static <T> BasePojo<T> getBaseFromJson(Context context, String json, Type type) throws IOException {
        // 解析Json字符串
        Gson gson = new Gson();
        BasePojo<T> basePojo = gson.fromJson(json, type);   // 解析Json
        if(basePojo != null){
            //Toast.makeText(context, basePojo.getMsg(), Toast.LENGTH_SHORT).show();
            Log.i("LoginActivity","解析数据成功！");
        }else{
            Log.i("LoginActivity","解析数据失败！");
            Toast.makeText(context, "解析数据失败！", Toast.LENGTH_SHORT).show();
        }
        return basePojo;
    }
}
