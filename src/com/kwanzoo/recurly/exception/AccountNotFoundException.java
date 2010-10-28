package com.kwanzoo.recurly.exception;

public class AccountNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;

	@Override
	public String toString(){
		return "Error performing recurly operation because precondition(existing recurly account) failed";
	}
}
