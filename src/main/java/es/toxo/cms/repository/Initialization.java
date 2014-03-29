package es.toxo.cms.repository;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

import es.toxo.cms.common.config.Configuration;
import es.toxo.cms.model.Page;
import es.toxo.cms.model.SiteConfiguration;

@Component
public class Initialization {

	private final Logger log = LoggerFactory.getLogger(Initialization.class);
	@Autowired
	private DataRepository repository;
	
	@PostConstruct
	public void initialize(){
		
		log.info("Initializing database");
		
		try {
			//Try to make some operation on database to determine if exists
			repository.countPages();
		} catch (BadSqlGrammarException e) {
			log.info("Database not exists, will try to create the database");
			repository.initialize();
		}
		
		//Creates default index if not exists
		boolean existsIndexPage =repository.countPages()>0;
		if(!existsIndexPage){
			Page defaultIndex = createDefaultIndexPage();
			repository.save(defaultIndex);
		}
		
		//Creates default configuration if not exists
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
