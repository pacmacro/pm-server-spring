package com.pm.server;

import org.springframework.http.HttpStatus;

public class PmServerException extends Exception {

	private static final long serialVersionUID = -3697335020257818445L;

	private HttpStatus status;

	private String message;

	public PmServerException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setCode(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
