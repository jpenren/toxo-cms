package es.toxo.cms.view.template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * This class is a Thymeleaf TemplateResolver Hack to use an String content
 * directly.
 * 
 * @author jpenren
 * 
 */
public class StringTemplateResolver extends TemplateResolver {

	public StringTemplateResolver() {
		super.setResourceResolver(new StringResourceResolver());
	}

	/**
	 * Returns the template name as InputStream to be processed by Thymeleaf Engine
	 * @author jpenren
	 */
	private class StringResourceResolver implements IResourceResolver {

		public String getName() {
			return "StringResourceResolver";
		}

		public InputStream getResourceAsStream(final TemplateProcessingParameters templateProcessingParameters, final String resourceName) {
			return new ByteArrayInputStream(resourceName.getBytes());
		}

	}
}