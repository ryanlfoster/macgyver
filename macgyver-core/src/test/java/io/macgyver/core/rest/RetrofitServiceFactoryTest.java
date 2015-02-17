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
package io.macgyver.core.rest;

import java.util.Properties;

import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceRegistry;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class RetrofitServiceFactoryTest {

	public static interface TestService {
		
	}
	
	
	@Test
	public void testIt() {
		
		
		ServiceRegistry r = new ServiceRegistry();
	
		RetrofitServiceFactory<TestService> x = new RetrofitServiceFactory<TestService>("TestService", TestService.class.getName());
		x.setServiceRegistry(r);
		
		Properties p = new Properties();
		p.put("url", "http://example.com");
		ServiceDefinition def = new ServiceDefinition("a", "a", p, x);
	
		r.addServiceDefinition(def);
		
		
		
		Assertions.assertThat(x).isNotNull();
		Assertions.assertThat(x.getServiceType()).isEqualTo("testservice");
		
		
		Object obj = x.get("a");
		
		Assertions.assertThat(obj).isInstanceOf(TestService.class);
	
	}
}
