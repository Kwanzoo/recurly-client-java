package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class InternalServerErrorException extends Base{
	private static final long serialVersionUID = 1L;

	public InternalServerErrorException(final Response response) {
		super(response);
	}
}
