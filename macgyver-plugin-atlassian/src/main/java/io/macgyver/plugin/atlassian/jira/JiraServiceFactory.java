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
package io.macgyver.plugin.atlassian.jira;

import retrofit.converter.JacksonConverter;
import io.macgyver.core.rest.RetrofitBuilder;
import io.macgyver.core.rest.RetrofitServiceFactory;
import io.macgyver.core.service.ServiceDefinition;


public class JiraServiceFactory extends RetrofitServiceFactory<JiraClient>{

	public JiraServiceFactory() {
		super("jira",JiraClient.class.getName());
	
	}

	
	@Override
	protected RetrofitBuilder doCreateRetrofitBuilder(RetrofitBuilder b, ServiceDefinition def) {
		RetrofitBuilder builder = b.converter(new JacksonConverter()).basicAuth(def.getProperties().getProperty("username"), def.getProperties().getProperty("password"));
	
		return builder;
	}

}
