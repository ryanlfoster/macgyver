package io.macgyver.plugin.ci;

import io.macgyver.core.service.ServiceScanner;
import io.macgyver.neorx.rest.NeoRxClient;

public abstract class CIScanner<T> extends ServiceScanner<T> {

	public CIScanner(NeoRxClient neo4j, T service) {
		super(neo4j, service);
		
	}

}
