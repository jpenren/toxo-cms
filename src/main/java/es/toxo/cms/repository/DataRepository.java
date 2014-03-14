package es.toxo.cms.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import es.toxo.cms.exception.PageNotFoundException;
import es.toxo.cms.model.Page;
import es.toxo.cms.model.PageLink;

@Repository
public class DataRepository {

	private List<Page> pages = new ArrayList<Page>();
	
	public DataRepository() {
		Page page = new Page();
		page.setUuid(UUID.randomUUID().toString());
		page.setFriendlyUrl("");
		page.setTitle("Home");
		pages.add(page);
	}
	
	public List<PageLink> getMenuLinks() {
		List<PageLink> links = new ArrayList<PageLink>(); 
		for (Page page : pages) {
			PageLink link = new PageLink();
			link.setFriendlyUrl(page.getFriendlyUrl());
			link.setTitle(page.getTitle());
			link.setUuid(page.getUuid());
			links.add(link);
		}
		return links;
	}

	public Page getIndexPage() {
		return pages.get(0);
	}

	public void save(Page page) {
		try {
			Page oldPage = getPageByUuid(page.getUuid());
			int index = pages.indexOf(oldPage);
			pages.set(index, page);
		} catch (Exception e) {
			pages.add(page);
		}
	}

	public Page getPageByFriendlyUrl(String friendlyUrl) throws PageNotFoundException {
		for (Page page : pages) {
			if(page.getFriendlyUrl().equals(friendlyUrl)){
				return page;
			}
		}
		
		throw new PageNotFoundException();
	}

	public Page getPageByUuid(String uuid) throws PageNotFoundException {
		for (Page page : pages) {
			if(page.getUuid().equals(uuid)){
				return page;
			}
		}
		
		throw new PageNotFoundException();
	}

	public Object createPage() {
		Page page = new Page();
		page.setUuid(UUID.randomUUID().toString());
		return page;
	}

}
