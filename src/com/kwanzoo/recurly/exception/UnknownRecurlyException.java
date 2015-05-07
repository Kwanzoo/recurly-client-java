package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class UnknownRecurlyException extends Base{
	private static final long serialVersionUID = 1L;

	public UnknownRecurlyException(final Response response) {
		super(response);
	}
}
