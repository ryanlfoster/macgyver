/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
