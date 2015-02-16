package io.macgyver.plugin.github;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SignautreBasedWebHookAuthenticatorTest {

	
	@Test
	public void testSha1() throws GeneralSecurityException, UnsupportedEncodingException {
		SignatureBasedWebHookAuthenticator s = new SignatureBasedWebHookAuthenticator("secret");
		
		Assertions.assertThat(s.getSecret()).isEqualTo("secret");
		
		byte [] b = "{\"payload\":\"test\"}".getBytes("UTF8");
		
		Assertions.assertThat(s.calculateSha1(b, "secret")).isEqualTo("sha1=914325B95BE16BBA47A9650FCB916625858ECA7F");
		
	}
}
