package com.pm.server.controller;

import com.pm.server.response.GhostsResponse;

public interface GhostController extends Controller, PlayerController {

	public int getInteger();

	public GhostsResponse getAllLocations();

}
