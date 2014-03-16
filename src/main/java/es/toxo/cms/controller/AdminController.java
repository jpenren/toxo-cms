package es.toxo.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import es.toxo.cms.common.util.CmsUtils;
import es.toxo.cms.controller.validator.AdminValidator;
import es.toxo.cms.exception.InternalErrorException;
import es.toxo.cms.exception.PageNotFoundException;
import es.toxo.cms.model.Page;
import es.toxo.cms.model.SiteConfiguration;
import es.toxo.cms.repository.DataRepository;

@Controller
public class AdminController {
	
	@Autowired
	private DataRepository repository;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new AdminValidator());
	}
	
	@RequestMapping("/login")
	public String login(){
		return "admin/login";
	}
	
	@RequestMapping("/admin/pages/add")
	public String addPage(Model model){
		model.addAttribute("menuLinks", repository.getMenuLinks());
		model.addAttribute(repository.createPage());
		return "admin/addPage";
	}
	
	@RequestMapping("/admin/pages/{uuid}")
	public String editPage(Model model, @PathVariable String uuid) throws PageNotFoundException{
		Page page = repository.getPageByUuid(uuid);
		model.addAttribute(page);
		model.addAttribute("menuLinks", repository.getMenuLinks());
		return "admin/editPage";
	}
	
	@RequestMapping(value = "/admin/pages/save")
	public String savePage(@Validated Page page, BindingResult result) throws InternalErrorException{
		
		if(result.hasErrors()){
			throw new InternalErrorException(result.getAllErrors().toString());
		}
		
		String friendlyUrl = page.getFriendlyUrl();
		if(StringUtils.isEmpty(friendlyUrl)){
			friendlyUrl = page.getTitle();
		}
		final String url = CmsUtils.cleanFriendlyUrl(friendlyUrl);
		page.setFriendlyUrl(url);
		repository.save(page);
		
		return CmsUtils.redirect(url);
	}
	
	@RequestMapping("/admin/pages/delete/{uuid}")
	public String deletePage(@PathVariable String uuid) throws PageNotFoundException{
		repository.deletePage(uuid);
		return CmsUtils.redirect("/");
	}
	
	@RequestMapping("/admin/configuration")
	public String configuration(Model model){
		model.addAttribute("config", repository.getSiteConfiguration());
		return "admin/configuration";
	}
	
	@RequestMapping("/admin/configuration/save")
	public String saveConfiguration(SiteConfiguration configuration){
		repository.save(configuration);
		return CmsUtils.redirect("/");
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(Exception ex){
		return CmsUtils.createErrorView(ex, repository);
	}

}
