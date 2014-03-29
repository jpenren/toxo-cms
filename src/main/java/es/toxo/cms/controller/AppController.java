package es.toxo.cms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.exceptions.TemplateInputException;

import es.toxo.cms.common.util.CmsUtils;
import es.toxo.cms.exception.PageNotFoundException;
import es.toxo.cms.model.Page;
import es.toxo.cms.model.SiteConfiguration;
import es.toxo.cms.repository.DataRepository;

@Controller
public class AppController {
	
	@Autowired
	private DataRepository repository;
	@Resource(name = "stringTemplateEngine")
	private TemplateEngine engine;
	
	@ModelAttribute("config")
	public SiteConfiguration getConfiguration(){
		return repository.getSiteConfiguration();
	}
	
	@RequestMapping("/")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws PageNotFoundException{
		final Page page = repository.getIndexPage();
		try {
			decorate(page, request, response);
		} catch (TemplateInputException e) {
			model.addAttribute("errorMsg", e.getMessage());
		}
		
		model.addAttribute("page", page);
		model.addAttribute("menuLinks", repository.getMenuLinks());
		return "index";
	}
	
	@RequestMapping("/{friendlyUrl}")
	public String page(Model model, @PathVariable String friendlyUrl, HttpServletRequest request, HttpServletResponse response) throws PageNotFoundException{
		final Page page = repository.getPageByFriendlyUrl(friendlyUrl);
		try {
			decorate(page, request, response);
		} catch (TemplateInputException e) {
			model.addAttribute("errorMsg", e.getMessage());
		}
		
		model.addAttribute("page", page);
		model.addAttribute("menuLinks", repository.getMenuLinks());
		return "index";
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(Exception ex){
		return CmsUtils.createErrorView(ex, repository);
	}
	
	/**
	 * Apply Thymeleaf tag transformation to the page content
	 * @param page
	 * @param request
	 * @param response
	 */
	private void decorate(Page page, HttpServletRequest request, HttpServletResponse response) {
		//Gets Thymeleaf context to process page content
		final IContext context = getContext(request, response);
		final String processedContent = engine.process(page.getContent(), context);
		page.setContent(processedContent);
	}
	
	/**
	 * Gets Thymeleaf context to process templates
	 * @param request current request
	 * @param response current request response
	 * @return Thymeleaf context
	 */
	private IContext getContext(HttpServletRequest request, HttpServletResponse response){
		return new WebContext(request, response, request.getServletContext());
	}
	
}
