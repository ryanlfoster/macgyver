package io.macgyver.core.web.handlebars;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.github.jknack.handlebars.springmvc.SpringTemplateLoader;

public class MacGyverTemplateLoader extends SpringTemplateLoader {

	Logger logger = LoggerFactory.getLogger(MacGyverTemplateLoader.class);
	public MacGyverTemplateLoader(ApplicationContext applicationContext) {
		super(applicationContext);
	}

	@Override
	protected URL getResource(String location) throws IOException {
		
		URL url = super.getResource(location);
		
		logger.info("getResource({}) = {}",location,url);
		return url;
	}

	@Override
	public String resolve(String location) {
		
		String r = super.resolve(location);
		
		logger.info("resolve({}) = {}",location,r);
		
		return r;
	}

}
