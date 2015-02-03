package io.macgyver.plugin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.macgyver.plugin.atlassian.jira.JiraServiceFactory;

@Configuration
public class AtlassianConfig {

	
	@Bean
	JiraServiceFactory jiraServiceFactory() {
		return new JiraServiceFactory();
	}
}
