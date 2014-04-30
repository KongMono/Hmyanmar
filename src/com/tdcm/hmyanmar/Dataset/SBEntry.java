package com.tdcm.hmyanmar.Dataset;

public class SBEntry {

    private String title;
    private String title_en;
    private String icon;
    private String icon_active;
    private String url;
    private String template;
    private String type;
    
    public SBEntry(){
    	
    }
    
	public SBEntry(String title, String title_en, String icon,
			String icon_active, String url, String template, String type) {
		super();
		this.title = title;
		this.title_en = title_en;
		this.icon = icon;
		this.icon_active = icon_active;
		this.url = url;
		this.template = template;
		this.type = type;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle_en() {
		return title_en;
	}
	public void setTitle_en(String title_en) {
		this.title_en = title_en;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIcon_active() {
		return icon_active;
	}
	public void setIcon_active(String icon_active) {
		this.icon_active = icon_active;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
    
    
}
