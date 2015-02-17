package io.macgyver.core.rest;

import io.macgyver.core.MacGyverException;

public class RestException extends MacGyverException {

	int statusCode;
	
	public RestException(Exception e) {
		super(e);
		statusCode=400;
	}
	public RestException(int statusCode) {
		super("status: "+statusCode);
		this.statusCode = statusCode;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
}
