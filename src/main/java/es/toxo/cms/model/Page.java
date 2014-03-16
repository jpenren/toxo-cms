package es.toxo.cms.model;

import java.io.Serializable;

public class Page implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String uuid;
	private String friendlyUrl;
	private String title;
	private String content;
	private String customStyle;
	private String customJavascript;
	private int position;
	private boolean hidden;
	private String parentPage;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getFriendlyUrl() {
		return friendlyUrl;
	}
	public void setFriendlyUrl(String friendlyUrl) {
		this.friendlyUrl = friendlyUrl;
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
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public String getParentPage() {
		return parentPage;
	}
	public void setParentPage(String parentPage) {
		this.parentPage = parentPage;
	}
}
