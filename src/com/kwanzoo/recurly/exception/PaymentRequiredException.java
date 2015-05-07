package com.kwanzoo.recurly.exception;

import javax.ws.rs.core.Response;

public class PaymentRequiredException extends Base{
	private static final long serialVersionUID = 1L;

	public PaymentRequiredException(final Response response) {
		super(response);
	}
}
