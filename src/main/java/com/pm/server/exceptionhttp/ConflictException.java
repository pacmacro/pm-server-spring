package com.pm.server.exceptionhttp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT)
public final class ConflictException extends Exception {

	private static final long serialVersionUID = -4945824956479915183L;

	public ConflictException(String message) {
		super(message);
	}

}
