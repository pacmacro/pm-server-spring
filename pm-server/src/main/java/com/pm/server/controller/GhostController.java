package com.pm.server.controller;

import javax.servlet.http.HttpServletResponse;

import com.pm.server.response.GhostResponse;
import com.pm.server.response.GhostsResponse;

public interface GhostController extends Controller, PlayerController {

	public GhostResponse getLocationById(
			Integer id,
			HttpServletResponse response
	);

	public GhostsResponse getAllLocations();

}
