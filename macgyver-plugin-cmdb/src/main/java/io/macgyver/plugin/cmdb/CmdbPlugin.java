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
package io.macgyver.plugin.cmdb;

import io.macgyver.core.Plugin;
import io.macgyver.core.web.vaadin.MacGyverUI;
import io.macgyver.neorx.rest.NeoRxClient;
import io.macgyver.plugin.cmdb.ui.ComputeInstancesView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.Navigator;

public class CmdbPlugin extends Plugin {

	Logger logger = LoggerFactory.getLogger(CmdbPlugin.class);

	@Autowired
	NeoRxClient neo4j;



	@Override
	public void registerViews(MacGyverUI ui) {
		logger.info("registerViews: {}", ui);

		ui.registerView(AppInstancesView.class);
		
		ui.registerView(ComputeInstancesView.class);
	}



}
