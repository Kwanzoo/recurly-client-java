package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class ResourceNotFoundException extends Base{
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(final Response response) {
		super(response);
	}
}
