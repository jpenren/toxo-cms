package es.toxo.cms.repository;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
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
		
		try {
			//Try to make some operation on database to determine if exists
			repository.countPages();
		} catch (BadSqlGrammarException e) {
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
