package io.macgyver.plugin.atlassian.jira;

import io.macgyver.core.rest.RetrofitServiceFactory;


public class JiraServiceFactory extends RetrofitServiceFactory<JiraClient>{

	public JiraServiceFactory() {
		super("jira",JiraClient.class.getName());
	
	}

}
