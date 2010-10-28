package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class PaymentRequiredException extends Base{
	private static final long serialVersionUID = 1L;

	public PaymentRequiredException(final ClientResponse response){
		super(response);
	}
}
