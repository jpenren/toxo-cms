package es.toxo.cms.model;

import java.io.Serializable;

public class SiteConfiguration implements Serializable{
	private static final long serialVersionUID = 1L;
	private String siteTitle;
	private String title;
	private String description;
	private String theme;
	
	public String getSiteTitle() {
		return siteTitle;
	}
	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
}
