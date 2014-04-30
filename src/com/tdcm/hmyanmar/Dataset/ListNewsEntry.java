package com.tdcm.hmyanmar.Dataset;

import com.tdcm.hmyanmar.Util.Util;


public class ListNewsEntry {

	private int id;
	private String section;
	private String thumbnail;
	private String title;
	private String description;
	private String share_url;
	private String subCategory;
	
	public ListNewsEntry(){
		
	}
	public ListNewsEntry(int id, String section, String thumbnail,
			String title, String description, String share_url) {
		super();
		this.id = id;
		this.section = section;
		this.thumbnail = thumbnail;
		this.title = title;
		this.description = description;
		this.share_url = share_url;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSection() {
		return Util.isNotNullNotEmptyNotWhiteSpace(section);
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getThumbnail() {
		return Util.isNotNullNotEmptyNotWhiteSpace(thumbnail);
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getTitle() {
		return Util.isNotNullNotEmptyNotWhiteSpace(title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return Util.isNotNullNotEmptyNotWhiteSpace(description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShare_url() {
		return Util.isNotNullNotEmptyNotWhiteSpace(share_url);
	}
	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	
}

