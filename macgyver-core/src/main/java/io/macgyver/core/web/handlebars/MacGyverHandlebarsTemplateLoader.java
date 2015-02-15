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

import io.macgyver.core.Bootstrap;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.github.jknack.handlebars.springmvc.SpringTemplateLoader;

public class MacGyverHandlebarsTemplateLoader extends SpringTemplateLoader {

	ApplicationContext applicationContext;

	Logger logger = LoggerFactory.getLogger(MacGyverHandlebarsTemplateLoader.class);

	public MacGyverHandlebarsTemplateLoader(ApplicationContext applicationContext) {
		super(applicationContext);
		this.applicationContext = applicationContext;
	}

	@Override
	protected URL getResource(String location) throws IOException {

		URL url = null;

		try {

			File resourceLocation = new File(Bootstrap.getInstance()
					.getWebDir(), location);
			if (resourceLocation != null && resourceLocation.exists()) {
				url = resourceLocation.toURI().toURL();
			}

			else {
				String resourceName = "classpath:"
						+ appendPath("web", location);
				Resource resource = applicationContext
						.getResource(resourceName);
				if (resource.exists()) {
					url = resource.getURL();
				}

			}

		} catch (RuntimeException e) {
			logger.warn("problem loading template", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getResource({}): {}", location, url);
		}

		return url;

	}

	String appendPath(String a, String b) {
		StringBuilder sb = new StringBuilder();

		sb.append(a);
		if (!a.endsWith("/") && !b.startsWith("/")) {
			sb.append("/");
		}
		sb.append(b);
		String path = sb.toString();
		while (path.contains("//")) {
			path = path.replace("//", "/");
		}
		return path;
	}

}
