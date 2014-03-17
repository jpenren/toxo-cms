package es.toxo.cms.model;

import java.io.Serializable;

public class SiteConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String siteTitle;
	private String title;
	private String description;
	private String keywords;
	private String customStyle;
	private String customJavascript;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCustomStyle() {
		return customStyle;
	}

	public void setCustomStyle(String customStyle) {
		this.customStyle = customStyle;
	}

	public String getCustomJavascript() {
		return customJavascript;
	}

	public void setCustomJavascript(String customJavascript) {
		this.customJavascript = customJavascript;
	}
}
