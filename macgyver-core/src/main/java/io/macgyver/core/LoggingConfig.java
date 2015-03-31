package io.macgyver.core;

import org.slf4j.bridge.SLF4JBridgeHandler;

public class LoggingConfig {

	public static volatile boolean bridged = false;

	public static synchronized final void ensureJavaUtilLoggingIsBridged() {
		if (!bridged) {
			SLF4JBridgeHandler.removeHandlersForRootLogger();
			SLF4JBridgeHandler.install();
		}
	}
}
