package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class BadRequestException extends Base{
	private static final long serialVersionUID = 1L;

	public BadRequestException(final Response response) {
		super(response);
	}
}
