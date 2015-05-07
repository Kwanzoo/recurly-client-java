package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class ForbiddenAccessException extends Base{
	private static final long serialVersionUID = 1L;

	public ForbiddenAccessException(final Response response) {
		super(response);
	}
}
