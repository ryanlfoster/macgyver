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

import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class RetrofitServiceFactory<T> extends BasicServiceFactory<T> {

	String serviceClassName=null;
	
	public RetrofitServiceFactory(String type, String serviceClassName) {
		super(type);
		this.serviceClassName = serviceClassName;
	}

	protected RetrofitBuilder doCreateRetrofitBuilder(RetrofitBuilder b, ServiceDefinition def) {
		return b;
	}
	@Override
	protected Object doCreateInstance(ServiceDefinition def) {

		String url = def.getProperties().getProperty("url");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(url),"url property must be set");
		
		RetrofitBuilder builder = new RetrofitBuilder().endpoint(def.getProperties().getProperty("url"));
		
		String serviceInterface = def.getProperties().getProperty("serviceClassName");
		if (Strings.isNullOrEmpty(serviceInterface)) {
			serviceInterface = serviceClassName;
		}
		
		builder = builder.serviceClassName(serviceInterface);
		
		Object service = builder.build();
		
		return service;
		

	}

}
