package io.macgyver.plugin.ci;

import com.fasterxml.jackson.databind.JsonNode;

import io.macgyver.neorx.rest.NeoRxClient;

public interface CIJobDecorator<T extends CIScanner> {

	public void scanJob(JsonNode n, T scanner);
}
