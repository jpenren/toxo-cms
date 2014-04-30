package es.toxo.cms.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.web.servlet.ModelAndView;

import es.toxo.cms.exception.UserInfoCapableException;
import es.toxo.cms.repository.DataRepository;

public final class CmsUtils {
	
	private static final String ENCODING = "UTF-8";
	private static final Collection<String> FORBIDDEN_CHARS=Arrays.asList(new String[]{"/","\\"," ","."});
	private static final String REPLACEMENT_CHAR="-";
	
	private CmsUtils() {
	}
	
	public static String cleanFriendlyUrl(String friendlyUrl){
		String cleanUrl=replaceForbiddenChars(friendlyUrl).toLowerCase();
		try {
			return URLEncoder.encode(cleanUrl, ENCODING).toString();
		} catch (Exception e) {
			return cleanUrl;
		}
	}
	
	public static String redirect(String url) {
		return new StringBuilder("redirect:/").append(url).toString();
	}
	
	private static String replaceForbiddenChars(String string){
		String cleanString = string;
		for (String forbiddenChar : FORBIDDEN_CHARS) {
			cleanString = cleanString.replace(forbiddenChar, REPLACEMENT_CHAR);
		}
		
		return cleanString;
	}
	
	public static ModelAndView createErrorView(Exception ex, DataRepository repository){
		ModelAndView model = new ModelAndView();
		model.setViewName("error");
		model.addObject("config", repository.getSiteConfiguration());
		model.addObject("exception", ex);
		if(ex instanceof UserInfoCapableException){
			model.addObject("message", ex.getMessage());
		}
		
		//StackTrace info
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		model.addObject("stackTrace", sw.toString());
		
		return model;
	}

}
