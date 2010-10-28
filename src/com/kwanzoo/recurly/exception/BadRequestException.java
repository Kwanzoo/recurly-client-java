package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class BadRequestException extends Base{
	private static final long serialVersionUID = 1L;

	public BadRequestException(final ClientResponse response){
		super(response);
	}
}
