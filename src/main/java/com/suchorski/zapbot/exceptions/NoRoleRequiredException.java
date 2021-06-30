package com.suchorski.zapbot.exceptions;

public class NoRoleRequiredException extends Exception {
	
	private static final long serialVersionUID = 1278996392532654457L;

	public NoRoleRequiredException(String message) {
		super(message);
	}

}
