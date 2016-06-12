package com.pm.server.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

	private final static Logger log =
			LogManager.getLogger(JsonUtils.class.getName());

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private JsonUtils() {
	}

	public static <T> String objectToJson(T object) {
		try {
			return objectMapper.writeValueAsString(object);
		}
		catch (Exception e) {
			log.debug(e);
			return null;
		}
	}

}
