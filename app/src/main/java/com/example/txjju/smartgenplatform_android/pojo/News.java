package com.example.txjju.smartgenplatform_android.pojo;

public class News{
	private int id;
	private String icon;
	private String title;
	private String content;
	private String type;
	private String comment;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "NewsInfo [id=" + id + ", icon=" + icon + ", title=" + title + ", content=" + content + ", type=" + type
				+ ", comment=" + comment + "]";
	}
}
