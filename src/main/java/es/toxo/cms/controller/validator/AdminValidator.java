package es.toxo.cms.controller.validator;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import es.toxo.cms.model.Page;

public class AdminValidator implements Validator {

	public boolean supports(Class<?> cls) {
		return true;
	}

	public void validate(Object bean, Errors errors) {
		if(bean instanceof Page){
			Page page = (Page) bean;
			if(!StringUtils.isEmpty(page.getUuid()) && !StringUtils.isEmpty(page.getParentPage())){
				if(page.getUuid().equals(page.getParentPage())){
					errors.reject("The page cannot be his parent");
				}
			}
		}
	}

}
