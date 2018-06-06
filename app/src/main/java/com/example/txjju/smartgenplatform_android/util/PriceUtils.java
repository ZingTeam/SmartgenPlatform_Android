package com.example.txjju.smartgenplatform_android.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * 价格工具类，对价格样式进行设置
 */
public class PriceUtils {

    public static String priceDoubleNoDot(double data) {
        BigDecimal bdRet = new BigDecimal(data);
        String price = bdRet.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        int dotIndex=price.indexOf(".");
        price=price.substring(0,dotIndex);
        return price;
    }
//对“价格”字体的大小进行设置
    public static void convertPriceSize(Context context, TextView tv, String money, int fontSize) {
        Spannable spannable = new SpannableString(money);
        if (money.contains(".")){
            int indexDot = money.indexOf(".");
            spannable.setSpan(new AbsoluteSizeSpan(UIUtils.dp2px(context, fontSize)), indexDot, money.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        spannable.setSpan(new AbsoluteSizeSpan(UIUtils.dp2px(context, fontSize)), 1, money.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(spannable);
    }
}
