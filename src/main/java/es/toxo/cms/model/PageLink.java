package es.toxo.cms.model;

import java.io.Serializable;

public class PageLink implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String uuid;
	private String friendlyUrl;
	private String title;
	
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
}
