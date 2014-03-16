package es.toxo.cms.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import es.toxo.cms.common.config.Configuration;
import es.toxo.cms.exception.PageNotFoundException;
import es.toxo.cms.model.Page;
import es.toxo.cms.model.PageLink;
import es.toxo.cms.model.SiteConfiguration;

@Repository
public class DataRepository {

	private static final String QUERY_PAGES = "select * from page order by parentPage, position";
	
	@Autowired
	private JdbcTemplate template;
	private SiteConfiguration config = createDefaultConfiguration();
	
	public List<PageLink> getMenuLinks() {
		
		List<Page> pages = queryPages();
		
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
		List<Page> pages = queryPages();
		if(pages.isEmpty()){
			Page defaultIndex = createDefaultIndexPage();
			save(defaultIndex);
			return defaultIndex;
		}
		
		return pages.get(0);
	}

	public void save(Page page) {
		savePage(page);
	}

	public Page getPageByFriendlyUrl(String friendlyUrl) throws PageNotFoundException {
		List<Page> pages = template.queryForList("select * from page where friendlyUrl=?", new Object[]{friendlyUrl}, Page.class);
		if(pages.size()==1){
			return pages.get(0);
		}
		
		throw new PageNotFoundException(String.format("Page %s not found", friendlyUrl));
	}

	public Page getPageByUuid(String uuid) throws PageNotFoundException {
		List<Page> pages = template.queryForList("select * from page where uuid=?", new Object[]{uuid}, Page.class);
		if(pages.size()==1){
			return pages.get(0);
		}
		
		throw new PageNotFoundException(String.format("Page %s not found", uuid));
	}

	public Object createPage() {
		Page page = new Page();
		page.setContent(Configuration.get("empty.page.content"));
		return page;
	}

	public void deletePage(String uuid) {
		template.update("delete from page where uuid=?", new Object[]{uuid});
	}
	
	public SiteConfiguration getSiteConfiguration() {
		return config;
	}

	public void save(SiteConfiguration configuration) {
		this.config = configuration;
	}
	
	private List<Page> queryPages(){
		return template.queryForList(QUERY_PAGES, Page.class);
	}
	
	private void savePage(Page page) {
		if(StringUtils.isEmpty(page.getUuid())){
			page.setUuid(UUID.randomUUID().toString());
			Object[] values = new Object[]{page.getUuid(), page.getTitle(), page.getFriendlyUrl()};
			template.update("insert into page (uuid,title,friendlyUrl) values(?,?,?)", values);
		}
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
