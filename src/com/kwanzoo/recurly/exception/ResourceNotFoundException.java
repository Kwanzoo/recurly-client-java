package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class ResourceNotFoundException extends Base{
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(final ClientResponse response){
		super(response);
	}
}
