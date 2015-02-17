package io.macgyver.core.rest;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

public class RetrofitBuilderTest {

	
	public static interface TestClient {
		
	}
	@Test
	public void testIt() {

		try {
			new RetrofitBuilder().build();
			Assert.fail();
		} catch (Exception e) {
			Assertions.assertThat(e).isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining("may not be null");
		}

	}
	
	@Test
	public void testIt2() {

		try {
			new RetrofitBuilder().endpoint("http://www.example.com").build();
			Assert.fail();
		} catch (Exception e) {
	
			Assertions.assertThat(e).isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining("serviceClass or serviceClassName must be set");
		}

	}
	@Test
	public void testIt3() {

		
			Object obj = new RetrofitBuilder().endpoint("http://www.example.com").serviceClass(TestClient.class).build();
			Assertions.assertThat(obj).isInstanceOf(TestClient.class);
			
			 obj = new RetrofitBuilder().endpoint("http://www.example.com").serviceClassName(TestClient.class.getName()).build();
			Assertions.assertThat(obj).isInstanceOf(TestClient.class);
	
	}
}
