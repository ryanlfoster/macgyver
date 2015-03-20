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
package io.macgyver.plugin.github;

import io.macgyver.core.ConfigurationException;
import io.macgyver.core.rest.BasicAuthInterceptor;
import io.macgyver.core.rest.OkRest;
import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceRegistry;

import java.io.IOException;
import java.net.Proxy;
import java.util.Properties;
import java.util.Set;

import org.kohsuke.github.GitHub;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.hazelcast.security.Credentials;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class GitHubServiceFactory extends BasicServiceFactory<GitHub> {

	public GitHubServiceFactory() {
		super("github");
	}

	@Override
	protected GitHub doCreateInstance(ServiceDefinition def) {
		try {
			Properties props = def.getProperties();
			String url = props.getProperty("url");
			String oauthToken = props.getProperty("oauthToken");
			String username = props.getProperty("username");
			String password = props.getProperty("password");

			boolean useToken = Strings.isNullOrEmpty(oauthToken) == false;
			boolean useUsernamePassword = (Strings.isNullOrEmpty(username) == false)
					&& (Strings.isNullOrEmpty(password) == false);

			String message = "connecting to {} using {}";

			GitHub gh = null;
			if (url != null) {

				if (useToken) {
					logger.info(message, url, "oauth");
					gh = GitHub.connectToEnterprise(url, oauthToken);
				} else if (useUsernamePassword) {
					logger.info(message, url, "username/password");
					gh = GitHub.connectToEnterprise(url, username, password);
				} else {
					throw new ConfigurationException(
							"connection to GitHub enterprise requires either oauth token or usernmae/password");
				}

			} else {

				if (useToken) {
					logger.info(message, "api.github.com", "oauth");
					gh = GitHub.connectUsingOAuth(oauthToken);
				} else if (useUsernamePassword) {
					logger.info(message, "api.github.com", "username/password");
					gh = GitHub.connectUsingPassword(username, password);
				} else {
					logger.info(message, "api.github.com", "anonymous");
					gh = GitHub.connectAnonymously();
				}

			}
			return gh;
		} catch (IOException e) {
			throw new io.macgyver.core.ConfigurationException(
					"problem creating GitHub client", e);
		}
	}

	@Override
	protected void doCreateCollaboratorInstances(ServiceRegistry registry,
			ServiceDefinition primaryDefinition, Object primaryBean) {

		OkHttpClient c = new OkHttpClient();
		GitHub h;
	
		final String oauthToken = primaryDefinition.getProperties()
				.getProperty("oauthToken");
		final String username = primaryDefinition.getProperties().getProperty(
				"username");
		final String password = primaryDefinition.getProperties().getProperty(
				"password");
		String url =  primaryDefinition.getProperties().getProperty(
				"url");
		if (!Strings.isNullOrEmpty(oauthToken)) {
			logger.info("using oauth");
			c.interceptors().add(new BasicAuthInterceptor(oauthToken, "x-oauth-token"));

		} else if (!Strings.isNullOrEmpty(username)) {
			logger.info("using username/password auth for OkRest client: "+username+"/"+password);
			c.interceptors().add(new BasicAuthInterceptor(username, password));

		}
		else {
			logger.info("using anonymous auth");
		}

		if (Strings.isNullOrEmpty(url)) {
			url = "https://api.github.com";
		}
		OkRest rest = new OkRest(c).url(url);
		
		registry.registerCollaborator(primaryDefinition.getName() + "Api",
				rest);

	}

	@Override
	public void doCreateCollaboratorDefinitions(Set<ServiceDefinition> defSet,
			ServiceDefinition def) {
		ServiceDefinition templateDef = new ServiceDefinition(def.getName()
				+ "Api", def.getName(), def.getProperties(), this);
		defSet.add(templateDef);

	}

}
