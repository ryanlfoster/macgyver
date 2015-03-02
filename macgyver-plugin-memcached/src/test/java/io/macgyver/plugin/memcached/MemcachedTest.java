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
package io.macgyver.plugin.memcached;

import io.macgyver.core.ConfigurationException;
import io.macgyver.core.service.ServiceDefinition;
import io.macgyver.core.service.ServiceRegistry;

import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;
import java.util.Properties;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.OperationTimeoutException;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MemcachedTest {

	@Autowired
	ServiceRegistry registry;

	MemcachedClient client;

	@After
	public void cleanup() {
		if (client != null) {
			try {
				client.shutdown();
			} catch (Exception e) {
				// ignore
			}
		}
	}

	@Test
	public void missingConnectionString() {

		try {
			MemcachedServiceFactory sf = new MemcachedServiceFactory();

			Properties p = new Properties();
			ServiceDefinition def = new ServiceDefinition("test", "test", p, sf);

			client = sf.doCreateInstance(def);
			Assert.fail();
		} catch (Exception e) {
			Assertions.assertThat(e).isInstanceOf(ConfigurationException.class)
					.hasMessageContaining("connectionString");
		}

	}

	@Test
	public void validConnection() {

		MemcachedServiceFactory sf = new MemcachedServiceFactory();

		Properties p = new Properties();
		p.put("connectionString", "localhost:54299"); // some unused port
		ServiceDefinition def = new ServiceDefinition("test", "test", p, sf);
		client = sf.doCreateInstance(def);

		Assertions.assertThat(client).isNotNull();

		try {
			client.get("Test");
			Assert.fail();
		} catch (OperationTimeoutException e) {

		}

	}

	@Test
	public void invalidHost() {

		try {
			MemcachedServiceFactory sf = new MemcachedServiceFactory();
			Properties p = new Properties();
			p.put("connectionString", "server.example.com:11211");
			ServiceDefinition def = new ServiceDefinition("test", "test", p, sf);
			client = sf.doCreateInstance(def);

		} catch (Exception e) {
			Assertions.assertThat(e).isInstanceOf(
					UnresolvedAddressException.class);
		}

	}
}
