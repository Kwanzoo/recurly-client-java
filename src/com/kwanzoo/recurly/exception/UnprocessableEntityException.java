package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class UnprocessableEntityException extends Base{
	private static final long serialVersionUID = 1L;

	public UnprocessableEntityException(final Response response) {
		super(response);
	}
}
