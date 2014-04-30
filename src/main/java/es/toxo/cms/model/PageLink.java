package es.toxo.cms.model;

import java.io.Serializable;
import java.util.List;

public class PageLink implements Serializable {
	private static final long serialVersionUID = 1L;
	private String uuid;
	private String friendlyUrl;
	private String title;
	private List<PageLink> subPages;
	private boolean hidden;

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

	public List<PageLink> getSubPages() {
		return subPages;
	}

	public void setSubPages(List<PageLink> subPages) {
		this.subPages = subPages;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
