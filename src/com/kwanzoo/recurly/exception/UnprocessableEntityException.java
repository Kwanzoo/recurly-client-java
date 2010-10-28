package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class UnprocessableEntityException extends Base{
	private static final long serialVersionUID = 1L;

	public UnprocessableEntityException(final ClientResponse response){
		super(response);
	}
}
