package es.toxo.cms.view.dialect;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractUnescapedTextChildModifierAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

import es.toxo.cms.model.Page;
import es.toxo.cms.model.PageLink;

public class MenuLinksProcessor extends AbstractUnescapedTextChildModifierAttrProcessor {

	private static final String LI = "<li class=\"%s\"><a href=\"%s\">%s</a></li>";
	private static final String LI_SUBMENU = "<li class=\"dropdown %s\"><a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><span>%s</span><b class=\"caret\"></b></a><ul class=\"dropdown-menu\">%s</ul></li>";
	
	public MenuLinksProcessor() {
		super("generate");
	}

	@Override
	public int getPrecedence() {
		return 10000;
	}
	
	@Override
	protected String getText(Arguments arguments, Element element, String attributeName) {
		final Configuration configuration = arguments.getConfiguration();
		/*
         * Obtain the attribute value
         */
        final String attributeValue = element.getAttributeValue(attributeName);

        /*
         * Obtain the Thymeleaf Standard Expression parser
         */
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        
        /*
         * Parse the attribute value as a Thymeleaf Standard Expression
         */
        final IStandardExpression expression = parser.parseExpression(configuration, arguments, attributeValue);
        
        /*
         * Execute the expression just parsed
         */
        @SuppressWarnings("unchecked")
		final List<PageLink> links = (List<PageLink>) expression.execute(configuration, arguments);
        
        HttpServletRequest request = getContextPath(parser, configuration, arguments);
        StringBuilder builder = new StringBuilder();
        for (PageLink link : links) {
        	if(link.getSubPages()==null){
        		//Main menu
        		builder.append(generateLi(link, request));
        	}else{
        		builder.append(generateLiSubmenu(link, request));
        	}
		}
        
		return builder.toString();
	}
	
	private HttpServletRequest getContextPath(IStandardExpressionParser parser, Configuration configuration, Arguments arguments){
		//Current request
        return (HttpServletRequest) parser.parseExpression(configuration, arguments, "${#httpServletRequest}").execute(configuration, arguments);
	}
	
	private String generateLi(PageLink link, HttpServletRequest request){
		String contextPath = new StringBuilder(request.getContextPath()).append("/").toString();
		String rootPage = getCurrentPage(request).getUuid();
		String clas = rootPage.equals(link.getUuid()) ? "active" : "";
		String href = new StringBuilder(contextPath).append(link.getFriendlyUrl()).toString();
    	String title = link.getTitle();
    	
    	return String.format(LI, clas, href, title);
	}
	
	private Page getCurrentPage(HttpServletRequest request) {
		return (Page) request.getAttribute("page");
	}
	
	private String generateLiSubmenu(PageLink link, HttpServletRequest request){
		StringBuilder builder = new StringBuilder();
		List<PageLink> links = link.getSubPages();
		String currentPage = getCurrentPage(request).getUuid();
		boolean selected = false;
		for (PageLink pageLink : links) {
			//Add submenu links
			if(!pageLink.isHidden()){
				builder.append(generateLi(pageLink, request));
			}
			if(currentPage.equals(pageLink.getUuid())){
				selected = true;
			}
		}
		
		String clas = selected ? "active" : "";
		return String.format(LI_SUBMENU, clas, link.getTitle(), builder.toString());
	}

}
