package io.macgyver.plugin.github;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Optional;

public abstract class WebHookAuthenticator {

	public abstract Optional<Boolean> authenticate(WebHookEvent event, HttpServletRequest request);
	
}
