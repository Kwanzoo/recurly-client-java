package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class Base extends Exception {

	private static final long serialVersionUID = 1L;

	protected final Response response;

	public Base(final Response response) {
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}

	@Override
	public String toString() {
		return "Internal error talking to payment system";
	}
}
