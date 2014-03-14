package es.toxo.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.toxo.cms.exception.PageNotFoundException;
import es.toxo.cms.repository.DataRepository;

@Controller
public class AppController {
	
	@Autowired
	private DataRepository service;
	
	@RequestMapping("/")
	public String index(Model model){
		model.addAttribute("page", service.getIndexPage());
		model.addAttribute("menuLinks", service.getMenuLinks());
		return "index";
	}
	
	@RequestMapping("/{friendlyUrl}")
	public String page(Model model, @PathVariable String friendlyUrl) throws PageNotFoundException{
		model.addAttribute("page", service.getPageByFriendlyUrl(friendlyUrl));
		model.addAttribute("menuLinks", service.getMenuLinks());
		return "index";
	}

}
