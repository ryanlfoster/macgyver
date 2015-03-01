package io.macgyver.config;

import io.macgyver.plugin.jython.ImportSiteWorkaround;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JythonConfig {

	@Bean
	public ImportSiteWorkaround macJythonSiteImportWorkaround() {
		return new ImportSiteWorkaround();
	}
}
