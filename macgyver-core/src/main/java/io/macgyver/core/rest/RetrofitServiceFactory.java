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
package io.macgyver.core.rest;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

public class RetrofitServiceFactory<T> extends BasicServiceFactory<T> {

	String serviceClassName=null;
	
	public RetrofitServiceFactory(String type, String serviceClassName) {
		super(type);
		this.serviceClassName = serviceClassName;
	}

	@Override
	protected Object doCreateInstance(ServiceDefinition def) {
		//"https://server/rest/api/latest"
		
		
		RetrofitBuilder builder = new RetrofitBuilder().endpoint(def.getProperties().getProperty("url"));
		
		String serviceInterface = def.getProperties().getProperty("serviceClassName");
		if (Strings.isNullOrEmpty(serviceInterface)) {
			serviceInterface = serviceClassName;
		}
		
		builder = builder.serviceClassName(serviceInterface);
		builder = builder.basicAuth(def.getProperties().getProperty("username"), def.getProperties().getProperty("password"));
		
		Object service = builder.build();
		
		return service;
		

	}

}
