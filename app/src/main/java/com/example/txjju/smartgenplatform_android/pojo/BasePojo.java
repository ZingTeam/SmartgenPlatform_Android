package com.example.txjju.smartgenplatform_android.pojo;

import java.util.ArrayList;
import java.util.List;

public class BasePojo<T> {
	private boolean success;
	private int page;
	private int total;
	private String msg;
	private List<T> data = new ArrayList();
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "BasePojo [success=" + success + ", page=" + page + ", total=" + total + ", msg=" + msg + ", data="
				+ data + "]";
	}
}
