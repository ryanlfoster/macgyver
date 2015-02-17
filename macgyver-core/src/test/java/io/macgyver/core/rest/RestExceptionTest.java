package io.macgyver.core.rest;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class RestExceptionTest {

	
	@Test
	public void testX() {
		RuntimeException x = new RuntimeException("abc123");
		
		RestException e = new RestException(x);
		
		Assertions.assertThat(e.getStatusCode()).isEqualTo(400);
		
		Assertions.assertThat(e).hasCauseExactlyInstanceOf(x.getClass()).hasMessageContaining("java.lang.RuntimeException: abc123");
		
	}
}
