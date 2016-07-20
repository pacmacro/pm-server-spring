package com.pm.server.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerState;
import com.pm.server.exceptionhttp.BadRequestException;
import com.pm.server.request.PlayerStateRequest;

public class ValidationUtils {

	private final static Logger log =
			LogManager.getLogger(ValidationUtils.class.getName());

	public static void validateRequestBodyWithLocation(Coordinate location)
			throws BadRequestException {

		String errorMessage = null;

		if(location == null) {
			errorMessage = "Request body requires latitude and longitude.";
		}
		else if(
				location.getLatitude() == null &&
				location.getLongitude() == null) {
			errorMessage = "Request body requires latitude and longitude.";
		}
		else if(location.getLatitude() == null) {
			errorMessage = "Request body requires latitude.";
		}
		else if(location.getLongitude() == null) {
			errorMessage = "Request body requires longitude.";
		}

		if(errorMessage != null) {
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}

	}

	public static PlayerState validateRequestBodyWithState(
			PlayerStateRequest stateRequest)
			throws BadRequestException {

		if(stateRequest == null) {
			String errorMessage = "Request body requires a state.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}
		else if(stateRequest.state == null) {
			String errorMessage = "Request body requires a state.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		PlayerState state = null;
		try {
			state = PlayerState.valueOf(stateRequest.state);
		}
		catch(IllegalArgumentException e) {
			log.warn(e.getMessage());

			String errorMessage = "Request body requires a valid state.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		return state;

	}

}
