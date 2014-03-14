package es.toxo.cms.util;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;

public final class CmsUtils {
	
	private static final String ENCODING = "UTF-8";
	private static final Collection<String> FORBIDDEN_CHARS=Arrays.asList(new String[]{"/","\\"});
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

}
