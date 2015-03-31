package io.macgyver.test;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class RequestUtilTest {

	
	@Test
	public void testX() {
		Map<String,String> m = RequestUtil.parseFormBody("a=1&b=2");
		
		Assertions.assertThat(m).hasSize(2).containsEntry("a", "1").containsEntry("b", "2");
	}
}
