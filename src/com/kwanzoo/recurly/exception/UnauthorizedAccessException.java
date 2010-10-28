package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class UnauthorizedAccessException extends Base{
	private static final long serialVersionUID = 1L;

	public UnauthorizedAccessException(final ClientResponse response){
		super(response);
	}
}
