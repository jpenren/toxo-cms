package es.toxo.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.toxo.cms.exception.PageNotFoundException;
import es.toxo.cms.model.Page;
import es.toxo.cms.repository.DataRepository;
import es.toxo.cms.util.CmsUtils;

@Controller
public class AdminController {
	
	@Autowired
	private DataRepository repository;
	
	@RequestMapping("/login")
	public String login(){
		return "admin/login";
	}
	
	@RequestMapping("/admin/pages/add")
	public String addPage(Model model){
		model.addAttribute(repository.createPage());
		return "admin/addPage";
	}
	
	@RequestMapping("/admin/pages/{uuid}")
	public String editPage(Model model, @PathVariable String uuid) throws PageNotFoundException{
		Page page = repository.getPageByUuid(uuid);
		model.addAttribute(page);
		return "admin/editPage";
	}
	
	@RequestMapping(value = "/admin/pages/save")
	public String savePage(Page page){
		String friendlyUrl = page.getFriendlyUrl();
		if(StringUtils.isEmpty(friendlyUrl)){
			friendlyUrl = page.getTitle();
		}
		final String url = CmsUtils.cleanFriendlyUrl(friendlyUrl);
		page.setFriendlyUrl(url);
		repository.save(page);
		
		return CmsUtils.redirect(url);
	}

}
