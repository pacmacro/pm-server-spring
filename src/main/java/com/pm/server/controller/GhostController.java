package com.pm.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.pm.server.response.GhostResponse;

public interface GhostController extends Controller, PlayerController {

	public GhostResponse getLocationById(
			Integer id,
			HttpServletResponse response
	);

	public List<GhostResponse> getAllLocations();

}
