package es.toxo.cms.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import es.toxo.cms.common.config.Configuration;
import es.toxo.cms.exception.PageNotFoundException;
import es.toxo.cms.model.Page;
import es.toxo.cms.model.PageLink;
import es.toxo.cms.model.SiteConfiguration;

@Repository
public class DataRepository {

	private List<Page> pages = new ArrayList<Page>();
	private SiteConfiguration config = createDefaultConfiguration();
	
	public List<PageLink> getMenuLinks() {
		List<PageLink> links = new ArrayList<PageLink>(); 
		Map<String, PageLink> linksMap = new HashMap<String, PageLink>();
		for (Page page : pages) {
			PageLink link = new PageLink();
			link.setFriendlyUrl(page.getFriendlyUrl());
			link.setTitle(page.getTitle());
			link.setUuid(page.getUuid());
			if(StringUtils.isEmpty(page.getParentPage())){
				links.add(link);
				linksMap.put(page.getUuid(), link);
			}else{
				PageLink parentLink = linksMap.get(page.getParentPage());
				if(parentLink!=null){
					if(parentLink.getSubPages()==null){
						parentLink.setSubPages(new ArrayList<PageLink>());
					}
					parentLink.getSubPages().add(link);
				}
			}
		}
		return links;
	}

	public Page getIndexPage() {
		if(pages.isEmpty()){
			pages.add(createDefaultIndexPage());
		}
		
		return pages.get(0);
	}

	public void save(Page page) {
		if(StringUtils.isEmpty(page.getUuid())){
			page.setUuid(UUID.randomUUID().toString());
			pages.add(page);
		}else{
			try {
				Page oldPage = getPageByUuid(page.getUuid());
				int index = pages.indexOf(oldPage);
				pages.set(index, page);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public Page getPageByFriendlyUrl(String friendlyUrl) throws PageNotFoundException {
		for (Page page : pages) {
			if(page.getFriendlyUrl().equals(friendlyUrl)){
				return page;
			}
		}
		
		throw new PageNotFoundException(String.format("Page %s not found", friendlyUrl));
	}

	public Page getPageByUuid(String uuid) throws PageNotFoundException {
		for (Page page : pages) {
			if(page.getUuid().equals(uuid)){
				return page;
			}
		}
		
		throw new PageNotFoundException(String.format("Page %s not found", uuid));
	}

	public Object createPage() {
		Page page = new Page();
		page.setContent(Configuration.get("empty.page.content"));
		return page;
	}

	public void deletePage(String uuid) throws PageNotFoundException {
		Page page = getPageByUuid(uuid);
		pages.remove(page);
	}
	
	public SiteConfiguration getSiteConfiguration() {
		return config;
	}

	public void save(SiteConfiguration configuration) {
		this.config = configuration;
	}
	
	private Page createDefaultIndexPage(){
		Page page = new Page();
		page.setUuid(UUID.randomUUID().toString());
		page.setFriendlyUrl("");
		page.setTitle("Home");
		page.setContent(Configuration.get("empty.index.content"));
		
		return page;
	}
	
	private SiteConfiguration createDefaultConfiguration(){
		SiteConfiguration config = new SiteConfiguration();
		config.setSiteTitle("My new site");
		config.setTitle("My site");
		config.setDescription("My site description");
		
		return config;
	}

}
