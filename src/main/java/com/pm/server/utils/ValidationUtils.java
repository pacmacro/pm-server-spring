package com.pm.server.utils;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.request.LocationRequest;
import com.pm.server.request.StateRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

public class ValidationUtils {

	private final static Logger log =
			LogManager.getLogger(ValidationUtils.class.getName());

	public static Player.Name validateRequestWithName(
			String playerNameRequest)
			throws PmServerException {

		if(playerNameRequest == null || playerNameRequest.isEmpty()) {
			String errorMessage = "Request body requires a name.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		Player.Name name;
		try {
			name = Player.Name.valueOf(playerNameRequest);
		}
		catch(IllegalArgumentException e) {
			log.warn(e.getMessage());

			String errorMessage = "Request body requires a valid name.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.NOT_FOUND, errorMessage);
		}

		return name;

	}

	/**
	 * Parses the name of a player in a tag (source or destination).
	 *
	 * @param playerNameRequest String name of the player
	 * @return The parsed name of the player, or null if the given name
	 * 			was null
	 * @throws PmServerException If the name was invalid
	 */
	public static Player.Name validateTagSourceDestination(
			String playerNameRequest)
			throws PmServerException {

		if(playerNameRequest == null || playerNameRequest.isEmpty()) {
			return null;
		}

		try {
			return Player.Name.valueOf(playerNameRequest);
		}
		catch(IllegalArgumentException e) {
			log.warn(e.getMessage());

			String errorMessage = "Request body requires a valid name.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.NOT_FOUND, errorMessage);
		}
	}

	public static Coordinate validateRequestBodyWithLocation(
			LocationRequest locationRequest)
			throws PmServerException {

		String errorMessage = null;

		if(locationRequest == null) {
			errorMessage = "Request body requires latitude and longitude.";
		}
		else if(
				locationRequest.getLatitude() == null &&
				locationRequest.getLongitude() == null) {
			errorMessage = "Request body requires latitude and longitude.";
		}
		else if(locationRequest.getLatitude() == null) {
			errorMessage = "Request body requires latitude.";
		}
		else if(locationRequest.getLongitude() == null) {
			errorMessage = "Request body requires longitude.";
		}

		if(errorMessage != null) {
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		return new Coordinate(
				locationRequest.getLatitude(), locationRequest.getLongitude()
		);

	}

	public static Player.State validateRequestBodyWithState(
			StateRequest stateRequest)
			throws PmServerException {

		if(stateRequest == null || stateRequest.getState() == null) {
			String errorMessage = "Request body requires a state.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		try {
			return Player.State.valueOf(stateRequest.getState());
		}
		catch(IllegalArgumentException e) {
			log.warn(e.getMessage());

			String errorMessage = "Request body requires a valid state.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}
	}

	public static GameState validateRequestBodyWithGameState(
			StateRequest request)
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
