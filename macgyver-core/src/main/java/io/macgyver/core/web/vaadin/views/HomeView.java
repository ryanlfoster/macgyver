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
package io.macgyver.core.web.vaadin.views;

import io.macgyver.core.web.vaadin.ViewConfig;
import io.macgyver.core.web.vaadin.ViewDecorators;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@ViewConfig(viewName="home", menuPath={"MacGyver","Home"})
public class HomeView extends VerticalLayout implements View {

	public HomeView() {
		setMargin(true);
		setSizeFull();
	//	Label home = new Label("Welcome to MacGyver");
		//addComponent(home);

		//setComponentAlignment(home, Alignment.MIDDLE_CENTER);

		ViewDecorators.decorate(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// do something...
	}


}
