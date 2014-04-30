package com.tdcm.hmyanmar.Dataset;

public class NovelEntry {

	private String section;
	private int totalPage;
	private int nowPage;
	private String description;
	private int view;
	
	public NovelEntry(){
		
	}

	public NovelEntry(String section, int totalPage, int nowPage,
			String description, int view) {
		super();
		this.section = section;
		this.totalPage = totalPage;
		this.nowPage = nowPage;
		this.description = description;
		this.view = view;
	}


	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}
	
	
	
}

