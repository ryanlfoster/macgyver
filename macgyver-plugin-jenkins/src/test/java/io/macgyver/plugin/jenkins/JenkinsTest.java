package io.macgyver.plugin.jenkins;

import io.macgyver.core.graph.NodeInfo;
import io.macgyver.neorx.rest.NeoRxClient;
import io.macgyver.plugin.jenkins.decorators.GitHubDecorator;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.jdom2.Document;
import org.jdom2.Text;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.squareup.okhttp.mockwebserver.rule.MockWebServerRule;

public class JenkinsTest {

	@Rule
	public MockWebServerRule mockServer = new MockWebServerRule();

	JenkinsClient client;

	@Before
	public void setupClient() {
		client = new JenkinsClientBuilder()
				.url(mockServer.getUrl("/jenkins").toString())
				.credentials("username", "password").build();

	}

	@Test
	public void testGetBuildQueue() throws InterruptedException {

		mockServer.enqueue(new MockResponse().setBody("{\"items\":[]}"));
		client.getBuildQueue();

		RecordedRequest rr = mockServer.takeRequest();

		Assertions.assertThat(rr.getPath())
				.isEqualTo("/jenkins/queue/api/json");
		Assertions.assertThat(rr.getMethod()).isEqualTo("GET");
	}

	@Test
	public void testGetLoadStats() throws InterruptedException {

		mockServer
				.enqueue(new MockResponse()
						.setBody("{\"busyExecutors\":{},\"queueLength\":{},\"totalExecutors\":{},\"totalQueueLength\":{}}"));
		client.getLoadStats();

		RecordedRequest rr = mockServer.takeRequest();

		Assertions.assertThat(rr.getPath()).isEqualTo(
				"/jenkins/overallLoad/api/json");
		Assertions.assertThat(rr.getMethod()).isEqualTo("GET");

	}

	@Test
	public void testGetServerInfo() throws InterruptedException {

		String x = "{\n" + "  \"assignedLabels\" : [ { } ],\n"
				+ "  \"mode\" : \"NORMAL\",\n"
				+ "  \"nodeDescription\" : \"the master Jenkins node\",\n"
				+ "  \"nodeName\" : \"\",\n" + "  \"numExecutors\" : 2,\n"
				+ "  \"description\" : null,\n" + "  \"jobs\" : [ {\n"
				+ "    \"name\" : \" myjob\",\n"
				+ "    \"url\" : \"https://jenkins.example.com/job/myjob/\",\n"
				+ "    \"color\" : \"red\"\n" + "  }]}";
		mockServer.enqueue(new MockResponse().setBody(x));
		client.getServerInfo();

		RecordedRequest rr = mockServer.takeRequest();

		Assertions.assertThat(rr.getPath()).isEqualTo("/jenkins/api/json");
		Assertions.assertThat(rr.getMethod()).isEqualTo("GET");

	}

	@Test
	@Ignore
	public void testReal() throws InterruptedException,
			JsonProcessingException, IOException {

		NeoRxClient neo4j = new NeoRxClient();

		JenkinsClient c = new JenkinsClientBuilder()
				.url("https://jenkins.example.com/")
				.credentials("username", "password").build();
	

		JenkinsScanner s = new JenkinsScanner(neo4j, c);	
	
		s.addDecorationAction(new GitHubDecorator());
		
		s.scan();
	}
}
	



