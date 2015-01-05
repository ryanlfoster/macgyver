package io.macgyver.core.incident;

import io.macgyver.neorx.rest.NeoRxClient;

import java.util.UUID;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class IncidentManagerImpl extends IncidentManager {

	@Inject
	NeoRxClient neo4j;

	@Override
	public IncidentBuilder newIncident() {

		IncidentBuilder b = new IncidentBuilder(this);
		return b;
	}

	public IncidentBuilder withIncidentKey(String key) {

		ObjectNode m = new ObjectMapper().createObjectNode().put("incidentKey",
				key);
		IncidentBuilder b = new IncidentBuilder(this, m);

		return b;
	}

	protected Optional<Incident> doGet(IncidentBuilder b) {
		String cypher = "match (x:Incident {incidentKey: {incidentKey}}) return x";

		JsonNode n = neo4j.execCypher(cypher, b.data()).toBlocking().firstOrDefault(null);

		if (n==null) {
			return Optional.absent();
		}
		Incident incident = new IncidentImpl(n);
		return Optional.fromNullable(incident);
		
	}

	protected Incident doResolve(IncidentBuilder b) {

		String cypher = "match (x:Incident {incidentKey: {incidentKey}}) set x.status='resolved', x.resolvedTs=timestamp() return x";

		JsonNode n = neo4j.execCypher(cypher, b.data()).toBlocking().first();

		return new IncidentImpl(n);

	}

	protected Incident doAcknowledge(IncidentBuilder b) {

		String cypher = "match (x:Incident {incidentKey: {incidentKey}}) set x.status='acknowledged', x.acknowledgdTs=timestamp() return x";

		JsonNode n = neo4j.execCypher(cypher, b.data()).toBlocking().first();

		return new IncidentImpl(n);
	}

	protected Incident doCreate(IncidentBuilder b) {

		String incidentKey = b.data().path("incidentKey").asText();
		if (Strings.isNullOrEmpty(incidentKey)) {
			incidentKey = UUID.randomUUID().toString();
		}

		JsonNode n = neo4j.execCypher("match (x:Incident) where x.incidentKey={incidentKey} return x",b.data()).toBlocking().firstOrDefault(null);
		if (n!=null) {
			IncidentImpl incident = new IncidentImpl(n);
			if (incident.isAcknowledged()  || incident.isOpen()) {
				return incident;
			}
		}
		
		 n = neo4j
				.execCypher(
						"merge (x:Incident {incidentKey: {incidentKey}}  ) return x",
						"incidentKey", incidentKey).toBlocking().first();

		String cypher = "MATCH (n { incidentKey: {incidentKey} }) SET n = { props } RETURN n";

		ObjectNode props = (ObjectNode) new ObjectMapper().createObjectNode()
				.setAll(b.data());
		props.put("status", "open");
		props.put("incidentKey", incidentKey);
		props.put("createTs", System.currentTimeMillis());
		ObjectNode args = new ObjectMapper().createObjectNode();
		args.put("incidentKey", incidentKey);
		args.set("props", props);
		n = neo4j.execCypher(cypher, args).toBlocking().first();

		return new IncidentImpl(n);
	}

}
