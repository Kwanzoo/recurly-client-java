package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class UnknownRecurlyException extends Base{
	private static final long serialVersionUID = 1L;

	public UnknownRecurlyException(final ClientResponse response){
		super(response);
	}
}
