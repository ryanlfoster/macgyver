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
package io.macgyver.plugin.ci.jenkins;

import io.macgyver.core.rest.RestException;
import io.macgyver.core.rest.UrlBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class JenkinsClientImpl implements JenkinsClient {

	Logger logger = LoggerFactory.getLogger(JenkinsClientImpl.class);
	OkHttpClient client = new OkHttpClient();

	private String urlBase;

	private String username;
	private String password;

	protected JenkinsClientImpl(String urlBase, String username, String password) {
		this.urlBase = urlBase;
		this.username = username;
		this.password = password;
	}

	Builder injectAuth(Builder b) {
		if (!Strings.isNullOrEmpty(username)
				|| !Strings.isNullOrEmpty(password)) {
			b = b.addHeader("Authorization",
					com.squareup.okhttp.Credentials.basic(username, password));
		}
		return b;
	}

	@Override
	public JsonNode getJson(String path) {
		try {

			Request request = injectAuth(new Request.Builder())
					.url(new UrlBuilder(urlBase).path(path).build()).get()
					.build();

			Response response = client.newCall(request).execute();
			throwRestExceptionOnError(response);
			JsonNode n = new ObjectMapper().readTree(response.body()
					.byteStream());

			return n;
		} catch (IOException e) {
			throw new RestException(e);
		}
	}

	@Override
	public JsonNode getServerInfo() {
		return getJson("api/json");
	}

	@Override
	public JsonNode getJob(String jobName) {

		return getJson(new UrlBuilder("job").path(jobName).path("api/json")
				.build());

	}

	@Override
	public JsonNode getBuild(String jobName, int buildNumber) {

		return getJson(new UrlBuilder("job").path(jobName).path("api/json")
				.build());

	}

	protected void throwRestExceptionOnError(Response r) {
		int sc = r.code();
		String body = null;
		if (sc > 299) {

			try {
				body = r.body().string();

			} catch (Exception e) {
			}
			logger.warn("sc={} body: {}", sc, body);
			throw new RestException(sc);
		}
	}

	@Override
	public org.jdom2.Document getJobConfig(String jobName) {
		try {

			String url = new UrlBuilder(urlBase).path("job").path(jobName)
					.path("config.xml").build();

			Request request = injectAuth(new Request.Builder()).url(url).get()
					.build();

			Response response = client.newCall(request).execute();
			throwRestExceptionOnError(response);

			return new SAXBuilder().build(response.body().byteStream());

		} catch (IOException | JDOMException e) {
			throw new RestException(e);
		}
	}

	@Override
	public List<String> getJobNames() {
		List<String> tmp = new ArrayList<String>();
		for (JsonNode n : Lists.newArrayList(getServerInfo().path("jobs")
				.elements())) {
			tmp.add(n.path("name").asText());
		}
		return tmp;
	}

	@Override
	public String executeGroovyScript(String groovy) {
		try {

			String url = new UrlBuilder(urlBase).path("scriptText").build();

			RequestBody formBody = new FormEncodingBuilder().add("script",
					groovy).build();

			Request request = injectAuth(new Request.Builder()).url(url)
					.post(formBody).build();

			Response response = client.newCall(request).execute();

			throwRestExceptionOnError(response);

			return response.body().string();

		} catch (IOException e) {
			throw new RestException(e);
		}
	}

	@Override
	public JsonNode build(String jobName) {
		try {

			String url = new UrlBuilder(urlBase).path("job").path(jobName)
					.path("build").build();

			RequestBody formBody = new FormEncodingBuilder().add("__dummy__",
					"").build();

			Request request = injectAuth(new Request.Builder())
					.addHeader("Accept", "application/json").url(url)
					.post(formBody).build();

			Response response = client.newCall(request).execute();

			throwRestExceptionOnError(response);

			Optional<String> qp = extractQueuePath(response.header("Location"));

			if (qp.isPresent()) {
				return getJson(new UrlBuilder(qp.get()).path("api/json")
						.build());
			} else {
				throw new IllegalStateException(
						"jenkins should have returned a Locaton header");
			}

		} catch (IOException e) {
			throw new RestException(e);
		}
	}

	Optional<String> extractQueuePath(String location) {
		Pattern p = Pattern.compile(".*(\\/queue\\/item\\/\\d+)[$\\/]*.*");
		Matcher m = p.matcher(location);
		if (m.matches()) {
			return Optional.fromNullable(m.group(1));
		}
		return Optional.absent();

	}

	@Override
	public JsonNode buildWithParameters(String jobName, Map<String, String> m) {
		try {

			String url = new UrlBuilder(urlBase).path("job").path(jobName)
					.path("buildWithParameters").build();

			FormEncodingBuilder builder = new FormEncodingBuilder();

			if (m == null || m.isEmpty()) {
				builder = builder.add("__dummy__", "__dummy__");
			} else {
				for (Map.Entry<String, String> entry : m.entrySet()) {
					builder = builder.add(entry.getKey(), entry.getValue());
				}
			}

			RequestBody formBody = builder.build();

			Request request = injectAuth(new Request.Builder())
					.addHeader("Accept", "application/json").url(url)
					.post(formBody).build();

			Response response = client.newCall(request).execute();

			throwRestExceptionOnError(response);

			Optional<String> qp = extractQueuePath(response.header("Location"));

			if (qp.isPresent()) {
				return getJson(new UrlBuilder(qp.get()).path("api/json")
						.build());
			} else {
				throw new IllegalStateException(
						"jenkins should have returned a Locaton header");
			}

		} catch (IOException e) {
			throw new RestException(e);
		}
	}

	@Override
	public JsonNode buildWithParameters(String name, String... args) {

		com.google.common.base.Preconditions.checkArgument(
				!Strings.isNullOrEmpty(name),
				"job name cannot be null or empty");

		Map<String, String> m = new HashMap<>();

		if (args != null) {
			Preconditions.checkArgument(args.length % 2 == 0,
					"parameters must be in key value pairs");
			for (int i = 0; i < args.length - 1; i += 2) {
				String key = args[i];
				String val = args[i + 1];
				m.put(key, val);

			}
		}
		return buildWithParameters(name, m);
	}

	@Override
	public JsonNode getBuildQueue() {

		return getJson(new UrlBuilder("queue/api/json")
				.build());
	}

	@Override
	public JsonNode getLoadStats() {
		return getJson(new UrlBuilder("overallLoad/api/json")
		.build());
	}

	@Override
	public void restart() {
		try {
			postWithoutResult("restart");
		}
		catch (RestException e) {
			if (e.getStatusCode()==503) {
				return;
			}
			throw e;
		}
	}

	@Override
	public void restartAfterJobsComplete() {
		try {
			postWithoutResult("safeRestart");
		}
		catch (RestException e) {
			if (e.getStatusCode()==503) {
				return;
			}
			throw e;
		}
		
	}

	@Override
	public void quietDown() {
		postWithoutResult("quietDown");
		
	}

	protected void postWithoutResult(String path) {
		try {

			String url = new UrlBuilder(urlBase).path(path).build();



			Request request = injectAuth(new Request.Builder())
					.addHeader("Accept", "application/json").url(url)
					.post(RequestBody.create(MediaType.parse("application/json"), "{}")).build();

			Response response = client.newCall(request).execute();

			throwRestExceptionOnError(response);

	

		} catch (IOException e) {
			throw new RestException(e);
		}
	}
	@Override
	public void cancelQuietDown() {
		postWithoutResult("cancelQuietDown");
	}
	
	public String getServerId() {
		return Hashing.sha1().hashString(getServerUrl(), Charsets.UTF_8).toString();
	}

	@Override
	public String getServerUrl() {
		return urlBase;
	}

}
