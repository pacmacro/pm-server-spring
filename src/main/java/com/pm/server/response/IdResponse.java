package com.pm.server.response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IdResponse {

	private Integer id;

	private static final Logger log =
			LogManager.getLogger(IdResponse.class.getName());

	public void setId(Integer id) {

		log.debug("Setting id to {}", id);

		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
