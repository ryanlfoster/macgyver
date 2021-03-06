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
package io.macgyver.core.auth;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class InternalUser {

	String username;
	String scryptHash;
	List<String> roles = Lists.newArrayList();
	
	public String getUsername() {
		return username;
	}

	public String getScryptHash() {
		return scryptHash;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String toString() {
		
		return Objects.toStringHelper(this).add("username", username).add("roles", roles).toString();
	}
}
