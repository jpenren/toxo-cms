package es.toxo.cms.view.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

public class MenuLinksDialect extends AbstractDialect {

	public String getPrefix() {
		return "menu";
	}
	
	@Override
	public Set<IProcessor> getProcessors() {
		final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new MenuLinksProcessor());
        return processors;
	}

}
