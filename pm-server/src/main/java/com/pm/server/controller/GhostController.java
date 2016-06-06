package com.pm.server.controller;

import java.util.Map;

import com.pm.server.datatype.Coordinate;

public interface GhostController extends Controller, PlayerController {

	public int getInteger();

	// Returns map of ghost id to location
	public Map<Integer, Coordinate> getAllLocations();

}
