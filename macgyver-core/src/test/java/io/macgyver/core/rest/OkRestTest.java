package io.macgyver.core.rest;

import java.io.IOException;

import javax.ws.rs.client.WebTarget;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.squareup.okhttp.mockwebserver.rule.MockWebServerRule;

public class OkRestTest {

	static OkHttpClient okClient = new OkHttpClient();

	@Rule
	public MockWebServerRule mockServer = new MockWebServerRule();

	public OkRest mock(MockResponse r) {
		mockServer.enqueue(r);
		OkRest okRest = new OkRest(okClient, mockServer.getUrl("/test")
				.toString());
		return okRest;
	}

	@Test
	public void testNoArgConstructor() throws IOException, InterruptedException {

		OkRest r = new OkRest();
		Assertions.assertThat(r.getClient()).isNotNull();

		mockServer.enqueue(new MockResponse().setBody("{}"));

		try {
			r.request().get().execute();
		} catch (Exception e) {
			Assertions.assertThat(e)
					.isInstanceOf(IllegalStateException.class);
		}
		
		mockServer.enqueue(new MockResponse().setBody("{}"));

		try {
			r=r.path("x");
			r.request().get().execute();
		} catch (Exception e) {
			Assertions.assertThat(e)
					.isInstanceOf(IllegalStateException.class);
		}
		
		
	}

	@Test
	public void testIt() throws IOException, InterruptedException {

		OkRest r = mock(new MockResponse().addHeader("Content-type",
				"application/json").setBody("{}"));

		r.request().get().execute().body().string();

		RecordedRequest request = mockServer.takeRequest();

		Assertions.assertThat(request.getMethod()).isEqualTo("GET");
		Assertions.assertThat(request.getPath()).isEqualTo("/test");

		// -----

		r = mock(new MockResponse().addHeader("Content-type",
				"application/json").setBody("{}"));

		r.queryParameter("hello", "world").request().get().execute().body()
				.string();

		request = mockServer.takeRequest();

		Assertions.assertThat(request.getMethod()).isEqualTo("GET");
		Assertions.assertThat(request.getPath()).isEqualTo("/test?hello=world");

	}

	@Test
	public void testPost() throws IOException, InterruptedException {

		OkRest r = mock(new MockResponse().addHeader("Content-type",
				"application/json").setBody("{}"));

		r.request()
				.post(RequestBody.create(MediaType.parse("application/json"),
						"{\"name\":\"Jerry\"")).execute();

		RecordedRequest request = mockServer.takeRequest();

		Assertions.assertThat(request.getMethod()).isEqualTo("POST");
		Assertions.assertThat(request.getPath()).isEqualTo("/test");
		Assertions.assertThat(request.getHeader("Content-type")).contains(
				"application/json");
		Assertions.assertThat(request.getUtf8Body()).isEqualTo(
				"{\"name\":\"Jerry\"");

	}
}
