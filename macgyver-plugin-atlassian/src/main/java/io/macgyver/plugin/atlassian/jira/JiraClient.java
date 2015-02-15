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
