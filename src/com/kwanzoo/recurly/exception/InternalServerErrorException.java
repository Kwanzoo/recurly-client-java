package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class InternalServerErrorException extends Base{
	private static final long serialVersionUID = 1L;

	public InternalServerErrorException(final ClientResponse response){
		super(response);
	}
}
