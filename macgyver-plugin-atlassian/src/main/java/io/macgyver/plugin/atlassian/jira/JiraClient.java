package io.macgyver.plugin.atlassian.jira;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import com.fasterxml.jackson.databind.JsonNode;

public interface JiraClient {

	@GET("/issue/{issue}")
	JsonNode getIssue(@Path("issue") String issue);

	@GET("/{path}")
	JsonNode getJson(@Path(value="path",encode=false) String path);
	
	@POST("/{path}")
	JsonNode postJson(@Path(value="path",encode=false) String path, @Body JsonNode body);
}
