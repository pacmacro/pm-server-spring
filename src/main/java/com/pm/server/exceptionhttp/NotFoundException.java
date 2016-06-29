package com.pm.server.exceptionhttp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public final class NotFoundException extends Exception {

	private static final long serialVersionUID = -5588376043118218062L;

	public NotFoundException(String message) {
		super(message);
	}

}
