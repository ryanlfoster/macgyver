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
