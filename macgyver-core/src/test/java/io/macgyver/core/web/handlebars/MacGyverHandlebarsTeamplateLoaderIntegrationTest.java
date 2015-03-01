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
package io.macgyver.core.web.handlebars;

import static org.assertj.core.api.Assertions.assertThat;
import io.macgyver.test.MacGyverIntegrationTest;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class MacGyverHandlebarsTeamplateLoaderIntegrationTest extends MacGyverIntegrationTest {

	
	@Inject
	ApplicationContext ctx;
	
	
	@Test
	public void testGetResourceNotFound() throws IOException {
		assertThat(ctx).isNotNull();
		
		MacGyverHandlebarsTemplateLoader mtl = new MacGyverHandlebarsTemplateLoader(ctx);
		
		
		
		assertThat(mtl.getResource("something/not/found")).isNull();

		
		
	}
	@Test
	public void testResolve() throws IOException {
		assertThat(ctx).isNotNull();
		
		MacGyverHandlebarsTemplateLoader mtl = new MacGyverHandlebarsTemplateLoader(ctx);
		
		assertThat(mtl.getPrefix()).isEqualTo("/");
		assertThat(mtl.getSuffix()).isEqualTo(".hbs");
		
		assertThat(mtl.resolve("web/macgyver-login")).isEqualTo("/web/macgyver-login.hbs");
		assertThat(mtl.resolve("/web/macgyver-login")).isEqualTo("/web/macgyver-login.hbs");
		
		
		assertThat(mtl.resolve("/not/found/anywhere")).isEqualTo("/not/found/anywhere.hbs");
		
	}
	
	@Test
	public void testGetResource() throws IOException {
		MacGyverHandlebarsTemplateLoader mtl = new MacGyverHandlebarsTemplateLoader(ctx);
		
		assertThat(mtl.getResource("/macgyver-login.hbs")).isNotNull().isInstanceOf(URL.class);
		assertThat(mtl.getResource("macgyver-login.hbs")).isNotNull().isInstanceOf(URL.class);
	}
}
