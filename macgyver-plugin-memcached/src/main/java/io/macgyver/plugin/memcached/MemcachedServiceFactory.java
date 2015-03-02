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
import io.macgyver.core.service.BasicServiceFactory;
import io.macgyver.core.service.ServiceDefinition;

import java.io.IOException;
import java.util.Set;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;




import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

public class MemcachedServiceFactory extends
		BasicServiceFactory<MemcachedClient> {

	public static final Set<String> PROTOCOL_OPTIONS = ImmutableSet.of("text",
			"binary");

	public MemcachedServiceFactory() {
		super("memcached");

	}

	@Override
	protected MemcachedClient doCreateInstance(ServiceDefinition def) {
		try {
			String connectionString = def.getProperties().getProperty(
					"connectionString");
			String protocol = def.getProperties()
					.getProperty("protocol", "text").trim().toLowerCase();

			if (Strings.isNullOrEmpty(connectionString)) {
				throw new ConfigurationException("connectionString must be set");
			}
			if (Strings.isNullOrEmpty(protocol)
					|| !PROTOCOL_OPTIONS.contains(protocol)) {
				throw new ConfigurationException(
						"protocol must be: text or binary");
			}

			MemcachedClient client = null;

			if (protocol.equals("binary")) {
				client = new MemcachedClient(new BinaryConnectionFactory(),
						AddrUtil.getAddresses(connectionString));
			}
			else {
				client = new MemcachedClient(new BinaryConnectionFactory(),
						AddrUtil.getAddresses(connectionString));
			}

			return client;
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}
	}

}
