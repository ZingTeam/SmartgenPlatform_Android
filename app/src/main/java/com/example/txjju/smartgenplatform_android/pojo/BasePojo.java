package com.example.txjju.smartgenplatform_android.pojo;

import java.util.List;

/**
 * 公共数据的基本实体类，便于解析
 */
public class BasePojo<T> {

	private String msg;
	private int pageSize;//页码列表数
	private int total;// 列表总数
	private int page;// 页码
	private boolean success;
	private List<T> datas;
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageSize() {
		return pageSize;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotal() {
		return total;
	}

	public void setPage(int page) {
		this.page = page;
	}
	public int getPage() {
		return page;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean getSuccess() {
		return success;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
	public List<T> getDatas() {
		return datas;
	}

	@Override
	public String toString() {
		return "BasePojo [success=" + success + ", msg=" + msg + ", page=" + page + ",pageSize=" + pageSize + ", total=" + total + ", list="
				+ datas + "]";
	}

}
