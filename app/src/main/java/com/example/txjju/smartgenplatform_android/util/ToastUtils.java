package com.example.txjju.smartgenplatform_android.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txjju.smartgenplatform_android.view.SHApplication;

/**
 * 提示框工具类
 */
public class ToastUtils {

    public static void showToast(CharSequence text) {
        Toast(SHApplication.getInstance(), text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context ct, CharSequence text) {
        Toast(ct, text, Toast.LENGTH_SHORT);
    }

    public static void Toast(Context context, CharSequence text, int duration) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        TextView v = new TextView(context);
        v.setBackgroundColor(0x88000000);
        v.setTextColor(Color.WHITE);
        v.setText(text);
        v.setSingleLine(false);
        v.setPadding(25, 15, 25, 15);
        v.setGravity(Gravity.CENTER);
        toast.setView(v);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
