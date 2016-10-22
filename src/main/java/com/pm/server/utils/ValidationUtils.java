package com.pm.server.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import com.pm.server.datatype.PlayerState;
import com.pm.server.exceptionhttp.BadRequestException;
import com.pm.server.exceptionhttp.NotFoundException;
import com.pm.server.game.GameState;
import com.pm.server.request.LocationRequest;
import com.pm.server.request.PlayerNameRequest;
import com.pm.server.request.PlayerStateRequest;
import com.pm.server.request.StringStateContainer;

public class ValidationUtils {

	private final static Logger log =
			LogManager.getLogger(ValidationUtils.class.getName());

	public static Player.Name validateRequestWithName(
			PlayerNameRequest playerNameRequest)
			throws BadRequestException, NotFoundException {

		if(playerNameRequest == null) {
			String errorMessage = "Request body requires a name.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}
		else if(playerNameRequest.name == null) {
			String errorMessage = "Request body requires a name.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		Player.Name name;
		try {
			name = Player.Name.valueOf(playerNameRequest.name);
		}
		catch(IllegalArgumentException e) {
			log.warn(e.getMessage());

			String errorMessage = "Request body requires a valid name.";
			log.warn(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		return name;

	}

	public static Coordinate validateRequestBodyWithLocation(
			LocationRequest locationRequest)
			throws BadRequestException {

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
			throw new BadRequestException(errorMessage);
		}

		return new Coordinate(
				locationRequest.latitude, locationRequest.longitude
		);

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

	public static GameState validateRequestBodyWithGameState(
			StringStateContainer request)
			throws BadRequestException {

		if(request == null) {
			String errorMessage = "Request body requires a state.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}
		else if(request.getState() == null) {
			String errorMessage = "Request body requires a state.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		try {
			return GameState.valueOf(request.getState());
		}
		catch(IllegalArgumentException e) {
			log.warn(e.getMessage());
			String errorMessage = "Request body requires a valid state.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}

	}

}
