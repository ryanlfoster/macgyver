package io.macgyver.core.incident;

import io.macgyver.core.MacGyverException;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;


public abstract class IncidentManager {

	public class IncidentBuilder {

		IncidentManager manager;

		ObjectNode props = new ObjectMapper().createObjectNode();

		protected IncidentBuilder() {

		}

		public IncidentBuilder(IncidentManager m) {
			this.manager = m;
		}

		public IncidentBuilder(IncidentManager m, ObjectNode props) {
			try {
				this.manager = m;
				this.props = (ObjectNode) new ObjectMapper().readTree(props
						.toString());
			} catch (IOException e) {
				throw new MacGyverException(e);
			}

		}

		public ObjectNode data() {
			return props;
		}

		protected IncidentBuilder copy(String key, String val) {

			IncidentBuilder b = new IncidentBuilder(this.manager, this.props);	
			b.props.put(key, val);
			return b;
		}

		public IncidentBuilder incidentKey(String key) {
			return copy("incidentKey", key);
		}

		public IncidentBuilder description(String description) {
			return copy("description", description);
		}

		public Incident create() {
			IncidentManagerImpl impl = (IncidentManagerImpl) manager;
			return impl.doCreate(this);
		}
		public Incident resolve() {
			IncidentManagerImpl impl = (IncidentManagerImpl) manager;
			return impl.doResolve(this);
		}
		public Incident acknowledge() {
			IncidentManagerImpl impl = (IncidentManagerImpl) manager;
			return impl.doAcknowledge(this);
		}
		public Optional<Incident> get() {
			IncidentManagerImpl impl = (IncidentManagerImpl) manager;
			return impl.doGet(this);		
		}
		public List<Incident> search() {
			return Lists.newArrayList();
		}
	}
		
//	public abstract IncidentBuilder with();
	public abstract IncidentBuilder newIncident();
	public abstract IncidentBuilder withIncidentKey(String key);
}
