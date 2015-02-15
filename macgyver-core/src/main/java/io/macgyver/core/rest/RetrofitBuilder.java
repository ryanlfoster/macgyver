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
import com.squareup.okhttp.Credentials;

import io.macgyver.core.MacGyverConfigurationException;
import io.macgyver.core.MacGyverException;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.converter.JacksonConverter;

public class RetrofitBuilder {

	Builder builder = new RestAdapter.Builder();

	Class serviceClass;
	String serviceClassName;

	public RetrofitBuilder endpoint(String e) {
		builder = builder.setEndpoint(e);
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

	public RetrofitBuilder basicAuth(final String username, final String password) {
		RequestInterceptor c = new RequestInterceptor() {

			@Override
			public void intercept(RequestFacade request) {
				request.addHeader("Authorization", Credentials.basic(username, password));
				
			}
			
		};
		return interceptor(c);
	}
	public RetrofitBuilder interceptor(RequestInterceptor r) {
		builder = builder.setRequestInterceptor(r);
		return this;
	}

	public Object build() {
		try {
			RestAdapter adapter = builder.setConverter(new JacksonConverter())
					.build();

			Class<?> c = serviceClass;
			
			if (c==null) {
				c = Class.forName(serviceClassName);
			}

			Object object = adapter.create(c);
			return object;

		} catch (ClassNotFoundException e) {
			throw new MacGyverException(e);
		}

	}


}
