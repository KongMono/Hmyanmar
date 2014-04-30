package com.tdcm.hmyanmar.Dataset;

public class ListHClusiveEntry {

	private int id;
	private String thumbnail;
	private String title;

	public ListHClusiveEntry(){
		
	}
	
	public ListHClusiveEntry(int id, String thumbnail, String title) {
		super();
		this.id = id;
		this.thumbnail = thumbnail;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}

