package com.ibm.cloud.ablum.beans;

import java.sql.Date;
import java.util.ArrayList;

public class CategoryBean {
	private ArrayList photoList;
	private long categoryId;
	private long userId;
	private String categoryName;
	private String categoryDesc;
	private Date categoryDate;
	
	public void setPhotoList(ArrayList list){
		photoList = list;
	}
	public ArrayList getPhotoList(){
		return photoList;
	}
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public Date getCategoryDate() {
		return categoryDate;
	}
	public void setCategoryDate(Date categoryDate) {
		this.categoryDate = categoryDate;
	}
	
}
