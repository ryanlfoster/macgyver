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



import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;

import com.squareup.okhttp.Credentials;

import io.macgyver.core.MacGyverConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.macgyver.core.MacGyverException;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.converter.Converter;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.squareup.okhttp.Credentials;

public class RetrofitBuilder {

	private Builder builder = new RestAdapter.Builder();

	private Class serviceClass;
	private String serviceClassName;

	
	public RetrofitBuilder url(String x) {
		return endpoint(x);
	}
	
	public RetrofitBuilder endpoint(String e) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(e), "endpoint cannot be null");
		builder = builder.setEndpoint(e);
		return this;
	}

	public RetrofitBuilder converter(Converter c) {
		Preconditions.checkNotNull(c);
		builder = builder.setConverter(c);
		return this;
	}

	public RetrofitBuilder serviceClass(Class c) {
		this.serviceClass = c;
		return this;
	}

	public RetrofitBuilder serviceClassName(String className) {
		this.serviceClassName = className;
		return this;
	}

	public RetrofitBuilder basicAuth(final String username,
			final String password) {
		RequestInterceptor c = new RequestInterceptor() {

			@Override
			public void intercept(RequestFacade request) {
				request.addHeader("Authorization",
						Credentials.basic(username, password));

			}

		};
		return interceptor(c);
	}

	public RetrofitBuilder interceptor(RequestInterceptor r) {
		builder = builder.setRequestInterceptor(r);
		return this;
	}

	public RestAdapter buildRestAdatper() {
		
		Logger logger = null;
		
		if (serviceClass != null) {
			logger = LoggerFactory.getLogger(serviceClass);
		}
		else if (serviceClassName!=null) {
			logger = LoggerFactory.getLogger(serviceClassName);
		}
		final Logger lg = logger;
		
		if (lg!=null && lg.isDebugEnabled()) {
		
			RestAdapter.Log log = new RestAdapter.Log() {
			
				@Override
				public void log(String message) {	
				
					lg.debug(message);
					
				}
			};
			builder = builder.setLogLevel(RestAdapter.LogLevel.FULL).setLog(log);
		}
		
		
		
		// not thrilled with having to turn logging up regardless of whether we are logging or not
		return builder.build();
	}

	public Object build() {
		try {
			RestAdapter adapter = buildRestAdatper();

			Class<?> c = serviceClass;

			if (c == null) {
				Preconditions.checkArgument(
						!Strings.isNullOrEmpty(serviceClassName),
						"serviceClass or serviceClassName must be set");
				c = Class.forName(serviceClassName);
			}
			
			Object object = adapter.create(c);
			return object;

		} catch (ClassNotFoundException e) {
			throw new MacGyverException(e);
		}

	}

}
