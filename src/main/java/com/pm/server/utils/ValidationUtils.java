package com.pm.server.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.request.LocationRequest;
import com.pm.server.request.PlayerNameRequest;
import com.pm.server.request.PlayerStateRequest;
import com.pm.server.request.StringStateContainer;

public class ValidationUtils {

	private final static Logger log =
			LogManager.getLogger(ValidationUtils.class.getName());

	public static Player.Name validateRequestWithName(
			PlayerNameRequest playerNameRequest)
			throws PmServerException {

		if(playerNameRequest == null) {
			String errorMessage = "Request body requires a name.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}
		else if(playerNameRequest.name == null) {
			String errorMessage = "Request body requires a name.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		Player.Name name;
		try {
			name = Player.Name.valueOf(playerNameRequest.name);
		}
		catch(IllegalArgumentException e) {
			log.warn(e.getMessage());

			String errorMessage = "Request body requires a valid name.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.NOT_FOUND, errorMessage);
		}

		return name;

	}

	public static Coordinate validateRequestBodyWithLocation(
			LocationRequest locationRequest)
			throws PmServerException {

		String errorMessage = null;

		if(locationRequest == null) {
			errorMessage = "Request body requires latitude and longitude.";
		}
		else if(
				locationRequest.latitude == null &&
				locationRequest.longitude == null) {
			errorMessage = "Request body requires latitude and longitude.";
		}
		else if(locationRequest.latitude == null) {
			errorMessage = "Request body requires latitude.";
		}
		else if(locationRequest.longitude == null) {
			errorMessage = "Request body requires longitude.";
		}

		if(errorMessage != null) {
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		return new Coordinate(
				locationRequest.latitude, locationRequest.longitude
		);

	}

	public static Player.State validateRequestBodyWithState(
			PlayerStateRequest stateRequest)
			throws PmServerException {

		if(stateRequest == null) {
			String errorMessage = "Request body requires a state.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}
		else if(stateRequest.state == null) {
			String errorMessage = "Request body requires a state.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		Player.State state = null;
		try {
			state = Player.State.valueOf(stateRequest.state);
		}
		catch(IllegalArgumentException e) {
			log.warn(e.getMessage());

			String errorMessage = "Request body requires a valid state.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		return state;

	}

	public static GameState validateRequestBodyWithGameState(
			StringStateContainer request)
			throws PmServerException {

		if(request == null) {
			String errorMessage = "Request body requires a state.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}
		else if(request.getState() == null) {
			String errorMessage = "Request body requires a state.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		try {
			return GameState.valueOf(request.getState());
		}
		catch(IllegalArgumentException e) {
			log.warn(e.getMessage());
			String errorMessage = "Request body requires a valid state.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

	}

}
