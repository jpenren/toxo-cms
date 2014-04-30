package es.toxo.cms.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

	private static final String COUNT_CONFIGURATION = "select count(*) from site_configuration";
	private static final String COUNT_PAGES = "select count(*) from page";
	private static final String GET_CONFIGURATION = "select * from site_configuration where id=1";
	private static final String GET_PAGES = "select * from page order by parentPage, position";
	private static final String PAGE_BY_UUID = "select * from page where uuid=?";
	private static final String PAGE_BY_FRIENDLY_URL = "select * from page where friendlyUrl=?";
	private static final String DELETE_BY_UUID = "delete from page where uuid=?";
	private static final String INSERT_SITE_CONFIGURATION = "insert into site_configuration (id,customJavascript,customStyle,description,keywords,siteTitle,title) values(?,?,?,?,?,?,?)";
	private static final String UPDATE_SITE_CONFIGURATION = "update site_configuration set customJavascript=?,customStyle=?,description=?,keywords=?,siteTitle=?,title=? where id=?";
	private static final String INSERT_PAGE = "insert into page (content,customJavascript,customStyle,friendlyUrl,hidden,parentPage,position,title,uuid) values(?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_PAGE = "update page set content=?,customJavascript=?,customStyle=?,friendlyUrl=?,hidden=?,parentPage=?,position=?,title=? where uuid=?";
	private static final String PAGE_FRIENDLY_URLS = "select uuid from page order by parentPage, position";
	private final Logger log = LoggerFactory.getLogger(DataRepository.class);
	
	@Autowired
	private JdbcTemplate template;
	private SiteConfiguration config;
	
	public void initialize() {
		try {
			final String sql = IOUtils.toString(Initialization.class.getResource("/database/scheme.sql"));
			template.execute(sql);
		} catch (Exception e) {
			log.error("Error crating database: ", e);
		}
	}
	
	public int countPages(){
		return template.queryForObject(COUNT_PAGES, Integer.class);
	}
	
	public boolean existsConfiguration(){
		return template.queryForObject(COUNT_CONFIGURATION, Integer.class)>0;
	}
	
	public List<PageLink> getMenuLinks() {
		List<Page> pages = template.query(GET_PAGES, new BeanPropertyRowMapper<Page>(Page.class));
		
		List<PageLink> links = new ArrayList<PageLink>(); 
		Map<String, PageLink> linksMap = new HashMap<String, PageLink>();
		for (Page page : pages) {
			PageLink link = new PageLink();
			link.setFriendlyUrl(page.getFriendlyUrl());
			link.setTitle(page.getTitle());
			link.setUuid(page.getUuid());
			link.setHidden(page.isHidden());
			if(StringUtils.isEmpty(page.getParentPage())){
				//Main menu links
				links.add(link);
				linksMap.put(page.getUuid(), link);
			}else{
				//Submenu links
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

	public Page getIndexPage() throws PageNotFoundException {
		List<String> urls = template.queryForList(PAGE_FRIENDLY_URLS, String.class);
		if(urls.size()>0){
			return getPageByUuid(urls.get(0));
		}
		throw new PageNotFoundException("Index page not found");
	}

	public void save(Page page) {
		if(StringUtils.isEmpty(page.getUuid())){
			insert(page);
		}else{
			update(page);
		}
	}
	
	public Page getPageByFriendlyUrl(String friendlyUrl) throws PageNotFoundException {
		Page page = template.queryForObject(PAGE_BY_FRIENDLY_URL, new String[]{friendlyUrl}, new BeanPropertyRowMapper<Page>(Page.class));
		if(page==null){
			throw new PageNotFoundException(String.format("Page %s not found", friendlyUrl));
		}
		
		return page;
	}

	public Page getPageByUuid(String uuid) throws PageNotFoundException {
		Page page = template.queryForObject(PAGE_BY_UUID, new String[]{uuid}, new BeanPropertyRowMapper<Page>(Page.class));
		if(page==null){
			throw new PageNotFoundException(String.format("Page %s not found", uuid));
		}
		
		return page;
	}

	public Page createPage() {
		Page page = new Page();
		page.setContent(Configuration.get("empty.page.content"));
		return page;
	}

	public void deletePage(String uuid) {
		template.update(DELETE_BY_UUID, new Object[]{uuid});
	}
	
	public SiteConfiguration getSiteConfiguration() {
		if(this.config==null){
			this.config = template.queryForObject(GET_CONFIGURATION, new BeanPropertyRowMapper<SiteConfiguration>(SiteConfiguration.class));
		}
		return config;
	}

	public void save(SiteConfiguration configuration) {
		if(configuration.getId()==null){
			insert(configuration);
		}else{
			update(configuration);
		}
		//force reload on next query
		config=null;
	}
	
	private void insert(Page page) {
		page.setUuid(UUID.randomUUID().toString());
		page.setPosition(this.countPages());
		Object[] values = new Object[]{page.getContent(), page.getCustomJavascript(), page.getCustomStyle(),page.getFriendlyUrl(),page.isHidden(),page.getParentPage(),page.getPosition(), page.getTitle(),page.getUuid()};
		template.update(INSERT_PAGE, values);
	}
	
	private void update(Page page) {
		Object[] values = new Object[]{page.getContent(), page.getCustomJavascript(), page.getCustomStyle(),page.getFriendlyUrl(),page.isHidden(),page.getParentPage(),page.getPosition(), page.getTitle(),page.getUuid()};
		template.update(UPDATE_PAGE, values);
	}
	
	private void insert(SiteConfiguration configuration) {
		Object[] values = new Object[]{1, configuration.getCustomJavascript(), configuration.getCustomStyle(), configuration.getDescription(), configuration.getKeywords(), configuration.getSiteTitle(), configuration.getTitle()};
		template.update(INSERT_SITE_CONFIGURATION, values);
	}
	
	private void update(SiteConfiguration configuration) {
		Object[] values = new Object[]{configuration.getCustomJavascript(), configuration.getCustomStyle(), configuration.getDescription(), configuration.getKeywords(), configuration.getSiteTitle(), configuration.getTitle(), 1};
		template.update(UPDATE_SITE_CONFIGURATION, values);
	}
	
}
