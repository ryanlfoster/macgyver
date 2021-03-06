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
package io.macgyver.core.web.vaadin.views.admin;

import io.macgyver.core.Kernel;
import io.macgyver.core.MacGyverException;
import io.macgyver.core.auth.AuthUtil;
import io.macgyver.core.auth.MacGyverRole;
import io.macgyver.core.resource.Resource;
import io.macgyver.core.resource.ResourceProvider;
import io.macgyver.core.resource.provider.filesystem.FileSystemResourceProvider;
import io.macgyver.core.scheduler.MacGyverTask;
import io.macgyver.core.script.ExtensionResourceProvider;
import io.macgyver.core.web.vaadin.IndexedJsonContainer;
import io.macgyver.core.web.vaadin.ViewConfig;
import io.macgyver.core.web.vaadin.ViewDecorators;
import it.sauronsoftware.cron4j.Scheduler;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

@ViewConfig(viewName = ScriptsView.VIEW_NAME, menuPath = { "Admin", "Scripts" })
public class ScriptsView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "admin/scripts";
	Table table;
	IndexedJsonContainer container;

	boolean userHasExecutPermissions = false;

	Logger logger = LoggerFactory.getLogger(ScriptsView.class);

	public ScriptsView() {
		super();
		setMargin(true);
		userHasExecutPermissions = AuthUtil
				.currentUserHasRole(MacGyverRole.ROLE_MACGYVER_ADMIN);
		container = new IndexedJsonContainer();
		container.addContainerProperty("resource", String.class, "");
		container.addContainerProperty("providerType", String.class, "");
		container.addContainerProperty("actions", Button.class, null);

		table = new Table("Scripts");
		table.setContainerDataSource(container);
		table.setWidth("100%");

		table.setColumnHeader("resource", "Resource");
		table.setColumnHeader("providerType", "Provider");
		table.setColumnHeader("actions", "Actions");
		ColumnGenerator cg = new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {

				if (!userHasExecutPermissions) {
					// no button to execute
					return null;
				}
				Button b = new Button("Invoke");
				b.setStyleName("tiny");
				Button.ClickListener cl = new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						Item item = container.getItem(itemId);
						Property p = item.getItemProperty("resource");
						Object value = p.getValue();

						JsonNode on = container.getJsonObject(itemId);
						if (on != null) {
							try {
								String hash = on.path("hash").asText();
								Optional<Resource> r = findResourceByHash(hash);
								if (r.isPresent()) {
									scheduleImmediate(r.get());
								}

							} catch (IOException e) {
								throw new MacGyverException(e);
							}

						}

					}
				};
				b.addClickListener(cl);
				return b;
			}
		};

		table.addGeneratedColumn("actions", cg);

		Button b = new Button("Reload Scripts");

		b.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				refresh();
			}
		});
		addComponent(b);
		addComponent(table);
		ViewDecorators.decorate(this);
	}

	protected ExtensionResourceProvider getExtensionResourceProvider() {
		return Kernel.getInstance().getApplicationContext()
				.getBean(ExtensionResourceProvider.class);
	}

	Optional<Resource> findResourceByHash(String hash) throws IOException {
		for (Resource r : getExtensionResourceProvider().findResources()) {
			String resourceHash = r.getHash();
			if (resourceHash.equals(hash)) {
				return Optional.of(r);
			}
		}
		return Optional.absent();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		refresh();

	}

	public void refresh() {
		try {
			logger.info("reloading");
			container.removeAllItems();
			ExtensionResourceProvider extensionProvider = Kernel.getInstance()
					.getApplicationContext()
					.getBean(ExtensionResourceProvider.class);
			extensionProvider.refresh();
			ObjectMapper mapper = new ObjectMapper();
			List<JsonNode> list = Lists.newArrayList();

			for (Resource r : extensionProvider.findResources()) {
				ResourceProvider rp = r.getResourceProvider();

				if (r.getPath().startsWith("scripts/")) {
					ObjectNode n = mapper.createObjectNode();
					n.put("resource", r.getPath());
					if (rp.getClass().equals(FileSystemResourceProvider.class)) {
						n.put("providerType", "filesystem");
					} else if (rp.getClass().getName().contains("Git")) {
						n.put("providerType", "git");
					}
					n.put("hash", r.getHash());
					list.add(n);

				}

			}
			container.addJsonObjects(list);
		} catch (IOException e) {
			throw new MacGyverException(e);
		}
	}

	public void scheduleImmediate(Resource r) {
		try {
			Scheduler scheduler = Kernel.getApplicationContext().getBean(
					Scheduler.class);
			ObjectNode n = new ObjectMapper().createObjectNode();
			n.put("script", r.getPath());
			MacGyverTask task = new MacGyverTask(n);
			scheduler.launch(task);
		} catch (IOException e) {
			throw new MacGyverException(e);
		}
	}

}
