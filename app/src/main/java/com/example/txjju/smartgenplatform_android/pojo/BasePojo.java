package com.example.txjju.smartgenplatform_android.pojo;

import java.util.ArrayList;
import java.util.List;

public class BasePojo<T> {

	private boolean success;
	private String msg;
	private int page; 	// 页码
	private int total; // 总页数
	private List<T> data = new ArrayList<>();

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
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
	public List<T> getList() {
		return data;
	}
	public void setList(List<T> list) {
		this.data = list;
	}
	
	@Override
	public String toString() {
		return "BasePojo [success=" + success + ", msg=" + msg + ", page=" + page + ", total=" + total + ", list="
				+ data + "]";
	}
}
