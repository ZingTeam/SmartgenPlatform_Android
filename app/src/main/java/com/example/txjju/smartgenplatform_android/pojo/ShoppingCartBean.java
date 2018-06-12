package com.example.txjju.smartgenplatform_android.pojo;

/**
 * Created by Administrator on 2018/6/10.
 */

public class ShoppingCartBean {
    String ShoppingName;
    double Price;
    int Count;
    boolean isChoosed;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    String picture;
    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getShoppingName() {
        return ShoppingName;
    }

    public void setShoppingName(String shoppingName) {
        ShoppingName = shoppingName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

}
