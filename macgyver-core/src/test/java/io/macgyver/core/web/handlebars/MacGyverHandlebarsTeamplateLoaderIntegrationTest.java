package io.macgyver.core.web.handlebars;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import io.macgyver.test.MacGyverIntegrationTest;

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
