package com.lqlsoftware.demo.entity;

// 图片实体类
public class ImgFile {
	
	// 大小
	private long size;
	
	// 类型
	private String type;
	
	// 路径
	private String path;
	
	// id
	private long id;
	
	// 发送用户id
	private String userId;

	public long getSize() {
		return size;
	}

	public void setSize(long l) {
		this.size = l;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
