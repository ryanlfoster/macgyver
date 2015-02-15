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
