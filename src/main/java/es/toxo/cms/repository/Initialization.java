package es.toxo.cms.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.toxo.cms.common.config.Configuration;
import es.toxo.cms.model.Page;
import es.toxo.cms.model.SiteConfiguration;

@Component
public class Initialization {

	@Autowired
	private DataRepository repository;
	
	@PostConstruct
	public void initialize(){
		//Create default index if not exists
		int count =repository.countPages();
		if(count==0){
			Page defaultIndex = createDefaultIndexPage();
			repository.save(defaultIndex);
		}
		
		boolean existsConfig = repository.existsConfiguration();
		if(!existsConfig){
			SiteConfiguration defaultConfiguration = createDefaultConfiguration();
			repository.save(defaultConfiguration);
		}
	}

	private Page createDefaultIndexPage(){
		Page page = new Page();
		page.setFriendlyUrl("home");
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
