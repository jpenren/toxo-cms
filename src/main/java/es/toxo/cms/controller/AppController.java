package es.toxo.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import es.toxo.cms.common.util.CmsUtils;
import es.toxo.cms.exception.PageNotFoundException;
import es.toxo.cms.model.SiteConfiguration;
import es.toxo.cms.repository.DataRepository;

@Controller
public class AppController {
	
	@Autowired
	private DataRepository repository;
	
	@ModelAttribute("config")
	public SiteConfiguration getConfiguration(){
		return repository.getSiteConfiguration();
	}
	
	@RequestMapping("/")
	public String index(Model model) throws PageNotFoundException{
		model.addAttribute("page", repository.getIndexPage());
		model.addAttribute("menuLinks", repository.getMenuLinks());
		return "index";
	}
	
	@RequestMapping("/{friendlyUrl}")
	public String page(Model model, @PathVariable String friendlyUrl) throws PageNotFoundException{
		model.addAttribute("page", repository.getPageByFriendlyUrl(friendlyUrl));
		model.addAttribute("menuLinks", repository.getMenuLinks());
		return "index";
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(Exception ex){
		return CmsUtils.createErrorView(ex, repository);
	}

}
