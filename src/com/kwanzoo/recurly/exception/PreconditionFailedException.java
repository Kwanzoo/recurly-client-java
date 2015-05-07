package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class PreconditionFailedException extends Base{
	private static final long serialVersionUID = 1L;

	public PreconditionFailedException(final Response response) {
		super(response);
	}
}
