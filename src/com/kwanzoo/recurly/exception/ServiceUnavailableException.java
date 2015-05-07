package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class ServiceUnavailableException extends Base{
	private static final long serialVersionUID = 1L;

	public ServiceUnavailableException(final Response response) {
		super(response);
	}
}
