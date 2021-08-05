package com.suchorski.zapbot.exceptions;

public class NotEnoughCoinsException extends Exception {
	
	private static final long serialVersionUID = 9143030155489015104L;

	public NotEnoughCoinsException(String message) {
		super(message);
	}

}
