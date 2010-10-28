package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class ForbiddenAccessException extends Base{
	private static final long serialVersionUID = 1L;

	public ForbiddenAccessException(final ClientResponse response){
		super(response);
	}
}
