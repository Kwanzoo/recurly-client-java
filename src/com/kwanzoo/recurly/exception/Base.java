package com.kwanzoo.recurly.exception;

import com.sun.jersey.api.client.ClientResponse;

public class Base extends Exception{
	private static final long serialVersionUID = 1L;

	protected final ClientResponse response;

	public Base(final ClientResponse response){
		this.response = response;
	}

	public ClientResponse getResponse(){
		return response;
	}

	@Override
	public String toString(){
		return "Internal error talking to payment system";
	}
}
