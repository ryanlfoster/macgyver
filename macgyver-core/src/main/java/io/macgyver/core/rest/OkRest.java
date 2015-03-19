package io.macgyver.core.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.ning.http.client.RequestBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class OkRest {

	private OkHttpClient client;
	private String url;

	static class KVPair {
		String key;
		String val;
	}

	private Multimap<String, String> queryParameters = ArrayListMultimap
			.create();

	public class InvocationBuilder {
		Request.Builder okBuilder = new Request.Builder();

		InvocationBuilder(Request.Builder okBuilder) {
			this.okBuilder = okBuilder;
		}

		public Response execute() throws IOException {
			Call c = getClient().newCall(okBuilder.build());
			return c.execute();
		}

		public Request.Builder okBuilder() {
			return okBuilder;
		}

		public InvocationBuilder with(Request.Builder b) {
			return new InvocationBuilder(b);
		}

		public InvocationBuilder post(RequestBody body) {
			return new InvocationBuilder(okBuilder.post(body));
		}

		public InvocationBuilder get() {
			return new InvocationBuilder(okBuilder.get());
		}

		public InvocationBuilder put(RequestBody body) {
			return new InvocationBuilder(okBuilder.put(body));
		}

		public InvocationBuilder delete(RequestBody body) {
			return new InvocationBuilder(okBuilder.delete());
		}
	}

	public OkRest() {
		this(new OkHttpClient(), null);
	}

	public OkRest(OkHttpClient c) {
		this(c, null);
	}

	public OkRest(OkHttpClient c, String url) {
		this(c, url, null);
	}

	OkRest(OkHttpClient c, String url, Multimap<String, String> qp) {

		this.client = c;
		this.url = url;
		if (qp != null) {
			this.queryParameters.putAll(qp);
		}
	}

	public OkRest queryParameter(String key, Object... vals) {

		ArrayListMultimap<String, String> mm = ArrayListMultimap.create();
		mm.putAll(queryParameters);
		for (Object x : vals) {
			if (x != null) {
				mm.put(key, x.toString());
			}
		}
		OkRest copy = new OkRest(client, url, mm);
		return copy;

	}

	public OkRest path(String path) {
		return new OkRest(client, new UrlBuilder(url).path(path).build(),
				queryParameters);
	}

	public OkRest url(String url) {
		return new OkRest(client, url, queryParameters);
	}

	public InvocationBuilder request() {
		String urlWithQueryString = toUrlWithQueryString();
		InvocationBuilder b = new InvocationBuilder(
				new Request.Builder().url(urlWithQueryString));
		return b;
	}

	public String getUrl() {
		return url;
	}
	public OkHttpClient getClient() {
		return client;
	}

	public Call newCall(Request.Builder b) {
		return client.newCall(b.build());
	}

	public String toUrlWithQueryString() {
		try {
			if (url == null) {
				throw new IllegalStateException("invalid url: " + url);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(url);

			if (!queryParameters.isEmpty()) {
				int count = 0;

				for (Entry<String, String> x : queryParameters.entries()) {
					if (count == 0) {
						sb.append("?");
					} else {
						sb.append("&");
					}
					sb.append(URLEncoder.encode(x.getKey(), "UTF8"));
					sb.append("=");
					sb.append(URLEncoder.encode(x.getValue(), "UTF8"));
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
