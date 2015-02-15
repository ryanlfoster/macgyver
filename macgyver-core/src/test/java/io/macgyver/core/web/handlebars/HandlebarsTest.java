package io.macgyver.core.web.handlebars;

import java.io.IOException;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Maps;

public class HandlebarsTest {

	
	@Test
	public void testIt() throws IOException {
		Handlebars h = new Handlebars();
		
		Template t = h.compileInline("Hello, {{name}}!");
		
		Map<String,String> m = Maps.newHashMap();
		m.put("name","world");
		String s = t.apply(m);
		
		Assertions.assertThat(s).isEqualTo("Hello, world!");
		
		
	}
	
	public static class TestBean {
		String name;
		
		public String getName() {
			return name;
		}
	}
	@Test
	public void testBean() throws IOException {
		Handlebars h = new Handlebars();
		
		Template t = h.compileInline("Hello, {{name}}!");
		
		TestBean b = new TestBean();
		b.name="world";
		
	
		String s = t.apply(b);
		
		Assertions.assertThat(s).isEqualTo("Hello, world!");
		
		
	}
	
	@Test
	public void testJackson2() throws IOException {
		Handlebars h = new Handlebars();
		
		Template t = h.compileInline("Hello, {{name}}!");
		
		JsonNode n = new ObjectMapper().createObjectNode().put("name", "world");
		
		Context c = Context.newBuilder(n).resolver(JsonNodeValueResolver.INSTANCE).build();
		
		Assertions.assertThat(t.apply(c)).isEqualTo("Hello, world!");
		
		
	}
}
