package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class ServiceUnavailableException extends Base{
	private static final long serialVersionUID = 1L;

	public ServiceUnavailableException(final ClientResponse response){
		super(response);
	}
}
