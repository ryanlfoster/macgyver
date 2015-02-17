package io.macgyver.plugin.jenkins;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.base.Strings;

public class JenkinsClientBuilder {

	
	private String url;
	private String username;
	private String password;
	
	public JenkinsClientBuilder() {
		
	}
	

	public JenkinsClientBuilder url(String url) {
		this.url = url;
		return this;
	}
	
	public JenkinsClientBuilder credentials(String username, String password) {
		this.username=username;
		this.password=password;
		return this;
	}
	
	public JenkinsClient build() {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(url));
		Preconditions.checkArgument(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"), "url must be http or https");
		return new JenkinsClientImpl(url, username, password);
	}
}
