package io.macgyver.core.web.handlebars;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import io.macgyver.test.MacGyverIntegrationTest;

public class MacGyverHandlebarsViewResolverTest extends MacGyverIntegrationTest {

	@Inject
	ApplicationContext ctx;
	
	@Test
	public void testApplicationContext() {
	
		MacGyverHandlebarsViewResolver vr = ctx.getBean(MacGyverHandlebarsViewResolver.class);
		
		Assertions.assertThat(vr).isNotNull();
		
		Assertions.assertThat(vr.getApplicationContext()).isSameAs(ctx);
		
	}
}
