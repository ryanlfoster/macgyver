package io.macgyver.plugin.github;

import io.macgyver.core.MacGyverException;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

public class WebHookEvent {

	private static ObjectMapper mapper = new ObjectMapper();

	JsonNode payload;
	byte[] rawPayload;

	public WebHookEvent(byte[] rawData) {
		Preconditions.checkNotNull(rawData);

		try {
			this.payload = mapper.readTree(rawData);
			this.rawPayload = rawData;
		} catch (IOException e) {
			throw new MacGyverException(e);
		}
	}

	public byte[] getRawData() {
		return rawPayload;
	}

	public JsonNode getPayload() {
		return payload;
	}

}
