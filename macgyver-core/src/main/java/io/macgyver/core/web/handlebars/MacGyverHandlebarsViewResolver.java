package io.macgyver.core.web.handlebars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.github.jknack.handlebars.io.URLTemplateLoader;
import com.github.jknack.handlebars.springmvc.HandlebarsView;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;

public class MacGyverHandlebarsViewResolver extends HandlebarsViewResolver {

	Logger logger = LoggerFactory.getLogger(MacGyverHandlebarsViewResolver.class);
	
	public MacGyverHandlebarsViewResolver() {
		super();
		
		
		setCache(false);  // need way to set this dynamically
		
	}

	public MacGyverHandlebarsViewResolver(
			Class<? extends HandlebarsView> viewClass) {
		super(viewClass);
		
	}

	@Override
	protected URLTemplateLoader createTemplateLoader(ApplicationContext context) {
		
		URLTemplateLoader loader = new MacGyverHandlebarsTemplateLoader(context);
		
		logger.info("createTemplateLoader: {}",loader);
		return loader;
	}

}
