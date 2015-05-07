package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class UnauthorizedAccessException extends Base{
	private static final long serialVersionUID = 1L;

	public UnauthorizedAccessException(final Response response) {
		super(response);
	}
}
