package com.tdcm.hmyanmar.Dataset;

public class EpisodeEntry {

	private int id;
	private String section;
	private String total;
	private String offset;
	private String limit;
	private String title;
	private String thumbnail;
	
	public EpisodeEntry(){
		
	}
	
	public EpisodeEntry(int id, String section, String total, String offset,
			String limit, String title, String thumbnail) {
		super();
		this.id = id;
		this.section = section;
		this.total = total;
		this.offset = offset;
		this.limit = limit;
		this.title = title;
		this.thumbnail = thumbnail;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	
}

