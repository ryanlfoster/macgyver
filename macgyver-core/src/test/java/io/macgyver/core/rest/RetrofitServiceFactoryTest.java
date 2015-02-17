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
	//	p.put("url", "http://example.com");
		ServiceDefinition def = new ServiceDefinition("a", "a", p, x);
	
		r.addServiceDefinition(def);
		
		
		
		Assertions.assertThat(x).isNotNull();
		Assertions.assertThat(x.getServiceType()).isEqualTo("testservice");
		
		
		Object obj = x.get("a");
		
		Assertions.assertThat(obj).isInstanceOf(TestService.class);
	
	}
}
